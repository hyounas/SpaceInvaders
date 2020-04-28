package com.heyletscode.SpaceInvaders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.heyletscode.SpaceInvaders.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class viewGame extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isRunning, gameOver = false; //this will be set to true or false depending on whether the game is running or not
    private int scrX, scrY, score = 0;
    public static float scrRatioX, scrRatioY; //making the game compatible on all screen sizes
    private Paint paint;
    private Enemy[] enemies;
    private SharedPreferences prefs;
    private Random random; // this random is going to be used to change the speed of the bird each time it spawns
    private SoundPool soundPool;
    private List<Laser> lasers;
    private int sound;
    private Player player;
    private GameActivity activity;
    private Space space1, space2; //we need 2 of these so that we can make the background move

    public viewGame(GameActivity activity, int scrX, int scrY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        //referring these variables to scrX and scrY
        this.scrX = scrX;
        this.scrY = scrY;
        scrRatioX = 1920f / scrX;
        scrRatioY = 1080f / scrY;

        space1 = new Space(scrX, scrY, getResources());
        space2 = new Space(scrX, scrY, getResources());

        player = new Player(this, scrY, getResources());

        lasers = new ArrayList<>();

        space2.a = scrX;


        paint = new Paint(); //initialising the paint constructor
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        //an array for the animation, meaning that there will be 4 pictures rendering at the same time
        enemies = new Enemy[4];

        for (int i = 0;i < 4;i++) {

            Enemy enemy = new Enemy(getResources());
            enemies[i] = enemy;

        }

        random = new Random();

    }

    @Override
    public void run() {

        /**a loop to keep the game running*/
        while (isRunning) {

            update ();
            draw ();
            sleep ();

        }

    }

    /**
     * an update method to keep updating the positions of the objects/players/enemies etc
     */
    private void update () {

        space1.a -= 10 * scrRatioX; /**this will move the background towards the left horizontally by 10px*/
        space2.a -= 10 * scrRatioX;

        if (space1.a + space1.space.getWidth() < 0) { /**if it is less than 0, the background is off the screen*/
            space1.a = scrX;
        }

        if (space2.a + space2.space.getWidth() < 0) { /**if it is less than 0, the background2 is off the screen*/
            space2.a = scrX;
        }

        if (player.goUp)
            player.b -= 30 * scrRatioY;
        else
            player.b += 30 * scrRatioY;

        if (player.b < 0)
            player.b = 0;

        if (player.b >= scrY - player.height)
            player.b = scrY - player.height;

        List<Laser> trash = new ArrayList<>();

        /**for loop for the lasers*/
        for (Laser laser : lasers) {

            if (laser.a > scrX) /**if the laser goes outside the screen in the x-axis, it is removed*/
                trash.add(laser);

            laser.a += 50 * scrRatioX;

            for (Enemy enemy : enemies) {

                if (Rect.intersects(enemy.getCollisionShape(),
                        laser.getCollisionShape())) {

                    score++;
                    enemy.x = -500;
                    laser.a = scrX + 500;
                    enemy.wasHit = true;

                }

            }

        }
        //for loop for the lasers going in the trash or out of the screen constantly
        for (Laser laser : trash)
            lasers.remove(laser);

        //generating enemies randomly with different speeds
        for (Enemy enemy : enemies) {

            enemy.x -= enemy.movSpeed;

            if (enemy.x + enemy.width < 0) {

                if (!enemy.wasHit) {
                    gameOver = true;
                    return;
                }

                int bound = (int) (30 * scrRatioX);
                enemy.movSpeed = random.nextInt(bound);

                if (enemy.movSpeed < 10 * scrRatioX)
                    enemy.movSpeed = (int) (10 * scrRatioX);

                enemy.x = scrX;
                enemy.y = random.nextInt(scrY - enemy.height);

                enemy.wasHit = false;
            }

            //if the 2 hit-boxes collide, game over :)
            if (Rect.intersects(enemy.getCollisionShape(), player.getHitRegistration())) {

                gameOver = true;
                return;
            }

        }

    }

    /**
     * a draw method to display images on the screen
     */
    private void draw () {

        //using getHolder to get the canvas from the SurfaceView
        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(space1.space, space1.a, space1.b, paint);
            canvas.drawBitmap(space2.space, space2.a, space2.b, paint);

            //a for loop for the enemies that are hit
            for (Enemy enemy : enemies)
                canvas.drawBitmap(enemy.getBird(), enemy.x, enemy.y, paint);

            //this will display the canvas on the screen
            canvas.drawText(score + "", scrX / 2f, 164, paint);

            if (gameOver) {
                isRunning = false;
                canvas.drawBitmap(player.getExplosion(), player.a, player.b, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                return;
            }

            canvas.drawBitmap(player.getPlayer(), player.a, player.b, paint);

            //a for loop for drawing the lasers
            for (Laser laser : lasers)
                canvas.drawBitmap(laser.laser, laser.a, laser.b, paint);

            //this will display the canvas on the screen
            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) { //we try and catch so that we can add an exception
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    //creating a stop method in order to pause the game
    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //creating a start method to resume the game
    public void resume () {

        isRunning = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isRunning = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param event
     * @return
     * controlling the player through touch and shooting lasers by tapping on the right side
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < scrX / 2) {
                    player.goUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                player.goUp = false;
                if (event.getX() > scrX / 2)
                    player.shootLaser++;
                break;
        }

        return true;
    }

    public void newLazer() {

        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0, 0, 1);

        Laser laser = new Laser(getResources());
        laser.a = player.a + player.width;
        laser.b = player.b + (player.height / 2);
        lasers.add(laser);

    }
}

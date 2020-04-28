package com.heyletscode.SpaceInvaders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.heyletscode.SpaceInvaders.R;

import static com.heyletscode.SpaceInvaders.viewGame.scrRatioX;
import static com.heyletscode.SpaceInvaders.viewGame.scrRatioY;

public class Player {
    int shootLaser = 0;
    boolean goUp = false;
    int a, b, width, height, ThrusterCounter = 0, laserCounter = 1;

    //creating a bitmap for the spaceship and bullets
    Bitmap player1, player2, shoot1, shoot2, shoot3, shoot4, shoot5, explosion;

    private viewGame gameView;

    Player(viewGame gameView, int scrY, Resources res) {

        this.gameView = gameView;

        player1 = BitmapFactory.decodeResource(res, R.drawable.spaceship1);
        player2 = BitmapFactory.decodeResource(res, R.drawable.spaceship2);

        width = player1.getWidth();
        height = player1.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * scrRatioX); //the reason why we are multiplying it with the ratio is so that the game is compatible with any other device
        height = (int) (height * scrRatioY);

        player1 = Bitmap.createScaledBitmap(player1, width, height, false);
        player2 = Bitmap.createScaledBitmap(player2, width, height, false);

        //these are for the laser effects coming out from the scapeship
        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);

        //resizing the bitmaps for the bullet effect
        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        explosion = BitmapFactory.decodeResource(res, R.drawable.explosion);
        explosion = Bitmap.createScaledBitmap(explosion, width, height, false);

        b = scrY / 2; //since the player will be set vertically, we are going to divide the y-axis by 2 to position the player in the center
        a = (int) (64 * scrRatioX);

    }

    Bitmap getPlayer() {

        if (shootLaser != 0) {

            if (laserCounter == 1) {
                laserCounter++;
                return shoot1;
            }

            if (laserCounter == 2) {
                laserCounter++;
                return shoot2;
            }

            if (laserCounter == 3) {
                laserCounter++;
                return shoot3;
            }

            if (laserCounter == 4) {
                laserCounter++;
                return shoot4;
            }

            //the next time a laser is shot, the animation is looped
            laserCounter = 1;
            shootLaser--;
            gameView.newLazer();

            return shoot5;
        }

        if (ThrusterCounter == 0) {
            ThrusterCounter++;
            return player1;
        }
        ThrusterCounter--;

        return player2;
    }

    Rect getHitRegistration() {
        return new Rect(a, b, a + width, b + height);
    }

    Bitmap getExplosion() {
        return explosion;
    }

}

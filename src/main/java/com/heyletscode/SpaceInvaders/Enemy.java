package com.heyletscode.SpaceInvaders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.heyletscode.SpaceInvaders.R;

import static com.heyletscode.SpaceInvaders.viewGame.scrRatioX;
import static com.heyletscode.SpaceInvaders.viewGame.scrRatioY;

public class Enemy {

    public int movSpeed = 20;
    public boolean wasHit = true;
    int x = 0, y, width, height, enemyCounter = 1;
    Bitmap enemy1, enemy2, enemy3, enemy4;

    Enemy(Resources res) {

        enemy1 = BitmapFactory.decodeResource(res, R.drawable.enemy1);
        enemy2 = BitmapFactory.decodeResource(res, R.drawable.enemy2);
        enemy3 = BitmapFactory.decodeResource(res, R.drawable.enemy3);
        enemy4 = BitmapFactory.decodeResource(res, R.drawable.enemy4);

        width = enemy1.getWidth();
        height = enemy1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * scrRatioX);
        height = (int) (height * scrRatioY);

        enemy1 = Bitmap.createScaledBitmap(enemy1, width, height, false);
        enemy2 = Bitmap.createScaledBitmap(enemy2, width, height, false);
        enemy3 = Bitmap.createScaledBitmap(enemy3, width, height, false);
        enemy4 = Bitmap.createScaledBitmap(enemy4, width, height, false);

        y = -height;
    }

    Bitmap getBird () {

        if (enemyCounter == 1) {
            enemyCounter++;
            return enemy1;
        }

        if (enemyCounter == 2) {
            enemyCounter++;
            return enemy2;
        }

        if (enemyCounter == 3) {
            enemyCounter++;
            return enemy3;
        }

        enemyCounter = 1;

        return enemy4;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}

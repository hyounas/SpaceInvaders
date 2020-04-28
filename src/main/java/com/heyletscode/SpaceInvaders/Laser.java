package com.heyletscode.SpaceInvaders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.heyletscode.SpaceInvaders.R;

import static com.heyletscode.SpaceInvaders.viewGame.scrRatioX;
import static com.heyletscode.SpaceInvaders.viewGame.scrRatioY;

public class Laser {

    int a, b, width, height;
    Bitmap laser;

    Laser(Resources res) {

        laser = BitmapFactory.decodeResource(res, R.drawable.laser);

        width = laser.getWidth();
        height = laser.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * scrRatioX);
        height = (int) (height * scrRatioY);

        laser = Bitmap.createScaledBitmap(laser, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(a, b, a + width, b + height);
    }

}

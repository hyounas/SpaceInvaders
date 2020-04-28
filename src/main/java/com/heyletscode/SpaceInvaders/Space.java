package com.heyletscode.SpaceInvaders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.heyletscode.SpaceInvaders.R;

//this is the background class
public class Space {

    int a = 0, b = 0; //assigning starting values of 0
    Bitmap space;

    //Constructor to take the size of the screen on the x and y axis, a resource object to decode the bitmap from the drawable folder.
    Space(int scrX, int scrY, Resources res) {
        space = BitmapFactory.decodeResource(res, R.drawable.space);
        space = Bitmap.createScaledBitmap(space, scrX, scrY, false);
    }
}

package com.example.amaroge3.project4game;

/**
 * Created by Amaroge3 on 7/18/16.
 */

import android.graphics.*;

/*
Helper class to check if the jar is selected
 */
public class GraphicTools {



    public static boolean graphicSelected (Bitmap inGraphic, float x, float y){

        if (x > MainActivity.getJarLocX()
                && x < (MainActivity.getJarLocX() + inGraphic.getWidth())
                && y > MainActivity.getJarLocY()
                && y < (MainActivity.getJarLocY() + inGraphic.getHeight())
                ){
            return true;
        }
        return false;
    }
}

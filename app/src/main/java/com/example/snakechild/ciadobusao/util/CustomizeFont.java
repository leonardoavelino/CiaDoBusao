package com.example.snakechild.ciadobusao.util;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Ramon on 05/05/2015.
 */
public class CustomizeFont {

    public static void customizeFont(Activity activity, String font, TextView textView){
        Typeface typeFont = Typeface.createFromAsset(activity.getAssets(), font);
        textView.setTypeface(typeFont);

    }
}

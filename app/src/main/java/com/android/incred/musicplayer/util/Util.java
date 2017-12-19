package com.android.incred.musicplayer.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by incred on 19/12/17.
 */

public class Util {

    /**
     * shows and hide animation
     *
     * @param context
     * @param visible
     * @return
     */
    public static Animation showHideAnim(Context context, boolean visible) {
        Animation animation = null;
        if (visible) {
            animation = AnimationUtils.loadAnimation(context, android.support.v7.appcompat.R.anim.abc_slide_in_bottom);
        } else {
            animation = AnimationUtils.loadAnimation(context, android.support.v7.appcompat.R.anim.abc_slide_out_bottom);
        }
        return animation;
    }


    /**
     * returns time in string
     * @param millis
     * @return
     */
    public static String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf.append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }
}

package com.rikkei.meetup.ultis;

import android.animation.ObjectAnimator;
import android.view.View;

public class AnimUtils {

    public static void translateY(View view, float from, float to) {
        ObjectAnimator mover = ObjectAnimator.ofFloat(view, "translationY", from, to);
        mover.setDuration(1000);
        mover.start();
    }
}

package com.wkz.framework.model;


import android.app.Activity;

import com.wkz.framework.R;


public class FRActivityAnimator {

    public void appearTopLeft(Activity a) {
        a.overridePendingTransition(R.anim.fr_appear_top_left_in, R.anim.fr_appear_top_left_out);
    }

    public void appearBottomRight(Activity a) {
        a.overridePendingTransition(R.anim.fr_appear_bottom_right_in, R.anim.fr_appear_bottom_right_out);
    }

    public void disappearBottomRight(Activity a) {
        a.overridePendingTransition(R.anim.fr_disappear_bottom_right_in, R.anim.fr_disappear_bottom_right_out);
    }

    public void disappearTopLeft(Activity a) {
        a.overridePendingTransition(R.anim.fr_disappear_top_left_in, R.anim.fr_disappear_top_left_out);
    }

    public void fade(Activity a) {
        a.overridePendingTransition(R.anim.fr_fade_in, R.anim.fr_fade_out);
    }

    public void flipHorizontal(Activity a) {
        a.overridePendingTransition(R.anim.fr_flip_horizontal_in, R.anim.fr_flip_horizontal_out);
    }

    public void flipVertical(Activity a) {
        a.overridePendingTransition(R.anim.fr_flip_vertical_in, R.anim.fr_flip_vertical_out);
    }

    public void jump(Activity a) {
        a.overridePendingTransition(R.anim.fr_jump_from_down, R.anim.fr_jump_to_down);
    }

    public void pullLeftPushLeft(Activity a) {
        a.overridePendingTransition(R.anim.fr_pull_in_left, R.anim.fr_push_out_left);
    }

    public void pullLeftPushRight(Activity a) {
        a.overridePendingTransition(R.anim.fr_pull_in_left, R.anim.fr_push_out_right);
    }

    public void pullTopPushDown(Activity a) {
        a.overridePendingTransition(R.anim.fr_pull_in_top, R.anim.fr_push_out_down);
    }

    public void pullTopPushTop(Activity a) {
        a.overridePendingTransition(R.anim.fr_pull_in_top, R.anim.fr_push_out_top);
    }

    public void pullRightPushLeft(Activity a) {
        a.overridePendingTransition(R.anim.fr_pull_in_right, R.anim.fr_push_out_left);
    }

    public void pullRightPushRight(Activity a) {
        a.overridePendingTransition(R.anim.fr_pull_in_right, R.anim.fr_push_out_right);
    }

    public void pullDownPushTop(Activity a) {
        a.overridePendingTransition(R.anim.fr_pull_in_down, R.anim.fr_push_out_top);
    }

    public void pullDownPushDown(Activity a) {
        a.overridePendingTransition(R.anim.fr_pull_in_down, R.anim.fr_push_out_down);
    }

    public void rotateDown(Activity a) {
        a.overridePendingTransition(R.anim.fr_rotate_down_in, R.anim.fr_rotate_down_out);
    }

    public void rotateUp(Activity a) {
        a.overridePendingTransition(R.anim.fr_rotate_up_in, R.anim.fr_rotate_up_out);
    }

    public void scale(Activity a) {
        a.overridePendingTransition(R.anim.fr_scale_up, R.anim.fr_scale_down);
    }

    public void unzoom(Activity a) {
        a.overridePendingTransition(R.anim.fr_unzoom_in, R.anim.fr_unzoom_out);
    }

    public void zoom(Activity a) {
        a.overridePendingTransition(R.anim.fr_zoom_in, R.anim.fr_zoom_out);
    }
}

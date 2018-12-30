package com.wkz.framework.model;


import android.app.Activity;
import android.support.annotation.StringDef;

import com.wkz.framework.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class FRActivityAnimator {

    @StringDef({
            Animator.APPEAR_TOP_LEFT,
            Animator.APPEAR_BOTTOM_RIGHT,
            Animator.DISAPPEAR_BOTTOM_RIGHT,
            Animator.DISAPPEAR_TOP_LEFT,
            Animator.FADE,
            Animator.FLIP_HORIZONTAL,
            Animator.FLIP_VERTICAL,
            Animator.JUMP,
            Animator.PULL_LEFT_PUSH_LEFT,
            Animator.PULL_LEFT_PUSH_RIGHT,
            Animator.PULL_TOP_PUSH_DOWN,
            Animator.PULL_TOP_PUSH_TOP,
            Animator.PULL_RIGHT_PUSH_LEFT,
            Animator.PULL_RIGHT_PUSH_RIGHT,
            Animator.PULL_DOWN_PUSH_TOP,
            Animator.PULL_DOWN_PUSH_DOWN,
            Animator.ROTATE_DOWN,
            Animator.ROTATE_UP,
            Animator.SCALE,
            Animator.UNZOOM,
            Animator.ZOOM,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Animator {
        String APPEAR_TOP_LEFT = "appearTopLeft";
        String APPEAR_BOTTOM_RIGHT = "appearBottomRight";
        String DISAPPEAR_BOTTOM_RIGHT = "disappearBottomRight";
        String DISAPPEAR_TOP_LEFT = "disappearTopLeft";
        String FADE = "fade";
        String FLIP_HORIZONTAL = "flipHorizontal";
        String FLIP_VERTICAL = "flipVertical";
        String JUMP = "jump";
        String PULL_LEFT_PUSH_LEFT = "pullLeftPushLeft";
        String PULL_LEFT_PUSH_RIGHT = "pullLeftPushRight";
        String PULL_TOP_PUSH_DOWN = "pullTopPushDown";
        String PULL_TOP_PUSH_TOP = "pullTopPushTop";
        String PULL_RIGHT_PUSH_LEFT = "pullRightPushLeft";
        String PULL_RIGHT_PUSH_RIGHT = "pullRightPushRight";
        String PULL_DOWN_PUSH_TOP = "pullDownPushTop";
        String PULL_DOWN_PUSH_DOWN = "pullDownPushDown";
        String ROTATE_DOWN = "rotateDown";
        String ROTATE_UP = "rotateUp";
        String SCALE = "scale";
        String UNZOOM = "unzoom";
        String ZOOM = "unzoom";
    }

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

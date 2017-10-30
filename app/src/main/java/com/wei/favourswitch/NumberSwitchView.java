package com.wei.favourswitch;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public class NumberSwitchView extends View {

    int oldNum;
    int newNum;

    float ratio;

    AnimatorSet mAnimatorSet;

    Paint viewPaint;

    List<Integer> diffPositionList;

    public NumberSwitchView(Context context) {
        super(context);
        init();
    }

    public NumberSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        viewPaint.setTextSize(40);
        viewPaint.setColor(Color.RED);

        Paint.FontMetrics fontMetrics = viewPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;

        Rect bounds = new Rect();
        String oldStr = String.valueOf(oldNum);

        viewPaint.getTextBounds(oldStr, 0, oldStr.length(), bounds);
        canvas.drawText(oldStr, 0, fontHeight, viewPaint);

        String newStr = String.valueOf(newNum);
        canvas.drawText(newStr, 0, fontHeight + viewPaint.getFontSpacing(), viewPaint);


    }

    private void init() {
        viewPaint = new Paint();
        viewPaint.setAntiAlias(true);

        diffPositionList = new ArrayList<>();

        oldNum = 123;
        newNum = 124;
    }

    private void compareStr(){

    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(
                this, "ratio", 0f, 1f);
        moveAnim.setRepeatCount(ValueAnimator.INFINITE);
        moveAnim.setDuration(1000);
        moveAnim.setInterpolator(new LinearInterpolator());
        animators.add(moveAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    public void startAnimation() {
        if (mAnimatorSet == null) {
            initAnimation();
        }
        mAnimatorSet.start();
    }


    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        if (this.ratio != ratio) {
            this.ratio = ratio;
            postInvalidate();
        }
    }
}

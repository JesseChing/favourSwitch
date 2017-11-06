package com.wei.favourswitch;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

    float ratio = 1f;

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

//        Paint.FontMetrics fontMetrics = viewPaint.getFontMetrics();
//        float fontHeight = fontMetrics.bottom - fontMetrics.top;
//
//        Rect bounds = new Rect();
//        String oldStr = String.valueOf(oldNum);
//
//        viewPaint.getTextBounds(oldStr, 0, oldStr.length(), bounds);
//        canvas.drawText(oldStr, 0, fontHeight, viewPaint);
//
//        String newStr = String.valueOf(newNum);
//        canvas.drawText(newStr, 0, fontHeight + viewPaint.getFontSpacing(), viewPaint);

        drawNum(canvas);


    }

    private void init() {
        viewPaint = new Paint();
        viewPaint.setAntiAlias(true);

        diffPositionList = new ArrayList<>();

        oldNum = 123;
        newNum = 22350;
    }

    private void compareStr(){

    }

    private void drawNum(Canvas canvas) {
        String oldStr = String.valueOf(oldNum);
        String newStr = String.valueOf(newNum);

        viewPaint.setTextSize(40);
        viewPaint.setColor(Color.RED);

        Paint.FontMetrics fontMetrics = viewPaint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float[] strWidths; //获取每个字符的宽度

        short direction = 0;
        int maxLength = 0;
        if (oldNum > newNum) {
            direction = -1;
            maxLength = oldStr.length();
            strWidths = new float[maxLength];
            viewPaint.getTextWidths(oldStr, strWidths);
        } else {
            direction = 1;
            maxLength = newStr.length();
            strWidths = new float[maxLength];
            viewPaint.getTextWidths(newStr, strWidths);
        }


        RectF rect = new RectF(0, 10, viewPaint.measureText(newStr), fontHeight+10);
        canvas.save();
        canvas.clipRect(rect);

        float x_index = 0;
        for (int i = 0; i < maxLength; i++) {
            String subStr = "", subStr2 = "";
            if (i < oldStr.length()) {
                subStr = oldStr.substring(i, i + 1);
            }

            if (i < newStr.length()) {
                subStr2 = newStr.substring(i, i + 1);
            }

            int cmpResult = subStr.compareTo(subStr2);
            if (cmpResult > 0) {
                direction = -1;
            } else if (cmpResult < 0) {
                direction = 1;
            } else {
                direction = 0;
            }

            if (!"".equals(subStr)) {
                if (direction == 0) {
                    canvas.drawText(subStr, x_index, fontHeight, viewPaint);
                } else {
                    canvas.drawText(subStr, x_index, fontHeight - viewPaint.getFontSpacing() * direction * (1f - ratio), viewPaint);
                }
            }

            if (!"".equals(subStr2) && direction != 0) {
                canvas.drawText(subStr2, x_index, fontHeight + viewPaint.getFontSpacing() * direction * ratio, viewPaint);
            }

            x_index += strWidths[i];

        }

        canvas.restore();


    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(
                this, "ratio", 1f, 0f);
        moveAnim.setRepeatCount(-1);
//        moveAnim.setRepeatMode(Animation.RESTART);
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

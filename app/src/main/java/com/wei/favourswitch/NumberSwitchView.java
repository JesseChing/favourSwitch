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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public class NumberSwitchView extends View implements Animator.AnimatorListener {

    int oldNum;
    int newNum;

    float ratio = 0f;

    AnimatorSet mAnimatorSet;

    Paint viewPaint;

//    List<Integer> diffPositionList;

    float fontHeight;
    RectF contentRect;

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

        if (ratio == 0 || oldNum == newNum) {
            canvas.drawText(String.valueOf(newNum), 0, fontHeight, viewPaint);
        } else {
            drawAnimatorNum(canvas);
        }

    }

    private void init() {
        oldNum = 0;
        newNum = 22350;

        viewPaint = new Paint();
        viewPaint.setAntiAlias(true);

        viewPaint.setTextSize(40);
        viewPaint.setColor(Color.RED);


//        diffPositionList = new ArrayList<>();

        Paint.FontMetrics fontMetrics = viewPaint.getFontMetrics();
        fontHeight = fontMetrics.bottom - fontMetrics.top;

        contentRect = new RectF(0, 10, viewPaint.measureText(String.valueOf(newNum)), fontHeight + 10);


    }


    private void drawAnimatorNum(Canvas canvas) {
        String oldStr = String.valueOf(oldNum);
        String newStr = String.valueOf(newNum);



        float[] strWidths; //获取每个字符的宽度

        short direction = 0;
        int maxLength = 0;
        if (oldNum > newNum) {
//            direction = -1;
            maxLength = oldStr.length();
            strWidths = new float[maxLength];
            viewPaint.getTextWidths(oldStr, strWidths);
        } else {
//            direction = 1;
            maxLength = newStr.length();
            strWidths = new float[maxLength];
            viewPaint.getTextWidths(newStr, strWidths);
        }



        canvas.save();
        canvas.clipRect(contentRect);

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
        moveAnim.addListener(this);
        animators.add(moveAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    public void startAnimation() {
        if (mAnimatorSet == null) {
            initAnimation();
        }

        if (oldNum == newNum) {
            return;
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

    public int getNewNum() {
        return newNum;
    }

    public void setNewNum(int newNum, boolean animation) {
        if (this.newNum == newNum){
            return;
        }
        this.newNum = newNum;
        if (animation){
            startAnimation();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
//        Toast.makeText(getContext(), "结束", Toast.LENGTH_SHORT).show();
        oldNum = newNum;
//        invalidate();
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        oldNum = newNum;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}

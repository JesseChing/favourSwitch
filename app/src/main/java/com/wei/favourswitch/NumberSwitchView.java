package com.wei.favourswitch;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public class NumberSwitchView extends View implements Animator.AnimatorListener {

    int oldNum = 0; //旧的值
    int newNum = 0; //新的值

    int maxNum;

    float ratio = 0f;

    AnimatorSet mAnimatorSet;

    Paint viewPaint;

//    List<Integer> diffPositionList;

    float fontHeight;
    Rect contentRect;

    int textSize = 10;
    int textColor = Color.BLACK;

    Drawable iconDrawable;
    Rect iconRect;

    public NumberSwitchView(Context context) {
        super(context);
        init(context, null);
    }

    public NumberSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        if (iconDrawable != null) {
            drawIcon(canvas);

            canvas.save();
            canvas.translate(iconRect.width(), 0);
        }


        if (ratio == 0 || oldNum == newNum) {
            canvas.drawText(String.valueOf(newNum), 0, fontHeight, viewPaint);
        } else {
            drawAnimatorNum(canvas);
        }

        if (iconDrawable != null) {
            canvas.restore();
        }

        canvas.restore();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        if (w!= oldw || h!=oldh){
//
//        }

        Log.d("onSizeChanged", "w:" + w + "h:" + h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int minWidth = getMinimumWidth();
        int minHeight = getMinimumHeight();

        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST: {
                width = (int) contentRect.width() + getPaddingLeft() + getPaddingRight();
                if (iconRect != null) {
                    width += iconRect.width();
                }

                width = Math.max(width, minWidth);
                break;
            }

            case MeasureSpec.EXACTLY: {
                width = widthSize;
                break;
            }

            default:
                break;
        }

        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST: {
                height = Math.round(fontHeight) + getPaddingTop() + getPaddingBottom();
                height = Math.max(height, iconRect.height());
                height = Math.max(height, minHeight);
                break;
            }

            case MeasureSpec.EXACTLY: {
                height = heightSize;
                break;
            }

            default:
                break;
        }


        setMeasuredDimension(width, height);
    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (iconDrawable != null && iconDrawable.isStateful()) {
            iconDrawable.setState(getDrawableState());
        }
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberSwitchView);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.NumberSwitchView_number: {
                    newNum = a.getInt(attr, newNum);
                    break;
                }

                case R.styleable.NumberSwitchView_android_textSize: {
                    textSize = a.getDimensionPixelSize(attr, textSize);
                    break;
                }

                case R.styleable.NumberSwitchView_android_textColor: {
                    textColor = a.getColor(attr, textColor);
                    break;
                }

                case R.styleable.NumberSwitchView_icon: {
                    iconDrawable = a.getDrawable(attr);
                    break;
                }

                default: {

                }
            }
        }
        a.recycle();

        oldNum = 22350;
        newNum = 22351;

        viewPaint = new Paint();
        viewPaint.setAntiAlias(true);

        viewPaint.setTextSize(textSize);
        viewPaint.setColor(Color.RED);

        Paint.FontMetrics fontMetrics = viewPaint.getFontMetrics();
        fontHeight = Math.abs(fontMetrics.bottom - fontMetrics.top);


        maxNum = Math.max(oldNum, newNum);
//        contentRect = new RectF(0, 0, viewPaint.measureText(String.valueOf(maxNum)), fontHeight);

        Rect tempRect = new Rect();
        String maxNumStr = String.valueOf(maxNum);
        viewPaint.getTextBounds(maxNumStr, 0, maxNumStr.length(), tempRect);
        contentRect = new Rect(0, 0, (int) viewPaint.measureText(maxNumStr), tempRect.height());

        if (iconDrawable != null) {
            int iconWidth = iconDrawable.getIntrinsicWidth();
            int iconHeight = iconDrawable.getIntrinsicHeight();
            iconRect = new Rect(0, 0, iconWidth, iconHeight);
            iconDrawable.setBounds(iconRect);

            float diffHeight = Math.abs(contentRect.height() - iconRect.height());
            contentRect.offset(0, Math.round(diffHeight * 0.5f));
        }

        setSelected(true);

    }


    private void drawIcon(Canvas canvas) {

        iconDrawable.draw(canvas);

    }

    private void drawAnimatorNum(Canvas canvas) {
        String oldStr = String.valueOf(oldNum);
        String newStr = String.valueOf(newNum);

        canvas.getMatrix();


        float[] strWidths; //获取每个字符的宽度

        short direction = 0;
        int maxLength = 0;
        if (oldNum > newNum) {
            maxLength = oldStr.length();
            strWidths = new float[maxLength];
            viewPaint.getTextWidths(oldStr, strWidths);
        } else {
            maxLength = newStr.length();
            strWidths = new float[maxLength];
            viewPaint.getTextWidths(newStr, strWidths);
        }


//        canvas.drawRect(contentRect,viewPaint);
//        canvas.drawLine(contentRect.left, contentRect.top, contentRect.right, contentRect.top, viewPaint);
//        canvas.drawLine(contentRect.left, contentRect.bottom, contentRect.right, contentRect.bottom, viewPaint);

        canvas.save();
        canvas.clipRect(contentRect);

        float x_index = contentRect.left;
        float height = contentRect.bottom;


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
                    canvas.drawText(subStr, x_index, height, viewPaint);
                } else {
                    canvas.drawText(subStr, x_index, height - viewPaint.getFontSpacing() * direction * (1f - ratio), viewPaint);
                }
            }

            if (!"".equals(subStr2) && direction != 0) {
                canvas.drawText(subStr2, x_index, height + viewPaint.getFontSpacing() * direction * ratio, viewPaint);
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
        if (this.newNum == newNum) {
            return;
        }
        this.newNum = newNum;
        if (animation) {
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

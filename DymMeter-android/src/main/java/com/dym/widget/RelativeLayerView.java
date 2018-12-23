package com.dym.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dym.utils.UIUtils;

/**
 * RelativeLayerView class
 *
 * @author hangwei
 * @date 2018/12/16
 */
public class RelativeLayerView extends View {
    public static final String TAG = "LayerView";
    private int txtSize;
    private Paint paint;

    private int[] location1 = new int[2];
    private int[] location2 = new int[2];

    public RelativeLayerView(Context context) {
        this(context,null);
        Log.i(TAG,"create view");
    }

    public RelativeLayerView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtSize = UIUtils.dp2px(this.getContext(), 12);
        paint.setTextSize(txtSize);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            clean();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != VISIBLE) {
            clean();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        View root = getRootView();
        canvas.translate(-root.getPaddingLeft(), -root.getPaddingTop());

        if (firstView == secondView || firstView == null) {
            return;
        }

        drawBorder(canvas, firstView, location1);
        if (secondView == null) {
            drawDistance(location1, canvas);
            return;
        } else {
            drawBorder(canvas, secondView, location2);
            drawDistance(location2, canvas);
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff000000);
        //2 在 1 左边
        if (location2[0] + secondView.getWidth() < location1[0]) {
            int x = location2[0] + secondView.getWidth();
            int y = location2[1] + secondView.getHeight() / 2;
            int length = location1[0] - (location2[0] + secondView.getWidth());

            canvas.save();
            canvas.drawLine(x, y, x+length, y, paint);
            drawNumWithBg(canvas, length, x+length/2, y);
            canvas.restore();
        }
        //2 在 1 右边
        if (location2[0] > location1[0] + firstView.getWidth()) {
            int x = location2[0];
            int y = location2[1] + secondView.getHeight() / 2;
            int length = location2[0] - (location1[0] + firstView.getWidth());

            canvas.save();
            canvas.drawLine(x-length, y, x, y, paint);
            drawNumWithBg(canvas, length, x-length/2, y);
            canvas.restore();
        }
        //2 在 1 上边
        if (location2[1] + secondView.getHeight() < location1[1]) {
            int x = location2[0] + secondView.getWidth() / 2;
            int y = location2[1] + secondView.getHeight();
            int length = location1[1] - (location2[1] + secondView.getHeight());

            canvas.save();
            canvas.drawLine(x, y, x, y+length, paint);
            drawNumWithBg(canvas, length, x, y+length/2);
            canvas.restore();
        }

        //2 在 1 下边
        if (location2[1] > location1[1] + firstView.getHeight()) {
            int x = location2[0] + secondView.getWidth() / 2;
            int y = location2[1];
            int length = location2[1] - (location1[1] + firstView.getHeight());

            canvas.save();
            canvas.drawLine(x, y-length, x, y, paint);
            drawNumWithBg(canvas, length, x, y-length/2);
            canvas.restore();
        }
    }

    /**
     * 画数字
     *
     * @param canvas 画布
     * @param num 数字
     * @param x 字符串中心点横坐标
     * @param y 字符串中心点纵坐标
     */
    private void drawNumWithBg(Canvas canvas, int num, int x, int y){
        String txt = String.valueOf(UIUtils.px2dp(getContext(), num));
        float txtLength = paint.measureText(txt);
        paint.setColor(0xffffffff);
        canvas.drawRect(x - txtLength / 2, y - txtSize / 2, x + txtLength / 2, y + txtSize / 2, paint);
        paint.setColor(0xff000000);
        canvas.drawText(txt, x - txtLength / 2, y + txtSize / 2, paint);
    }

    /**
     * 画边框
     * @param canvas
     * @param view
     * @param location
     */
    private void drawBorder(Canvas canvas, View view, int[] location) {
        view.getLocationInWindow(location);

        int viewH = view.getHeight();
        int viewW = view.getWidth();

        canvas.save();
        // 画框
        paint.setColor(0xFFDD0000);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight(), paint);

        // 画角
        paint.setColor(0xFF0000DD);
        paint.setStrokeWidth(3);
        if(view.getWidth() > 8 && view.getHeight() > 8){
            int dx = 20;
            int dy = 20;
            int x = location[0] + 1;
            int y = location[1] + 1;

            // 上
            canvas.drawLine(x, y, x+dx, y, paint);
            canvas.drawLine(x+viewW-1-dx, y, x+viewW-1, y, paint);

            // 下
            y = location[1] + viewH - 1;
            canvas.drawLine(x, y, x+dx, y, paint);
            canvas.drawLine(x+viewW-1-dx, y, x+viewW-1, y, paint);

            // 左
            y = location[1] + 1;
            canvas.drawLine(x, y, x, y+dy, paint);
            canvas.drawLine(x, y+viewH-1-dy, x, y+viewH-1, paint);

            // 右
            x = location[0] + viewW - 1;
            canvas.drawLine(x, y, x, y+dy, paint);
            canvas.drawLine(x, y+viewH-1-dy, x, y+viewH-1, paint);
        }

        paint.setStrokeWidth(2);

        canvas.restore();
    }

    private void drawDistance(int[] location, Canvas canvas){
        int x = location[0] + 30;
        int y = location[1] + 30;

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);

        paint.setColor(0xff00ff00);

        if(location[0] != 0){
            canvas.drawLine(0, y, location[0], y, paint);
            drawNumWithBg(canvas, location[0], location[0]/2, y);
        }

        if(location[1] != 0){
            canvas.drawLine(x, 0, x, location[1], paint);
            drawNumWithBg(canvas, location[1], x, location[1]/2);
        }

    }

    private View firstView;
    private View secondView;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        invalidate();// TODO: 2018/12/17 这个是干啥的
        super.dispatchTouchEvent(event);
        Log.i(TAG, "dispatchTouchEvent");

        int curX = (int) event.getRawX();
        int curY = (int) event.getRawY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            findPressView(curX, curY);
        }

        return true;
    }

    protected void findPressView(int x, int y) {
        View rootView = getRootView();
        View targetView = traversal(rootView, x, y);

        if (targetView == null || targetView == rootView) {
            Log.d(TAG, "findPressView, targetView == null");
            return;
        }

        if( targetView == firstView || targetView == secondView){
            Log.d(TAG, "findPressView, targetView has selected.");
            return;
        }

        if (firstView == null) {
            firstView = targetView;
            secondView = null;
        } else {
            if(secondView == null){
                secondView = targetView;
            }else{
                firstView = secondView;
                secondView = targetView;
            }
        }

        Log.i(TAG,"findPressView view="+firstView);

        invalidate();
    }

    private View traversal(View view, int x, int y) {
        if (this == view) {
            return null;
        }
        if (view.getVisibility() != VISIBLE) {
            return null;

        }
        if (!inRange(view, x, y)) {
            return null;
        }

        View rtnView = view;

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = ((ViewGroup) view);
            View child;
            View childRtnView;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                childRtnView = traversal(child, x, y);
                if (childRtnView != null) {
                    rtnView = childRtnView;
                    break;
                }
            }
        }

        return rtnView;
    }

    private int location[] = new int[2];

    private boolean inRange(View view, int x, int y) {
        view.getLocationOnScreen(location);

        return (location[0] <= x
                && location[1] <= y
                && location[0] + view.getWidth() >= x
                && location[1] + view.getHeight() >= y);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clean();
    }

    private void clean() {
        firstView = secondView = null;
    }
}

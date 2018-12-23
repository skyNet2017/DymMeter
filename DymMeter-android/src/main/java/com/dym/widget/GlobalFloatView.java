package com.dym.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dym.meter.R;

/**
 * GlobalFloatView class 悬浮按钮控件。
 * 方案一：使用 WindowManager 直接添加到窗口中去，这个测量小工具希望在APPlication初始化的时候启动，但是悬浮窗需要申请权限，存在冲突。
 * 方案二：把悬浮窗加到界面的根部局中去，缺点是只能在应用内使用，后因为没有实现跨进程测量，所以这个方案也够用。
 *
 * @author hangwei
 * @date 2018/12/22
 */
public class GlobalFloatView extends ImageView {

    private IFloatStrategy floatStrategy;

    public GlobalFloatView(Context context) {
        super(context);
        init();
    }

    public void showView(ViewGroup rootView){
        setTag(rootView);
        floatStrategy.show(this);
    }

    public void hideView(){
        floatStrategy.hide(this);
    }

    public void updateView(int offsetX, int offsetY){
        floatStrategy.update(this, offsetX, offsetY);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected){
            setColorFilter(Color.parseColor("#1296db"));
        }else{
            setColorFilter(Color.parseColor("#bfbfbf"));
        }
    }

    private void init(){
        // 使用方案二实现悬浮窗
        floatStrategy = new FloatStrategyRootVIewImpl();

        setImageResource(R.drawable.switch_src);
        setColorFilter(Color.parseColor("#bfbfbf"));
        setOnTouchListener(new FloatViewOnTouchListener());
    }

    private class FloatViewOnTouchListener implements View.OnTouchListener {
        private int downX;
        private int downY;

        private int nowX;
        private int nowY;

        private static final int TOUCH_SLOP = 20;
        private static final long TIME_GAP = 1000;

        boolean isConsumeEvent = false;

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                floatStrategy.hide(GlobalFloatView.this);
            }
        };

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    nowX = downX = (int) event.getRawX();
                    nowY = downY = (int) event.getRawY();

                    postDelayed(runnable, TIME_GAP);
                    isConsumeEvent = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();
                    int movedX = x - nowX;
                    int movedY = y - nowY;
                    nowX = x;
                    nowY = y;

                    if(Math.abs(nowX-downX) > TOUCH_SLOP || Math.abs(nowY-downY) > TOUCH_SLOP){
                        removeCallbacks(runnable);
                        isConsumeEvent = true;
                    }

                    updateView(movedX, movedY);
                    break;
                case MotionEvent.ACTION_UP:
                    removeCallbacks(runnable);
                    break;
                default:
                    break;
            }
            return isConsumeEvent;
        }
    }
}

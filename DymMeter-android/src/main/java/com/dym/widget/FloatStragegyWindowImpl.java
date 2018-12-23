package com.dym.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * FloatStragegyWindowImpl class
 *
 * @author hangwei
 * @date 2018/12/23
 */
public class FloatStragegyWindowImpl implements IFloatStrategy {

    private WindowManager windowManager;

    @Override
    public void show(View floatView){
        if (floatView == null) {
            return;
        }

        if (windowManager == null) {
            windowManager = (WindowManager) floatView.getContext().getSystemService(Context.WINDOW_SERVICE);
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.width = 222;
        layoutParams.height = 200;
        layoutParams.x = 20;
        layoutParams.y = 20;

        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(floatView, layoutParams);
    }

    @Override
    public void hide(View floatView) {
        if (floatView == null) {
            return;
        }

        if (windowManager != null) {
            windowManager.removeView(floatView);
        }
    }

    @Override
    public void update(View floatView, int moveX, int moveY) {
        if (floatView == null) {
            return;
        }

        if (windowManager != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) floatView.getLayoutParams();
            layoutParams.x = layoutParams.x + moveX;
            layoutParams.y = layoutParams.y + moveY;
            windowManager.updateViewLayout(floatView, layoutParams);
        }
    }
}

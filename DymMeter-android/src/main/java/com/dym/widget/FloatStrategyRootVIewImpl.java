package com.dym.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * FloatStrategyRootVIewImpl class
 *
 * @author hangwei
 * @date 2018/12/23
 */
public class FloatStrategyRootVIewImpl implements IFloatStrategy {
    @Override
    public void show(View floatView) {
        if (floatView == null) {
            return;
        }

        ViewGroup rootView = ((ViewGroup) floatView.getTag());
        if (rootView == null) {
            return;
        }

        ViewGroup parentView = (ViewGroup) floatView.getParent();
        if(parentView == rootView){
            return;
        }

        if(parentView != null){
            hide(floatView);
        }

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) floatView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new FrameLayout.LayoutParams(222, 200);
            layoutParams.topMargin = 800;
        } else {
            layoutParams.width = 222;
            layoutParams.height = 200;
        }
        floatView.setLayoutParams(layoutParams);

        rootView.addView(floatView);
    }

    @Override
    public void hide(View floatView) {
        if (floatView == null) {
            return;
        }

        ViewGroup parentView = (ViewGroup) floatView.getParent();
        if (parentView != null) {
            parentView.removeView(floatView);
        }
    }

    @Override
    public void update(View floatView, int moveX, int moveY) {
        if (floatView == null) {
            return;
        }

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) floatView.getLayoutParams();
        layoutParams.topMargin += moveY;
        layoutParams.leftMargin += moveX;
        floatView.setLayoutParams(layoutParams);
    }
}

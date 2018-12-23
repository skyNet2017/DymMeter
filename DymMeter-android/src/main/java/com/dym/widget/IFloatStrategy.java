package com.dym.widget;

import android.view.View;

/**
 * IFloatStrategy class
 *
 * @author hangwei
 * @date 2018/12/23
 */
public interface IFloatStrategy {
    /**
     * 展示悬浮窗
     */
    void show(View floatView);

    /**
     * 隐藏悬浮窗
     */
    void hide(View floatView);

    /**
     * 移动悬浮窗
     * @param moveX x轴移动距离
     * @param moveY y轴移动距离
     */
    void update(View floatView, int moveX, int moveY);
}

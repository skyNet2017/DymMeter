package com.dym.meter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.dym.widget.CoverView;
import com.dym.widget.GlobalFloatView;

/**
 * DymMeter.java 是测量工具管理类。主要协调三大组件。
 * 1、coverLayout：测试模式下，覆盖在源界面（被测量界面）上面的那层透明view。主要作用是拦截触摸事件，判断出用户要测量的view再绘制边距；
 * 2、floatView：开关测量模式的悬浮按钮；只在应用内可用
 * 3、AppWindowWatcher：在非测量模式下，监视窗口变化，确保进入测量模式后能正确的拿到被测量的窗口界面。
 *
 * @author hangwei
 * @date 2018/12/16
 */
public class DymMeter {
    public static final String TAG = "DymMeter";
    private static DymMeter INSTANCE;

    private ViewGroup rootView;

    private CoverView coverLayout;
    private GlobalFloatView floatView;
    private AppWindowWatcher appWindowWatcher;

    /**
     * 显示悬浮图标
     * @param context 应用上下文
     */
    public static void install(Context context){
        if (INSTANCE == null) {
            INSTANCE = new DymMeter(context);
        }
    }

    private DymMeter(Context context){
        coverLayout = new CoverView(context.getApplicationContext());

        floatView = new GlobalFloatView(context.getApplicationContext());
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(INSTANCE.isMeasureMode()){
                    exitMeasureMode();
                } else {
                    enterMeasureMode();
                }
            }
        });

        appWindowWatcher = new AppWindowWatcher(new AppWindowWatcher.Callback() {
            @Override
            public void onWindowChanged(ViewGroup rootView) {
                DymMeter.this.rootView = rootView;
                exitMeasureMode();
            }
        });
    }

    private void enterMeasureMode(){
        coverLayout.attachToRoot(rootView);
        setFloatViewToRoot(coverLayout);
    }

    private void exitMeasureMode(){
        coverLayout.detachFromRoot();
        setFloatViewToRoot(rootView);
    }

    private boolean isMeasureMode(){
        return coverLayout.getParent() != null;
    }

    private void setFloatViewToRoot(ViewGroup rootView){
        if(rootView == null){
            floatView.hideView();
        }else{
            floatView.showView(rootView);
        }

        floatView.setSelected(isMeasureMode());
    }
}

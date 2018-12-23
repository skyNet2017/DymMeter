package com.dym.meter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManagerGlobal;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * AppWindowWatcher class
 *
 * @author hangwei
 * @date 2018/12/23
 */
public class AppWindowWatcher {
    private static final String TAG = "AppWindowWatcher";

    private Callback mWatcherCallback;

    public AppWindowWatcher(Callback watcherCallback) {
        mWatcherCallback = watcherCallback;

        WindowManagerGlobal managerGlobal = WindowManagerGlobal.getInstance();
        try {
            Field mViewsFiled = WindowManagerGlobal.class.getDeclaredField("mViews");
            mViewsFiled.setAccessible(true);

            synchronized (managerGlobal) {
                List<View> views = (List<View>) mViewsFiled.get(managerGlobal);

                ViewGroup initRootView = findTopRootView(views);
                if (initRootView != null) {
                    notifyWindowChange(initRootView);
                }

                mViewsFiled.set(managerGlobal, new ArrayList<View>(views) {
                    @Override
                    public boolean add(final View view) {
                        boolean b = super.add(view);
                        Log.i(TAG, "add new root view, "+view);
                        if (view instanceof FrameLayout) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    notifyWindowChange((ViewGroup) view);
                                }
                            });
                        }
                        return b;
                    }

                    @Override
                    public View remove(int index) {
                        View view = super.remove(index);
                        notifyWindowChange(findTopRootView(this));
                        Log.d(TAG, "remove old root view  "+view+" index="+index+", listSize="+size());
                        return view;
                    }
                });
            }
            mViewsFiled.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置窗口变化监听器
     * @param watcherCallback 窗口切换时回调
     */
    public void setWatcherCallback(Callback watcherCallback) {
        mWatcherCallback = watcherCallback;
    }

    /**
     * 第一个 FrameLayout 就是顶层的视图的根布局
     * @param views 应用中所有窗口根布局集合
     * @return 返回顶层窗口根布局
     */
    private ViewGroup findTopRootView(List<View> views){
        if (views == null) {
            return null;
        }

        for (View view : views) {
            if (view instanceof FrameLayout) {
                return (ViewGroup) view;
            }
        }

        return null;
    }

    private void notifyWindowChange(ViewGroup newRootView){
        if (mWatcherCallback != null) {
            mWatcherCallback.onWindowChanged(newRootView);
        }
    }

    interface Callback{
        void onWindowChanged(ViewGroup rootView);
    }
}

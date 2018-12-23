package com.dym.widget;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dym.meter.R;

/**
 * 测量模式下覆盖在被测量界面的View管理类
 */
public class CoverView extends FrameLayout {
    private static final String TAG = CoverView.class.getSimpleName();

    public CoverView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.cover_layout, this);
    }

    public void attachToRoot(ViewGroup rootView) {
        if (rootView == null) {
            Log.e(TAG, "showView, but rootView == null");
            return;
        }

        int h = rootView.getHeight() - rootView.getPaddingTop() - rootView.getPaddingBottom();
        int w = rootView.getWidth() - rootView.getPaddingLeft() - rootView.getPaddingRight();

        if (w > 0 && h > 0) {
            setLayoutParams(new FrameLayout.LayoutParams(w, h));
        }

        rootView.addView(this);
    }

    public void detachFromRoot(){
        ViewGroup parentView = (ViewGroup) getParent();
        if (parentView != null) {
            parentView.removeView(this);
        }
    }
}

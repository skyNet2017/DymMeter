package com.dym.dymmeter;

import android.app.Application;

import com.dym.meter.DymMeter;

/**
 * DymApplication class
 *
 * @author hangwei
 * @date 2018/12/23
 */
public class DymApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DymMeter.install(this);
    }
}

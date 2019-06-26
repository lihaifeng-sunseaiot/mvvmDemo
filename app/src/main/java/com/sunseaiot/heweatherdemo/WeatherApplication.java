package com.sunseaiot.heweatherdemo;

import android.app.Application;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class WeatherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HeConfig.init("HE1906111851181789", "970577e2a92e48938a016bbb9f5c0f0f");
        HeConfig.switchToFreeServerNode();
    }
}

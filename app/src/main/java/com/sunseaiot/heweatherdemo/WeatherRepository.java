package com.sunseaiot.heweatherdemo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherRepository {
    public void getWeatherNowInfo(Context context, String s, Lang land, Unit unit, HeWeather.OnResultWeatherNowBeanListener listener) {
        if(TextUtils.isEmpty(s)) {
            HeWeather.getWeatherNow(context, land, unit, listener);
        } else {
            HeWeather.getWeatherNow(context, s, land, unit, listener);
        }
    }

    public void getAQIInfo(Context context, String s, Lang land, Unit unit, HeWeather.OnResultAirNowBeansListener listener) {
        if(TextUtils.isEmpty(s)) {
            HeWeather.getAirNow(context, land, unit, listener);
        } else {
            HeWeather.getAirNow(context, s, land, unit, listener);
        }
    }
}

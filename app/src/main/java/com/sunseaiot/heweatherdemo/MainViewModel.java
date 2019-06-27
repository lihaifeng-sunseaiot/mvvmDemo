package com.sunseaiot.heweatherdemo;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.util.Log;

import com.google.gson.Gson;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class MainViewModel extends BaseObservable {
    public static final String TAG = "MainViewModel";

    public ObservableField<String> mNowInfo = new ObservableField<>();

    public ObservableField<String> mAQIInfo = new ObservableField<>();

    private WeatherRepository mRepository;

    public MainViewModel(WeatherRepository repository) {
        this.mRepository = repository;
    }

    public void getWeatherNowInfo(Context context, String s, Lang land, Unit unit) {
        mRepository.getWeatherNowInfo(context, s, land, unit, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                mNowInfo.set(throwable.getLocalizedMessage());
            }

            @Override
            public void onSuccess(Now dataObject) {
                String nowData = new Gson().toJson(dataObject);
                Log.i(TAG, " Weather Now onSuccess: " + nowData);
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ){
                    //此时返回数据
                    NowBase now = dataObject.getNow();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("地区：");
                    stringBuilder.append(dataObject.getBasic().getLocation());
                    stringBuilder.append("\r\n");
                    stringBuilder.append("天气：");
                    stringBuilder.append(now.getCond_txt());
                    stringBuilder.append("\r\n");
                    stringBuilder.append("室外温度：");
                    stringBuilder.append(now.getTmp());
                    stringBuilder.append("\r\n");
                    stringBuilder.append("相对湿度：");
                    stringBuilder.append(now.getHum());
                    stringBuilder.append("\r\n");
                    stringBuilder.append("风力等级：");
                    stringBuilder.append(now.getWind_sc());
                    stringBuilder.append("\r\n");
                    stringBuilder.append("风向：");
                    stringBuilder.append(now.getWind_dir());
                    mNowInfo.set(stringBuilder.toString());

                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                    mNowInfo.set("failed code: " + code);
                }
            }
        });
    }

    public void getAQIInfo() {
        //mRepository.getAQIInfo();
        // todo
    }
}

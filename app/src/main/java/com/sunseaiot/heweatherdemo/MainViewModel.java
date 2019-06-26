package com.sunseaiot.heweatherdemo;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

public class MainViewModel extends BaseObservable {

    public ObservableField<String> mNowInfo = new ObservableField<>();

    public ObservableField<String> mAQIInfo = new ObservableField<>();

    private WeatherRepository mRepository;

    public MainViewModel(WeatherRepository repository) {
        this.mRepository = repository;
    }

    public void getWeatherNowInfo() {
        mRepository.getWeatherNowInfo();
        // todo
    }

    public void getAQIInfo() {
        mRepository.getAQIInfo();
        // todo
    }
}

package com.sunseaiot.heweatherdemo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunseaiot.heweatherdemo.databinding.ActivityMainBinding;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import me.jessyan.autosize.utils.ScreenUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    EditText etCity;
    Button btnGetNow;
    Button btnGetAir;
    TextView tvNowinfo;
    TextView tvAirinfo;
    MainViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvNowinfo = findViewById(R.id.tv_nowinfo);
        tvAirinfo = findViewById(R.id.tv_airinfo);
        btnGetNow = findViewById(R.id.btn_getNow);
        btnGetNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherNow();
            }
        });

        btnGetAir = findViewById(R.id.btn_getAQI);
        btnGetAir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAQI();
            }
        });

        etCity = findViewById(R.id.et_city_code);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.tvAirinfo.setText("haha");

        mViewModel = new MainViewModel(new WeatherRepository());
    }

    public void getWeatherNow() {
        if (etCity.getText().toString().trim().length() <= 0) {
            HeWeather.getWeatherNow(MainActivity.this, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
                @Override
                public void onError(Throwable throwable) {
                    Log.i(TAG, "Weather Now onError: ", throwable);
                    showToast(throwable.getMessage());
                }

                @Override
                public void onSuccess(Now now) {
                    showNowInfo(now);
                }
            });
        } else {
            HeWeather.getWeatherNow(MainActivity.this, etCity.getText().toString(), Lang.ENGLISH , Unit.METRIC , new HeWeather.OnResultWeatherNowBeanListener() {
                @Override
                public void onError(Throwable e) {
                    Log.i(TAG, "Weather Now onError: ", e);
                    showToast(e.getMessage());
                }

                @Override
                public void onSuccess(Now dataObject) {
                    showNowInfo(dataObject);
                }
            });
        }

    }

    public void getAQI() {
        if (etCity.getText().toString().trim().length() <= 0) {
            HeWeather.getAirNow(MainActivity.this, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultAirNowBeansListener() {
                @Override
                public void onError(Throwable throwable) {
                    Log.i(TAG, "AirNow On Error", throwable);
                    showToast(throwable.getMessage());
                }

                @Override
                public void onSuccess(AirNow airNow) {
                    showAirInfo(airNow);
                }
            });
        } else {
            HeWeather.getAirNow(MainActivity.this, etCity.getText().toString(), Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultAirNowBeansListener() {
                @Override
                public void onError(Throwable throwable) {
                    Log.i(TAG, "AirNow On Error", throwable);
                    showToast(throwable.getMessage());
                }

                @Override
                public void onSuccess(AirNow airNow) {
                   showAirInfo(airNow);
                }
            });
        }
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void showNowInfo(Now dataObject) {
        String nowData = new Gson().toJson(dataObject);
        Log.i(TAG, " Weather Now onSuccess: " + nowData);
        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
        if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ){
            //此时返回数据
            NowBase now = dataObject.getNow();
            tvNowinfo.setText("地区：" + dataObject.getBasic().getLocation() + "\r\n"
                    + "天气：" + now.getCond_txt() + "\r\n"
                    + "室外温度：" + now.getTmp() + "\r\n"
                    + "相对湿度：" + now.getHum() + "\r\n"
                    + "风力等级：" + now.getWind_sc() + "\r\n"
                    + "风向：" + now.getWind_dir());

        } else {
            //在此查看返回数据失败的原因
            String status = dataObject.getStatus();
            Code code = Code.toEnum(status);
            Log.i(TAG, "failed code: " + code);
            tvNowinfo.setText("failed code: " + code);
            showToast("failed code: " + code);
        }
    }

    private void showAirInfo(AirNow airNow) {
        String nowData = new Gson().toJson(airNow);
        Log.i(TAG, " Weather Now onSuccess: " + nowData);
        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
        if ( Code.OK.getCode().equalsIgnoreCase(airNow.getStatus()) ){
            //此时返回数据
            AirNowCity airNowCity = airNow.getAir_now_city();
            tvAirinfo.setText("空气指数：" + airNowCity.getAqi() + "\r\n"
                    + "空气质量：" + airNowCity.getQlty() + "\r\n"
                    + "PM2.5：" + airNowCity.getPm25() + "\r\n"
                    + "PM10：" + airNowCity.getPm10() + "\r\n");
        } else {
            //在此查看返回数据失败的原因
            String status = airNow.getStatus();
            Code code = Code.toEnum(status);
            Log.i(TAG, "failed code: " + code);
            tvAirinfo.setText("failed code: " + code);
            showToast("failed code: " + code);
        }
    }
}

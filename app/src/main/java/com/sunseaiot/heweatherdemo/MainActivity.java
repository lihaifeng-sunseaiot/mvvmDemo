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
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import io.reactivex.functions.Consumer;
import me.jessyan.autosize.utils.ScreenUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MainViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyPermission();

        mViewModel = new MainViewModel(new WeatherRepository());
        final ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.btnGetNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.getWeatherNowInfo(getBaseContext(), activityMainBinding.etCityCode.getText().toString(),Lang.CHINESE_SIMPLIFIED, Unit.METRIC);
            }
        });
        activityMainBinding.setViewModel(mViewModel);

    }

    public void getAQI() {

    }

    private void applyPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        if(!rxPermissions.isGranted("android.permission.ACCESS_COARSE_LOCATION")
                || !rxPermissions.isGranted("android.permission.ACCESS_FINE_LOCATION")) {
            rxPermissions.requestEach("android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.ACCESS_FINE_LOCATION")
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) throws Exception {
                            if (permission.granted) {
                                showToast("权限申请成功");
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                showToast("是否永远");
                            } else {
                                showToast("权限申请失败");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            showToast("申请失败");
                        }
                    });
        }
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

/*    private void showNowInfo(Now dataObject) {
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
    }*/
}

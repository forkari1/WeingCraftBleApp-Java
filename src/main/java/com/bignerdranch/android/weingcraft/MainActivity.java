package com.bignerdranch.android.weingcraft;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageButton mBlindBtn, mOptionBtn;
    TextView t1, t2, t3, t4, t5, t6;
    ImageView WeatherImg;
    private GpsTracker gpsTracker;
    double lat;
    double lon;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        t6 = findViewById(R.id.t6);
        WeatherImg = findViewById(R.id.weatherImg);
        mBlindBtn = findViewById(R.id.mainBlind);
//        mDisplayBtn = findViewById(R.id.mainDisplay);
        mOptionBtn = findViewById(R.id.mainOption);


        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        } else {

            checkRunTimePermission();
        }


        gpsTracker = new GpsTracker(MainActivity.this);
        lat = gpsTracker.getLatitude();
        lon = gpsTracker.getLongitude();
        getWeatherData(lat, lon);


        mBlindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BlindActivity.class);
                startActivity(intent);
            }
        });


        mOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void getWeatherData(double lat, double lng) {
        String key = "45c1f7653d4d36c6f7b88fcf9a5c93fb";
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&units=metric&appid=" + key;
        ReceiveWeatherTask receiveWeatherTask = new ReceiveWeatherTask();
        receiveWeatherTask.execute(url);
    }

    private class ReceiveWeatherTask extends AsyncTask<String, Void, JSONObject> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... datas) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while ((readed = in.readLine()) != null) {
                        JSONObject jObject = new JSONObject(readed);
                        return jObject;
                    }
                } else {
                    return null;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject result) {
            Log.i("@@", result.toString());
            if (result != null) {
                String iconName = "";
                String nowTemp = "";
                String maxTemp = "";
                String minTemp = "";

                String humidity = "";
                String speed = "";
                String main = "";
                String description = "";

                try {
                    iconName = result.getJSONArray("weather").getJSONObject(0).getString("icon");
                    nowTemp = result.getJSONObject("main").getString("temp");
                    humidity = result.getJSONObject("main").getString("humidity");
                    maxTemp = result.getJSONObject("main").getString("temp_max");
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    speed = result.getJSONObject("wind").getString("speed");
                    main = result.getJSONArray("weather").getJSONObject(0).getString("main");
                    description = result.getJSONArray("weather").getJSONObject(0).getString("description");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                description = transferWeather(description);
                t1.setText(description);
                t2.setText("습도 :" + humidity + "%");
                t2.setTextSize(9);
                t3.setText("풍속 : " + speed + "m/s");
                t3.setTextSize(9);

                t4.setText(" 현재 온도:" + nowTemp + "℃");
                t4.setTextSize(9);

                t5.setText(" 최저 온도:" + minTemp + "℃" + "/" + "최고 온도" + maxTemp + "℃");
                t5.setTextSize(9);

            }
        }

        private String transferWeather(String weather) {
            weather = weather.toLowerCase();

            if (weather.equals("haze")) {
                WeatherImg.setImageResource(R.drawable.fog);
                return "안개";
            } else if (weather.equals("fog")) {
                WeatherImg.setImageResource(R.drawable.fog);
                return "안개";
            } else if (weather.equals("clouds")) {
                WeatherImg.setImageResource(R.drawable.cloud1);
                return "구름";
            } else if (weather.equals("few clouds")) {
                WeatherImg.setImageResource(R.drawable.cloud2);
                return "구름 조금";
            } else if (weather.equals("scattered clouds")) {
                WeatherImg.setImageResource(R.drawable.cloud2);
                return "구름 낌";
            } else if (weather.equals("broken clouds")) {
                WeatherImg.setImageResource(R.drawable.cloud3);
                return "구름 많음";
            } else if (weather.equals("overcast clouds")) {
                WeatherImg.setImageResource(R.drawable.cloud3);
                return "구름 많음";
            } else if (weather.equals("clear sky")) {
                WeatherImg.setImageResource(R.drawable.sun);
                return "맑음";
            }
            return "";
        }
    }
}






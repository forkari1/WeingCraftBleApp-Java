//package com.bignerdranch.android.weingcraft;
//
//import android.accounts.AccountManager;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.app.TimePickerDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.Manifest;
//import android.app.AlertDialog;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
//import com.google.api.client.extensions.android.http.AndroidHttp;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
//import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.util.DateTime;
//import com.google.api.client.util.ExponentialBackOff;
//import com.google.api.services.calendar.CalendarScopes;
//import com.google.api.services.calendar.model.Calendar;
//import com.google.api.services.calendar.model.CalendarList;
//import com.google.api.services.calendar.model.CalendarListEntry;
//import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.model.EventDateTime;
//import com.google.api.services.calendar.model.Events;
//
//import org.w3c.dom.Text;
//
//import java.io.IOException;
//import java.lang.reflect.Array;
//import java.sql.Time;
//import java.text.BreakIterator;
//import java.text.SimpleDateFormat;
//import java.time.Month;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.EasyPermissions;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//
//public class DisplayActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
//    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
//    GpsTracker gpsTracker;
//    String accountName;
//
//    private com.google.api.services.calendar.Calendar mService = null;
//
//    private int mID = 0;
//
//    GoogleAccountCredential mCredential;
//
//    static final int REQUEST_ACCOUNT_PICKER = 1000;
//    static final int REQUEST_AUTHORIZATION = 1001;
//    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
//    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
//    private static final String PREF_ACCOUNT_NAME = "accountName";
//    private static final String[] SCOPES = {CalendarScopes.CALENDAR};
//
//    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
//    private static final int PERMISSIONS_REQUEST_CODE = 100;
//    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//    Button WeatherBtn, scheduleBtn, schedulerBtn ,btn1, createCalBtn, enterScheduleBtn ,changeAccountBtn,DateBtn,TimeBtn;
//    TextView color1, color2, color3, t1, ScheduleCheckTx, mStatusText ;
//    EditText et1,et2,et3,year,month,date,hour,minute;
//    LinearLayout l1, l2, l3;
//    Switch sw, sw2;
//    ProgressDialog mProgress;
//
//    String x;
//    String y;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate( savedInstanceState );
//        setContentView( R.layout.activity_display );
//
//
////        ScheduleCheckTx = findViewById( R.id.i2 );
//        //sw2 = findViewById( R.id.ScheduleCheckBtn );
//        WeatherBtn = findViewById( R.id.weatherBtn );
////        scheduleBtn = findViewById( R.id.ScheduleBtn );
////        schedulerBtn = findViewById( R.id.SchedulerBtn );
//        sw = findViewById( R.id.loc );
//        t1 = findViewById( R.id.weatherResult );
////        mStatusText = findViewById( R.id.SheduleStatusTx );
////        btn1 = findViewById( R.id.initialBtn );
////        createCalBtn = findViewById( R.id.CreateScheduleBtn );
////        enterScheduleBtn = findViewById( R.id.EnterScheduleBtn );
////        changeAccountBtn = findViewById( R.id.ChangeAccountBtn );
////        et1 = findViewById( R.id.EnterScheduleEtx1 );
////        et2 = findViewById( R.id.EnterScheduleEtx2 );
////        et3 = findViewById( R.id.EnterScheduleEtx3 );
////        year = findViewById(R.id.year );
////        month = findViewById(R.id.Month );
////        date = findViewById(R.id.Date );
////        hour = findViewById(R.id.Hour );
////        minute = findViewById(R.id.Minute );
//
//
//        color1 = findViewById( R.id.c1 );
//       // color2 = findViewById( R.id.c2 );
//        //color3 = findViewById( R.id.c3 );
//
//
//        l1 = findViewById( R.id.WeatherSwitch );
////        l2 = findViewById( R.id.Schedule );
//        //l3 = findViewById( R.id.Scheduler );
//
//
//
//
//
//
////        mProgress = new ProgressDialog( this );
////        mProgress.setMessage( "Google Calendar API 호출 중입니다." );
////
////        mCredential = GoogleAccountCredential.usingOAuth2( getApplicationContext(), Arrays.asList( SCOPES ) ).setBackOff( new ExponentialBackOff() );
////
////        mCredential.setSelectedAccount( null );
//
//
////        WeatherBtn.setOnClickListener( new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                color1.setBackgroundColor( Color.rgb( 250, 156, 126 ) );
////                color2.setBackgroundColor( Color.rgb( 180, 190, 231 ) );
////                color3.setBackgroundColor( Color.rgb( 180, 190, 231 ) );
////
////                l1.setVisibility( View.VISIBLE );
////                l2.setVisibility( View.GONE );
////                l3.setVisibility( View.GONE );
////
////            }
////        } );
//
////        scheduleBtn.setOnClickListener( new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                color1.setBackgroundColor( Color.rgb( 180, 190, 231 ) );
////                color2.setBackgroundColor( Color.rgb( 250, 156, 126 ) );
////                color3.setBackgroundColor( Color.rgb( 180, 190, 231 ) );
////
////
////                l1.setVisibility( View.GONE );
////                l2.setVisibility( View.VISIBLE );
////                l3.setVisibility( View.GONE );
////
////            }
////        } );
////
////        schedulerBtn.setOnClickListener( new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                color1.setBackgroundColor( Color.rgb( 180, 190, 231 ) );
////                color2.setBackgroundColor( Color.rgb( 180, 190, 231 ) );
////                color3.setBackgroundColor( Color.rgb( 250, 156, 126 ) );
////
////
////                l1.setVisibility( View.GONE );
////                l2.setVisibility( View.GONE );
////                l3.setVisibility( View.VISIBLE );
////            }
////        } );
////
////        if (!checkLocationServicesStatus()) {
////
////            showDialogForLocationServiceSetting();
////        } else {
////
////            checkRunTimePermission();
////        }
//        final TextView textview_address = (TextView) findViewById( R.id.text1 );
//        final Switch ShowLocationSwitch = findViewById( R.id.loc );
//
//        ShowLocationSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (ShowLocationSwitch.isChecked() == true) {
//                    gpsTracker = new GpsTracker( DisplayActivity.this );
//
//                    double latitude = gpsTracker.getLatitude();
//                    double longitude = gpsTracker.getLongitude();
//
//                    final WeatherRepo[] a = {new WeatherRepo()};
//
//                    Retrofit client = new Retrofit.Builder().baseUrl( "http://apis.skplanetx.com/" )
//                            .addConverterFactory( GsonConverterFactory.create() ).build();
//                    final WeatherRepo.WeatherApiInterface weatherApi =
//                            client.create( WeatherRepo.WeatherApiInterface.class );
//                    String lat = String.valueOf( latitude );
//                    String lon = String.valueOf( longitude );
//                    Call<WeatherRepo> call = weatherApi.get_Weather_retrofit( 1, lat, lon );
//                    call.enqueue( new Callback<WeatherRepo>() {
//                        @Override
//                        public void onResponse(Call<WeatherRepo> call, Response<WeatherRepo> response) {
//                            a[0] = response.body();
//
//                            if (a[0].getResult().getCode().equals( "9200" )) {
//                                t1.setText( a[0].getWeather().getHourly().get( 0 ).getHumidity() );
//                                t1.setText( a[0].getWeather().getHourly().get( 0 ).getTemperature().getTc() );
//                            } else {
//                                Log.d( "@@", "server return error code :" + a[0].getResult().getCode() );
//                            }
//                       }
//
//                        @Override
//                        public void onFailure(Call<WeatherRepo> call, Throwable t) {
//                            //Log.d( "@@", "onFailure" + t );
//                        }
//                    } );
//
//
//                    String address = getCurrentAddress( latitude, longitude );
//                    textview_address.setText( address );
//                    Toast.makeText( DisplayActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_SHORT ).show();
//
//                } else {
//                    textview_address.setText( "현재위치" );
//
//                }
//
//            }
//        } );
//
//        btn1.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn1.setEnabled( false );
//                mStatusText.setText( "" );
//                mID = 3;
//                getResultsFromAPi();
//                btn1.setEnabled( true );
//            }
//        } );
//
//        createCalBtn.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createCalBtn.setEnabled(false);
//                mStatusText.setText("");
//                mID = 1;           //캘린더 생성
//                getResultsFromAPi();
//                createCalBtn.setEnabled(true);
//
//
//            }
//        } );
//
//        enterScheduleBtn.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enterScheduleBtn.setEnabled(false);
//                mStatusText.setText("");
//                mID = 2;        //이벤트 생성
//                getResultsFromAPi();
//                enterScheduleBtn.setEnabled(true);
//            }
//        } );
//
//        changeAccountBtn.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences settings = getPreferences( getApplicationContext().MODE_PRIVATE );
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putString( PREF_ACCOUNT_NAME, "" );
//                editor.apply();
//
//                mCredential.setSelectedAccountName("");
//                startActivityForResult( mCredential.newChooseAccountIntent(),REQUEST_ACCOUNT_PICKER );
//
//            }
//        } );
//    }
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int permsRequestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grandResults) {
//        super.onRequestPermissionsResult( permsRequestCode, permissions, grandResults );
//        EasyPermissions.onRequestPermissionsResult( permsRequestCode, permissions, grandResults, this );
//
//        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
//
//            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
//
//            boolean check_result = true;
//
//
//            // 모든 퍼미션을 허용했는지 체크합니다.
//
//            for (int result : grandResults) {
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    check_result = false;
//                    break;
//                }
//            }
//
//
//            if (check_result) {
//
//                //위치 값을 가져올 수 있음
//                ;
//            } else {
//                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
//
//                if (ActivityCompat.shouldShowRequestPermissionRationale( this, REQUIRED_PERMISSIONS[0] )
//                        || ActivityCompat.shouldShowRequestPermissionRationale( this, REQUIRED_PERMISSIONS[1] )) {
//
//                    Toast.makeText( DisplayActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG ).show();
//                    finish();
//
//
//                } else {
//
//                    Toast.makeText( DisplayActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG ).show();
//
//                }
//            }
//
//        }
//    }
//
//    void checkRunTimePermission() {
//
//        //런타임 퍼미션 처리
//        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
//        int hasFineLocationPermission = ContextCompat.checkSelfPermission( DisplayActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION );
//        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission( DisplayActivity.this,
//                Manifest.permission.ACCESS_COARSE_LOCATION );
//
//
//        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
//                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
//
//            // 2. 이미 퍼미션을 가지고 있다면
//            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
//
//
//            // 3.  위치 값을 가져올 수 있음
//
//
//        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
//
//            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
//            if (ActivityCompat.shouldShowRequestPermissionRationale( DisplayActivity.this, REQUIRED_PERMISSIONS[0] )) {
//
//                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
//                Toast.makeText( DisplayActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG ).show();
//                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
//                ActivityCompat.requestPermissions( DisplayActivity.this, REQUIRED_PERMISSIONS,
//                        PERMISSIONS_REQUEST_CODE );
//
//
//            } else {
//                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
//                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
//                ActivityCompat.requestPermissions( DisplayActivity.this, REQUIRED_PERMISSIONS,
//                        PERMISSIONS_REQUEST_CODE );
//            }
//
//        }
//
//    }
//
//
//    public String getCurrentAddress(double latitude, double longitude) {
//
//        //지오코더... GPS를 주소로 변환
//        Geocoder geocoder = new Geocoder( this, Locale.getDefault() );
//
//        List<Address> addresses;
//
//        try {
//
//            addresses = geocoder.getFromLocation(
//                    latitude,
//                    longitude,
//                    7 );
//        } catch (IOException ioException) {
//            //네트워크 문제
//            Toast.makeText( this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG ).show();
//            return "지오코더 서비스 사용불가";
//        } catch (IllegalArgumentException illegalArgumentException) {
//            Toast.makeText( this, "잘못된 GPS 좌표", Toast.LENGTH_LONG ).show();
//            return "잘못된 GPS 좌표";
//
//        }
//
//
//        if (addresses == null || addresses.size() == 0) {
//            Toast.makeText( this, "주소 미발견", Toast.LENGTH_LONG ).show();
//            return "주소 미발견";
//
//        }
//
//        Address address = addresses.get( 0 );
//        return address.getAddressLine( 0 ).toString() + "\n";
//
//    }
//
//
//    private void showDialogForLocationServiceSetting() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder( DisplayActivity.this );
//        builder.setTitle( "위치 서비스 비활성화" );
//        builder.setMessage( "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
//                + "위치 설정을 수정하실래요?" );
//        builder.setCancelable( true );
//        builder.setPositiveButton( "설정", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                Intent callGPSSettingIntent
//                        = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS );
//                startActivityForResult( callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE );
//            }
//        } );
//        builder.setNegativeButton( "취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//        } );
//        builder.create().show();
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult( requestCode, resultCode, data );
//
//        switch (requestCode) {
//
//            case GPS_ENABLE_REQUEST_CODE:
//
//
//                //사용자가 GPS 활성 시켰는지 검사
//                if (checkLocationServicesStatus()) {
//                    if (checkLocationServicesStatus()) {
//
//                        Log.d( "@@@", "onActivityResult : GPS 활성화 되있음" );
//                        checkRunTimePermission();
//                        return;
//                    }
//                }
//
//                break;
//
//            case REQUEST_GOOGLE_PLAY_SERVICES:
//
//                if (resultCode != RESULT_OK) {
//
//                    mStatusText.setText( " 앱을 실행시키려면 구글 플레이 서비스가 필요합니다."
//                            + "구글 플레이 서비스를 설치 후 다시 실행하세요." );
//                } else {
//
//                    getResultsFromAPi();
//                }
//                break;
//
//
//            case REQUEST_ACCOUNT_PICKER:
//                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
//                    accountName = data.getStringExtra( AccountManager.KEY_ACCOUNT_NAME );
//                    if (accountName != null) {
//                        SharedPreferences settings = getPreferences( Context.MODE_PRIVATE );
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString( PREF_ACCOUNT_NAME, accountName );
//                        editor.apply();
//                        mCredential.setSelectedAccountName( accountName );
//                        getResultsFromAPi();
//                    }
//                }
//                break;
//
//
//            case REQUEST_AUTHORIZATION:
//
//                if (resultCode == RESULT_OK) {
//                    getResultsFromAPi();
//                }
//                break;
//        }
//    }
//
//
//    public boolean checkLocationServicesStatus() {
//        LocationManager locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );
//
//        return locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )
//                || locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
//    }
//
//
//    private String getResultsFromAPi() {
//        if (!isGooglePlayServicesAvailable()) { // Google Play Services를 사용할 수 없는 경우
//
//            acquireGooglePlayServices();
//        } else if (mCredential.getSelectedAccountName() == null) { // 유효한 Google 계정이 선택되어 있지 않은 경우
//            chooseAccount();
//        } else if (!isDeviceOnline()) {    // 인터넷을 사용할 수 없는 경우
//
//            mStatusText.setText( "No network connection available." );
//        } else {
//
//            // Google Calendar API 호출
//            new MakeRequestTask( this, mCredential ).execute();
//        }
//        return null;
//    }
//
//
//    private boolean isGooglePlayServicesAvailable() {
//
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//
//        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable( this );
//        return connectionStatusCode == ConnectionResult.SUCCESS;
//    }
//
//    private void acquireGooglePlayServices() {
//
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable( this );
//
//        if (apiAvailability.isUserResolvableError( connectionStatusCode )) {
//
//            showGooglePlayServicesAvailabilityErrorDialog( connectionStatusCode );
//        }
//    }
//
//    void showGooglePlayServicesAvailabilityErrorDialog(
//            final int connectionStatusCode
//    ) {
//
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//
//        Dialog dialog = apiAvailability.getErrorDialog(
//                DisplayActivity.this,
//                connectionStatusCode,
//                REQUEST_GOOGLE_PLAY_SERVICES
//        );
//        dialog.show();
//    }
//
//    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
//    private void chooseAccount() {
//        // GET_ACCOUNTS 권한을 가지고 있다면
//        if (EasyPermissions.hasPermissions( this, Manifest.permission.GET_ACCOUNTS )) {
//            // SharedPreferences에서 저장된 Google 계정 이름을 가져온다.
//            accountName = getPreferences( Context.MODE_PRIVATE )
//                    .getString( PREF_ACCOUNT_NAME, null );
//            if (accountName != null) {
//                // 선택된 구글 계정 이름으로 설정한다.
//                mCredential.setSelectedAccountName( accountName );
//                getResultsFromAPi();
//            } else {
//
//
//                // 사용자가 구글 계정을 선택할 수 있는 다이얼로그를 보여준다.
//                startActivityForResult(
//                        mCredential.newChooseAccountIntent(),
//                        REQUEST_ACCOUNT_PICKER );
//            }
//
//        } else {
//
//
//            // 사용자에게 GET_ACCOUNTS 권한을 요구하는 다이얼로그를 보여준다.(주소록 권한 요청함)
//            EasyPermissions.requestPermissions(
//                    (Activity) this,
//                    "This app needs to access your Google account (via Contacts).",
//                    REQUEST_PERMISSION_GET_ACCOUNTS,
//                    Manifest.permission.GET_ACCOUNTS );
//        }
//
//    }
//
//
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//
//    }
//
//    private boolean isDeviceOnline() {
//
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//        return (networkInfo != null && networkInfo.isConnected());
//    }
//
//
//    /*
//     * 캘린더 이름에 대응하는 캘린더 ID를 리턴
//     */
//    private String getCalendarID(String calendarTitle) {
//
//        String id = null;
//
//        // Iterate through entries in calendar list
//        String pageToken = null;
//        do {
//            CalendarList calendarList = null;
//            try {
//                calendarList = mService.calendarList().list().setPageToken( pageToken ).execute();
//            } catch (UserRecoverableAuthIOException e) {
//                startActivityForResult( e.getIntent(), REQUEST_AUTHORIZATION );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            List<CalendarListEntry> items = calendarList.getItems();
//
//
//            for (CalendarListEntry calendarListEntry : items) {
//
//                if (calendarListEntry.getSummary().toString().equals( calendarTitle )) {
//
//                    id = calendarListEntry.getId().toString();
//                }
//            }
//            pageToken = calendarList.getNextPageToken();
//        } while (pageToken != null);
//
//        return id;
//    }
//
//    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
//
//        private Exception mLastError = null;
//        private DisplayActivity mActivity;
//        List<String> eventStrings = new ArrayList<String>();
//
//
//        public MakeRequestTask(DisplayActivity activity, GoogleAccountCredential credential) {
//
//            mActivity = activity;
//
//            HttpTransport transport = AndroidHttp.newCompatibleTransport();
//            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//            mService = new com.google.api.services.calendar.Calendar
//                    .Builder( transport, jsonFactory, credential )
//                    .setApplicationName( "Google Calendar API Android Quickstart" )
//                    .build();
//        }
//
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            try {
//
//                if ( mID == 1) {
//
//                    return createCalendar();
//
//                }else if (mID == 2) {
//
//                    return addEvent();
//                }
//                else if(mID == 3){
//
//                    return getEvent();
//                }
//
//
//            } catch (Exception e) {
//                mLastError = e;
//                cancel( true );
//                return null;
//            }
//
//            return null;
//        }
//
//
//
//
//
//
//
//        protected void onPreExecute() {
//            // mStatusText.setText("");
//            mProgress.show();
//            mStatusText.setText( "데이터 가져오는 중..." );
//
//        }
//
//
//
//
//
//
//        private String addEvent() {
//            String a = et1.getText().toString();
//            String b = et2.getText().toString();
//            String c = et3.getText().toString();
//
//            String Year = year.getText().toString();
//            String Month = month.getText().toString();
//            String Date = date.getText().toString();
//            String Hour = hour.getText().toString();
//            String Minute = minute.getText().toString();
//
//
//            String calendarID = getCalendarID("CalendarTitle");
//
//            if ( calendarID == null ){
//
//                return "캘린더를 먼저 생성하세요.";
//
//            }
//
//            Event event = new Event()
//                    .setSummary(a)
//                    .setLocation(b)
//                    .setDescription(c);
//
//
//            java.util.Calendar calander;
//
//            calander = java.util.Calendar.getInstance();
//            SimpleDateFormat simpledateformat;
//            //simpledateformat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREA);
//            // Z에 대응하여 +0900이 입력되어 문제 생겨 수작업으로 입력
//            simpledateformat = new SimpleDateFormat( "Year-MM-dd'T'HH:mm:ss+09:00", Locale.KOREA);
//            String datetime = simpledateformat.format(calander.getTime());
//
//            DateTime startDateTime = new DateTime(datetime);
//            EventDateTime start = new EventDateTime()
//                    .setDateTime(startDateTime)
//                    .setTimeZone("Asia/Seoul");
//            event.setStart(start);
//
//            Log.d( "@@@", datetime );
//
//
//            DateTime endDateTime = new  DateTime(datetime);
//            EventDateTime end = new EventDateTime()
//                    .setDateTime(endDateTime)
//                    .setTimeZone("Asia/Seoul");
//            event.setEnd(end);
//
//            //String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
//            //event.setRecurrence(Arrays.asList(recurrence));
//
//
//            try {
//                event = mService.events().insert(calendarID, event).execute();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("Exception", "Exception : " + e.toString());
//            }
//            System.out.printf("Event created: %s\n", event.getHtmlLink());
//            Log.e("Event", "created : " + event.getHtmlLink());
//            String eventStrings = "created : " + event.getHtmlLink();
//            return eventStrings;
//        }
//
//
//
//        private String getEvent() throws IOException {
//
//
//            DateTime now = new DateTime( System.currentTimeMillis() );
//
//            String calendarID = getCalendarID( "CalendarTitle" );
//            if (calendarID == null) {
//
//                return "캘린더를 먼저 생성하세요.";
//            }
//
//
//            Events events = mService.events().list( calendarID )//"primary")
//                    .setMaxResults( 100 )
//                    //.setTimeMin(now)
//                    .setOrderBy( "startTime" )
//                    .setSingleEvents( true )
//                    .execute();
//            List<Event> items = events.getItems();
//
//
//            for (Event event : items) {
//
//                DateTime start = event.getStart().getDateTime();
//                if (start == null) {
//
//                    // 모든 이벤트가 시작 시간을 갖고 있지는 않다. 그런 경우 시작 날짜만 사용
//                    start = event.getStart().getDate();
//                }
//
//
//                eventStrings.add( String.format( "%s \n (%s)", event.getSummary(),event.getDescription(), start ) );
//            }
//
//
//            return eventStrings.size() + "개의 데이터를 가져왔습니다.";
//        }
//
//        /*
//         * 선택되어 있는 Google 계정에 새 캘린더를 추가한다.
//         */
//        private String createCalendar() throws IOException {
//
//            String ids = getCalendarID( "CalendarTitle" );
//
//            if (ids != null) {
//
//                return "이미 캘린더가 생성되어 있습니다. ";
//            }
//
//            // 새로운 캘린더 생성
//            com.google.api.services.calendar.model.Calendar calendar = new Calendar();
//
//            // 캘린더의 제목 설정
//            calendar.setSummary( "CalendarTitle" );
//
//
//            // 캘린더의 시간대 설정
//            calendar.setTimeZone( "Asia/Seoul" );
//
//            // 구글 캘린더에 새로 만든 캘린더를 추가
//            Calendar createdCalendar = mService.calendars().insert( calendar ).execute();
//
//            // 추가한 캘린더의 ID를 가져옴.
//            String calendarId = createdCalendar.getId();
//
//
//            // 구글 캘린더의 캘린더 목록에서 새로 만든 캘린더를 검색
//            CalendarListEntry calendarListEntry = mService.calendarList().get( calendarId ).execute();
//
//            // 캘린더의 배경색을 파란색으로 표시  RGB
//            calendarListEntry.setBackgroundColor( "#0000ff" );
//
//            // 변경한 내용을 구글 캘린더에 반영
//            CalendarListEntry updatedCalendarListEntry =
//                    mService.calendarList()
//                            .update( calendarListEntry.getId(), calendarListEntry )
//                            .setColorRgbFormat( true )
//                            .execute();
//
//            // 새로 추가한 캘린더의 ID를 리턴
//            return "캘린더가 생성되었습니다.";
//        }
//
//
//        protected void onPostExecute(String output) {
//
//            mProgress.hide();
//            mStatusText.setText( output );
//
//            if (mID == 3) ScheduleCheckTx.setText( TextUtils.join( "\n\n", eventStrings ) );
//        }
//
//
//        protected void onCancelled() {
//            mProgress.hide();
//            if (mLastError != null) {
//                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                    showGooglePlayServicesAvailabilityErrorDialog(
//                            ((GooglePlayServicesAvailabilityIOException) mLastError)
//                                    .getConnectionStatusCode() );
//                } else if (mLastError instanceof UserRecoverableAuthIOException) {
//                    startActivityForResult(
//                            ((UserRecoverableAuthIOException) mLastError).getIntent(), com.bignerdranch.android.weingcraft.DisplayActivity.REQUEST_AUTHORIZATION );
//                } else {
//                    mStatusText.setText( "MakeRequestTask The following error occurred:\n" + mLastError.getMessage() );
//                }
//            } else {
//                mStatusText.setText( "요청 취소됨." );
//            }
//        }
//
//
//    }
//}
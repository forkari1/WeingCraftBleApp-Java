package com.bignerdranch.android.weingcraft;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bignerdranch.android.weingcraft.BleUtils.BleUtil;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;

import java.util.ArrayList;
import java.util.Calendar;

public class BlindActivity extends AppCompatActivity {
    AlarmManager alarm_manager;
    Context context;
    PendingIntent pendingIntent;
    ImageView voice1;
    TimePicker timePicker;
    Button b1, b2, b3, b4, b5, BlindSwitch, BlindVoice, BlindReservation, reservationBtn, cancleBtn, WeatherBtn;
    TextView color1, color2, color3, color4;
    LinearLayout l1, l2, l3, l4;
    TextView tx, tx2;
    Intent i;
    SpeechRecognizer mRecognizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    OptionActivity bt = new OptionActivity();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind);
        setTitle("블라인드 조작홈");

        this.context = this;
        final Calendar calendar = Calendar.getInstance();
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Intent intent = new Intent(this.context, Alarm_Reciver.class);
        l2 = findViewById(R.id.WeatherSwitch);
        WeatherBtn = findViewById(R.id.weatherBtn);
        tx2 = findViewById(R.id.result);
        cancleBtn = findViewById(R.id.cancleBtn);
        tx = findViewById(R.id.text1);
        timePicker = findViewById(R.id.tp1);
        reservationBtn = findViewById(R.id.ReservationBtn);
        BlindSwitch = (Button) findViewById(R.id.BlindSwitch);
        BlindVoice = (Button) findViewById(R.id.BlindVoice);
        BlindReservation = (Button) findViewById(R.id.BlindReservation);
        voice1 = findViewById(R.id.v1);
        b1 = findViewById(R.id.blind1);
        b2 = findViewById(R.id.blind2);
        b3 = findViewById(R.id.blind3);
        b4 = findViewById(R.id.blind4);
        b5 = findViewById(R.id.blind5);


        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);


        color1 = (TextView) findViewById(R.id.color1);
        color3 = (TextView) findViewById(R.id.color3);
        color2 = (TextView) findViewById(R.id.color2);

        l1 = (LinearLayout) findViewById(R.id.Switch);
        l3 = (LinearLayout) findViewById(R.id.Voice);
        l4 = (LinearLayout) findViewById(R.id.Reservation);

        b1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                sendData("1");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                sendData("2");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                sendData("3");
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                sendData("4");
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                sendData("5");
            }
        });


        BlindSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color1.setBackgroundColor(Color.rgb(250, 156, 126));
                color2.setBackgroundColor(Color.rgb(180, 190, 231));
                color3.setBackgroundColor(Color.rgb(180, 190, 231));

                l1.setVisibility(View.VISIBLE);
                l3.setVisibility(View.GONE);
                l4.setVisibility(View.GONE);
            }
        });

        BlindVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color1.setBackgroundColor(Color.rgb(180, 190, 231));
                color3.setBackgroundColor(Color.rgb(180, 190, 231));
                color2.setBackgroundColor(Color.rgb(250, 156, 126));

                l1.setVisibility(View.GONE);
                l3.setVisibility(View.VISIBLE);
                l4.setVisibility(View.GONE);
            }
        });

        BlindReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color1.setBackgroundColor(Color.rgb(180, 190, 231));
                color2.setBackgroundColor(Color.rgb(180, 190, 231));
                color3.setBackgroundColor(Color.rgb(250, 156, 126));

                l1.setVisibility(View.GONE);
                l3.setVisibility(View.GONE);
                l4.setVisibility(View.VISIBLE);

            }
        });

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                cancleBtn.setVisibility(View.VISIBLE);
                reservationBtn.setVisibility(View.GONE);

                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                int time = timePicker.getHour();
                int minute = timePicker.getMinute();
                Toast.makeText(getApplicationContext(), time + "시" + minute + "분" + "예약 되었습니다.", Toast.LENGTH_SHORT).show();

//                intent.putExtra( "state",  );
//
//                pendingIntent = PendingIntent.getBroadcast( getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
//
//                alarm_manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

                sendData(Integer.toString(time) + minute);
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                cancleBtn.setVisibility(View.GONE);
                reservationBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT);
//                alarm_manager.cancel( pendingIntent );
//                intent.putExtra( "state", "0" );
                sendData("0");
            }
        });

        voice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice1.setImageResource(R.drawable.voice2);
                mRecognizer.startListening(i);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean sendData(String str) {
        if (str.equals("") || str.equals(null)) {
            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (BleUtil.getIns().getBleDevice() == null) {
            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!BleUtil.getIns().isBlueEnable() || BleUtil.getIns().mBluetoothAdapter == null) {
            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        BleUtil.getIns().write(new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
            }

            @Override
            public void onWriteFailure(BleException exception) {
            }
        }, str);
        return true;
    }

    public boolean sendTime(String str) {
        if (str.equals("") || str.equals(null)) {
            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (BleUtil.getIns().getBleDevice() == null) {
            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!BleUtil.getIns().isBlueEnable() || BleUtil.getIns().mBluetoothAdapter == null) {
            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        BleUtil.getIns().write(new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
            }

            @Override
            public void onWriteFailure(BleException exception) {
            }
        }, str);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendVoiceData(String[] str) {
        if (BleUtil.getIns().getBleDevice() == null) {
            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;

        }
        if (!BleUtil.getIns().isBlueEnable() || !BleUtil.getIns().isConnected()) {
            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;

        }

        BleUtil.getIns().write(new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
            }

            @Override
            public void onWriteFailure(BleException exception) {
            }
        }, String.valueOf(str[0]));
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식시작", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

            Toast.makeText(getApplicationContext(), "음성인식종료", Toast.LENGTH_SHORT).show();
            voice1.setImageResource(R.drawable.voice1);
        }

        @Override
        public void onError(int error) {
            Toast.makeText(getApplicationContext(), "오류발생! 다시입력해주세요", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            String key = " ";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            tx2.setText(rs[0]);
            sendVoiceData(rs);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };
}

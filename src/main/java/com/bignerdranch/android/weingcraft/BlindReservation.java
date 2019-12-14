package com.bignerdranch.android.weingcraft;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.weingcraft.BleUtils.BleUtil;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;

public class BlindReservation extends Service {
    int startId;
    TextView t1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();



        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("알람시작")
                    .setContentText("블라인드가 올라갑니다.")
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .build();

            startForeground(1, notification);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String getState = intent.getExtras().getString("state");

        assert getState != null;
        switch (getState) {
            case "5":
                startId = 1;
                break;
            case "0":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }


        if (startId == 1) {
            sendData("5");

        } else if (startId == 0) {
            sendData("0");
        }
        return START_NOT_STICKY;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean sendData(String str) {
        if(str.equals( "" ) || str.equals( null )){
            Toast.makeText( this,"블루투스 디바이스 연결이 되지 않았습니다.",Toast.LENGTH_SHORT ).show();
            return false;
        }
        if (BleUtil.getIns().getBleDevice() == null) {
            Toast.makeText( this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT ).show();
            return false;

        }
        if (!BleUtil.getIns().isBlueEnable() || BleUtil.getIns().mBluetoothAdapter == null ){
            Toast.makeText( this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT ).show();
            return false;
        }

        BleUtil.getIns().write( new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
            }

            @Override
            public void onWriteFailure(BleException exception) {
            }
        }, str);
        return true;
    }
}





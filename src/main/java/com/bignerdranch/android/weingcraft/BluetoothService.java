package com.bignerdranch.android.weingcraft;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.logging.Handler;

public class BluetoothService{
    private static final String TAG = "BluetoothService";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter btAdapter;

    private AppCompatActivity mActivity;
    private Handler mHandler;

   public BluetoothService(AppCompatActivity ac, Handler h){
       mActivity = ac;
       mHandler = h;
   }

    public boolean getDeviceState() {
        Log.i( TAG, "Check the Bluetooth support" );

        if (btAdapter == null){
            Log.d(TAG,"Bluetooth is not available");

            return false;
        }else{
            Log.d(TAG,"Bluetooth is available");
            return true;
        }
    }

    public void enableBluetooth(){
        Log.i(TAG,"Check the enabled Bluetooth");

        if(btAdapter.isEnabled()){
            Log.d(TAG,"Bluetooth Enable Now");
        }else{
            Log.d(TAG,"Bluetooth Enable Request");

            Intent i = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
            mActivity.startActivityForResult( i,REQUEST_ENABLE_BT );
        }
    }
}

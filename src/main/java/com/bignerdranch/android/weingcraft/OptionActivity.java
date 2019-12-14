package com.bignerdranch.android.weingcraft;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.weingcraft.BleUtils.BleUtil;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class OptionActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_BT_PERMISSIONS = 0;

    /////////////GUI 컴포넌트///////////////
    LinearLayout l1, l2;
    Button conBtn;

    ProgressDialog progressDialog;

    @SuppressLint("HandlerLeak")
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_option );

        //////////////////////GUI 컴포넌트//////////////////////////////////

        l2 = findViewById( R.id.l2 );
        conBtn = findViewById( R.id.conBtn );

        // onClick 콜백함수를 액티비티 onClick메소드로 처리하기 위한 세팅
        conBtn.setOnClickListener( this );
        progressDialog = new ProgressDialog( this );


        BleUtil.getIns().init( this );
        checkBtPermissions();               // 권한 체크 위치, 블루투스
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.conBtn: {
                if (!checkBtPermissions()) return;
                BleUtil.getIns().scan();        // scan -> listing-> click -> connect -> write/read/notify
                break;
            }

        }
    }


    public boolean checkBtPermissions() {

        if (!BleManager.getInstance().isBlueEnable() || BleManager.getInstance().getBluetoothAdapter() == null) {
            Log.e( "Error", "Doesn't exist Bluetooth Device" );
            return false;
        }

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled() || bluetoothAdapter == null) {
            Toast.makeText( this, "블루투스가 활성화 되지 않았습니다.", Toast.LENGTH_LONG ).show();
            return false;
        }

        this.requestPermissions(
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN
                }, REQUEST_BT_PERMISSIONS );
        return true;
    }




}
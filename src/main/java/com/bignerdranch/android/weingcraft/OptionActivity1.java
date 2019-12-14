//package com.bignerdranch.android.weingcraft;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.bluetooth.BluetoothAdapter;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bignerdranch.android.weingcraft.BleUtils.BleUtil;
//import com.clj.fastble.callback.BleNotifyCallback;
//import com.clj.fastble.callback.BleReadCallback;
//import com.clj.fastble.callback.BleWriteCallback;
//import com.clj.fastble.exception.BleException;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.GoogleAuthProvider;
//
//
//public class OptionActivity extends AppCompatActivity implements View.OnClickListener {
//    public static final int REQUEST_BT_PERMISSIONS = 0;
//
//    /////////////GUI 컴포넌트///////////////
//    LinearLayout l1, l2;
//    Button BltTabBtn, conBtn, sendBtn, notifyBtn, readBtn;
//    TextView t1, t2, mTvReceiveData;
//    EditText etSend;
//
//    Spinner         mProtocolNameSpinner;
//    ProgressDialog  progressDialog;
//    private String[] protocolNumsArr;
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @SuppressLint("HandlerLeak")
//    protected void onCreate(final Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_option);
//
//        //////////////////////GUI 컴포넌트//////////////////////////////////
//
//        l2 = findViewById(R.id.l2);
//        t1 = findViewById(R.id.c1);
//       // t2 = findViewById(R.id.c2);
//
//        //sendBtn = findViewById(R.id.sendBtn);
//        //etSend = findViewById(R.id.etSend);
//        BltTabBtn = findViewById(R.id.BltTabBtn);
//        conBtn = findViewById(R.id.conBtn);
////        notifyBtn = findViewById(R.id.notifyBtn);
////        readBtn = findViewById(R.id.readBtn);
////        mTvReceiveData = findViewById(R.id.bltRcv);
////        mProtocolNameSpinner = findViewById(R.id.protocolName);
//        protocolNumsArr = getResources().getStringArray(R.array.번호);
//
//        // onClick 콜백함수를 액티비티 onClick메소드로 처리하기 위한 세팅
//        BltTabBtn.setOnClickListener(this);
//        conBtn.setOnClickListener(this);
////        sendBtn.setOnClickListener(this);
////        notifyBtn.setOnClickListener(this);
////        readBtn.setOnClickListener(this);
//        progressDialog = new ProgressDialog(this);
//
//////        mProtocolNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                String protocolNum = protocolNumsArr[position];
////                Toast.makeText(getApplicationContext(),"프로토콜번호 : "+protocolNum,Toast.LENGTH_LONG).show();
////            }
////            @Override
////            public void onNothingSelected(AdapterView<?> parent) {
////            }
////        });
//
////        BleUtil.getIns().init(this);
////        checkBtPermissions();               // 권한 체크 위치, 블루투스
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.BltTabBtn: {
//                l2.setVisibility(View.VISIBLE);
//                l1.setVisibility(View.GONE);
//                t2.setBackgroundColor(Color.rgb(250, 156, 126));
//                t1.setBackgroundColor(Color.rgb(180, 190, 231));
//                break;
//            }
//            case R.id.conBtn: {
//                if( !checkBtPermissions() ) return;
//                BleUtil.getIns().scan();        // scan -> listing-> click -> connect -> write/read/notify
//                break;
//            }
////            case R.id.sendBtn: {
////                sendData( etSend.getText().toString() );   // 에디트 텍스트에 있는 내용을 전송
////                etSend.setText("");
////                break;
////            }
////            case R.id.readBtn:{
////                read();
////                break;
////            }
////            case R.id.notifyBtn: {
////                notification();
////                break;
////            }
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void sendVoiceData(String[] str){
//        if (BleUtil.getIns().getBleDevice() == null) {
//            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!BleUtil.getIns().isBlueEnable() || !BleUtil.getIns().isConnected()) {
//            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        BleUtil.getIns().write(new BleWriteCallback() {
//            @Override
//            public void onWriteSuccess(int current, int total, byte[] justWrite) {
//            }
//
//            @Override
//            public void onWriteFailure(BleException exception) {
//            }
//        }, String.valueOf(str[0]));
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void sendData(String str ){
//
//        if (BleUtil.getIns().getBleDevice() == null) {
//            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!BleUtil.getIns().isBlueEnable() || !BleUtil.getIns().isConnected()) {
//            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        BleUtil.getIns().write(new BleWriteCallback() {
//            @Override
//            public void onWriteSuccess(int current, int total, byte[] justWrite) {
//            }
//
//            @Override
//            public void onWriteFailure(BleException exception) {
//            }
//        }, str);
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void read(){
//        if (BleUtil.getIns().getBleDevice() == null) {
//            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!BleUtil.getIns().isBlueEnable() || !BleUtil.getIns().isConnected()) {
//            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        BleUtil.getIns().read(new BleReadCallback() {
//            @Override
//            public void onReadSuccess(byte[] data) {
//                mTvReceiveData.append(data.toString()+"\n");
//            }
//
//            @Override
//            public void onReadFailure(BleException exception) {
//
//            }
//        });
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void notification(){
//
//        if (BleUtil.getIns().getBleDevice() == null) {
//            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!BleUtil.getIns().isBlueEnable() || !BleUtil.getIns().isConnected()) {
//            Toast.makeText(this, "블루투스 디바이스 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        BleUtil.getIns().notification(new BleNotifyCallback() {
//            @Override
//            public void onNotifySuccess() {
//
//            }
//
//            @Override
//            public void onNotifyFailure(BleException exception) {
//
//            }
//
//            @Override
//            public void onCharacteristicChanged(byte[] data) {
//
//            }
//        }, false);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public boolean checkBtPermissions() {
//
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//////        if (!bluetoothAdapter.isEnabled() || bluetoothAdapter == null) {
////            Toast.makeText(this, "블루투스가 활성화 되지 않았습니다.", Toast.LENGTH_LONG).show();
////            return false;
////        }
//
//        this.requestPermissions(
//            new String[]{
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.BLUETOOTH,
//                Manifest.permission.BLUETOOTH_ADMIN
//            },  REQUEST_BT_PERMISSIONS);
//        return true;
//    }
//}

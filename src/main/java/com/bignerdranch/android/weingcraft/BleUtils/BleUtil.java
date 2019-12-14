package com.bignerdranch.android.weingcraft.BleUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.bignerdranch.android.weingcraft.BluetoothService;
import com.bignerdranch.android.weingcraft.OptionActivity;
import com.clj.fastble.BleManager;
import com.clj.fastble.bluetooth.BleBluetooth;
import com.clj.fastble.bluetooth.BleConnector;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BleUtil {

    private static BleUtil ins = null;      // 싱글턴 클래스

    public static BleUtil getIns() {
        if (ins == null) ins = new BleUtil();
        return ins;
    }

    public static final int REQUEST_BT_PERMISSIONS = 0;

    private BleDevice bleDevice = null;
    private BleConnector bleConn = null;
    private BleBluetooth bleBluetooth = null;
    public BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothLeScanner mBluetoothLeScanner = null;
    private BluetoothGatt mBluetoothGatt = null;
    private Set<BluetoothDevice> devices;
    private ArrayList<String> mScanedDeviceList;
    private ProgressDialog progressDialog;
    public Activity app = null;
    private ScanCallback scanCallback;
    public UUID serviceUUID;
    public UUID characteristicUUID;
    private List<String> charaList;
    AlertDialog.Builder builder;

    public BleUtil() {


    }

    public void init(Activity app) {
        // 액티비티 변수 app이 필요한 경우

        this.app = app;
        BleManager.getInstance().init(app.getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);

        if (!BleManager.getInstance().isBlueEnable() || BleManager.getInstance().getBluetoothAdapter() == null) {
            Log.e("Error", "Doesn't exist Bluetooth Device");
            Toast.makeText(app.getApplicationContext(), "블루투스 장치가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        devices = new HashSet<>();  // 스캔한 BLE디바이스 HashSet(중복없음)
        mScanedDeviceList = new ArrayList<>();
        charaList = new ArrayList<String>();

        progressDialog = new ProgressDialog(app);
        serviceUUID = null;
        characteristicUUID = null;
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, final ScanResult result) {
                super.onScanResult(callbackType, result);
                addDevice(result.getDevice());     // scan한 디바이스를 디바이스 리스트에 추가
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };
    }

    // 알고리즘 순서 scan -> list -> conenct -> send

    public void scan() {
        Log.i("Scanning", "start");
        if (devices != null) devices.clear();
        progressDialog.setMessage("BLE Device Scanning..");                             // 메시지
        progressDialog.setCancelable(false);                                        // 중간에 종료하지 못하게 설정
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal); // 스타일 설정
        progressDialog.show();

                mBluetoothLeScanner.startScan(scanCallback);

        // 설정한 시간후의 동작
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothLeScanner.stopScan(scanCallback);
                Toast.makeText(app.getApplicationContext(), "Scanning Complete", Toast.LENGTH_SHORT).show();
                listScannedDevices();                          // 스캔한 장치들의 이름을 리스팅
                progressDialog.dismiss();                      // 프로그레스 다이얼로그 종료
            }
        }, 7000);  // 7초동안 진행
    }

    public void listScannedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            if (devices.size() > 0) {                       // 검색된 블루투스 디바이스 하나 이상이라면
                mScanedDeviceList.clear();                  // 기존에 리스팅됐던 리스트들을 clear
                builder = new AlertDialog.Builder(app);     // 장치 주소 및 이름들이 표시될 새 다이얼로그
                builder.setTitle("Scanned Device List");
                builder.setCancelable(false); // 스캔 되는 도중에는 강제로 종료할 수 없음\
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

                for (BluetoothDevice device : devices) {
                    if (device.getName() == null)
                        mScanedDeviceList.add(device.getAddress());     // Name이 없는경우 MAC주소로 리스트뷰의 항목으로 설정
                    else mScanedDeviceList.add(device.getName());
                }

                final CharSequence[] items = mScanedDeviceList.toArray(new CharSequence[mScanedDeviceList.size()]);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());      // 리스트뷰에서 선택한 디바이스에 접속
                    }
                });
                AlertDialog alert = builder.create();   // 리스트뷰를 띄울 다이얼로그 생성
                alert.show();                           // 다이얼로그 show
            } else
                Toast.makeText(app.getApplicationContext(), "검색된 장치가 없습니다.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(app.getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
    }

    public void connectSelectedDevice(String selectedDeviceName) {
        for (BluetoothDevice tempDevice : devices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {                  // 선택한 디바이스의 문자열과 검색한 전체 디바이스들과 대조후 최종적으로 선택완료
                bleDevice = BleManager.getInstance().convertBleDevice(tempDevice);  // 블루투스 디바이스로 부터 FastBle의 새로운 블루투스 객체로 컨버팅
                break;
            }
        }
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(app, "블루투스 연결 중 오류가 발생 했습니다.", Toast.LENGTH_SHORT).show();
        }
        devices.clear();        // 스캔한 디바이스 리스트를 비움.
    }

    private void connect() {
        bleBluetooth = new BleBluetooth(bleDevice);

        bleBluetooth.connect(bleDevice, true, new BleGattCallback() {       // 실질적인 연결
            @Override
            public void onStartConnect() {
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {

                Toast.makeText(app, "연결에 성공했습니다.", Toast.LENGTH_SHORT).show();
                bleConn = bleBluetooth.newBleConnector();       // getBleConnector  bleDevice -> bleBluetooth -> bleConnector
                listGattServices();                             // read,write,notify 등의 서비스를 판별하고 사용자를 위해 리스트뷰 생성
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

            }
        });
    }

    private void listGattServices() {
        List<BluetoothGattService> services = bleBluetooth.getBluetoothGatt().getServices();
        for (BluetoothGattService s : services) {
            List<BluetoothGattCharacteristic> charas = s.getCharacteristics();
            for (BluetoothGattCharacteristic c : charas) {

                charaList.clear();
                int charaProp = c.getProperties();      // 특이하게 중복되지 않는 비트값으로 되어있음.  ex) 1110 이면 read,write,notify 이런식 0010 이면 read만
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    charaList.add("read");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    charaList.add("write");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                    charaList.add("write_no_response");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    charaList.add("notify");
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    charaList.add("indicate");
                }

                if (charaList.contains("read") && charaList.contains("write") && charaList.contains("notify")) {     // read,write, notify가 있는 서비스를 찾는다.
                    serviceUUID = s.getUuid();
                    characteristicUUID = c.getUuid();
                }

            }
        }
    }

    public void read(BleReadCallback bleReadCallback) {
        if (serviceUUID == null || characteristicUUID == null) {
            Toast.makeText(app, "serviceUUID 또는 characteristicUUID가 없습니다..", Toast.LENGTH_LONG).show();
            return;
        }
        bleConn.withUUIDString(serviceUUID.toString(), characteristicUUID.toString());
        bleConn.readCharacteristic(bleReadCallback, characteristicUUID.toString());
    }

    public void write(BleWriteCallback bleWriteCallback, String data) {
        if (serviceUUID == null || characteristicUUID == null) {
            Toast.makeText(app, "serviceUUID 또는 characteristicUUID가 없습니다..", Toast.LENGTH_LONG).show();
            return;
        }
        bleConn.withUUIDString(serviceUUID.toString(), characteristicUUID.toString());
        bleConn.writeCharacteristic(data.getBytes(), bleWriteCallback, characteristicUUID.toString());
    }

    public void notification(BleNotifyCallback bleNotifyCallback, boolean userCharacteristicDescription) {
        if (serviceUUID == null || characteristicUUID == null) {
            Toast.makeText(app, "serviceUUID 또는 characteristicUUID가 없습니다..", Toast.LENGTH_LONG).show();
            return;
        }
        bleConn.withUUIDString(serviceUUID.toString(), characteristicUUID.toString());
        bleConn.enableCharacteristicNotify(bleNotifyCallback, characteristicUUID.toString(), userCharacteristicDescription);
    }

    public BleDevice getBleDevice() {
        return this.bleDevice;
    }

    public boolean isConnected() {
        return BleManager.getInstance().isConnected(bleDevice);
    }

    public boolean isBlueEnable() {
        return BleManager.getInstance().isBlueEnable();
    }

    public void addDevice(BluetoothDevice bluetoothDevice) {
        devices.add(bluetoothDevice);
    }


    public void disConnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
        }
    }
}

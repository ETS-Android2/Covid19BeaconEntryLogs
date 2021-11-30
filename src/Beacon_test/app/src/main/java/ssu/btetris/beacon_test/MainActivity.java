package ssu.btetris.beacon_test;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.altbeacon.beacon.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "sampleCreateBeacon";

    public static final Identifier MY_MATCHING_IDENTIFIER = Identifier.fromInt(0x8b9c);

    private Button startbtn, settingbtn;
    private TextView textview;

    private String PhoneNumber = "01012341234";
    private String MENU = "sandwich";

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.startbtn:
                    String stringToTransmit2 = PhoneNumber;
                    byte[] stringToTransmitAsAsciiBytes2 = stringToTransmit2.getBytes(StandardCharsets.US_ASCII);

                    String stringToTransmit3 = MENU;
                    byte[] stringToTransmitAsAsciiBytes3 = stringToTransmit3.getBytes(StandardCharsets.US_ASCII);

                    // 비콘 생성 후 시작. 실제 가장 필요한 소스
                    Beacon beacon = new Beacon.Builder()
                            .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")  // uuid for beacon
                            .setId2( Identifier.fromBytes(stringToTransmitAsAsciiBytes2, 0, 2, false).toString() )
                            .setId3( Identifier.fromBytes(stringToTransmitAsAsciiBytes3, 0, 2, false).toString() )
                            .setManufacturer(0x0118)  // Radius Networks. 0x0118 : Change this for other beacon layouts // 0x004C : for iPhone
                            .setTxPower(-59)  // Power in dB
                            .setDataFields(Arrays.asList(new Long[] {0l}))  // Remove this for beacon layouts without d: fields
                            .build();
                    BeaconParser beaconParser = new BeaconParser()
                            .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");//위에 데이터 정보 형식을 기술하는 곳
                    BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
                    beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
                        @Override
                        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                            super.onStartSuccess(settingsInEffect);
                            Log.d(TAG, "onStartSuccess: ");
                        }

                        @Override
                        public void onStartFailure(int errorCode) {
                            super.onStartFailure(errorCode);
                            Log.d(TAG, "onStartFailure: " + errorCode);
                        }
                    });

                    break;
                case R.id.settingbtn:
                    startSettingActivity();
                    break;
            }

            }
    };

    private void startSettingActivity() {
        Intent reqmsg = new Intent(MainActivity.this, SettingActivity.class);
        reqmsg.putExtra("PhoneNumber", PhoneNumber);
        reqmsg.putExtra("MENU", MENU);
        startActivityResult.launch(reqmsg);
    }
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int rescode = result.getResultCode();
                    if (rescode == Activity.RESULT_OK) {
                        Intent res = result.getData();
                        PhoneNumber = res.getStringExtra("PhoneNumber");
                        MENU = res.getStringExtra("MENU");
                        Log.d("MainActivity", "response=("+PhoneNumber+","+MENU+")");
                    }
                    else if (rescode == Activity.RESULT_CANCELED)
                        Log.d("MainActivity", "response=(null)");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textview = (TextView) findViewById(R.id.textview);
        startbtn = (Button) findViewById(R.id.startbtn);
        settingbtn = (Button) findViewById(R.id.settingbtn);

        startbtn.setOnClickListener(OnClickListener);
        settingbtn.setOnClickListener(OnClickListener);

        // 퍼미션 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("위치 권한이 필요합니다.");
                builder.setMessage("비콘 송신 기능을 위해서 권한을 부여해주시기 바랍니다.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }



    }


    // 퍼미션 요청후 callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("위치 권한 - 앱을 사용하는 동안에 허용");
                    builder.setMessage("앱이 백그라운드로 동작하는 동안에는 비콘 송신 기능이 정상적으로 작동하지 않을 수 있습니다.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }
}
package org.altbeacon.beaconreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends Activity {
    public static final Identifier MY_MATCHING_IDENTIFIER = Identifier.fromInt(0x8b9c);

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    private String servHostName = "255.255.255.255";
    private int servPortNo = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        Intent res = getIntent();
        servHostName = res.getStringExtra("servHostName");
        String s = res.getStringExtra("servPortNo");
        servPortNo = Integer.parseInt(s);
        if (servHostName == null) {
            Log.d(TAG, "getintent error");
            servHostName = "255.255.255.255";
            servPortNo = 9999;
        }
        Log.d(TAG, "servHostName: "+servHostName + " servPortNo: " +String.valueOf(servPortNo));
    }

    @Override
    protected void onResume() {
        super.onResume();
        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                    Beacon firstBeacon = beacons.iterator().next();

                    logToDisplay("비콘 발견: " + firstBeacon.toString() + " 떨어진 거리: " + firstBeacon.getDistance() + " 미터");
                }


                for (Beacon beacon : beacons) {
                    Log.d(TAG, beacon.getId1().toString() + " "+ beacon.getId2().toString() + " " + beacon.getId3().toString());
                    //if (beacon.getId1().equals(MY_MATCHING_IDENTIFIER)) {
                    String receivedString = "null", receivedString2 = "null";
                    byte[] bytes = beacon.getId2().toByteArray();
                    byte[] bytes2 = beacon.getId3().toByteArray();
                    try {
                        receivedString = new String(bytes, 0, bytes.length, "ASCII");
                        receivedString2 = new String(bytes2, 0, bytes2.length, "ASCII");
                    } catch (Exception e) {
                        Log.d(TAG, "Cannot decode ASII");
                    }
                    Log.d(TAG, "I just received: "+receivedString + " 2: " +receivedString2);

                    //socket 서버로 전달
                    String data = "in;"+ receivedString +";"+ receivedString2;

                    Log.d("socketclient", servHostName + String.valueOf(servPortNo) + data);
                    socketclient thread = new socketclient(servHostName, servPortNo, data);
                    thread.start();
                    //}
                }
            }

        };
        beaconManager.addRangeNotifier(rangeNotifier);
        beaconManager.startRangingBeacons(BeaconReferenceApplication.wildcardRegion);
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.stopRangingBeacons(BeaconReferenceApplication.wildcardRegion);
        beaconManager.removeAllRangeNotifiers();
    }

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                editText.append(line+"\n");
            }
        });
    }
}

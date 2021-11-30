package org.altbeacon.beaconreference;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

/**
 *
 * @author dyoung
 * @author Matt Tyler
 */
public class MonitoringActivity extends Activity implements MonitorNotifier {
	public static final Identifier MY_MATCHING_IDENTIFIER = Identifier.fromInt(0x8b9c);

	protected static final String TAG = "MonitoringActivity";
	private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
	private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

	private EditText viewHostName;
	private EditText viewPortNo;

	private String servHostName = "255.255.255.255";
	private int servPortNo = 9999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);

		viewHostName = (EditText) findViewById(R.id.editview1);
		viewPortNo = (EditText) findViewById(R.id.editview2);

		verifyBluetooth();
		requestPermissions();
		BeaconManager.getInstanceForApplication(this).addMonitorNotifier(this);
		// No need to start monitoring here because we already did it in
		// BeaconReferenceApplication.onCreate
		// check if we are currently inside or outside of that region to update the display
		if (BeaconReferenceApplication.insideRegion) {
			logToDisplay("Beacons are visible.");
		}
		else {
			logToDisplay("No beacons are visible.");
		}

	}

	@Override
	public void didEnterRegion(Region region) {
		logToDisplay("didEnterRegion called");
		Log.d(TAG, "beacon in");

	}
	@Override
	public void didExitRegion(Region region) {
		logToDisplay("didExitRegion called");

		Log.d(TAG, "beacon out");

		servHostName = viewHostName.getText().toString();
		servPortNo = Integer.parseInt( viewPortNo.getText().toString() );

		String data = "out,";
		socketclient thread = new socketclient(servHostName, servPortNo, data);
		thread.start();
	}
	@Override
	public void didDetermineStateForRegion(int state, Region region) {
		logToDisplay("비콘 상태 변화: " + (state == 1 ? "INSIDE ("+state+")" : "OUTSIDE ("+state+")"));
	}



	private void requestPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
							!= PackageManager.PERMISSION_GRANTED) {
						if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
							final AlertDialog.Builder builder = new AlertDialog.Builder(this);
							builder.setTitle("이 앱은 백그라운드 위치 권한이 필요합니다.");
							builder.setMessage("백그라운드에서 비콘을 인식하기 위해 권한을 허용해주십시오.");
							builder.setPositiveButton(android.R.string.ok, null);
							builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@TargetApi(23)
								@Override
								public void onDismiss(DialogInterface dialog) {
									requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
											PERMISSION_REQUEST_BACKGROUND_LOCATION);
								}

							});
							builder.show();
						}
						else {
							final AlertDialog.Builder builder = new AlertDialog.Builder(this);
							builder.setTitle("기능이 제한됨.");
							builder.setMessage("백그라운드 위치 접근 권한이 허용되지 않아서, 백그라운드 상에서는 비콘 신호를 찾을 수 없습니다.");
							builder.setPositiveButton(android.R.string.ok, null);
							builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {
								}

							});
							builder.show();
						}
					}
				}
			} else {
				if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
					requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
									Manifest.permission.ACCESS_BACKGROUND_LOCATION},
							PERMISSION_REQUEST_FINE_LOCATION);
				}
				else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("기능이 제한됨.");
					builder.setMessage("백그라운드 위치 접근 권한이 허용되지 않아서, 백그라운드 상에서는 비콘 신호를 찾을 수 없습니다.");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
						}

					});
					builder.show();
				}

			}
		}
	}



	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_FINE_LOCATION: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "fine location permission granted");
				} else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("기능이 제한됨.");
					builder.setMessage("위치 접근 권한이 허용되지 않아서, 비콘 신호를 찾을 수 없습니다.");
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
			case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "background location permission granted");
				} else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("기능이 제한됨.");
					builder.setMessage("백그라운드 위치 접근 권한이 허용되지 않아서, 백그라운드 상에서는 비콘 신호를 찾을 수 없습니다.");
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

	public void onRangingClicked(View view) {
		Intent myIntent = new Intent(this, RangingActivity.class);

		servHostName = viewHostName.getText().toString();
		servPortNo = Integer.parseInt( viewPortNo.getText().toString() );

		myIntent.putExtra("servHostName", servHostName);
		myIntent.putExtra("servPortNo", String.valueOf(servPortNo));

		Log.d(TAG, "putExtra, servHostName: "+servHostName + " servPortNo: " +String.valueOf(servPortNo));

		this.startActivity(myIntent);
	}

	public void onEnableClicked(View view) {
		// This is a toggle.  Each time we tap it, we start or stop
		Button button = (Button) findViewById(R.id.enableButton);
		if (BeaconManager.getInstanceForApplication(this).getMonitoredRegions().size() > 0) {
			BeaconManager.getInstanceForApplication(this).stopMonitoring(BeaconReferenceApplication.wildcardRegion);
			button.setText("Enable Monitoring");
		}
		else {
			BeaconManager.getInstanceForApplication(this).startMonitoring(BeaconReferenceApplication.wildcardRegion);
			button.setText("Disable Monitoring");
		}
	}

	private void verifyBluetooth() {
		try {
			if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("블루투스 꺼짐");
				builder.setMessage("블루투스 설정을 켜고 앱을 재시작해주십시오.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						finishAffinity();
					}
				});
				builder.show();
			}
		}
		catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE 기능 미지원");
			builder.setMessage("이 단말에서는 블루투스 비콘 기능을 사용할 수 없습니다.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					finishAffinity();
				}

			});
			builder.show();
		}
	}

	private String cumulativeLog = "";
	private void logToDisplay(String line) {
		cumulativeLog += line+"\n";
		runOnUiThread(new Runnable() {
			public void run() {
				EditText editText = (EditText)MonitoringActivity.this
						.findViewById(R.id.monitoringText);
				editText.setText(cumulativeLog);
			}
		});
	}

}

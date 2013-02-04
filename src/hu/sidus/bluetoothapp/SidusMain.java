package hu.sidus.bluetoothapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SidusMain extends Activity {

	// Debugging
	private static final String TAG = "SidusMain";
	private static final boolean D = true;

	private static final int REQUEST_CONNECT = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	public static final int MESSAGE_TOAST = 1;
	public static final int MESSAGE_DEVICE_NAME = 2;
	public static String TOAST;

	private static BluetoothAdapter mBluetoothAdapter;
	private static BluetoothDevice mBluetoothDevice;

	private static final UUID UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	SidusService mSidusService;

	public static String mConnectedDeviceName;
	public static String DEVICE_NAME;

	byte[] CMD_GETSWVER = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x6B, 0x6B, 0x45 };

	private TextView answer;
	private Button send;

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "ONSTART");
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sidus_main);
		Log.v(TAG, "ONCREATE");
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		answer = (TextView) findViewById(R.id.answer);
		send = (Button) findViewById(R.id.button_send);
		
		mSidusService = new SidusService();
		mSidusService.start();

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSidusService.write(CMD_GETSWVER);
			}
		});

	}

//	@Override
//	protected synchronized void onResume() {
//		super.onResume();
//		Log.v(TAG, "ONRESUME");
//		
//		if (mSidusService != null)
//			mSidusService.start();
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSidusService.stop();
	}

	public void setupService() {
		// mService = new SidusService(getApplicationContext(), mHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sidus_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_connect:
			Intent sidusConnect = new Intent(getApplicationContext(), SidusConnect.class);
			startActivityForResult(sidusConnect, REQUEST_CONNECT);
			break;

		case R.id.menu_discoverable:
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
			break;

		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_CONNECT:

			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "No device selected", Toast.LENGTH_LONG).show();
				break;
			}

			String address = data.getStringExtra(SidusConnect.EXTRA_DEVICE_ADDRESS);
			mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);
			Log.i(TAG, mBluetoothDevice.getName() + mBluetoothDevice.getAddress());

			mSidusService.connect(mBluetoothDevice);
			break;

		case REQUEST_ENABLE_BT:

			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Can't enable bluetooth", Toast.LENGTH_LONG).show();
				break;
			}

			if (resultCode == RESULT_OK) {
				Toast.makeText(getApplicationContext(), "Bluetooth is enabled", Toast.LENGTH_LONG).show();
				break;
			}

			break;
		}
	}
}

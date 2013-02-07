package hu.sidus.bluetoothapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
	public static final int MESSAGE_READ = 1;

	public static String TOAST;

	private static BluetoothAdapter mBluetoothAdapter;
	private static BluetoothDevice mBluetoothDevice;

	private static final UUID UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	SidusService mSidusService;

	public static String mConnectedDeviceName;
	public static String DEVICE_NAME;

	byte[] CMD_GETSWVER = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x6B, 0x6B, 0x45 };
	byte[] CMD_GETTIMERS = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x61, 0x61, 0x45 };

	private TextView answer;
	private Button cmd_getswver;
	private Button cmd_gettimers;

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
		
		cmd_getswver = (Button) findViewById(R.id.button_cmd_getswver);
		cmd_gettimers = (Button) findViewById(R.id.button_cmd_gettimers);

		mSidusService = new SidusService(mHandler);
		mSidusService.start();

		cmd_getswver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSidusService.write(CMD_GETSWVER);
			}
		});
		
		cmd_gettimers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSidusService.write(CMD_GETTIMERS);
			}
		});

	}

	// @Override
	// protected synchronized void onResume() {
	// super.onResume();
	// Log.v(TAG, "ONRESUME");
	//
	// if (mSidusService != null)
	// mSidusService.start();
	// }

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
				Toast.makeText(getApplicationContext(), "Shit happens", Toast.LENGTH_LONG).show();
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

	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MESSAGE_READ:

				byte[] readBuf = (byte[]) msg.obj;
				int paramInt = msg.arg1;

				String hexString = new String(byte2HexStr(readBuf, paramInt));
				answer.setText(answer.getText() + hexString + "\n");

				// // Valami itt meg nem mukodik megfeleloen ...
				// try {
				//
				// // hexString = String.format("%02X", new
				// BigInteger(readMessage.getBytes("US-ASCII")));
				// // answer.setText(answer.getText() + hexString + "\n");
				//
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				break;

			}
		}
	};

	public static String byte2HexStr(byte[] paramArrayOfByte, int paramInt) {

		StringBuilder localStringBuilder = new StringBuilder("");

		for (int i = 0;; i++) {
			
			if (i >= paramInt)
				return localStringBuilder.toString().toUpperCase().trim();
			
			String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
			
			if (str.length() == 1)
				str = "0" + str;
			
			localStringBuilder.append(str);
			localStringBuilder.append(" ");
		}
	}
}

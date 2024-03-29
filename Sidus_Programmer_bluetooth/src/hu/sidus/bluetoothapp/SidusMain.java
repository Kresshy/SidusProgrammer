package hu.sidus.bluetoothapp;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
	public static final int MESSAGE_READ = 3;

	public static String TOAST;

	private static BluetoothAdapter mBluetoothAdapter;
	private static BluetoothDevice mBluetoothDevice;
	private static byte[] datastream;

	private static final UUID UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	SidusService mSidusService;

	public static String mConnectedDeviceName;
	public static String DEVICE_NAME;

	byte[] CMD_GETSWVER = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x6B, 0x6B, 0x45 };
	byte[] CMD_GETTIMERS = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x61, 0x61, 0x45 };
	byte[] CMD_GETSERVOPOS = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x67, 0x67, 0x45 };

	private TextView answer;
	private Button cmd_getswver;
	private Button cmd_gettimers;
	private Button cmd_getservopos;
	private Button send_cmd;
	private EditText enter_cmd;
	private Button connect;

	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (D) {
				if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON) {
					Log.v(TAG, "RECEIVED BLUETOOTH STATE CHANGE: STATE_TURNING_ON");
				}

				if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
					Log.v(TAG, "RECEIVED BLUETOOTH STATE CHANGE: STATE_ON");
				}
			}

			if (mBluetoothAdapter.isEnabled()) {
				mSidusService = new SidusService(mHandler);
				mSidusService.start();
			}
		}
	};

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

		enter_cmd = (EditText) findViewById(R.id.edittext_enter_command);

		cmd_getswver = (Button) findViewById(R.id.button_cmd_getswver);
		cmd_gettimers = (Button) findViewById(R.id.button_cmd_gettimers);
		cmd_getservopos = (Button) findViewById(R.id.button_cmd_getservopos);
		send_cmd = (Button) findViewById(R.id.button_send_command);
		connect = (Button) findViewById(R.id.button_connect);

		registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

		if (mBluetoothAdapter.isEnabled()) {
			mSidusService = new SidusService(mHandler);
			mSidusService.start();
		}

		final SidusProgram program = new SidusProgram(10, 3, mSidusService);

		Intent debugActivity = new Intent(getApplicationContext(), DebugActivity.class);
		startActivity(debugActivity);

		cmd_getswver.setOnClickListener(mOnClickListener);

		cmd_gettimers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSidusService.write(CMD_GETTIMERS);
			}
		});

		cmd_getservopos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSidusService.write(CMD_GETSERVOPOS);
			}
		});

		send_cmd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 String cmd = enter_cmd.getText().toString();
				 byte[] cmd_byte = hexStr2Bytes(cmd);
				 mSidusService.write(cmd_byte);

//				try {
//					program.sendData();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				// mSidusService.write(datastream);
			}
		});

		connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:12:08:27:25:19");
				mSidusService.connect(device);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSidusService.stop();
		unregisterReceiver(bluetoothReceiver);
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

	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MESSAGE_TOAST:

				String message = (String) msg.obj;
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

				break;

			case MESSAGE_READ:

				byte[] readBuf = (byte[]) msg.obj;
				int paramInt = msg.arg1;

				String hexString = new String(byte2HexStr(readBuf, paramInt));
				answer.setText(answer.getText() + hexString + "\n");

				break;

			}
		}
	};

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_cmd_getswver:
				
//				Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_LONG).show();
				
				mSidusService.write(CMD_GETSWVER);
				
				break;
			case R.id.line_one_two:

				break;
			case R.id.line_one_three:

				break;
			case R.id.line_one_four:

				break;
			case R.id.line_one_five:

				break;
			case R.id.line_two_two:

				break;
			case R.id.line_two_three:

				break;
			case R.id.line_two_four:

				break;
			case R.id.line_two_five:

				break;
			case R.id.line_three_two:

				break;
			case R.id.line_three_three:

				break;
			case R.id.line_three_four:

				break;
			case R.id.line_three_five:

				break;
			case R.id.line_four_two:

				break;
			case R.id.line_four_three:

				break;
			case R.id.line_four_four:

				break;
			case R.id.line_four_five:

				break;
			case R.id.line_five_two:

				break;
			case R.id.line_five_three:

				break;
			case R.id.line_five_four:

				break;
			case R.id.line_five_five:

				break;
			case R.id.line_six_two:

				break;
			case R.id.line_six_three:

				break;
			case R.id.line_six_four:

				break;
			case R.id.line_six_five:

				break;
			case R.id.line_seven_two:

				break;
			case R.id.line_seven_three:

				break;
			case R.id.line_seven_four:

				break;
			case R.id.line_seven_five:

				break;
			case R.id.line_eight_two:

				break;
			case R.id.line_eight_three:

				break;
			case R.id.line_eight_four:

				break;
			case R.id.line_eight_five:

				break;
			case R.id.line_nine_two:

				break;
			case R.id.line_nine_three:

				break;
			case R.id.line_nine_four:

				break;
			case R.id.line_nine_five:

				break;
			case R.id.line_ten_two:

				break;
			case R.id.line_ten_three:

				break;
			case R.id.line_ten_four:

				break;
			case R.id.line_ten_five:

				break;
			case R.id.line_eleven_two:

				break;
			case R.id.line_eleven_three:

				break;
			case R.id.line_eleven_four:

				break;
			case R.id.line_eleven_five:

				break;

			default:

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

	public static byte[] hexStr2Bytes(String paramString) {

		String str = paramString.trim().replace(" ", "").toUpperCase();
		int i = str.length() / 2;

		byte[] arrayOfByte = new byte[i];

		for (int j = 0;; j++) {
			if (j >= i)
				return arrayOfByte;

			int k = 1 + j * 2;
			int m = k + 1;
			arrayOfByte[j] = ((byte) (0xFF & Integer.decode("0x" + str.substring(j * 2, k) + str.substring(k, m)).intValue()));
		}
	}
}

package hu.sch.kresshy.sidusprogrammer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SidusProgrammer extends Activity {

	TextView firstTime;
	TextView secondTime;
	TextView thirdTime;
	TextView fourthTime;
	TextView fifthTime;
	TextView sixthTime;
	TextView seventhTime;
	TextView eighthTime;
	TextView ninthTime;
	TextView tenthTime;
	TextView DTTime;

	TextView servo1pos1;
	TextView servo1pos2;
	TextView servo1pos3;
	TextView servo1pos4;
	TextView servo1pos5;
	TextView servo1pos6;
	TextView servo1pos7;
	TextView servo1pos8;
	TextView servo1pos9;
	TextView servo1pos10;
	TextView servo1posDT;

	TextView servo2pos1;
	TextView servo2pos2;
	TextView servo2pos3;
	TextView servo2pos4;
	TextView servo2pos5;
	TextView servo2pos6;
	TextView servo2pos7;
	TextView servo2pos8;
	TextView servo2pos9;
	TextView servo2pos10;
	TextView servo2posDT;

	TextView servo3pos1;
	TextView servo3pos2;
	TextView servo3pos3;
	TextView servo3pos4;
	TextView servo3pos5;
	TextView servo3pos6;
	TextView servo3pos7;
	TextView servo3pos8;
	TextView servo3pos9;
	TextView servo3pos10;
	TextView servo3posDT;

	Button send;
	Button receive;
	ImageButton settings;

	private final int REQUEST_FIRSTTIME = 1;
	private final int REQUEST_SECONDTIME = 2;
	private final int REQUEST_THIRDTIME = 3;
	private final int REQUEST_FOURTHTIME = 4;
	private final int REQUEST_FIFTHTIME = 5;
	private final int REQUEST_SIXTHTIME = 6;
	private final int REQUEST_SEVENTHTIME = 7;
	private final int REQUEST_EIGHTHTIME = 8;
	private final int REQUEST_NINTHTIME = 9;
	private final int REQUEST_TENTHTIME = 10;
	private final int REQUEST_DTTIME = 11;

	private final int REQUEST_SERVO1_1 = 12;
	private final int REQUEST_SERVO1_2 = 13;
	private final int REQUEST_SERVO1_3 = 14;
	private final int REQUEST_SERVO1_4 = 15;
	private final int REQUEST_SERVO1_5 = 16;
	private final int REQUEST_SERVO1_6 = 17;
	private final int REQUEST_SERVO1_7 = 18;
	private final int REQUEST_SERVO1_8 = 19;
	private final int REQUEST_SERVO1_9 = 20;
	private final int REQUEST_SERVO1_10 = 21;
	private final int REQUEST_SERVO1_DT = 22;

	private final int REQUEST_SERVO2_1 = 23;
	private final int REQUEST_SERVO2_2 = 24;
	private final int REQUEST_SERVO2_3 = 25;
	private final int REQUEST_SERVO2_4 = 26;
	private final int REQUEST_SERVO2_5 = 27;
	private final int REQUEST_SERVO2_6 = 28;
	private final int REQUEST_SERVO2_7 = 29;
	private final int REQUEST_SERVO2_8 = 30;
	private final int REQUEST_SERVO2_9 = 31;
	private final int REQUEST_SERVO2_10 = 32;
	private final int REQUEST_SERVO2_DT = 33;

	private final int REQUEST_SERVO3_1 = 34;
	private final int REQUEST_SERVO3_2 = 35;
	private final int REQUEST_SERVO3_3 = 36;
	private final int REQUEST_SERVO3_4 = 37;
	private final int REQUEST_SERVO3_5 = 38;
	private final int REQUEST_SERVO3_6 = 39;
	private final int REQUEST_SERVO3_7 = 40;
	private final int REQUEST_SERVO3_8 = 41;
	private final int REQUEST_SERVO3_9 = 42;
	private final int REQUEST_SERVO3_10 = 43;
	private final int REQUEST_SERVO3_DT = 44;

	private final int REQUEST_SAVE = 45;
	private final int REQUEST_LOAD = 46;

	private final int REQUEST_ENABLE_BT = 47;

	public static TimerProgram tp;

	ArrayAdapter<String> mArrayAdapter;

	// Create a BroadcastReceiver for ACTION_FOUND

	private BroadcastReceiver mReceiver;
	private BroadcastReceiver nReceiver;

//	private int onBackPressedNumber;

	// ONCREATE
	@SuppressWarnings("resource")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sidus_programmer);

//		onBackPressedNumber = 0;

		tp = new TimerProgram();

		mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
				MODE_PRIVATE);

		mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to
					// show in a
					// ListView
					mArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());

					StringBuilder sb = new StringBuilder("Discovered devices: ");

					for (int i = 0; i < mArrayAdapter.getCount(); i++) {
						sb.append(mArrayAdapter.getItem(i)).append("|");
					}

					Toast.makeText(getApplicationContext(), sb.toString(),
							Toast.LENGTH_LONG).show();
				}
			}
		};

		nReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					Toast.makeText(getApplicationContext(),
							"Device discovery finished!", Toast.LENGTH_LONG)
							.show();
				}

			}
		};

		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);

		IntentFilter nFilter = new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(nReceiver, nFilter);
		// Don't forget to unregister during onDestroy

		firstTime = (TextView) findViewById(R.id.firstRowTime);
		secondTime = (TextView) findViewById(R.id.secondRowTime);
		thirdTime = (TextView) findViewById(R.id.thirdRowTime);
		fourthTime = (TextView) findViewById(R.id.fourthRowTime);
		fifthTime = (TextView) findViewById(R.id.fifthRowTime);
		sixthTime = (TextView) findViewById(R.id.sixthRowTime);
		seventhTime = (TextView) findViewById(R.id.seventhRowTime);
		eighthTime = (TextView) findViewById(R.id.eighthRowTime);
		ninthTime = (TextView) findViewById(R.id.ninthRowTime);
		tenthTime = (TextView) findViewById(R.id.tenthRowTime);
		DTTime = (TextView) findViewById(R.id.DTTime);

		servo1pos1 = (TextView) findViewById(R.id.firstRowServo1pos1);
		servo1pos2 = (TextView) findViewById(R.id.secondRowServo1Pos2);
		servo1pos3 = (TextView) findViewById(R.id.thirdRowServo1pos3);
		servo1pos4 = (TextView) findViewById(R.id.fourthRowServo1pos4);
		servo1pos5 = (TextView) findViewById(R.id.fifthRowServo1pos5);
		servo1pos6 = (TextView) findViewById(R.id.sixthRowServo1pos6);
		servo1pos7 = (TextView) findViewById(R.id.seventhRowServo1pos7);
		servo1pos8 = (TextView) findViewById(R.id.eighthRowServo1pos8);
		servo1pos9 = (TextView) findViewById(R.id.ninthRowServo1pos9);
		servo1pos10 = (TextView) findViewById(R.id.tenthRowServo1pos10);
		servo1posDT = (TextView) findViewById(R.id.DTServo1);

		servo2pos1 = (TextView) findViewById(R.id.firstRowServo2pos1);
		servo2pos2 = (TextView) findViewById(R.id.secondRowServo2Pos2);
		servo2pos3 = (TextView) findViewById(R.id.thirdRowServo2pos3);
		servo2pos4 = (TextView) findViewById(R.id.fourthRowServo2pos4);
		servo2pos5 = (TextView) findViewById(R.id.fifthRowServo2pos5);
		servo2pos6 = (TextView) findViewById(R.id.sixthRowServo2pos6);
		servo2pos7 = (TextView) findViewById(R.id.seventhRowServo2pos7);
		servo2pos8 = (TextView) findViewById(R.id.eighthRowServo2pos8);
		servo2pos9 = (TextView) findViewById(R.id.ninthRowServo2pos9);
		servo2pos10 = (TextView) findViewById(R.id.tenthRowServo2pos10);
		servo2posDT = (TextView) findViewById(R.id.DTServo2);

		servo3pos1 = (TextView) findViewById(R.id.firstRowServo3pos1);
		servo3pos2 = (TextView) findViewById(R.id.secondRowServo3Pos2);
		servo3pos3 = (TextView) findViewById(R.id.thirdRowServo3pos3);
		servo3pos4 = (TextView) findViewById(R.id.fourthRowServo3pos4);
		servo3pos5 = (TextView) findViewById(R.id.fifthRowServo3pos5);
		servo3pos6 = (TextView) findViewById(R.id.sixthRowServo3pos6);
		servo3pos7 = (TextView) findViewById(R.id.seventhRowServo3pos7);
		servo3pos8 = (TextView) findViewById(R.id.eighthRowServo3pos8);
		servo3pos9 = (TextView) findViewById(R.id.ninthRowServo3pos9);
		servo3pos10 = (TextView) findViewById(R.id.tenthRowServo3pos10);
		servo3posDT = (TextView) findViewById(R.id.DTServo3);

		send = (Button) findViewById(R.id.Send);
		receive = (Button) findViewById(R.id.Receive);
		settings = (ImageButton)findViewById(R.id.Settings);

		// Intent intentURI = getIntent();
		// Uri uri = null;
		// String receivedNum = "";
		// Log.d("TAG", "intent= "+intentURI);
		// if (Intent.ACTION_VIEW.equals(intentURI.getAction())) {
		// if (intentURI!=null){
		// uri = intentURI.getData();
		// Log.d("TAG", "uri= "+uri);
		// }
		// if (uri!=null)
		// receivedNum = uri.getQueryParameter("number");
		// }

		try {

			Intent openFile = getIntent();
			Uri openFileUri = null;

			if (Intent.ACTION_VIEW.equals(openFile.getAction())) {
				if (openFile != null) {

					openFileUri = openFile.getData();
					File openFileUriPath = new File(openFileUri.getPath());
					FileInputStream fis = new FileInputStream(openFileUriPath);
					ObjectInputStream ois;
					ois = new ObjectInputStream(fis);
					TimerProgram tpload = (TimerProgram) ois.readObject();
					loadTimerProgram(tpload);
				}
			}

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		firstTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 1);
				startActivityForResult(intent, REQUEST_FIRSTTIME);

			}
		});
		secondTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 1);
				startActivityForResult(intent, REQUEST_SECONDTIME);

			}
		});
		thirdTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 2);
				startActivityForResult(intent, REQUEST_THIRDTIME);

			}
		});
		fourthTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 3);
				startActivityForResult(intent, REQUEST_FOURTHTIME);

			}
		});
		fifthTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 4);
				startActivityForResult(intent, REQUEST_FIFTHTIME);

			}
		});
		sixthTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 5);
				startActivityForResult(intent, REQUEST_SIXTHTIME);

			}
		});
		seventhTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 6);
				startActivityForResult(intent, REQUEST_SEVENTHTIME);

			}
		});
		eighthTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 7);
				startActivityForResult(intent, REQUEST_EIGHTHTIME);

			}
		});
		ninthTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 8);
				startActivityForResult(intent, REQUEST_NINTHTIME);

			}
		});
		tenthTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 9);
				startActivityForResult(intent, REQUEST_TENTHTIME);

			}
		});
		DTTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TimeEditor.class);
				intent.putExtra(getPackageName(), 10);
				startActivityForResult(intent, REQUEST_DTTIME);

			}
		});
		servo1pos1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_1);
			}
		});
		servo1pos2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_2);

			}
		});
		servo1pos3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_3);

			}
		});
		servo1pos4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_4);

			}
		});
		servo1pos5.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_5);

			}
		});
		servo1pos6.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_6);

			}
		});
		servo1pos7.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_7);

			}
		});
		servo1pos8.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_8);

			}
		});
		servo1pos9.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_9);

			}
		});
		servo1pos10.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_10);

			}
		});
		servo1posDT.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO1_DT);

			}
		});
		servo2pos1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_1);
			}
		});
		servo2pos2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_2);

			}
		});
		servo2pos3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_3);

			}
		});
		servo2pos4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_4);

			}
		});
		servo2pos5.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_5);

			}
		});
		servo2pos6.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_6);

			}
		});
		servo2pos7.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_7);

			}
		});
		servo2pos8.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_8);

			}
		});
		servo2pos9.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_9);

			}
		});
		servo2pos10.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_10);

			}
		});
		servo2posDT.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO2_DT);

			}
		});
		servo3pos1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_1);
			}
		});
		servo3pos2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_2);

			}
		});
		servo3pos3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_3);

			}
		});
		servo3pos4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_4);

			}
		});
		servo3pos5.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_5);

			}
		});
		servo3pos6.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_6);

			}
		});
		servo3pos7.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_7);

			}
		});
		servo3pos8.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_8);

			}
		});
		servo3pos9.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_9);

			}
		});
		servo3pos10.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_10);

			}
		});
		servo3posDT.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ServoEditor.class);
				startActivityForResult(intent, REQUEST_SERVO3_DT);

			}
		});

		send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// Toast.makeText(getApplicationContext(), "Send pressed",
				// Toast.LENGTH_SHORT).show();

				BluetoothAdapter bAdapter = BluetoothAdapter
						.getDefaultAdapter();
				if (bAdapter == null) {
					Toast.makeText(getApplicationContext(),
							"Device does not support Bluetooth",
							Toast.LENGTH_SHORT).show();
				} else {
					// Toast.makeText(getApplicationContext(),
					// "Device does support Bluetooth", Toast.LENGTH_SHORT)
					// .show();
				}

				if (!bAdapter.isEnabled()) {
					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

				} else {

					bAdapter.startDiscovery();

					Toast.makeText(getApplicationContext(),
							"Device discovery starting", Toast.LENGTH_LONG)
							.show();
				}

				Set<BluetoothDevice> pairedDevices = bAdapter
						.getBondedDevices();

				// If there are paired devices
				if (pairedDevices.size() > 0) {
					// Loop through paired devices
					for (BluetoothDevice device : pairedDevices) {
						// Add the name and address to an array adapter to show
						// in a
						// ListView
						mArrayAdapter.add(device.getName() + "\n"
								+ device.getAddress());
					}
				}

				// Intent discoverableIntent = new Intent(
				// BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				// discoverableIntent.putExtra(
				// BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				// startActivity(discoverableIntent);

			}
		});

		receive.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Receive pressed",
						Toast.LENGTH_SHORT).show();
			}
		});
		settings.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
		if (nReceiver != null) {
			unregisterReceiver(nReceiver);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sidus_programmer, menu);
		return true;
	}

	@SuppressWarnings("resource")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED)
			return;

		switch (requestCode) {
		case REQUEST_FIRSTTIME:
			firstTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					1);
			break;
		case REQUEST_SECONDTIME:
			secondTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					2);
			break;
		case REQUEST_THIRDTIME:
			thirdTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					3);
			break;
		case REQUEST_FOURTHTIME:
			fourthTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					4);
			break;
		case REQUEST_FIFTHTIME:
			fifthTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					5);
			break;
		case REQUEST_SIXTHTIME:
			sixthTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					6);
			break;
		case REQUEST_SEVENTHTIME:
			seventhTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					7);
			break;
		case REQUEST_EIGHTHTIME:
			eighthTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					8);
			break;
		case REQUEST_NINTHTIME:
			ninthTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					9);
			break;
		case REQUEST_TENTHTIME:
			tenthTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					10);
			break;
		case REQUEST_DTTIME:
			DTTime.setText(data.getStringExtra(getPackageName()));
			tp.setOneTime(
					Double.parseDouble(data.getStringExtra(getPackageName())),
					11);
			break;
		case REQUEST_SERVO1_1:
			servo1pos1.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 1,
					1);
			break;
		case REQUEST_SERVO1_2:
			servo1pos2.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 2,
					1);
			break;
		case REQUEST_SERVO1_3:
			servo1pos3.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 3,
					1);
			break;
		case REQUEST_SERVO1_4:
			servo1pos4.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 4,
					1);
			break;
		case REQUEST_SERVO1_5:
			servo1pos5.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 5,
					1);
			break;
		case REQUEST_SERVO1_6:
			servo1pos6.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 6,
					1);
			break;
		case REQUEST_SERVO1_7:
			servo1pos7.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 7,
					1);
			break;
		case REQUEST_SERVO1_8:
			servo1pos8.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 8,
					1);
			break;
		case REQUEST_SERVO1_9:
			servo1pos9.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 9,
					1);
			break;
		case REQUEST_SERVO1_10:
			servo1pos10.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())),
					10, 1);
			break;
		case REQUEST_SERVO1_DT:
			servo1posDT.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())),
					11, 1);
			break;
		case REQUEST_SERVO2_1:
			servo2pos1.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 1,
					2);
			break;
		case REQUEST_SERVO2_2:
			servo2pos2.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 2,
					2);
			break;
		case REQUEST_SERVO2_3:
			servo2pos3.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 3,
					2);
			break;
		case REQUEST_SERVO2_4:
			servo2pos4.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 4,
					2);
			break;
		case REQUEST_SERVO2_5:
			servo2pos5.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 5,
					2);
			break;
		case REQUEST_SERVO2_6:
			servo2pos6.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 6,
					2);
			break;
		case REQUEST_SERVO2_7:
			servo2pos7.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 7,
					2);
			break;
		case REQUEST_SERVO2_8:
			servo2pos8.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 8,
					2);
			break;
		case REQUEST_SERVO2_9:
			servo2pos9.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 9,
					2);
			break;
		case REQUEST_SERVO2_10:
			servo2pos10.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())),
					10, 2);
			break;
		case REQUEST_SERVO2_DT:
			servo2posDT.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())),
					11, 2);
			break;
		case REQUEST_SERVO3_1:
			servo3pos1.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 1,
					3);
			break;
		case REQUEST_SERVO3_2:
			servo3pos2.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 2,
					3);
			break;
		case REQUEST_SERVO3_3:
			servo3pos3.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 3,
					3);
			break;
		case REQUEST_SERVO3_4:
			servo3pos4.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 4,
					3);
			break;
		case REQUEST_SERVO3_5:
			servo3pos5.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 5,
					3);
			break;
		case REQUEST_SERVO3_6:
			servo3pos6.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 6,
					3);
			break;
		case REQUEST_SERVO3_7:
			servo3pos7.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 7,
					3);
			break;
		case REQUEST_SERVO3_8:
			servo3pos8.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 8,
					3);
			break;
		case REQUEST_SERVO3_9:
			servo3pos9.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())), 9,
					3);
			break;
		case REQUEST_SERVO3_10:
			servo3pos10.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())),
					10, 3);
			break;
		case REQUEST_SERVO3_DT:
			servo3posDT.setText(data.getStringExtra(getPackageName()));
			tp.setOneServopos(
					Integer.parseInt(data.getStringExtra(getPackageName())),
					11, 3);
			break;
		case REQUEST_SAVE:
			Toast.makeText(
					getApplicationContext(),
					"File saved! Name: "
							+ data.getStringExtra(getPackageName()) + ".sid",
					Toast.LENGTH_SHORT).show();
			break;
		case REQUEST_LOAD:

			String path = Environment.getExternalStorageDirectory().getPath()
					+ "/Android/data/" + getPackageName();
			File dir = new File(path);
			dir.mkdir();

			try {

				FileInputStream fis = new FileInputStream(path + "/"
						+ data.getStringExtra(getPackageName()));
				ObjectInputStream ois = new ObjectInputStream(fis);
				TimerProgram loadedProgram = (TimerProgram) ois.readObject();
				tp = loadedProgram;

				loadTimerProgram(tp);

				// String path1 = Environment.getExternalStorageDirectory()
				// .getPath() + "/Android/data/" + getPackageName();
				// File dir1 = new File(path1);
				//
				// StringBuilder sb = new StringBuilder();
				//
				// File[] files = dir1.listFiles();
				// for (int i = 0; i < files.length; i++) {
				// sb.append(files[i].getName()).append("#");
				// }

				Toast.makeText(
						getApplicationContext(),
						"Program: " + data.getStringExtra(getPackageName())
								+ " opened.", Toast.LENGTH_SHORT).show();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case REQUEST_ENABLE_BT:
			Toast.makeText(getApplicationContext(), "Press send again!",
					Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.Save:

			// String path = Environment.getExternalStorageDirectory().getPath()
			// + "/Android/data/" + getPackageName();
			// File dir = new File(path);
			// dir.mkdir();
			//
			// Toast.makeText(
			// getApplicationContext(),
			// "Save pressed. Location: " + dir.getPath() + " State: "
			// + Environment.getExternalStorageState()
			// + " Exist: " + dir.exists(), Toast.LENGTH_LONG)
			// .show();

			Intent intent = new Intent(getApplicationContext(),
					SaveDialog.class);
			startActivityForResult(intent, REQUEST_SAVE);

			return true;
		case R.id.Load:
			// Toast.makeText(getApplicationContext(), "Load pressed",
			// Toast.LENGTH_SHORT).show();

			Intent intent1 = new Intent(getApplicationContext(),
					LoadDialog.class);
			startActivityForResult(intent1, REQUEST_LOAD);

			return true;
		case R.id.Exit:
			finish();

			return true;
		case R.id.PrintProgramValues:
			Toast.makeText(getApplicationContext(), tp.toString(),
					Toast.LENGTH_LONG).show();
			return true;

		default:

			return super.onOptionsItemSelected(item);
		}

	}

	public void loadTimerProgram(TimerProgram tp) {

		firstTime.setText(String.valueOf(tp.getOneTime(1)));
		secondTime.setText(String.valueOf(tp.getOneTime(2)));
		thirdTime.setText(String.valueOf(tp.getOneTime(3)));
		fourthTime.setText(String.valueOf(tp.getOneTime(4)));
		fifthTime.setText(String.valueOf(tp.getOneTime(5)));
		sixthTime.setText(String.valueOf(tp.getOneTime(6)));
		seventhTime.setText(String.valueOf(tp.getOneTime(7)));
		eighthTime.setText(String.valueOf(tp.getOneTime(8)));
		ninthTime.setText(String.valueOf(tp.getOneTime(9)));
		tenthTime.setText(String.valueOf(tp.getOneTime(10)));
		DTTime.setText(String.valueOf(tp.getOneTime(11)));

		servo1pos1.setText(String.valueOf(tp.getOneServopos(1, 1)));
		servo1pos2.setText(String.valueOf(tp.getOneServopos(2, 1)));
		servo1pos3.setText(String.valueOf(tp.getOneServopos(3, 1)));
		servo1pos4.setText(String.valueOf(tp.getOneServopos(4, 1)));
		servo1pos5.setText(String.valueOf(tp.getOneServopos(5, 1)));
		servo1pos6.setText(String.valueOf(tp.getOneServopos(6, 1)));
		servo1pos7.setText(String.valueOf(tp.getOneServopos(7, 1)));
		servo1pos8.setText(String.valueOf(tp.getOneServopos(8, 1)));
		servo1pos9.setText(String.valueOf(tp.getOneServopos(9, 1)));
		servo1pos10.setText(String.valueOf(tp.getOneServopos(10, 1)));
		servo1posDT.setText(String.valueOf(tp.getOneServopos(11, 1)));

		servo2pos1.setText(String.valueOf(tp.getOneServopos(1, 2)));
		servo2pos2.setText(String.valueOf(tp.getOneServopos(2, 2)));
		servo2pos3.setText(String.valueOf(tp.getOneServopos(3, 2)));
		servo2pos4.setText(String.valueOf(tp.getOneServopos(4, 2)));
		servo2pos5.setText(String.valueOf(tp.getOneServopos(5, 2)));
		servo2pos6.setText(String.valueOf(tp.getOneServopos(6, 2)));
		servo2pos7.setText(String.valueOf(tp.getOneServopos(7, 2)));
		servo2pos8.setText(String.valueOf(tp.getOneServopos(8, 2)));
		servo2pos9.setText(String.valueOf(tp.getOneServopos(9, 2)));
		servo2pos10.setText(String.valueOf(tp.getOneServopos(10, 2)));
		servo2posDT.setText(String.valueOf(tp.getOneServopos(11, 2)));

		servo3pos1.setText(String.valueOf(tp.getOneServopos(1, 3)));
		servo3pos2.setText(String.valueOf(tp.getOneServopos(2, 3)));
		servo3pos3.setText(String.valueOf(tp.getOneServopos(3, 3)));
		servo3pos4.setText(String.valueOf(tp.getOneServopos(4, 3)));
		servo3pos5.setText(String.valueOf(tp.getOneServopos(5, 3)));
		servo3pos6.setText(String.valueOf(tp.getOneServopos(6, 3)));
		servo3pos7.setText(String.valueOf(tp.getOneServopos(7, 3)));
		servo3pos8.setText(String.valueOf(tp.getOneServopos(8, 3)));
		servo3pos9.setText(String.valueOf(tp.getOneServopos(9, 3)));
		servo3pos10.setText(String.valueOf(tp.getOneServopos(10, 3)));
		servo3posDT.setText(String.valueOf(tp.getOneServopos(11, 3)));
	}

//	@Override
//	public void onBackPressed() {
//		if (onBackPressedNumber == 0) {
//			onBackPressedNumber++;
//			Toast.makeText(getApplicationContext(), "Press again to exit!",
//					Toast.LENGTH_SHORT).show();
//		} else {
//			if (onBackPressedNumber == 1) {
//				finish();
//			}
//		}
//	}
	
}

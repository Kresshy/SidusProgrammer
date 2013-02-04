package hu.sidus.bluetoothapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class SidusService {

	// Debugging
	private static final String TAG = "SidusService";
	private static final boolean D = true;

	private static BluetoothAdapter mBluetoothAdapter;
	private static BluetoothServerSocket mBluetoothServerSocket;
	private static BluetoothSocket mBluetoothSocket;
	private static BluetoothDevice mBluetoothDevice;

	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;

	private static String NAME = "SIDUS";
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public SidusService() {
		mBluetoothDevice = null;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothServerSocket = null;
		mBluetoothSocket = null;
	}

	public synchronized void start() {
		if (D)
			Log.d(TAG, "start");

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to listen on a BluetoothServerSocket
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}

	}

	public synchronized void connect(BluetoothDevice device) {
		if (D)
			Log.d(TAG, "connect to: " + device);

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
	}

	public synchronized void connected(BluetoothSocket socket) {
		if (D)
			Log.d(TAG, "connected");

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Cancel the accept thread because we only want to connect to one
		// device
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();
	}

	public synchronized void stop() {
		if (D)
			Log.d(TAG, "stop");

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
	}

	public void write(byte[] out) {
		// Create temporary object
		ConnectedThread r;

		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mConnectedThread != null)
				;
			r = mConnectedThread;
		}

		// Perform the write unsynchronized
		r.write(out);
	}

	private class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {
			// Use a temporary object that is later assigned to mmServerSocket,
			// because mmServerSocket is final
			BluetoothServerSocket tmp = null;
			try {
				// MY_UUID is the app's UUID string, also used by the client
				// code
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "Accept Thread " + e.getMessage());
			}
			mmServerSocket = tmp;
		}

		public void run() {
			BluetoothSocket socket = null;
			// Keep listening until exception occurs or a socket is returned
			while (true) {
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.e(TAG, "Accepting connection " + e.getMessage());
					break;
				}
				// If a connection was accepted
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					Log.i(TAG, "Connected");
					connected(socket);
					try {
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}

		/** Will cancel the listening socket, and cause the thread to finish */
		public void cancel() {
//			try {
//				mmServerSocket.close();
//			} catch (IOException e) {
//			}
		}
	}

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;

			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
				Log.i(TAG, "RFCOMM_OK");
			} catch (IOException e) {
			}
			mmSocket = tmp;
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDiscovery();

			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				mmSocket.connect();
				Log.i(TAG, "CONNECT_OK");
				
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					Log.e(TAG, "CONNECT_FAIL " + connectException.getMessage());

					mmSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}

			// Do work to manage the connection (in a separate thread)
			connected(mmSocket);
		}

		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
//			try {
//				mmSocket.close();
//			} catch (IOException e) {
//			}
		}
	}

	private class ConnectedThread extends Thread {
		private BluetoothSocket mmSocket;
		private InputStream mmInStream;
		private OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			
			try {
				
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
				Log.i(TAG, "STREAMS_OK");
				
			} catch (IOException e) {
				
				Log.e(TAG, "STREAMS_FAIL " + e.getMessage());
				
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					
					// Send the obtained bytes to the UI activity
					// mHandler.obtainMessage(MESSAGE_READ, bytes, -1,
					// buffer).sendToTarget();
					
//					InputStream is;
//					byte[] bytes = IOUtils.toByteArray(is);
					
//					InputStream is = ...
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							int nRead;
//							byte[] data = new byte[16384];
//
//							while ((nRead = is.read(data, 0, data.length)) != -1) {
//							  buffer.write(data, 0, nRead);
//							}
//
//							buffer.flush();
//
//							return buffer.toByteArray();
					
					Log.i(TAG, Integer.toString(bytes));
					
				} catch (IOException e) {
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
				Log.i(TAG, "WRITE_OK");
			} catch (IOException e) {
				Log.e(TAG, "WRITE_FAIL " + e.getMessage());
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}
}

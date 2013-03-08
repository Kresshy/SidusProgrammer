package hu.sidus.bluetoothapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import android.util.Log;

public class SidusProgram implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final boolean D = true;
	private static final String TAG = "SidusProgram";
	private SidusService mSidusService;

	int functions, servos, memories;
	byte[][] data;

	// AFTER THE TIMER VERSION IS READED OUT, SET ALL THESE PROPERTIES
	public SidusProgram(int functions, int servos, SidusService service) { 
	
		this.data = new byte[functions][servos];

		for (int i = 0; i < functions; i++) {
			for (int j = 0; j < servos; j++) {

				data[i][j] = intToByteArray(i + j);

			}
		}

		this.functions = functions;
		this.servos = servos;
		this.mSidusService = service;
	}

	public void setDataValue(int function, int servo, byte value) {
		this.data[function][servo] = value;
	}

	public byte getDataValue(int function, int servo) {
		return data[function][servo];
	}

	public void setData(byte[][] data) {
		this.data = data;
	}

	public byte[][] getData() {
		return data;
	}

	public byte[] sendData() throws IOException {
		if (D)
			Log.v(TAG, "CONCAT DATA STREAM");

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		for (byte i = 0; i < functions; i++) {

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int CMD_MOVETOFUNC = 0x69;
			int SUM_MOVETOFUNC = CMD_MOVETOFUNC + i;
			int CHK_MOVETOFUNC = SUM_MOVETOFUNC % 256;

			byte[] moveToFunc = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x69, i, intToByteArray(CHK_MOVETOFUNC), 0x45 };

			mSidusService.write(moveToFunc);

			Log.i(TAG, "CMD_MOVETOFUNC: " + byteArrayToHexString(moveToFunc));

			out.write(moveToFunc);

			for (byte j = 0; j < servos; j++) {

				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int CMD_SETSERVO = 0x63;
				int SUM_SETSERVO = CMD_SETSERVO + i + j + data[i][j];
				int CHK_SETSERVO = SUM_SETSERVO % 256;

				byte[] setServo = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x63, i, j, data[i][j], intToByteArray(CHK_SETSERVO), 0x45 };

				mSidusService.write(setServo);

				Log.i(TAG, "CMD_SETSERVO: " + byteArrayToHexString(setServo));

				out.write(setServo);
			}
		}

		return out.toByteArray();
	}

	public static final byte intToByteArray(int value) {
		return (byte) (value & 0xff);
	}

	public static String byteArrayToHexString(byte[] data) {

		StringBuilder localStringBuilder = new StringBuilder("");

		for (int i = 0; i < data.length; i++) {

			String str = Byte.toString(data[i]);

			localStringBuilder.append(str);
			localStringBuilder.append(" ");
		}

		return localStringBuilder.toString();
	}
}

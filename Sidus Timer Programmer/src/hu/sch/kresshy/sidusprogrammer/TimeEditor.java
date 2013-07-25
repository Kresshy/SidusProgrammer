package hu.sch.kresshy.sidusprogrammer;

import hu.sch.kresshy.wheel.OnWheelChangedListener;
import hu.sch.kresshy.wheel.OnWheelScrollListener;
import hu.sch.kresshy.wheel.WheelView;
import hu.sch.kresshy.wheel.adapter.NumericWheelAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TimeEditor extends Activity {

	// EditText value;

	Button ok;
	Button cancel;
	TextView timeValue;
	WheelView timeNumber;
	WheelView timeDecimal;

	double beforeValue;

	int number;
	double decimal;

	String resultString;

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
				updateStatus();
			}
		}
	};

	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			updateStatus();
		}
	};

	private void updateStatus() {

		String decimalString = "00";

		if (timeDecimal.getCurrentItem() >= 0
				&& timeDecimal.getCurrentItem() <= 9) {
			decimalString = "0"
					+ Integer.toString(timeDecimal.getCurrentItem());
		} else {
			decimalString = Integer.toString(timeDecimal.getCurrentItem());
		}

		timeValue.setText(new StringBuilder("Current value: ")
				.append(timeNumber.getCurrentItem()).append(".")
				.append(decimalString).toString());

		resultString = new StringBuilder().append(timeNumber.getCurrentItem())
				.append(".").append(decimalString).toString();
	}

	private boolean wheelScrolled = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_editor);

		// value = (EditText) findViewById(R.id.timeValue);

		ok = (Button) findViewById(R.id.btnok);
		cancel = (Button) findViewById(R.id.btncancel);
		timeValue = (TextView) findViewById(R.id.time_value);

		Intent intent = getIntent();
		beforeValue = SidusProgrammer.tp.getOneTime(intent.getIntExtra(
				getPackageName(), 1));

		number = (int) beforeValue;

		timeNumber = (WheelView) findViewById(R.id.time_WheelNumber);
		timeDecimal = (WheelView) findViewById(R.id.time_WheelDecimal);

		timeNumber.setViewAdapter(new NumericWheelAdapter(this, 0, 1000));
		timeNumber.addChangingListener(changedListener);
		timeNumber.addScrollingListener(scrolledListener);
		timeNumber.setCurrentItem(number);

		decimal = 100 * (beforeValue - number) + 0.05;

		// Toast.makeText(getApplicationContext(), Double.toString(decimal),
		// Toast.LENGTH_SHORT).show();

		timeDecimal
				.setViewAdapter(new NumericWheelAdapter(this, 0, 99, "%02d"));
		timeDecimal.addChangingListener(changedListener);
		timeDecimal.addScrollingListener(scrolledListener);
		timeDecimal.setCurrentItem((int) decimal);

		updateStatus();

		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// String resultString = value.getText().toString();

				if (resultString == null || resultString.equals("")) {
					setResult(RESULT_CANCELED);
					finish();
				}
				
				if (Double.parseDouble(resultString) > beforeValue) {
					Intent resultIntent = new Intent();
					resultIntent.putExtra(getPackageName(), resultString);
					setResult(RESULT_OK, resultIntent);
					finish();
					
				} else {
					Toast.makeText(getApplicationContext(),
							"Must enter increasing values!",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	// private class DecimalAdapter extends AbstractWheelTextAdapter {
	//
	// private String[] decimal_Numbers;
	//
	// protected DecimalAdapter(Context context) {
	// super(context);
	//
	// }
	//
	// public DecimalAdapter(Context context, int decimalnumbers) {
	// super(context);
	//
	// decimal_Numbers = new String[decimalnumbers];
	//
	// for (int i = 0; i < decimalnumbers; i++){
	// for (int j = 0; j < 99; j++ ){
	// if ( j >= 0 && j <= 9){
	// decimal_Numbers[i] = Integer.toString(i) + ".0" + Integer.toString(j);
	// } else {
	// decimal_Numbers[i] = Integer.toString(i) + "." + Integer.toString(j);
	// }
	//
	// }
	// }
	// }
	//
	// public int getItemsCount() {
	// return decimal_Numbers.length;
	// }
	//
	// @Override
	// protected CharSequence getItemText(int index) {
	// return decimal_Numbers[index];
	// }
	//
	// }

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_time_editor, menu);
	// return true;
	// }
}

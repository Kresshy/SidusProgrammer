package hu.kresshy.sidusprogrammer.application;

import hu.kresshy.sidusprogrammer.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartActivity extends Activity {

	private final String TAG = "StartActivity";

	private LinearLayout linearLayout;
	// private TextView textView;

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String rowNumber = ((TextView) ((LinearLayout) v.getParent())
					.getChildAt(0)).getText().toString();

			Log.i(TAG, "RowNumber: " + rowNumber + "id: " + v.getId());

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		linearLayout = (LinearLayout) findViewById(R.id.startactivity_linearlayout);

		Button send = new Button(this);
		send.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lParams.setMargins(10, 20, 10, 30);
		
		linearLayout.addView(send, lParams);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (int i = 0; i < 10; i++) {

			LinearLayout lLayout = (LinearLayout) inflater.inflate(
					R.layout.program_row, null);

			TextView view = (TextView) lLayout.getChildAt(0);
			view.setText("" + i);

			for (int j = 0; j < lLayout.getChildCount(); j++) {
				TextView tView = (TextView) lLayout.getChildAt(j);
				tView.setTextSize(20);
				tView.setOnClickListener(onClickListener);
				// System.out.println(view.getText());
			}

			linearLayout.addView(lLayout);
		}

		setContentView(linearLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

}

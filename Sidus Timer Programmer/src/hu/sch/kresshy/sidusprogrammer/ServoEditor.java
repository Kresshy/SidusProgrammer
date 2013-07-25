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

public class ServoEditor extends Activity {
	
//	EditText value;
	TextView servoValue;
	WheelView servoWheel;
	Button ok;
	Button cancel;
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

    	servoValue.setText(new StringBuilder("Current value: ").append(servoWheel.getCurrentItem()));
        resultString = Integer.toString(servoWheel.getCurrentItem());
    
    }
    
    private boolean wheelScrolled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servo_editor);
        
//        value = (EditText) findViewById(R.id.servoValue);
        
        ok = (Button) findViewById(R.id.btnservonok);
        cancel = (Button) findViewById(R.id.btnservocancel);
        
        servoWheel = (WheelView) findViewById(R.id.servo_wheel);
        servoWheel.setViewAdapter(new NumericWheelAdapter(this, 0, 130));
        servoWheel.setCurrentItem(0);
        
        servoWheel.addChangingListener(changedListener);
        servoWheel.addScrollingListener(scrolledListener);
        servoWheel.setCyclic(false);
        
        servoValue =(TextView) findViewById(R.id.servo_value);
        
        updateStatus();
//        servoWheel.setInterpolator(new AnticipateOvershootInterpolator());
        
        ok.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
//				String resultString = value.getText().toString();
				
				if (resultString == null || resultString.equals("")) {
					setResult(RESULT_CANCELED);
					finish();
				}
				
				Intent resultIntent = new Intent();
				resultIntent.putExtra(getPackageName(), resultString);
				setResult(RESULT_OK, resultIntent);
				finish();
				
			}
		});
     
        cancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_servo_editor, menu);
//        return true;
//    }
}

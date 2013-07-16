package com.keita.pinganautoinsurance;

import android.R.color;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class InsuranceNextActivity extends Activity {
	private Button record_btn = null;

	boolean isLongClick = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_next);
		record_btn = (Button)findViewById(R.id.record_btn);
		
		//录音按钮监听
		/*record_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast recording_toast = Toast.makeText(getApplicationContext(), " 正在录音  ", Toast.LENGTH_LONG);
				ImageView iv = new ImageView(getApplicationContext());
				
				recording_toast.setGravity(Gravity.CENTER, 0, 0);
				iv.setImageResource(R.drawable.record_animate_02);
				LinearLayout toast_view = (LinearLayout)recording_toast.getView();
				toast_view.addView(iv, 0);
				recording_toast.setView(toast_view);
				recording_toast.show();
			}
				
		});*/
		record_btn.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("test", "长按");
				isLongClick = true;
				return false;
			}
			
		});
		record_btn.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
				case MotionEvent.ACTION_UP :
					if(isLongClick){
						Log.v("test", "松开");
						isLongClick = false;
					}
					break;
					default:
						break;
				}
				return false;
			}
			
		});
	}
	
}

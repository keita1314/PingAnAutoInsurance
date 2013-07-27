package com.keita.pinganautoinsurance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InsuranceActivity extends Activity {
	private Button continue_btn = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_text);
		continue_btn =(Button) findViewById(R.id.continue_btn);
		continue_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(InsuranceActivity.this,InsuranceRecordActivity.class);
				startActivity(intent);
				
				
			}
			
		});
	

	}
}
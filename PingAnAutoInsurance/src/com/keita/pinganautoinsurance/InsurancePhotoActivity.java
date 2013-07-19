package com.keita.pinganautoinsurance;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class InsurancePhotoActivity extends Activity {
	private Button take_photo_btn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_photo);
		take_photo_btn = (Button) findViewById(R.layout.activity_insurance_photo);
		
	}

}

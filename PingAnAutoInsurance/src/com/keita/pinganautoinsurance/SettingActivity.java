package com.keita.pinganautoinsurance;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class SettingActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		TextView tv = (TextView) findViewById(R.id.setting);
		ActionBar bar = getSupportActionBar();
		bar.setTitle("设置");
		bar.show();
		
		tv.setText("关基达 金融IT人作品");
	}
	
}

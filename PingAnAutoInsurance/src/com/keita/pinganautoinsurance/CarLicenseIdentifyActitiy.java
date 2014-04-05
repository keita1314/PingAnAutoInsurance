package com.keita.pinganautoinsurance;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class CarLicenseIdentifyActitiy extends ActionBarActivity{
	
	private ActionBar actionBar = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		actionBar = getSupportActionBar();
		actionBar.setTitle("拍照");
		actionBar.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		return super.onCreateOptionsMenu(menu);
	}
	
}

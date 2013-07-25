package com.keita.pinganautoinsurance;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class InsuranceTextActivity extends Activity {
	private EditText caseNo = null;
	private EditText caseOwner = null;
	private EditText caseDriver = null;
	private Spinner relationShip = null;
	private EditText caseOwnerPhone = null;
	private EditText caseDriverPhone = null;
	private EditText caseDriverLicence = null;
	private EditText caseCarNo = null;
	private EditText caseCarType = null;
	private EditText caseThirdCarNo = null;
	private EditText caseThirdCarType = null;
	private Button continueBtn = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance);
		caseNo = (EditText)findViewById(R.id.case_no);
		caseOwner = (EditText)findViewById(R.id.case_owner);
		caseDriver = (EditText)findViewById(R.id.case_driver);
		
		caseOwnerPhone = (EditText)findViewById(R.id.case_owner_phone);
		caseDriverPhone = (EditText)findViewById(R.id.case_driver_phone);
		caseDriverLicence = (EditText)findViewById(R.id.case_driver_licence);
		caseCarNo = (EditText)findViewById(R.id.case_car_no);
		caseCarType = (EditText)findViewById(R.id.case_car_type);
		caseThirdCarNo = (EditText)findViewById(R.id.case_third_car_no);
		caseThirdCarType = (EditText)findViewById(R.id.case_third_car_type);
		
		continueBtn = (Button)findViewById(R.id.continue_btn);
		
	}

}

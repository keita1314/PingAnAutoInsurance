package com.keita.pinganautoinsurance;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InsuranceViewActivity extends Activity {
	private String insurancePolicyId = null;
	
	private TextView caseNoTv = null;

	private TextView caseOwnerTv = null;
	private TextView caseDriverTv = null;
	private TextView relationShipTv = null;
	private TextView caseOwnerPhoneTv = null;
	private TextView caseDriverPhoneTv = null;
	private TextView caseDriverLicenceTv = null;
	private TextView caseCarNoTv = null;
	private TextView caseCarTypeTv = null;
	private TextView caseCarVinTv = null;
	private TextView caseThirdCarNoTv = null;
	private TextView caseThirdCarTypeTv = null;
	private TextView locationTv = null;
	private TextView hurtNumTv = null;
	private TextView deadNumTv = null;
	private TextView caseReasonTv = null;
	private TextView accidentReasonTv = null;
	private TextView accidentDetailTv = null;
	
	private String caseNo = null;
	private String caseOwner = null;
	private String caseDriver = null;
	private String relationShip = null;
	private String caseOwnerPhone = null;
	private String caseDriverPhone = null;
	private String caseDriverLicence = null;
	private String caseCarNo = null;
	private String caseCarType = null;
	private String caseCarVin = null;
	private String caseThirdCarNo = null;
	private String caseThirdCarType = null;
	
	private String hurtNumStr = "0";
	private String deadNumStr = "0";
	private String location = "";
	private String caseReasonStr = "";
	private String accidentReasonStr = "";
	private String accidentDetailStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_view);
		caseNoTv = (TextView)findViewById(R.id.case_no);
		caseOwnerTv = (TextView)findViewById(R.id.case_owner);
		relationShipTv = (TextView)findViewById(R.id.relationship);
		caseOwnerPhoneTv = (TextView)findViewById(R.id.case_owner_phone);
		caseDriverPhoneTv = (TextView)findViewById(R.id.case_driver_phone);
		caseDriverLicenceTv = (TextView)findViewById(R.id.case_driver_licence);
		caseCarTypeTv = (TextView)findViewById(R.id.case_car_type);
		caseCarVinTv = (TextView)findViewById(R.id.case_car_vin);
		caseThirdCarNoTv = (TextView)findViewById(R.id.case_third_car_no);
		caseThirdCarTypeTv = (TextView)findViewById(R.id.case_car_type);
		locationTv = (TextView)findViewById(R.id.location_text);
		caseReasonTv = (TextView) findViewById(R.id.case_reason);
		accidentReasonTv = (TextView) findViewById(R.id.accident_reason);
		hurtNumTv = (TextView)findViewById(R.id.hurt_num);
		deadNumTv = (TextView)findViewById(R.id.dead_num);
		
		
	}
	

}

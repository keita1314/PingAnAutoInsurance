package com.keita.pinganautoinsurance;

import com.keita.painganautoinsurance.entity.TextImage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class InsuranceTextActivity extends Activity {
	// 初始化控件
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
	// 初始化字符串
	private String caseNoStr = "";
	private String caseOwnerStr = "";
	private String caseDriverStr = "";
	private String relationShipStr = "";
	private String caseOwnerPhoneStr = "";
	private String caseDriverPhoneStr = "";
	private String caseDriverLicenceStr = "";
	private String caseCarNoStr = "";
	private String caseCarTypeStr = "";
	private String caseThirdCarNoStr = "";
	private String caseThirdCarTypeStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance);
		caseNo = (EditText) findViewById(R.id.case_no);
		caseOwner = (EditText) findViewById(R.id.case_owner);
		caseDriver = (EditText) findViewById(R.id.case_driver);

		caseOwnerPhone = (EditText) findViewById(R.id.case_owner_phone);
		caseDriverPhone = (EditText) findViewById(R.id.case_driver_phone);
		caseDriverLicence = (EditText) findViewById(R.id.case_driver_licence);
		caseCarNo = (EditText) findViewById(R.id.case_car_no);
		caseCarType = (EditText) findViewById(R.id.case_car_type);
		caseThirdCarNo = (EditText) findViewById(R.id.case_third_car_no);
		caseThirdCarType = (EditText) findViewById(R.id.case_third_car_type);
		continueBtn = (Button) findViewById(R.id.continue_btn);
		//获得输入的文本
		if (caseNo.getText().toString() != null)
			caseNoStr = caseNo.getText().toString();
		if (caseOwner.getText().toString() != null)
			caseOwnerStr = caseOwner.getText().toString();
		if (caseDriver.getText().toString() != null)
			caseDriverStr = caseDriver.getText().toString();
		if (caseOwnerPhone.getText().toString() != null)
			caseOwnerStr = caseOwnerPhone.getText().toString();
		if (caseDriverPhone.getText().toString() != null)
			caseDriverPhoneStr = caseDriverPhone.getText().toString();
		if (caseDriverLicence.getText().toString() != null)
			caseDriverLicenceStr = caseDriverLicence.getText().toString();
		if (caseCarNo.getText().toString() != null)
			caseCarNoStr = caseCarNo.getText().toString();
		if (caseCarType.getText().toString() != null)
			caseCarTypeStr = caseCarType.getText().toString();
		if (caseThirdCarNo.getText().toString() != null)
			caseThirdCarNoStr = caseThirdCarNo.getText().toString();
		if (caseThirdCarType.getText().toString() != null)
			caseThirdCarTypeStr = caseThirdCarType.getText().toString();
		// 下一步按钮监听事件
		continueBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(InsuranceTextActivity.this,
						InsuranceNextActivity.class);
				startActivity(intent);
				InsuranceTextActivity.this.finish();
			}

		});

	}

}

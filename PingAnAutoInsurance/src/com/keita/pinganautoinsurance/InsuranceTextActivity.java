package com.keita.pinganautoinsurance;

import java.util.ArrayList;

import com.keita.painganautoinsurance.entity.TextImage;
import com.keita.pinganautoinsurance.database.DBHelper;
import com.keita.pinganautoinsurance.mywidget.ComboEditText;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
	private ComboEditText caseCarNo = null;
	private EditText caseCarType = null;
	private EditText caseCarVin = null;
	private ComboEditText caseThirdCarNo = null;
	private EditText caseThirdCarType = null;
	private Button continueBtn = null;
	private ArrayAdapter<String> adapter = null;
	// 初始化字符串
	private String location = "";
	private String insurancePhotoId = "";
	private String caseNoStr = "";
	private String caseOwnerStr = "";
	private String caseDriverStr = "";
	private String relationShipStr = "";
	private String caseOwnerPhoneStr = "";
	private String caseDriverPhoneStr = "";
	private String caseDriverLicenceStr = "";
	private String caseCarNoStr = "";
	private String caseCarTypeStr = "";
	private String caseCarVinStr = "";
	private String caseThirdCarNoStr = "";
	private String caseThirdCarTypeStr = "";

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_text);
		caseNo = (EditText) findViewById(R.id.case_no);
		caseOwner = (EditText) findViewById(R.id.case_owner);
		caseDriver = (EditText) findViewById(R.id.case_driver);

		caseOwnerPhone = (EditText) findViewById(R.id.case_owner_phone);
		caseDriverPhone = (EditText) findViewById(R.id.case_driver_phone);
		caseDriverLicence = (EditText) findViewById(R.id.case_driver_licence);
		caseCarNo = (ComboEditText) findViewById(R.id.case_car_no);
		caseCarType = (EditText) findViewById(R.id.case_car_type);
		caseCarVin = (EditText)findViewById(R.id.case_car_vin);
		caseThirdCarNo = (ComboEditText) findViewById(R.id.case_third_car_no);
		caseThirdCarType = (EditText) findViewById(R.id.case_third_car_type);
		continueBtn = (Button) findViewById(R.id.continue_btn);
		final String[] relationArray = { "本人", "非本人" };
		relationShip = (Spinner) findViewById(R.id.relationship);
		location = this.getIntent().getStringExtra("location");
		insurancePhotoId = this.getIntent().getStringExtra("insurance_photo_id");
		
		//车牌数据源
		final String[] carNoArray={"粤","京","津","沪","渝","冀","豫","云","辽","黑","湘","皖",
									"鲁","新","苏","浙","赣","鄂","桂","甘"};
		
		// 设置adapter
		caseCarNo.setAdapter(carNoArray);
		caseCarNo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				caseCarNoStr = caseCarNo.getText();
				
			}
			
		});
		
		caseThirdCarNo.setAdapter(carNoArray);
		caseThirdCarNo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				caseThirdCarNoStr = caseThirdCarNo.getText();
			}
			
		});
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, relationArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		relationShip.setAdapter(adapter);
		relationShip.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				relationShipStr = relationArray[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		
		// 下一步按钮监听事件
		continueBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 获得输入的文本
				if (caseNo.getText().toString() != null)
					caseNoStr = caseNo.getText().toString();
				if (caseOwner.getText().toString() != null)
					caseOwnerStr = caseOwner.getText().toString();
				if (caseDriver.getText().toString() != null)
					caseDriverStr = caseDriver.getText().toString();
				if (caseOwnerPhone.getText().toString() != null)
					caseOwnerPhoneStr = caseOwnerPhone.getText().toString();
				if (caseDriverPhone.getText().toString() != null)
					caseDriverPhoneStr = caseDriverPhone.getText().toString();
				if (caseDriverLicence.getText().toString() != null)
					caseDriverLicenceStr = caseDriverLicence.getText().toString();
				if (caseCarNo.getText().toString() != null)
					caseCarNoStr = caseCarNo.getText().toString();
				if (caseCarType.getText().toString() != null)
					caseCarTypeStr = caseCarType.getText().toString();
				if(caseCarVin.getText().toString()!=null)
					caseCarVinStr = caseCarVin.getText().toString();
				if (caseThirdCarNo.getText().toString() != null)
					caseThirdCarNoStr = caseThirdCarNo.getText().toString();
				if (caseThirdCarType.getText().toString() != null)
					caseThirdCarTypeStr = caseThirdCarType.getText().toString();
				
				System.out.println("test"+caseNoStr + caseOwnerStr + caseDriverStr
						+ caseOwnerStr + caseDriverPhoneStr
						+ caseDriverLicenceStr + caseCarNoStr + caseCarTypeStr
						+ caseThirdCarNoStr + caseThirdCarTypeStr+relationShipStr);
				
				
				Intent intent = new Intent();
				intent.setClass(InsuranceTextActivity.this,
						InsuranceRecordActivity.class);
				intent.putExtra("location", location);
				intent.putExtra("insurance_photo_id", insurancePhotoId);
				intent.putExtra("caseOwnerStr", caseOwnerStr);
				intent.putExtra("caseNoStr", caseNoStr);
				intent.putExtra("caseDriverStr", caseDriverStr);
				intent.putExtra("relationShipStr", relationShipStr);
				intent.putExtra("caseOwnerPhoneStr", caseOwnerPhoneStr);
				intent.putExtra("caseDriverPhoneStr", caseDriverPhoneStr);
				intent.putExtra("caseDriverLicenceStr", caseDriverLicenceStr);
				intent.putExtra("caseCarNoStr", caseCarNoStr);
				intent.putExtra("caseCarTypeStr", caseCarTypeStr);
				intent.putExtra("caseCarVinStr", caseCarVinStr);
				intent.putExtra("caseThirdCarNoStr", caseThirdCarNoStr);
				intent.putExtra("caseThirdCarTypeStr", caseThirdCarTypeStr);
				//intent.putCharSequenceArrayListExtra("imageIdList", imageIdList);
				startActivity(intent);
		
				//InsuranceTextActivity.this.finish();
			}

		});

	}

}

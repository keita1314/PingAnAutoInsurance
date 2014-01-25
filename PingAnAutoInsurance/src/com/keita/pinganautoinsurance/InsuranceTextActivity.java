package com.keita.pinganautoinsurance;

import java.util.ArrayList;

import com.keita.painganautoinsurance.entity.Template;
import com.keita.painganautoinsurance.entity.TextImage;
import com.keita.pinganautoinsurance.database.DBHelper;
import com.keita.pinganautoinsurance.mywidget.ComboEditText;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
/*
 * 案件文本记录页面
 */
public class InsuranceTextActivity extends Activity {
	// 初始化控件
	private EditText caseNo = null;
	private EditText caseOwner = null;
	private EditText caseDriver = null;
	private Spinner relationShip = null;
	private EditText caseInsurnceId = null;
	private EditText caseOwnerPhone = null;
	private EditText caseDriverPhone = null;
	private EditText caseDriverLicence = null;
	private ComboEditText caseCarNo = null;
	private ComboEditText caseCarType = null;
	private EditText caseCarVin = null;
	private ComboEditText caseThirdCarNo = null;
	private ComboEditText caseThirdCarType = null;
	private Button continueBtn = null;
	private Button insertTemplateBtn = null;
	private ArrayAdapter<String> adapter = null;
	// 初始化字符串
	private String location = "";
	private String insurancePhotoId = "";
	private String caseNoStr = "";
	private String caseOwnerStr = "";
	private String caseDriverStr = "";
	private String caseInsuranceIdStr = "";
	private String relationShipStr = "";
	private String caseOwnerPhoneStr = "";
	private String caseDriverPhoneStr = "";
	private String caseDriverLicenceStr = "";
	private String caseCarNoStr = "";
	private String caseCarTypeStr = "";
	private String caseCarVinStr = "";
	private String caseThirdCarNoStr = "";
	private String caseThirdCarTypeStr = "";

	private boolean isInsert = false;
	private String templateName = null;
	private ArrayList<Template> templateList = null;
	private String[] templatNameArray = null;
	private int templatePosition = 0;

	//
	String[] carNoArray = null;
	String[] carTypeArray = null;

	private MyApplication application = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_text);
		application = (MyApplication) this.getApplication();
		application.getActivityList().add(this);
		// 获得模版
		templateList = application.getTemplateList();
		templatNameArray = new String[templateList.size()];
		for (int i = 0; i < templateList.size(); i++) {
			templatNameArray[i] = templateList.get(i).getTemplateName();
		}
		// 设置标题栏
		setTopBar();
		insertTemplateBtn = (Button) findViewById(R.id.insert_template_btn);
		caseNo = (EditText) findViewById(R.id.case_no);
		caseOwner = (EditText) findViewById(R.id.case_owner);
		caseDriver = (EditText) findViewById(R.id.case_driver);
		caseInsurnceId = (EditText) findViewById(R.id.case_insurance_id);

		caseOwnerPhone = (EditText) findViewById(R.id.case_owner_phone);
		caseDriverPhone = (EditText) findViewById(R.id.case_driver_phone);
		caseDriverLicence = (EditText) findViewById(R.id.case_driver_licence);
		caseCarNo = (ComboEditText) findViewById(R.id.case_car_no);
		caseCarType = (ComboEditText) findViewById(R.id.case_car_type);
		caseCarVin = (EditText) findViewById(R.id.case_car_vin);
		caseThirdCarNo = (ComboEditText) findViewById(R.id.case_third_car_no);
		caseThirdCarType = (ComboEditText) findViewById(R.id.case_third_car_type);
		continueBtn = (Button) findViewById(R.id.continue_btn);
		final String[] relationArray = { "本人", "非本人" };
		relationShip = (Spinner) findViewById(R.id.relationship);
		location = this.getIntent().getStringExtra("location");
		insurancePhotoId = this.getIntent()
				.getStringExtra("insurance_photo_id");

		/*
		 * //车牌数据源 final String[]
		 * carNoArray={"粤","京","津","沪","渝","冀","豫","云","辽","黑","湘","皖",
		 * "鲁","新","苏","浙","赣","鄂","桂","甘"};
		 */
		carNoArray = new String[application.getCarNoList().size()];
		for (int i = 0; i < application.getCarNoList().size(); i++) {
			carNoArray[i] = application.getCarNoList().get(i);
		}
		// 设置adapter
		caseCarNo.setAdapter(carNoArray);
		caseCarNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				caseCarNoStr = caseCarNo.getText();

			}

		});

		caseThirdCarNo.setAdapter(carNoArray);
		caseThirdCarNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				caseThirdCarNoStr = caseThirdCarNo.getText();
			}

		});
		// 车型
		carTypeArray = new String[application.getCarTypeList().size()];
		for (int i = 0; i < application.getCarTypeList().size(); i++) {
			carTypeArray[i] = application.getCarTypeList().get(i);
		}

		caseCarType.setAdapter(carTypeArray);
		caseThirdCarType.setAdapter(carTypeArray);

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
		// 插入情景模板
		insertTemplateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTemplateListDialog();

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
				if (caseInsurnceId.getText().toString() != null)
					caseInsuranceIdStr = caseInsurnceId.getText().toString();
				if (caseOwnerPhone.getText().toString() != null)
					caseOwnerPhoneStr = caseOwnerPhone.getText().toString();
				if (caseDriverPhone.getText().toString() != null)
					caseDriverPhoneStr = caseDriverPhone.getText().toString();
				if (caseDriverLicence.getText().toString() != null)
					caseDriverLicenceStr = caseDriverLicence.getText()
							.toString();
				if (caseCarNo.getText().toString() != null)
					caseCarNoStr = caseCarNo.getText().toString();
				if (caseCarType.getText().toString() != null)
					caseCarTypeStr = caseCarType.getText().toString();
				if (caseCarVin.getText().toString() != null)
					caseCarVinStr = caseCarVin.getText().toString();
				if (caseThirdCarNo.getText().toString() != null)
					caseThirdCarNoStr = caseThirdCarNo.getText().toString();
				if (caseThirdCarType.getText().toString() != null)
					caseThirdCarTypeStr = caseThirdCarType.getText().toString();

				// 将文本记录传给下一个页面 在最终页面保存进数据库
				Intent intent = new Intent();
				intent.setClass(InsuranceTextActivity.this,
						InsuranceRecordActivity.class);
				intent.putExtra("location", location);
				intent.putExtra("insurance_photo_id", insurancePhotoId);
				intent.putExtra("caseOwnerStr", caseOwnerStr);
				intent.putExtra("caseNoStr", caseNoStr);
				intent.putExtra("caseDriverStr", caseDriverStr);
				intent.putExtra("caseInsuranceIdStr", caseInsuranceIdStr);
				intent.putExtra("relationShipStr", relationShipStr);
				intent.putExtra("caseOwnerPhoneStr", caseOwnerPhoneStr);
				intent.putExtra("caseDriverPhoneStr", caseDriverPhoneStr);
				intent.putExtra("caseDriverLicenceStr", caseDriverLicenceStr);
				intent.putExtra("caseCarNoStr", caseCarNoStr);
				intent.putExtra("caseCarTypeStr", caseCarTypeStr);
				intent.putExtra("caseCarVinStr", caseCarVinStr);
				intent.putExtra("caseThirdCarNoStr", caseThirdCarNoStr);
				intent.putExtra("caseThirdCarTypeStr", caseThirdCarTypeStr);
				intent.putExtra("templatePosition", templatePosition);
				intent.putExtra("isInsert", isInsert);
				// intent.putCharSequenceArrayListExtra("imageIdList",
				// imageIdList);
				startActivity(intent);
				// InsuranceTextActivity.this.finish();
			}

		});

	}

	// 设置标题栏
	public void setTopBar() {
		ImageButton previous_button = null;
		ImageButton index_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("文本记录");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		index_button = (ImageButton) view.findViewById(R.id.top_bar_index);
		previous_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				application.getActivityList().remove(this);
				InsuranceTextActivity.this.finish();

			}

		});
		index_button.setVisibility(View.VISIBLE);
		index_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int activityNum = application.getActivityList().size();
				for (int i = activityNum - 1; i >= 0; i--) {
					application.getActivityList().get(i).finish();
					application.getActivityList().remove(i);
				}
			}
		});
	}

	// 显示模版名列表的对话框
	public void showTemplateListDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("情景模板");
		builder.setItems(templatNameArray,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// 记录选择的模板
						templatePosition = which;
						isInsert = true;
						insertTemplate();

					}
				}).setNegativeButton("取消", null).show();
	}

	// 插入情景模版
	public void insertTemplate() {
		Template template = templateList.get(templatePosition);

		caseOwner.setText(template.getPeopleName());
		caseDriver.setText(template.getPeopleName());
		caseOwnerPhone.setText(template.getPhone());
		caseDriverPhone.setText(template.getPhone());
		caseDriverLicence.setText(template.getLicence());
		caseCarNo.setText(template.getCarNo());
		caseCarType.setText(template.getCarType());
	
		// 重新设置
		caseCarNo.setAdapter(carNoArray);
		caseCarType.setAdapter(carTypeArray);
		
	}
}

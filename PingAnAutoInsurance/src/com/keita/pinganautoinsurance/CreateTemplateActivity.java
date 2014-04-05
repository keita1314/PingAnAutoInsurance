package com.keita.pinganautoinsurance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.keita.pinganautoinsurance.mywidget.ComboEditText;
import com.keita.painganautoinsurance.entity.Template;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 创建模板相关类
 */
public class CreateTemplateActivity extends ActionBarActivity {
	private EditText templateName;
	private EditText peopleName;
	private EditText phone;
	private EditText licence;
	private EditText hurtNum;
	private EditText deadNum;
	private ComboEditText carNo;
	private ComboEditText carType;
	private ComboEditText caseLoss;
	private ComboEditText caseReason;
	private ComboEditText accidentReason;
	private EditText accidentDetail;
	private Button button;
	private MyApplication application;
	private ArrayList<Template> list;
	private SimpleDateFormat dateformat = null;
	private boolean isAdd = true;
	private int position;
	private Template template = null;
	private ActionBar actionBar= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template);
		// 格式化时间
		dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		application = (MyApplication) this.getApplication();
		setTopBar();
		templateName = (EditText) findViewById(R.id.template_name);
		peopleName = (EditText) findViewById(R.id.people_name);
		phone = (EditText) findViewById(R.id.phone);
		licence = (EditText)findViewById(R.id.licence);
		hurtNum = (EditText) findViewById(R.id.hurt_num);
		deadNum = (EditText) findViewById(R.id.dead_num);
		carNo = (ComboEditText) findViewById(R.id.car_no);
		carType = (ComboEditText) findViewById(R.id.case_car_type);
		caseLoss = (ComboEditText) findViewById(R.id.case_loss);
		caseReason = (ComboEditText) findViewById(R.id.case_reason);
		accidentReason = (ComboEditText) findViewById(R.id.accident_reason);
		accidentDetail = (EditText) findViewById(R.id.accident_detail);
		button = (Button) findViewById(R.id.complete_btn);

		list = application.getTemplateList();
		// applaction中取
		String[] carNoArray = new String[application.getCarNoList()
				.size()];
		for (int i = 0; i < application.getCarNoList().size(); i++) {
			carNoArray[i] = application.getCarNoList().get(i);
		}
		String[] carTypeArray = new String[application.getCarTypeList().size()];
		for (int i = 0; i < application.getCarTypeList().size(); i++) {
			carTypeArray[i] = application.getCarTypeList().get(i);
		}
		String[] caseLossArray = new String[application.getCarLossList().size()];
		for (int i = 0; i < application.getCarLossList().size(); i++) {
			caseLossArray[i] = application.getCarLossList().get(i);
		}
		String[] caseReasonArray = new String[application.getCaseReasonList()
				.size()];
		for (int i = 0; i < application.getCaseReasonList().size(); i++) {
			caseReasonArray[i] = application.getCaseReasonList().get(i);
		}
		String[] accidentReasonArray = new String[application.getAccidentList()
				.size()];
		for (int i = 0; i < application.getAccidentList().size(); i++) {
			accidentReasonArray[i] = application.getAccidentList().get(i);
		}

		Intent intent = this.getIntent();
		// 查看
		if (intent.getAction().equals("view")) {
			position = intent.getIntExtra("position", 0);
			isAdd = false;
			template = list.get(position);
			templateName.setText(template.getTemplateName());
			peopleName.setText(template.getPeopleName());
			phone.setText(template.getPhone());
			licence.setText(template.getLicence());
			hurtNum.setText(template.getHurtNum());
			deadNum.setText(template.getDeadNum());
			carNo.setText(template.getCarNo());
			carType.setText(template.getCarType());
			caseLoss.setText(template.getCaseLoss());
			caseReason.setText(template.getCarReason());
			accidentReason.setText(template.getAccidentReason());
			accidentDetail.setText(template.getAccidentDetail());

		}
		carNo.setAdapter(carNoArray);
		carType.setAdapter(carTypeArray);
		caseLoss.setAdapter(caseLossArray);
		caseReason.setAdapter(caseReasonArray);
		accidentReason.setAdapter(accidentReasonArray);
		// 完成按钮监听
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!templateName.getText().toString().equals("")) {
					// 若非新增模版
					if (!isAdd) {
						template = list.get(position);
					} else
						template = new Template();

					template.setTemplateName(templateName.getText().toString());
					template.setPeopleName(peopleName.getText().toString());
					template.setPhone(phone.getText().toString());
					template.setLicence(licence.getText().toString());
					template.setCarNo(carNo.getText());
					template.setHurtNum(hurtNum.getText().toString());
					template.setDeadNum(deadNum.getText().toString());
					template.setCarType(carType.getText());
					template.setCaseLoss(caseLoss.getText());
					template.setCarReason(caseReason.getText());
					template.setAccidentReason(accidentReason.getText());
					template.setAccidentDetail(accidentDetail.getText()
							.toString());
					template.setDate(dateformat.format(new Date()));
					// 如果是新增模版
					if (isAdd)
						list.add(template);
					Intent intent = new Intent();
					intent.setClass(CreateTemplateActivity.this,
							TemplateListActivity.class);
					startActivity(intent);
					CreateTemplateActivity.this.finish();

				} else
					Toast.makeText(CreateTemplateActivity.this, "请输入模板名",
							Toast.LENGTH_SHORT).show();

			}

		});

	}

	// 标题栏退出按钮
	public void setTopBar() {

		/*ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("情景模板");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(CreateTemplateActivity.this,
						TemplateListActivity.class);
				startActivity(intent);
				CreateTemplateActivity.this.finish();
			}

		});*/
		actionBar = getSupportActionBar();
		actionBar.show();
		actionBar.setTitle("新建模版");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	// 重写返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(CreateTemplateActivity.this,
					TemplateListActivity.class);
			startActivity(intent);
			CreateTemplateActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}

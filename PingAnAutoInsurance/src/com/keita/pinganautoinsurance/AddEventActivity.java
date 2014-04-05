package com.keita.pinganautoinsurance;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddEventActivity extends ActionBarActivity {
	private TextView carTypeTv = null;
	private TextView caselossTv = null;
	private TextView caseReasonTv = null;
	private TextView accidetnReasonTv = null;

	private EditText carTypeEt = null;
	private EditText caselossEt = null;
	private EditText caseReasonEt = null;
	private EditText accidetnReasonEt = null;

	private Button carTypeBtn = null;
	private Button caselosBtn = null;
	private Button caseReasonBtn = null;
	private Button accidetnReasonBtn = null;

	private MyApplication application = null;
	private ActionBar actionBar = null;

	StringBuilder carTypeSb = new StringBuilder("已有：");
	StringBuilder caseLossSb = new StringBuilder("已有：");
	StringBuilder caseReasonSb = new StringBuilder("已有：");
	StringBuilder accidentReasonSb = new StringBuilder("已有：");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		application = (MyApplication) this.getApplication();
		//设置标题栏
	
		setContentView(R.layout.activity_add_event);
		setTopBar();
		carTypeTv = (TextView) findViewById(R.id.car_type_sample);
		caselossTv = (TextView) findViewById(R.id.case_loss_sample);
		caseReasonTv = (TextView) findViewById(R.id.case_reason_sample);
		accidetnReasonTv = (TextView) findViewById(R.id.accident_reason_sample);

		carTypeEt = (EditText) findViewById(R.id.case_car_type);
		caselossEt = (EditText) findViewById(R.id.case_loss);
		caseReasonEt = (EditText) findViewById(R.id.case_reason);
		accidetnReasonEt = (EditText) findViewById(R.id.accident_reason);

		carTypeBtn = (Button) findViewById(R.id.add_car_type_btn);
		caselosBtn = (Button) findViewById(R.id.add_loss_btn);
		caseReasonBtn = (Button) findViewById(R.id.add_case_reason_btn);
		accidetnReasonBtn = (Button) findViewById(R.id.add_accidetn_reason_btn);

		// 退出按钮

		for (int i = 0; i < application.getCarTypeList().size(); i++) {
			carTypeSb.append(application.getCarTypeList().get(i) + " ");
		}
		carTypeTv.setText(carTypeSb);

		for (int i = 0; i < application.getCarLossList().size(); i++) {
			caseLossSb.append(application.getCarLossList().get(i) + " ");
		}
		caselossTv.setText(caseLossSb);

		for (int i = 0; i < application.getCaseReasonList().size(); i++) {
			caseReasonSb.append(application.getCaseReasonList().get(i) + " ");
		}
		caseReasonTv.setText(caseReasonSb);

		for (int i = 0; i < application.getAccidentList().size(); i++) {
			accidentReasonSb.append(application.getAccidentList().get(i) + " ");
		}
		accidetnReasonTv.setText(accidentReasonSb);

		carTypeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!carTypeEt.getText().toString().equals("")) {
					carTypeSb.append(carTypeEt.getText().toString() + " ");
					application.getCarTypeList().add(
							carTypeEt.getText().toString());
					carTypeTv.setText(carTypeSb);
				}
			}

		});

		caselosBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!caselossEt.getText().toString().equals("")) {
					caseLossSb.append(caselossEt.getText().toString() + " ");
					application.getCarLossList().add(
							caselossEt.getText().toString());
					caselossTv.setText(caseLossSb);
				}

			}

		});

		caseReasonBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!caseReasonEt.getText().toString().equals("")) {
					caseReasonSb
							.append(caseReasonEt.getText().toString() + " ");
					application.getCaseReasonList().add(
							caseReasonEt.getText().toString());
					caseReasonTv.setText(caseReasonSb);
				}
			}

		});
		accidetnReasonBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!accidetnReasonEt.getText().toString().equals("")) {
					accidentReasonSb.append(accidetnReasonEt.getText()
							.toString() + " ");
					application.getAccidentList().add(
							accidetnReasonEt.getText().toString());
					accidetnReasonTv.setText(accidentReasonSb);
				}
			}

		});

	}

	// 设置标题栏
	public void setTopBar() {
		/*ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("添加事件");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddEventActivity.this.finish();
			}

		});*/
		
		actionBar = getSupportActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
}

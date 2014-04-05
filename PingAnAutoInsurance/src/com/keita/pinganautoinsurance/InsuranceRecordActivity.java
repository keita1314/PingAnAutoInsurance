package com.keita.pinganautoinsurance;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.keita.painganautoinsurance.entity.InsuranceCase;
import com.keita.painganautoinsurance.entity.Template;
import com.keita.pinganautoinsurance.database.DBHelper;
import com.keita.pinganautoinsurance.mywidget.ComboEditText;

import android.R.color;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 案件录音页面 有少量文本记录
 */
public class InsuranceRecordActivity extends ActionBarActivity {
	private EditText hurtNum = null;
	private EditText deadNum = null;
	private EditText accidentDetail = null;
	private ComboEditText caseLoss = null;
	private ComboEditText caseReason = null;
	private ComboEditText accidentReason = null;

	private Button record_btn = null;
	private Button record_stop_btn = null;
	private Button record_play_btn = null;
	private Button save_case_btn = null;
	private Timer timer = null;
	Toast recording_toast = null;
	ImageView record_animate = null;
	LinearLayout toast_view = null;

	private ArrayAdapter<String> caseAdapter = null;
	private ArrayAdapter<String> accidentAdapter = null;

	private String hurtNumStr = "0";
	private String deadNumStr = "0";
	private String caseLossStr = "";
	private String caseReasonStr = "";
	private String accidentReasonStr = "";
	private String accidentDetailStr = "";

	// 理赔案件
	InsuranceCase insuranceCase = null;

	private String caseNo = "";
	private String caseOwner = "";
	private String caseDriver = "";
	private String relationShip = "";
	private String caseInsuranceId = "";
	private String caseOwnerPhone = "";
	private String caseDriverPhone = "";
	private String caseDriverLicence = "";
	private String caseCarNo = "";
	private String caseCarType = "";
	private String caseCarVin = "";
	private String caseThirdCarNo = "";
	private String caseThirdCarType = "";

	private MediaRecorder mr;
	private MediaPlayer mp;

	/* 录制音频文件 */
	private File recAudioFile = null;
	DataOutputStream dos = null;
	private File recordDir = null;
	private String SDPath = null;
	private String record_name = null;
	boolean isRecording = false;
	boolean isShowing = true;
	boolean isPlaying = false;

	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;
	private SimpleDateFormat dateformat = null;

	private MyApplication application = null;
	// 模板相关
	private boolean isInsert = false;
	private String templateName = null;
	private ArrayList<Template> templateList = null;
	private int templatePosition = 0;
	
	private ActionBar actionBar = null;
	String[] caseLossArray = null;
	String[] caseReasonArray = null;
	String[] accidentReasonArray = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_record);

		application = (MyApplication) this.getApplication();
		application.getActivityList().add(this);
		// 标题栏的设置
		setTopBar();
		// 获得模版
		templateList = application.getTemplateList();

		record_btn = (Button) findViewById(R.id.record_btn);
		record_stop_btn = (Button) findViewById(R.id.record_stop_btn);
		record_play_btn = (Button) findViewById(R.id.record_play_btn);
		save_case_btn = (Button) findViewById(R.id.save_case_btn);
		hurtNum = (EditText) findViewById(R.id.hurt_num);
		deadNum = (EditText) findViewById(R.id.dead_num);
		accidentDetail = (EditText) findViewById(R.id.accident_detail);
		caseLoss = (ComboEditText) findViewById(R.id.case_loss);

		// 格式化时间
		dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getWritableDatabase();

		// application中取出 事件 为ComboEdit控件设置adapter
		caseLossArray = new String[application.getCarLossList().size()];
		for (int i = 0; i < application.getCarLossList().size(); i++) {
			caseLossArray[i] = application.getCarLossList().get(i);
		}
		caseReasonArray = new String[application.getCaseReasonList().size()];
		for (int i = 0; i < application.getCaseReasonList().size(); i++) {
			caseReasonArray[i] = application.getCaseReasonList().get(i);
		}

		accidentReasonArray = new String[application.getAccidentList().size()];
		for (int i = 0; i < application.getAccidentList().size(); i++) {
			accidentReasonArray[i] = application.getAccidentList().get(i);
		}
		// 设置adapter
		caseAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, caseReasonArray);
		accidentAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, accidentReasonArray);
		caseAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		accidentAdapter
				.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		caseReason = (ComboEditText) findViewById(R.id.case_reason);
		accidentReason = (ComboEditText) findViewById(R.id.accident_reason);

		caseReason.setAdapter(caseReasonArray);
		accidentReason.setAdapter(accidentReasonArray);
		caseLoss.setAdapter(caseLossArray);

		Intent intent = this.getIntent();

		// 新建一份案件
		insuranceCase = new InsuranceCase();
		insuranceCase.setLocation(intent.getStringExtra("location"));
		insuranceCase.setInsurancePhotoId(intent
				.getStringExtra("insurance_photo_id"));
		insuranceCase.setDate(dateformat.format(new Date()));
		caseNo = intent.getStringExtra("caseNoStr");
		caseDriver = intent.getStringExtra("caseDriverStr");
		caseOwner = intent.getStringExtra("caseOwnerStr");
		caseInsuranceId = intent.getStringExtra("caseInsuranceIdStr");
		relationShip = intent.getStringExtra("relationShipStr");
		caseOwnerPhone = intent.getStringExtra("caseOwnerPhoneStr");
		caseDriverPhone = intent.getStringExtra("caseDriverPhoneStr");
		caseDriverLicence = intent.getStringExtra("caseDriverLicenceStr");
		caseCarNo = intent.getStringExtra("caseCarNoStr");
		caseCarType = intent.getStringExtra("caseCarTypeStr");
		caseCarVin = intent.getStringExtra("caseCarVinStr");
		caseThirdCarNo = intent.getStringExtra("caseThirdCarNoStr");
		caseThirdCarType = intent.getStringExtra("caseThirdCarTypeStr");

		isInsert = intent.getBooleanExtra("isInsert", false);
		templatePosition = intent.getIntExtra("templatePosition", 0);

		/* 检测SD卡存在 */
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			Log.v("SD", "exist");
			SDPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			Log.v("SDPath", SDPath);
			String fileName = "/PingAn/record";
			recordDir = new File(SDPath + "/PingAn/record");
			if (!isFileExist(fileName)) {
				Log.v("test", "not exist");
				if (recordDir.mkdirs())
					Log.v("create", "succ");
				else
					Log.v("create", "failed");
			}

		} else {
			Toast.makeText(this, "SD卡不存在", Toast.LENGTH_SHORT).show();
		}

		// 录音按钮监听
		record_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save_case_btn.setClickable(false);
				recording_toast = Toast.makeText(getApplicationContext(),
						" 正在录音  ", Toast.LENGTH_SHORT);
				record_animate = new ImageView(getApplicationContext());

				recording_toast.setGravity(Gravity.CENTER, 0, 0);
				record_animate.setImageResource(R.drawable.record_animate);
				toast_view = (LinearLayout) recording_toast.getView();
				toast_view.addView(record_animate, 0);
				recording_toast.setView(toast_view);
				mr = new MediaRecorder();
				mr.setAudioSource(MediaRecorder.AudioSource.MIC);
				mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

				try {
					Date now = new Date();
					record_name = Long.toString(now.getTime());
					recAudioFile = new File(recordDir.getAbsolutePath()
							+ "/REC_" + record_name + ".amr");
					recAudioFile.createNewFile();
					insuranceCase.setRecordPath(recAudioFile.getAbsolutePath());
					mr.setOutputFile(recAudioFile.getAbsolutePath());
					mr.prepare();
					mr.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* ar.startRecording(); */

				Log.v("test", "录音");
				isShowing = true;
				isRecording = true;
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub

						while (isShowing && isRecording) {
							recording_toast.show();
						}
					}

				}, 0);

			}

		});

		record_stop_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isRecording) {
					save_case_btn.setClickable(true);
					isShowing = false;
					Log.v("test", "停止");
					mr.release();
					mr = null;
					isRecording = false;
					timer.cancel();
					toast_view.setVisibility(View.INVISIBLE);
					recording_toast.cancel();
					record_play_btn.setClickable(true);
					

				}
			}

		});
		// 录音播放按钮监听
		record_play_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("Play", "Play");
				
				record_play_btn.setClickable(false);
				new PlayAsyncTask().execute();
				
			}

		});
		// 保存案件按钮
		save_case_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!hurtNum.getText().toString().equals(""))
					hurtNumStr = hurtNum.getText().toString();
				if (!deadNum.getText().toString().equals(""))
					deadNumStr = deadNum.getText().toString();
				if (caseLoss.getText().toString() != null)
					caseLossStr = caseLoss.getText().toString();
				if (accidentDetail.getText().toString() != null)
					accidentDetailStr = accidentDetail.getText().toString();

				ContentValues cv = new ContentValues();
				cv.put("case_no", caseNo);
				cv.put("case_owner", caseOwner);
				cv.put("case_driver", caseDriver);
				cv.put("relation", relationShip);
				cv.put("case_insurance_id", caseInsuranceId);
				cv.put("case_owner_phone", caseOwnerPhone);
				cv.put("case_driver_phone", caseDriverPhone);
				cv.put("case_driver_lience", caseDriverLicence);
				cv.put("case_car_no", caseCarNo);
				cv.put("case_car_type", caseCarType);
				cv.put("vin", caseCarVin);
				cv.put("case_third_car_no", caseThirdCarNo);
				cv.put("case_third_car_type", caseThirdCarType);
				cv.put("hurt_num", Integer.valueOf(hurtNumStr));
				cv.put("dead_num", Integer.valueOf(deadNumStr));
				cv.put("case_loss", caseLossStr);
				cv.put("case_reason", caseReason.getText());
				cv.put("accident_reason", accidentReason.getText());
				cv.put("accident_detail", accidentDetailStr);

				dbHelper.insertData(dataBase, cv, "insurance_text_table");

				String[] id = { "text_id" };
				Cursor cur = dbHelper.queryByCol(dataBase,
						"insurance_text_table", id);
				if (cur != null) {
					cur.moveToLast();
					insuranceCase.setTextId(cur.getString(cur
							.getColumnIndex("text_id")));
					cur.close();
				}

				cv.clear();
				cv = new ContentValues();

				cv.put("location", insuranceCase.getLocation());
				cv.put("record_path", insuranceCase.getRecordPath());
				cv.put("date", insuranceCase.getDate());
				cv.put("photos_id", insuranceCase.getInsurancePhotoId());
				cv.put("text_id", insuranceCase.getTextId());
				dbHelper.insertData(dataBase, cv, "insurance_case_table");

				// 从数据库中取出最后一个案件
				cur = dbHelper.query(dataBase, "insurance_case_table", null,
						null, null, null, null, null);
				if (cur.moveToLast()) {
					insuranceCase.setId(cur.getString(cur
							.getColumnIndex("case_id")));
				}
				cur.close();
				Intent intent = new Intent();
				intent.putExtra("insuranceCaseId", insuranceCase.getId());
				intent.putExtra("date", insuranceCase.getDate());
				intent.putExtra("location", insuranceCase.getLocation());
				intent.setClass(InsuranceRecordActivity.this,
						InsuranceViewActivity.class);
				startActivity(intent);
				int activitynum = application.getActivityList().size();
				for (int i = activitynum - 1; i >= 0; i--) {

					Activity prevActivity = (Activity) application
							.getActivityList().get(i);
					prevActivity.finish();
				}
				InsuranceRecordActivity.this.finish();
			}

		});
		if (isInsert == true)
			insertTemplate();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isShowing = false;
		isRecording = false;
		if (timer != null)
			timer.cancel();
		if(recording_toast!=null){
			recording_toast.cancel();
		}
		try {
			if (dos != null)
				dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//释放录音
		if(mr!=null){
			mr.release();
			mr = null;
		}
		Log.v("test", "destory");
		dataBase.close();
		dbHelper.close();
	}

	// 设置标题栏
	public void setTopBar() {
		/*ImageButton previous_button = null;
		ImageButton index_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("案件录音");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		index_button = (ImageButton) view.findViewById(R.id.top_bar_index);
		previous_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//若不在录音 可正常退出
				if(isRecording == false){
				application.getActivityList().remove(this);
				InsuranceRecordActivity.this.finish();
				}
				
			}

		});
		index_button.setVisibility(View.VISIBLE);
		index_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("test", "record index");
				//若不在录音 可正常退出
				if(isRecording == false){
				int activityNum = application.getActivityList().size();
				for (int i = activityNum - 1; i >= 0; i--) {
					application.getActivityList().get(i).finish();
					application.getActivityList().remove(i);
				}
			}
				
			}
		});*/
		actionBar = getSupportActionBar();
		actionBar.setTitle("案件录音");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	// 判断文件是否存在
	public boolean isFileExist(String fileName) {
		File file = new File(SDPath + fileName);
		return file.exists();
	}

	// 异步播放
	class PlayAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			isPlaying = true;

			try {

				mp = new MediaPlayer();
				if (recAudioFile != null) {
					mp.setDataSource(recAudioFile.getAbsolutePath());
					mp.prepare();
					mp.start();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			record_play_btn.setClickable(true);
		}
		
	}

	// 插入模板

	public void insertTemplate() {

		Template template = templateList.get(templatePosition);
		hurtNum.setText(template.getHurtNum());
		deadNum.setText(template.getDeadNum());
		caseLoss.setText(template.getCaseLoss());
		caseReason.setText(template.getCarReason());
		accidentReason.setText(template.getAccidentReason());
		accidentDetail.setText(template.getAccidentDetail());
		// 重新设计自定义控件的下拉内容
		caseLoss.setAdapter(caseLossArray);
		caseReason.setAdapter(caseReasonArray);
		accidentReason.setAdapter(accidentReasonArray);

	}
}

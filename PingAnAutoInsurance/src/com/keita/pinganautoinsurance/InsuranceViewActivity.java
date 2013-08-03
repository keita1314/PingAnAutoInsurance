package com.keita.pinganautoinsurance;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.keita.pinganautoinsurance.database.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InsuranceViewActivity extends Activity {
	private String insurancePolicyId = null;

	private TextView caseNoTv = null;

	private TextView caseOwnerTv = null;
	private TextView caseDriverTv = null;
	private TextView caseInsuranceIdTv = null;
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
	private TextView caseLossTv = null;
	private TextView hurtNumTv = null;
	private TextView deadNumTv = null;
	private TextView caseReasonTv = null;
	private TextView accidentReasonTv = null;
	private TextView accidentDetailTv = null;
	private TextView accidentDateTv = null;

	private ImageView imageView_1 = null;
	private ImageView imageView_2 = null;
	private ImageView imageView_3 = null;
	private ImageView imageView_4 = null;
	private ImageView imageView_5 = null;
	private ImageView imageView_6 = null;

	private Button playBtn = null;
	private Button stopBtn = null;

	private String caseNo = null;
	private String caseOwner = null;
	private String caseDriver = null;
	private String caseInsuranceId = null;
	private String relationShip = null;
	private String caseOwnerPhone = null;
	private String caseDriverPhone = null;
	private String caseDriverLicence = null;
	private String caseCarNo = null;
	private String caseCarType = null;
	private String caseCarVin = null;
	private String caseThirdCarNo = null;
	private String caseThirdCarType = null;

	private String insuracePolicyId = null;
	private String hurtNumStr = "0";
	private String deadNumStr = "0";
	private String locationStr = "";
	private String caseLossStr = "";
	private String caseReasonStr = "";
	private String accidentReasonStr = "";
	private String accidentDetailStr = "";
	private String accidentDateStr = "";

	private Bitmap bitmapArray[] = null;
	String[] imgId = null;
	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;

	/* 音频文件 */
	private AudioTrack track = null;
	private File recAudioFile = null;
	private static int SAMPLE_RATE_IN_HZ = 8000;
	boolean isPlaying = false;
	PlayAsyncTask play = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_view);
		ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		previous_button =(ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InsuranceViewActivity.this.finish();
			}
			
		});
		caseNoTv = (TextView) findViewById(R.id.case_no);
		caseOwnerTv = (TextView) findViewById(R.id.case_owner);
		relationShipTv = (TextView) findViewById(R.id.relationship);
		caseInsuranceIdTv = (TextView) findViewById(R.id.case_insurance_id);
		caseOwnerPhoneTv = (TextView) findViewById(R.id.case_owner_phone);
		caseDriverTv = (TextView) findViewById(R.id.case_driver);
		caseDriverPhoneTv = (TextView) findViewById(R.id.case_driver_phone);
		caseDriverLicenceTv = (TextView) findViewById(R.id.case_driver_licence);
		caseCarTypeTv = (TextView) findViewById(R.id.case_car_type);
		caseCarNoTv = (TextView) findViewById(R.id.case_car_no);
		caseCarVinTv = (TextView) findViewById(R.id.case_car_vin);
		caseThirdCarNoTv = (TextView) findViewById(R.id.case_third_car_no);
		caseThirdCarTypeTv = (TextView) findViewById(R.id.case_third_car_type);
		locationTv = (TextView) findViewById(R.id.location);
		caseLossTv = (TextView) findViewById(R.id.case_loss);
		caseReasonTv = (TextView) findViewById(R.id.case_reason);
		accidentReasonTv = (TextView) findViewById(R.id.accident_reason);
		accidentDetailTv = (TextView) findViewById(R.id.accident_detail);
		hurtNumTv = (TextView) findViewById(R.id.hurt_num);
		deadNumTv = (TextView) findViewById(R.id.dead_num);
		accidentDateTv = (TextView) findViewById(R.id.accident_date);
		// 六张图片
		imageView_1 = (ImageView) findViewById(R.id.insurance_view_img1);
		imageView_2 = (ImageView) findViewById(R.id.insurance_view_img2);
		imageView_3 = (ImageView) findViewById(R.id.insurance_view_img3);
		imageView_4 = (ImageView) findViewById(R.id.insurance_view_img4);
		imageView_5 = (ImageView) findViewById(R.id.insurance_view_img5);
		imageView_6 = (ImageView) findViewById(R.id.insurance_view_img6);

		playBtn = (Button) findViewById(R.id.play_btn);
		stopBtn = (Button) findViewById(R.id.stop_btn);

		bitmapArray = new Bitmap[] { null, null, null, null, null, null };

		imgId = new String[] { null, null, null, null, null, null };

		// 数据库操作
		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getReadableDatabase();

		Intent intent = this.getIntent();
		insuracePolicyId = intent.getStringExtra("insurancePolicyId");
		locationStr = intent.getStringExtra("location");
		accidentDateStr = intent.getStringExtra("date");
		if (locationStr != null)
			locationTv.setText(locationStr);
		if (accidentDateStr != null)
			accidentDateTv.setText(accidentDateStr);
		getDataFromDatabase();
		setDataToInsurance();
		setImageViewListener();
		
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playBtn.setClickable(false);
				Log.v("play", "play");
				if(recAudioFile!=null)
					new PlayAsyncTask().execute();
				else
					Toast.makeText(InsuranceViewActivity.this, "找不到录音文件", Toast.LENGTH_SHORT).show();

			}

		});
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("stop", "stop");
				// play.cancel(true);
				
				isPlaying = false;
				playBtn.setClickable(true);

			}

		});
	}

	// 从数据库中获取信息
	public void getDataFromDatabase() {
		// 文本记录表和照片表的id
		String text_id = null;
		String photos_id = null;
		String record_path = null;
		Cursor cur = dbHelper.query(dataBase, "insurance_policy_table",
				new String[] { "record_path", "photos_id", "text_id" },
				"policy_id=?", new String[] { insuracePolicyId }, null, null,
				null);
		if (cur.moveToFirst()) {
			text_id = cur.getString(cur.getColumnIndex("text_id"));
			photos_id = cur.getString(cur.getColumnIndex("photos_id"));
			record_path = cur.getString(cur.getColumnIndex("record_path"));
			if(record_path	!= null)
				recAudioFile = new File(record_path);
		}
		cur.close();
		// 从文本表中得到数据
		if (text_id != null) {
			cur = dbHelper.query(dataBase, "insurance_text_table", null,
					"text_id=?", new String[] { text_id }, null, null, null);
			if (cur.moveToFirst()) {
				caseNo = cur.getString(cur.getColumnIndex("case_no"));
				System.out.println(caseNo);

				caseOwner = cur.getString(cur.getColumnIndex("case_owner"));
				System.out.println(caseOwner);

				caseDriver = cur.getString(cur.getColumnIndex("case_driver"));
				System.out.println("驾驶员"+caseDriver);

				relationShip = cur.getString(cur.getColumnIndex("relation"));
				System.out.println(relationShip);
				
				caseInsuranceId =  cur.getString(cur.getColumnIndex("case_insurance_id"));
				System.out.println("保单"+caseInsuranceId);
				
				caseOwnerPhone = cur.getString(cur
						.getColumnIndex("case_owner_phone"));
				System.out.println(caseOwnerPhone);

				caseDriverPhone = cur.getString(cur
						.getColumnIndex("case_driver_phone"));
				System.out.println(caseDriverPhone);

				caseDriverLicence = cur.getString(cur
						.getColumnIndex("case_driver_lience"));
				System.out.println(caseDriverLicence);

				caseCarNo = cur.getString(cur.getColumnIndex("case_car_no"));
				System.out.println(caseCarNo);

				caseCarType = cur
						.getString(cur.getColumnIndex("case_car_type"));
				System.out.println(caseCarType);

				caseCarVin = cur.getString(cur.getColumnIndex("vin"));
				System.out.println(caseCarVin);

				caseThirdCarNo = cur.getString(cur
						.getColumnIndex("case_third_car_no"));
				System.out.println(caseThirdCarNo);

				caseThirdCarType = cur.getString(cur
						.getColumnIndex("case_third_car_type"));
				System.out.println(caseThirdCarType);

				hurtNumStr = cur.getString(cur.getColumnIndex("hurt_num"));
				System.out.println(hurtNumStr);

				deadNumStr = cur.getString(cur.getColumnIndex("dead_num"));
				System.out.println(deadNumStr);
				
				caseLossStr = cur.getString(cur.getColumnIndex("case_loss"));
				System.out.println("物损"+caseLossStr);
				
				caseReasonStr = cur
						.getString(cur.getColumnIndex("case_reason"));
				System.out.println(caseReasonStr);

				accidentReasonStr = cur.getString(cur
						.getColumnIndex("accident_reason"));
				System.out.println(accidentReasonStr);

				accidentDetailStr = cur.getString(cur
						.getColumnIndex("accident_detail"));
				System.out.println(accidentDetailStr);

			}
			cur.close();
		}
		// 从照片表中得到数据

		String[] imgPath = new String[] { null, null, null, null, null, null };
		Cursor cur_img = null;
		if (photos_id != null) {
			cur = dbHelper.query(dataBase, "insurance_photo_table", null,
					"id = ?", new String[] { photos_id }, null, null, null);
			if (cur.moveToFirst()) {
				for (int i = 0; i < 6; i++) {

					imgId[i] = cur.getString(cur.getColumnIndex("img" + (i + 1)
							+ "_id"));
					if (imgId[i] != null) {
						cur_img = dbHelper.query(dataBase, "text_image_table",
								null, "id = ?", new String[] { imgId[i] },
								null, null, null);
						if (cur_img.moveToFirst())
							imgPath[i] = cur_img.getString(cur_img
									.getColumnIndex("img_path"));
						cur_img.close();
					}
				}
				cur.close();
			}
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		InputStream is = null;
		Bitmap bitmap = null;

		for (int i = 0; i < imgPath.length; i++) {
			if (imgPath[i] != null) {
				try {
					is = new FileInputStream(imgPath[i]);
					bitmap = BitmapFactory.decodeStream(is, null, options);
					is.close();
					bitmapArray[i] = ThumbnailUtils.extractThumbnail(bitmap,
							80, 80);

					bitmap.recycle();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	public void setDataToInsurance() {
		if (caseNo != null)
			caseNoTv.setText(caseNo);
		else
			caseNoTv.setText("空");
		if (caseOwner != null)
			caseOwnerTv.setText(caseOwner);
		else
			caseOwnerTv.setText("空");
		if (caseDriver != null)
			caseDriverTv.setText(caseDriver);
		else
			caseDriverTv.setText("空");

		if (relationShip != null)
			relationShipTv.setText(relationShip);
		else
			relationShipTv.setText("空");
		if (caseInsuranceId != null){
			caseInsuranceIdTv.setText(caseInsuranceId);
			System.out.println("保单2"+caseInsuranceId);
		}
		else
			caseInsuranceIdTv.setText("空");
		if (caseOwnerPhone != null)
			caseOwnerPhoneTv.setText(caseOwnerPhone);
		else
			caseOwnerPhoneTv.setText("空");

		if (caseDriverPhone != null)
			caseDriverPhoneTv.setText(caseDriverPhone);
		else
			caseDriverPhoneTv.setText("空");

		if (caseDriverLicence != null)
			caseDriverLicenceTv.setText(caseDriverLicence);
		else
			caseDriverLicenceTv.setText("空");
		if (caseCarNo != null)
			caseCarNoTv.setText(caseCarNo);
		else
			caseCarNoTv.setText("空");

		if (caseCarType != null)
			caseCarTypeTv.setText(caseCarType);
		else
			caseCarTypeTv.setText("空");

		if (caseCarVin != null)
			caseCarVinTv.setText(caseCarVin);
		else
			caseCarVinTv.setText("空");

		if (caseThirdCarNo != null)
			caseThirdCarNoTv.setText(caseThirdCarNo);
		else
			caseThirdCarNoTv.setText("空");

		if (caseThirdCarType != null)
			caseThirdCarTypeTv.setText(caseThirdCarType);
		else
			caseThirdCarTypeTv.setText("空");

		if (hurtNumStr != null)
			hurtNumTv.setText(hurtNumStr);
		else
			hurtNumTv.setText("空");

		if (deadNumStr != null)
			deadNumTv.setText(deadNumStr);
		else
			deadNumTv.setText("空");
		if(caseLossStr != null)
			caseLossTv.setText(caseLossStr);
		else
			caseLossTv.setText("空");
		if (caseReasonStr != null)
			caseReasonTv.setText(caseReasonStr);
		else
			caseReasonTv.setText("空");

		if (accidentReasonStr != null)
			accidentReasonTv.setText(accidentReasonStr);
		else
			accidentReasonTv.setText("空");

		if (accidentDetailStr != null)
			accidentDetailTv.setText(accidentDetailStr);
		else
			accidentDetailTv.setText("空");
		// 设置图片
		for (int i = 0; i < bitmapArray.length; i++) {
			if (bitmapArray[i] != null)
				switch (i + 1) {
				case 1:
					imageView_1.setImageBitmap(bitmapArray[i]);
					break;
				case 2:
					imageView_2.setImageBitmap(bitmapArray[i]);
					break;
				case 3:
					imageView_3.setImageBitmap(bitmapArray[i]);
					break;
				case 4:
					imageView_4.setImageBitmap(bitmapArray[i]);
					break;
				case 5:
					imageView_5.setImageBitmap(bitmapArray[i]);
					break;
				case 6:
					imageView_6.setImageBitmap(bitmapArray[i]);
					break;

				}
		}
	}

	public void setImageViewListener() {
		imageView_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imgId[0] != null) {
					Intent intent = new Intent();
					intent.putExtra("imgId", imgId[0]);
					intent.setClass(InsuranceViewActivity.this,
							PhotoViewActivity.class);
					startActivity(intent);
				} else
					Toast.makeText(InsuranceViewActivity.this, "没有图片",
							Toast.LENGTH_SHORT).show();

			}

		});

		imageView_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imgId[1] != null) {
					Intent intent = new Intent();
					intent.putExtra("imgId", imgId[1]);
					intent.setClass(InsuranceViewActivity.this,
							PhotoViewActivity.class);
					startActivity(intent);
				} else
					Toast.makeText(InsuranceViewActivity.this, "没有图片",
							Toast.LENGTH_SHORT).show();

			}

		});

		imageView_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imgId[2] != null) {
					Intent intent = new Intent();
					intent.putExtra("imgId", imgId[2]);
					intent.setClass(InsuranceViewActivity.this,
							PhotoViewActivity.class);
					startActivity(intent);
				} else
					Toast.makeText(InsuranceViewActivity.this, "没有图片",
							Toast.LENGTH_SHORT).show();

			}

		});

		imageView_4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imgId[3] != null) {
					Intent intent = new Intent();
					intent.putExtra("imgId", imgId[3]);
					intent.setClass(InsuranceViewActivity.this,
							PhotoViewActivity.class);
					startActivity(intent);
				} else
					Toast.makeText(InsuranceViewActivity.this, "没有图片",
							Toast.LENGTH_SHORT).show();

			}

		});

		imageView_5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imgId[4] != null) {
					Intent intent = new Intent();
					intent.putExtra("imgId", imgId[4]);
					intent.setClass(InsuranceViewActivity.this,
							PhotoViewActivity.class);
					startActivity(intent);
				} else
					Toast.makeText(InsuranceViewActivity.this, "没有图片",
							Toast.LENGTH_SHORT).show();

			}

		});
		imageView_6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imgId[5] != null) {
					Intent intent = new Intent();
					intent.putExtra("imgId", imgId[5]);
					intent.setClass(InsuranceViewActivity.this,
							PhotoViewActivity.class);
					startActivity(intent);
				} else
					Toast.makeText(InsuranceViewActivity.this, "没有图片",
							Toast.LENGTH_SHORT).show();

			}

		});
	}

	// 异步播放
	class PlayAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			isPlaying = true;
			int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			byte[] buffer = new byte[bufferSize];
			try {
				DataInputStream dis = new DataInputStream(
						new BufferedInputStream(new FileInputStream(
								recAudioFile)));
				track = new AudioTrack(AudioManager.STREAM_MUSIC,
						SAMPLE_RATE_IN_HZ,
						AudioFormat.CHANNEL_CONFIGURATION_MONO,
						AudioFormat.ENCODING_PCM_16BIT, bufferSize*4,
						AudioTrack.MODE_STREAM);
				// 开始播放
				track.setStereoVolume(1.0f, 1.0f);

				track.play();
				while (isPlaying && dis.available() > 0) {

					int i = 0;
					while (dis.available() > 0 && i < buffer.length) {
						// Log.v("Play", "isPlaying");
						buffer[i] = dis.readByte();
						i++;
					}
					track.write(buffer, 0, buffer.length);
				}

				// 播放结束
				track.stop();
				dis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			playBtn.setClickable(true);
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		if (track != null) {
			track.stop();
			track.release();
		}
		dataBase.close();
		dbHelper.close();
		// 回收图片内存
		for (int i = 0; i < bitmapArray.length; i++) {
			if (bitmapArray[i] != null)
				bitmapArray[i].recycle();
		}
	}
}

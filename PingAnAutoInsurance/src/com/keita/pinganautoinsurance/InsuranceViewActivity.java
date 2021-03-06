package com.keita.pinganautoinsurance;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;




import com.keita.pinganautoinsurance.database.DBHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 案件详情页面
 */
public class InsuranceViewActivity extends ActionBarActivity {
	private String insuranceCaseId = null;

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

	private String hurtNumStr = "0";
	private String deadNumStr = "0";
	private String locationStr = "";
	private String caseLossStr = "";
	private String caseReasonStr = "";
	private String accidentReasonStr = "";
	private String accidentDetailStr = "";
	private String accidentDateStr = "";
	
	private String[] imgPath = new String[] { null, null, null, null, null, null };
	private Bitmap bitmapArray[] = null;
	String[] imgId = null;
	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;

	/* 音频文件 */

	private File recAudioFile = null;
	private MediaRecorder mr;
	private MediaPlayer mp;
	boolean isPlaying = false;
	PlayAsyncTask play = null;
	
	/*上传文件按钮*/
	private Button uploadButton = null;
	
	
	private ActionBar actionBar = null;
	
	private Context context = null;
	
	private int status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_view);
		
		/*ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("案件详情");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InsuranceViewActivity.this.finish();
			}

		});*/
		
		actionBar = getSupportActionBar();
		actionBar.setTitle("案件详情");
		actionBar.setDisplayHomeAsUpEnabled(true);
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
		
		uploadButton = (Button) findViewById(R.id.insurance_upload);

		bitmapArray = new Bitmap[] { null, null, null, null, null, null };

		imgId = new String[] { null, null, null, null, null, null };

		// 数据库操作
		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getReadableDatabase();

		Intent intent = this.getIntent();
		insuranceCaseId = intent.getStringExtra("insuranceCaseId");
		locationStr = intent.getStringExtra("location");
		accidentDateStr = intent.getStringExtra("date");
		if (locationStr != null)
			locationTv.setText(locationStr);
		if (accidentDateStr != null)
			accidentDateTv.setText(accidentDateStr);
		getDataFromDatabase();
		setDataToInsurance();
		setImageViewListener();
		//播放按钮
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playBtn.setClickable(false);
				Log.v("play", "play");
				if (recAudioFile != null) {
					String filePath = recAudioFile.getAbsolutePath();
					new PlayAsyncTask().execute(filePath);
				} else
					Toast.makeText(InsuranceViewActivity.this, "找不到录音文件",
							Toast.LENGTH_SHORT).show();

			}

		});
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("stop", "stop");
				// play.cancel(true);
				if (mp != null)
					mp.stop();
				playBtn.setClickable(true);

			}

		});
		
		if(getParent()!=null)
			context = getParent();
		else
			context = this;
		/*上传保单*/
		uploadButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new UploadTask(context).execute();
			}
			
		});
	}

	// 从数据库中获取信息
	public void getDataFromDatabase() {
		// 文本记录表和照片表的id
		String text_id = null;
		String photos_id = null;
		String record_path = null;
		Cursor cur = dbHelper
				.query(dataBase, "insurance_case_table", new String[] {
						"record_path", "photos_id", "text_id" }, "case_id=?",
						new String[] { insuranceCaseId }, null, null, null);
		if (cur.moveToFirst()) {
			text_id = cur.getString(cur.getColumnIndex("text_id"));
			photos_id = cur.getString(cur.getColumnIndex("photos_id"));
			record_path = cur.getString(cur.getColumnIndex("record_path"));
			if (record_path != null)
				recAudioFile = new File(record_path);
		}
		cur.close();
		// 从文本表中得到数据
		if (text_id != null) {
			cur = dbHelper.query(dataBase, "insurance_text_table", null,
					"text_id=?", new String[] { text_id }, null, null, null);
			if (cur.moveToFirst()) {
				caseNo = cur.getString(cur.getColumnIndex("case_no"));

				caseOwner = cur.getString(cur.getColumnIndex("case_owner"));

				caseDriver = cur.getString(cur.getColumnIndex("case_driver"));

				relationShip = cur.getString(cur.getColumnIndex("relation"));

				caseInsuranceId = cur.getString(cur
						.getColumnIndex("case_insurance_id"));

				caseOwnerPhone = cur.getString(cur
						.getColumnIndex("case_owner_phone"));

				caseDriverPhone = cur.getString(cur
						.getColumnIndex("case_driver_phone"));

				caseDriverLicence = cur.getString(cur
						.getColumnIndex("case_driver_lience"));

				caseCarNo = cur.getString(cur.getColumnIndex("case_car_no"));

				caseCarType = cur
						.getString(cur.getColumnIndex("case_car_type"));

				caseCarVin = cur.getString(cur.getColumnIndex("vin"));

				caseThirdCarNo = cur.getString(cur
						.getColumnIndex("case_third_car_no"));

				caseThirdCarType = cur.getString(cur
						.getColumnIndex("case_third_car_type"));

				hurtNumStr = cur.getString(cur.getColumnIndex("hurt_num"));

				deadNumStr = cur.getString(cur.getColumnIndex("dead_num"));

				caseLossStr = cur.getString(cur.getColumnIndex("case_loss"));

				caseReasonStr = cur
						.getString(cur.getColumnIndex("case_reason"));

				accidentReasonStr = cur.getString(cur
						.getColumnIndex("accident_reason"));

				accidentDetailStr = cur.getString(cur
						.getColumnIndex("accident_detail"));

			}
			cur.close();
		}
		// 从照片表中得到数据

		
		Cursor cur_img = null;
		if (photos_id != null) {
			cur = dbHelper.query(dataBase, "insurance_photo_table", null,
					"photos_id = ?", new String[] { photos_id }, null, null,
					null);
			if (cur.moveToFirst()) {
				for (int i = 0; i < 6; i++) {

					imgId[i] = cur.getString(cur.getColumnIndex("img" + (i + 1)
							+ "_id"));
					if (imgId[i] != null) {
						cur_img = dbHelper.query(dataBase, "text_image_table",
								null, "text_img_id = ?",
								new String[] { imgId[i] }, null, null, null);
						if (cur_img.moveToFirst())
							imgPath[i] = cur_img.getString(cur_img
									.getColumnIndex("img_path"));
						cur_img.close();
					}
				}
				cur.close();
			}
		}

		

		for (int i = 0; i < imgPath.length; i++) {
			if (imgPath[i] != null) {
				try {
					System.gc();
					bitmapArray[i] = BitMapUtil.getSmallBitmap(imgPath[i]);
					
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
		if (caseInsuranceId != null) {
			caseInsuranceIdTv.setText(caseInsuranceId);
		} else
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
		if (caseLossStr != null)
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

	// 标题栏退出按钮
	public void setTopBar() {

		ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("案件详情");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InsuranceViewActivity.this.finish();
			}

		});
	}

	// 异步播放
	class PlayAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			recAudioFile = new File(params[0]);
			isPlaying = true;

			try {
				mp = new MediaPlayer();
				mp.setDataSource(recAudioFile.getAbsolutePath());
				mp.prepare();
				mp.start();

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
	/*上传函数*/
	public void upload(){
		
		//Toast.makeText(this, "开始上传", Toast.LENGTH_SHORT);
		String url = "http://192.168.1.102:8080/AdminDemo/upload.action";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		MultipartEntity entity = new MultipartEntity();

		//int status;
		FileBody record_body = null;
		if(recAudioFile != null)
			record_body = new FileBody(recAudioFile);
		FileBody img_body[] = new FileBody[]{null,null,null,null,null,null};
		for(int i = 0;i<imgPath.length;i++){
			if(imgPath[i] != null )
				img_body[i] = new FileBody(new File(imgPath[i]));
		}
		try {
			System.out.println(locationStr);
			entity.addPart("location", new StringBody(locationStr,Charset.forName(HTTP.UTF_8)));
			entity.addPart("date", new StringBody(accidentDateStr));
			entity.addPart("case_no", new StringBody(caseNo));
			entity.addPart("insurance_no", new StringBody(caseInsuranceId));
			entity.addPart("insurance_owner", new StringBody(caseOwner,Charset.forName(HTTP.UTF_8)));
			entity.addPart("insurance_driver", new StringBody(caseDriver,Charset.forName(HTTP.UTF_8)));
			entity.addPart("relationship", new StringBody(relationShip,Charset.forName(HTTP.UTF_8)));
			entity.addPart("insurance_owner_phone", new StringBody(
					caseOwnerPhone));
			entity.addPart("insurance_driver_phone", new StringBody(
					caseDriverPhone));
			entity.addPart("insurance_driver_liencese", new StringBody(
					caseDriverLicence,Charset.forName(HTTP.UTF_8)));
			entity.addPart("car_no", new StringBody(caseCarNo,Charset.forName(HTTP.UTF_8)));
			entity.addPart("car_type", new StringBody(caseCarType,Charset.forName(HTTP.UTF_8)));
			entity.addPart("vin", new StringBody(caseCarVin));
			entity.addPart("third_car_no", new StringBody(caseThirdCarNo,Charset.forName(HTTP.UTF_8)));
			entity.addPart("third_car_type", new StringBody(caseThirdCarType,Charset.forName(HTTP.UTF_8)));
			entity.addPart("hurt_num", new StringBody(hurtNumStr));
			entity.addPart("loss", new StringBody(caseLossStr,Charset.forName(HTTP.UTF_8)));
			entity.addPart("case_reason", new StringBody(caseReasonStr,Charset.forName(HTTP.UTF_8)));
			entity.addPart("accident_reason", new StringBody(accidentReasonStr,Charset.forName(HTTP.UTF_8)));
			entity.addPart("accident_detail", new StringBody(accidentDetailStr,Charset.forName(HTTP.UTF_8)));
			if(record_body != null)
				entity.addPart("record", record_body);
			for(int i = 0;i<img_body.length;i++){
				if(img_body[i] != null)
					entity.addPart("img"+i, img_body[i]);
			}
			
			httpPost.setEntity(entity);
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 35000);
			status = httpClient.execute(httpPost).getStatusLine().getStatusCode();
			

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private class UploadTask extends AsyncTask<Void,Void,Void>{
		private ProgressDialog pd = null;
		private Context context = null;
		public UploadTask(Context context){
			this.context = context;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setTitle("上传保单");
			pd.setMessage("保单正在上传中...");
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
			upload();
			}catch(Exception e){
				e.printStackTrace();
				this.cancel(true);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if(pd != null)
				pd.cancel();
			if (status == HttpStatus.SC_OK)
			{
				Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
				System.out.println("上传成功"+status);
				// 上传成功
			} else
			{
				Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
				System.out.println("上传失败"+status);
				// 上传失败
			}
			super.onPostExecute(result);
		}

		
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		if (mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
		dataBase.close();
		dbHelper.close();
		// 回收图片内存
		for (int i = 0; i < bitmapArray.length; i++) {
			if (bitmapArray[i]!=null && !bitmapArray[i].isRecycled())
				bitmapArray[i].recycle();
				bitmapArray[i] = null;
		}
		System.gc();
	}
}

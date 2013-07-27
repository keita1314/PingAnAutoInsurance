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
import java.util.Timer;
import java.util.TimerTask;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class InsuranceNextActivity extends Activity {
	private Button record_btn = null;
	private Button record_stop_btn = null;
	private Button record_play_btn = null;
	private Timer timer = null;
	Toast recording_toast = null;
	ImageView record_animate = null;
	LinearLayout toast_view = null;
	private AudioRecord ar;
	private int bs;
	private static int SAMPLE_RATE_IN_HZ = 8000;
	/* 录制音频文件 */
	private File recAudioFile = null;
	DataOutputStream dos = null;
	private File recordDir = null;
	private String SDPath = null;
	boolean isRecording = false;
	boolean isShowing = true;
	boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_next);
		record_btn = (Button) findViewById(R.id.record_btn);
		record_stop_btn = (Button) findViewById(R.id.record_stop_btn);
		record_play_btn = (Button)findViewById(R.id.record_play_btn);
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

		}
		else{
			Toast.makeText(this, "SD卡不存在", Toast.LENGTH_SHORT);
		}
		
	
		// 录音按钮监听
		record_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recording_toast = Toast.makeText(getApplicationContext(),
						" 正在录音  ", Toast.LENGTH_SHORT);
				record_animate = new ImageView(getApplicationContext());

				recording_toast.setGravity(Gravity.CENTER, 0, 0);
				record_animate.setImageResource(R.drawable.record_animate_02);
				toast_view = (LinearLayout) recording_toast.getView();
				toast_view.addView(record_animate, 0);
				recording_toast.setView(toast_view);

				bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
						AudioFormat.CHANNEL_CONFIGURATION_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				ar = new AudioRecord(MediaRecorder.AudioSource.MIC,
						SAMPLE_RATE_IN_HZ,
						AudioFormat.CHANNEL_CONFIGURATION_MONO,
						AudioFormat.ENCODING_PCM_16BIT, bs * 10);
				try {
					recAudioFile = new File(recordDir.getAbsolutePath()
							+ "/1.pcm");
					recAudioFile.createNewFile();
					dos = new DataOutputStream(new BufferedOutputStream(
							new FileOutputStream(recAudioFile)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ar.startRecording();
				isShowing = true;
				Log.v("test", "录音");
				timer = new Timer();
				isRecording = true;
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub

						while (isShowing && isRecording) {

							// 用于读取的
							byte[] buffer = new byte[bs];
							int r = ar.read(buffer, 0, bs);
							int v = 0;
							// 将 buffer 内容取出
							for (int i = 0; i < buffer.length; i++) {

								try {
									if (dos != null)
										dos.write(buffer[i]);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								v += buffer[i] * buffer[i];
							}
							// 平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。
							Log.d("spl", String.valueOf(v / (float) r));
							int volume =0;
							if(r!=0)
								volume = v /  r;
							else 
								volume = 2400;
							//根据音量大小设置动画
							Message msg = new Message();
							msg.what = volume;
							handler.sendMessage(msg);
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
					isShowing = false;
					Log.v("test", "停止");
				/*	try {
						dos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					ar.stop();
					ar.release();
					ar = null;
					isRecording = false;
					timer.cancel();
					toast_view.setVisibility(View.INVISIBLE);
					recording_toast.cancel();

				}
			}

		});
		record_play_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(recAudioFile), "audio");
				startActivity(intent);*/
				Log.v("Play", "Play");
				new PlayAsyncTask().execute();
			}
			
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer!=null)
			timer.cancel();
		try {
			if(dos != null)
				dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v("test", "destory");
	}
	//根据声音大小 更新UI
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.v("handler", ""+msg.what);
			toast_view = (LinearLayout) recording_toast.getView();
			toast_view.removeViewAt(0);
			//根据音量大小显示不同的图片 数值是由录音计算出的音量大小
			if(msg.what>2400 &&msg.what<2500){
				record_animate.setImageResource(R.drawable.record_animate_03);
			}
			if(msg.what>2500 &&msg.what<2600){
				record_animate.setImageResource(R.drawable.record_animate_04);
			
			}
			if(msg.what>2600 &&msg.what<2700){
				record_animate.setImageResource(R.drawable.record_animate_05);
		
			}
			if(msg.what>2700 &&msg.what<2800){
				record_animate.setImageResource(R.drawable.record_animate_06);

			}
			if(msg.what>2800 &&msg.what<2900){
				record_animate.setImageResource(R.drawable.record_animate_07);
				
			}
			if(msg.what>2900 &&msg.what<3000){
				record_animate.setImageResource(R.drawable.record_animate_08);
				
			}
			if(msg.what>3000 &&msg.what<3100){
				record_animate.setImageResource(R.drawable.record_animate_09);
				
			}
			if(msg.what>3100 &&msg.what<3200){
				record_animate.setImageResource(R.drawable.record_animate_09);
				
			}
			if(msg.what>3200 &&msg.what<3300){
				record_animate.setImageResource(R.drawable.record_animate_10);
				
			}
			if(msg.what>3300 &&msg.what<3400){
				record_animate.setImageResource(R.drawable.record_animate_11);
				
			}
			if(msg.what>3400 &&msg.what<3500){
				record_animate.setImageResource(R.drawable.record_animate_12);
				
			}
			if(msg.what>3500 &&msg.what<3600){
				record_animate.setImageResource(R.drawable.record_animate_13);
				
			}
			if(msg.what>3600 ){
				record_animate.setImageResource(R.drawable.record_animate_14);
				
			}
			else if(msg.what<2400)
				record_animate.setImageResource(R.drawable.record_animate_01);
			toast_view.addView(record_animate, 0);
			recording_toast.setView(toast_view);
		}
		
		
	};
	// 判断文件是否存在
	public boolean isFileExist(String fileName) {
		File file = new File(SDPath + fileName);
		return file.exists();
	}
	class PlayAsyncTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			isPlaying = true;
			int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			byte[] buffer = new byte[bufferSize];
			try{
				DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(recAudioFile)));
				AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
						SAMPLE_RATE_IN_HZ, 
						AudioFormat.CHANNEL_CONFIGURATION_MONO,
						AudioFormat.ENCODING_PCM_16BIT,
						bufferSize,
						AudioTrack.MODE_STREAM);
				//开始播放
				track.setStereoVolume(1.0f, 1.0f);
				track.play();
				while(isPlaying && dis.available()>0){
					
					int i = 0;
					while(dis.available()>0 &&i<buffer.length){
						Log.v("Play", "isPlaying");
						buffer[i] = dis.readByte();
						i++;
					}
					track.write(buffer, 0, buffer.length);
				}
				//播放结束
				track.stop();
				dis.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
	}
}

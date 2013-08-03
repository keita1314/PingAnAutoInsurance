package com.keita.pinganautoinsurance;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.keita.painganautoinsurance.entity.TextImage;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordListActivity extends Activity {
	private ListView listView = null;
	private ArrayList<File> list = null;
	private RecordAdapter adapter = null;
	private SimpleDateFormat dateformat = null;
	
	/* 音频文件 */
	private AudioTrack track = null;
	private File recAudioFile = null;
	private static int SAMPLE_RATE_IN_HZ = 8000;
	boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
		ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		previous_button =(ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RecordListActivity.this.finish();
			}
			
		});
		list = new ArrayList<File>();
		listView = (ListView) findViewById(R.id.record_list);
		File fileDirectory = new File("/mnt/sdcard//PingAn/record");
		// 格式化时间
		dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (fileDirectory.exists()) {
			File[] childFile = fileDirectory.listFiles();
			for (File f : childFile) {
				list.add(f);

			}
		} else
			Toast.makeText(this, "没有资源", Toast.LENGTH_SHORT).show();
		adapter = new RecordAdapter(this, list);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// 定义adapter
	class RecordAdapter extends BaseAdapter {
		private Activity context;
		private List<File> list;

		public RecordAdapter(Activity context, List<File> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = context.getLayoutInflater();
			View itemView = inflater.inflate(R.layout.record_list_item, null);
			File currentFile = list.get(position);
			TextView recordNameTv = (TextView) itemView
					.findViewById(R.id.record_name);
			TextView recordDateTv = (TextView) itemView
					.findViewById(R.id.record_date);
			Button play_Btn = (Button)itemView.findViewById(R.id.play_btn);
			Button stop_Btn = (Button)itemView.findViewById(R.id.stop_btn);
			
			recordNameTv.setText(currentFile.getName());
			recordDateTv.setText(dateformat.format(new Date(currentFile
					.lastModified())));
			
			final String filePath = currentFile.getAbsolutePath();
			//播放
			play_Btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new PlayAsyncTask().execute(filePath);
				}
				
			});
			stop_Btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isPlaying = false;
				}
				
			});
				
			
			return itemView;
		}
	}
	// 异步播放
		class PlayAsyncTask extends AsyncTask<String, Void, Void> {

			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				recAudioFile = new File(params[0]);
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
				
			}
			
		}
}

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
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
/*
 * 录音库页面
 */
public class RecordListActivity extends ActionBarActivity {
	private ListView listView = null;
	private ArrayList<File> list = null;
	private RecordAdapter adapter = null;
	private SimpleDateFormat dateformat = null;
	
	private ActionBar actionBar = null;

	/* 音频文件 */
	private MediaRecorder mr;
	private MediaPlayer mp;
	private File recAudioFile = null;
	boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
		// 设置标题栏
		setTopBar();
		list = new ArrayList<File>();
		listView = (ListView) findViewById(R.id.record_list);
		File fileDirectory = new File("/mnt/sdcard//PingAn/record");
		// 格式化时间
		dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (fileDirectory.exists()) {
			File[] childFile = fileDirectory.listFiles();
			//按最近时间插入
			for (int i = (childFile.length - 1); i >= 0; i--) {
				list.add(childFile[i]);

			}
		}
		if (list.size() == 0)
			Toast.makeText(this, "没有资源", Toast.LENGTH_SHORT).show();
		adapter = new RecordAdapter(this, list);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mp != null) {
			mp.reset();
			mp.release();
			mp = null;
		}
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
			Button play_Btn = (Button) itemView.findViewById(R.id.play_btn);
			Button stop_Btn = (Button) itemView.findViewById(R.id.stop_btn);

			recordNameTv.setText(currentFile.getName());
			recordDateTv.setText(dateformat.format(new Date(currentFile
					.lastModified())));

			final String filePath = currentFile.getAbsolutePath();
			// 播放
			play_Btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new PlayAsyncTask().execute(filePath);
				}

			});
			stop_Btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mp != null)
						mp.stop();
				}

			});

			return itemView;
		}
	}

	// 标题栏退出按钮
	public void setTopBar() {

		/*ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("录音库");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RecordListActivity.this.finish();
			}

		});*/
		actionBar = getSupportActionBar();
		actionBar.show();
		actionBar.setTitle("录音库");
		actionBar.setDisplayHomeAsUpEnabled(true);
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

		}

	}
}

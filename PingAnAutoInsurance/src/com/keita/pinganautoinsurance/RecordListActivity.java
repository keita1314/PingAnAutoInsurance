package com.keita.pinganautoinsurance;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.keita.painganautoinsurance.entity.TextImage;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordListActivity extends Activity {
	private ListView listView = null;
	private ArrayList<File> list = null;
	private RecordAdapter adapter = null;
	private SimpleDateFormat dateformat = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
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
			Toast.makeText(this, "没有资源", Toast.LENGTH_SHORT);
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
			recordNameTv.setText(currentFile.getName());
			recordDateTv.setText(dateformat.format(new Date(currentFile
					.lastModified())));
			return itemView;
		}
	}
}

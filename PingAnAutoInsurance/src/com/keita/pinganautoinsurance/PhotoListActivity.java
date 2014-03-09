package com.keita.pinganautoinsurance;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.keita.painganautoinsurance.entity.TextImage;
import com.keita.pinganautoinsurance.database.DBHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/*
 * 照片库页面
 */
public class PhotoListActivity extends ActionBarActivity {
	private ListView listView = null;
	private TextImage textImage = null;
	private ArrayList<TextImage> list = null;
	private TextImageAdapter adapter = null;
	private Bitmap bitmap = null;
	private ProgressDialog dialog = null;
	boolean isLoaded = false;
	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;

	private int perPageNum = 6;
	private int start = 0;
	private int end = 0;
	
	private ActionBar actionBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_list);
		// 标题栏设置
		setTopBar();
		listView = (ListView) findViewById(R.id.photo_list);
		list = new ArrayList<TextImage>();
		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getWritableDatabase();
		end = start + perPageNum;
		getDataFromDataBase(start, end);
		adapter = new TextImageAdapter(this, list);
		listView.setAdapter(adapter);
		// 照片列表的监听
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(PhotoListActivity.this, PhotoViewActivity.class);
				intent.putExtra("imgId", list.get(position).getImageId());
				startActivity(intent);
			}

		});

	}

	public void getDataFromDataBase(int start, int end) {
		Cursor cur = dbHelper.query(dataBase, "text_image_table", null, null,
				null, null, null, "img_date desc");
		if (cur.moveToPosition(start)) {
		
			if (end > cur.getCount()){
				end = cur.getCount();
				
			}
			do {
				textImage = new TextImage();
				String imageId = cur.getString(cur
						.getColumnIndex("text_img_id"));
				String imageText = cur
						.getString(cur.getColumnIndex("img_text"));
				String imageDate = cur
						.getString(cur.getColumnIndex("img_date"));
				String imagePath = cur
						.getString(cur.getColumnIndex("img_path"));
				
				try {
					//对图片进行处理
					
					bitmap = BitMapUtil.getSmallBitmap(imagePath);
				} catch (OutOfMemoryError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				textImage.setImageId(imageId);
				textImage.setImage(bitmap);
				textImage.setText(imageText);
				textImage.setImageDate(imageDate);
				list.add(textImage);
				start++;
			} while (cur.moveToNext() && start < end);
			cur.close();

		} else
			Toast.makeText(PhotoListActivity.this, "没有更多的资源",
					Toast.LENGTH_SHORT).show();

	}

	// 定义adapter
	class TextImageAdapter extends BaseAdapter {
		private Activity context;
		private List<TextImage> list;

		public TextImageAdapter(Activity context, List<TextImage> list) {
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
			View itemView = inflater
					.inflate(R.layout.photo_listview_item, null);
			TextImage currentTextImage = list.get(position);

			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.list_item_image);
			TextView textView = (TextView) itemView
					.findViewById(R.id.list_item_text);
			TextView textView_time = (TextView) itemView
					.findViewById(R.id.list_item_text_time);
			// 设置item的内容
			imageView.setImageBitmap(currentTextImage.getImage());
			textView.setText(currentTextImage.getText());
			textView_time.setText(currentTextImage.getImageDate());
			return itemView;
		}

	}

	// 标题栏退出按钮
	public void setTopBar() {

		/*ImageButton previous_button = null;
		ImageButton load_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("照片库");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		load_button = (ImageButton) view.findViewById(R.id.top_bar_index);
		load_button.setVisibility(View.VISIBLE);
		load_button.setImageResource(R.drawable.update_btn_normal);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PhotoListActivity.this.finish();
			}

		});
		// 加载数据
		load_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(PhotoListActivity.this, "加载更多数据",
						Toast.LENGTH_SHORT).show();
				start = end;
				end += perPageNum;
				getDataFromDataBase(start, end);
				adapter.notifyDataSetChanged();
				//移动到最后
				listView.setSelection(adapter.getCount());
			}

		});*/
		actionBar = getSupportActionBar();
		actionBar.show();
		actionBar.setTitle("照片库");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dataBase.close();
		dbHelper.close();
		if (bitmap != null)
			bitmap.recycle();
	}

}

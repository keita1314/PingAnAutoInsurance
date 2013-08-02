package com.keita.pinganautoinsurance;

/*
 * 照片库页面
 */
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.keita.painganautoinsurance.entity.TextImage;
import com.keita.pinganautoinsurance.database.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PhotoListActivity extends Activity {
	private ListView listView = null;
	private TextImage textImage = null;
	private ArrayList<TextImage> list = null;
	private TextImageAdapter adapter = null;
	private Bitmap bitmap = null;

	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_list);
		listView = (ListView)findViewById(R.id.photo_list);
		list = new ArrayList<TextImage>();
		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getWritableDatabase();
		 getDataFromDataBase();
		adapter = new TextImageAdapter(this,list);
		listView.setAdapter(adapter);
		// 照片列表的监听
			listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent();
						intent.setClass(PhotoListActivity.this,
								PhotoViewActivity.class);
						intent.putExtra("imgId", list.get(position).getImageId());
						startActivity(intent);
					}

				});

	}

	public void getDataFromDataBase() {
		Cursor cur = dbHelper.query(dataBase, "text_image_table", null, null,
				null, null, null, null);
		if(cur.moveToFirst()){
		do{
			textImage = new TextImage();
			String imageId =  cur.getString(cur.getColumnIndex("id"));
			String imageText = cur.getString(cur.getColumnIndex("img_text"));
			String imageDate =  cur.getString(cur.getColumnIndex("img_date"));
			String imagePath =cur.getString(cur.getColumnIndex("img_path"));
			//根据路径得到图片
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			InputStream is = null;
			Bitmap source = null;
			
			
			try {
				is = new FileInputStream(imagePath);
				source = BitmapFactory.decodeStream(is, null, options);
				bitmap = ThumbnailUtils.extractThumbnail(source, 90,
						90);
				if(source != null && !source.isRecycled())
				source.recycle();
				is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textImage.setImageId(imageId);
			textImage.setImage(bitmap);
			textImage.setText(imageText);
			textImage.setImageDate(imageDate);
			list.add(textImage);
			
		}while(cur.moveToNext());
		cur.close();
	}
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(bitmap!=null)
			bitmap.recycle();
	}
	
}

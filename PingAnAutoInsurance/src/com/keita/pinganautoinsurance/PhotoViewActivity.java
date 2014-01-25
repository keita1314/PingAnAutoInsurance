package com.keita.pinganautoinsurance;

import java.io.FileInputStream;
import java.io.InputStream;

import com.keita.pinganautoinsurance.database.DBHelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 查看照片页面
 */
public class PhotoViewActivity extends Activity {

	private ImageView imageView = null;
	private TextView textView = null;
	private String imgId = null;
	private Bitmap bitmap = null;
	String imagePath = null;
	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view);
		// 设置标题栏
		setTopBar();
		imageView = (ImageView) findViewById(R.id.photo);
		textView = (TextView) findViewById(R.id.comment_text);
		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getWritableDatabase();
		imgId = this.getIntent().getStringExtra("imgId");

		getDataFromDataBase();
	}

	// 从数据库中取出图片
	public void getDataFromDataBase() {
		Cursor cur = dbHelper.query(dataBase, "text_image_table", null,
				"text_img_id = ?", new String[] { imgId }, null, null, null);
		if (cur.moveToFirst()) {
			String imagePath = cur.getString(cur.getColumnIndex("img_path"));
			try {

				System.gc();
				bitmap = BitMapUtil.getMiddleBitmap(imagePath);
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imageView.setImageBitmap(bitmap);
			String iamgeText = cur.getString(cur.getColumnIndex("img_text"));
			textView.setText(iamgeText);
		}
		if(cur !=null)
			cur.close();
	}

	// 标题栏退出按钮
	public void setTopBar() {

		ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("查看照片");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PhotoViewActivity.this.finish();
			}

		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dataBase.close();
		dbHelper.close();
		if (bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
		System.gc();
	}

}

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
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoViewActivity extends Activity {

	private ImageView imageView = null;
	private TextView textView = null;
	private String imgId = null;
	private Bitmap bitmap = null;
	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view);
		imageView = (ImageView) findViewById(R.id.photo);
		textView = (TextView) findViewById(R.id.comment_text);
		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getWritableDatabase();
		imgId = this.getIntent().getStringExtra("imgId");
		
		getDataFromDataBase();
	}

	public void getDataFromDataBase() {
		Cursor cur = dbHelper.query(dataBase, "text_image_table", null,
				"id = ?", new String[] { imgId }, null, null, null);
		if(cur.moveToFirst()){
			String imagePath = cur.getString(cur.getColumnIndex("img_path"));
			InputStream is = null;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			try {
				 is = new FileInputStream(imagePath);
				 bitmap = BitmapFactory.decodeStream(is,null,options);
				 is.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//bitmap = BitmapFactory.decodeFile(imagePath);
			imageView.setImageBitmap(bitmap);
			String iamgeText = cur.getString(cur.getColumnIndex("img_text"));
			textView.setText(iamgeText);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dataBase.close();
		dbHelper.close();
		if(bitmap != null)
			bitmap.recycle();
	}

}

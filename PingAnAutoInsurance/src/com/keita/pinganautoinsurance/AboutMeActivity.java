package com.keita.pinganautoinsurance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/*
 * 证件页面
 */
public class AboutMeActivity extends ActionBarActivity {
	
	TextView tv = null;
	TessBaseAPI baseAPI = null;
	String text = null;
	ImageView iv = null;
	Bitmap bitmap = null;
	ActionBar actionBar = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_me);
		setTopBar();
		tv = (TextView)findViewById(R.id.result_text);
		iv = (ImageView) findViewById(R.id.img_reco);
		
		String image_url = "/mnt/sdcard/PingAn/test/test2.jpg";
		File file = new File(image_url);
		InputStream is = null;
		try {
			 is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=2;
		
		if(is != null){
			Log.v("bitmap", "bitmap");
		bitmap = BitmapFactory.decodeStream(is,null,options);
		iv.setImageBitmap(bitmap);
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ORCTask orcTask = new ORCTask();
		orcTask.execute(bitmap);
		
		
	}
	// 标题栏退出按钮
		public void setTopBar() {

			/*ImageButton previous_button = null;
			View view = findViewById(R.id.top_bar);
			TextView title = (TextView) view.findViewById(R.id.top_title);
			title.setText("关于作者");
			previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
			previous_button.setOnClickListener(new OnClickListener(){
			
				
			
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AboutMeActivity.this.finish();
				}
				
			});*/
			actionBar = getSupportActionBar();
			actionBar.setTitle("证件识别");
			actionBar.show();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		class ORCTask extends AsyncTask<Bitmap,Void,Void>{
			
			@Override
			protected Void doInBackground(Bitmap... params) {
				// TODO Auto-generated method stub
				baseAPI = new TessBaseAPI();
				baseAPI.init("/mnt/sdcard/PingAn/", "chi_sim");
				
				baseAPI.setImage(gray2Binary(params[0]));
				text = baseAPI.getUTF8Text();
				Log.v("orc", "bg finish");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				tv.setText(text);
				if(baseAPI != null)
					baseAPI.end();
				Log.v("orc", "post finish");
			}

		}
		public Bitmap bitmap2Gray(Bitmap bmSrc) {
			// 得到图片的长和宽
			int width = bmSrc.getWidth();
			int height = bmSrc.getHeight();
			// 创建目标灰度图像
			Bitmap bmpGray = null;
			bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			// 创建画布
			Canvas c = new Canvas(bmpGray);
			Paint paint = new Paint();
			ColorMatrix cm = new ColorMatrix();
			cm.setSaturation(0);
			ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
			paint.setColorFilter(f);
			c.drawBitmap(bmSrc, 0, 0, paint);
			return bmpGray;
		}
		
		// 该函数实现对图像进行二值化处理
		public Bitmap gray2Binary(Bitmap graymap) {
			//得到图形的宽度和长度
			int width = graymap.getWidth();
			int height = graymap.getHeight();
			//创建二值化图像
			Bitmap binarymap = null;
			binarymap = graymap.copy(Config.ARGB_8888, true);
			//依次循环，对图像的像素进行处理
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					//得到当前像素的值
					int col = binarymap.getPixel(i, j);
					//得到alpha通道的值
					int alpha = col & 0xFF000000;
					//得到图像的像素RGB的值
					int red = (col & 0x00FF0000) >> 16;
					int green = (col & 0x0000FF00) >> 8;
					int blue = (col & 0x000000FF);
					// 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
					int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
					//对图像进行二值化处理
					if (gray <= 95) {
						gray = 0;
					} else {
						gray = 255;
					}
					// 新的ARGB
					int newColor = alpha | (gray << 16) | (gray << 8) | gray;
					//设置新图像的当前像素值
					binarymap.setPixel(i, j, newColor);
				}
			}
			return binarymap;
		}
}

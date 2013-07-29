package com.keita.pinganautoinsurance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.keita.painganautoinsurance.entity.TextImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoCommentActivity extends Activity {
	private ImageView imageView = null;
	private TextImage textImage = null;
	private EditText editText = null;
	private TextView textView = null;
	private Button comment_btn = null;
	private Bitmap bitmap = null;
	private String comment = null;
	private Intent resultIntent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_comment);
		imageView = (ImageView) findViewById(R.id.photo_add_comment);
		editText = (EditText)findViewById(R.id.edittext_add_comment);
		textView = (TextView)findViewById(R.id.photo_comment_text);
		comment_btn = (Button)findViewById(R.id.comment_btn);
		Intent intent = this.getIntent();
		textImage = (TextImage) intent.getParcelableExtra("TextImage");
		String imagePath = null;
		if (textImage != null)
			imagePath = textImage.getImagePath();
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
		textView.setText(textImage.getText());
		resultIntent = new Intent();
		comment_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(editText.getText().toString() != null){
					textView.setText(editText.getText().toString());
					//textImage.setText(editText.getText().toString());
					editText.setText("");
					comment = (String)textView.getText();
					resultIntent.putExtra("comment", comment);
				}
				
			}
			
		});
		//默认情况 没有进行评论
		comment = (String)textView.getText();
		resultIntent.putExtra("comment", comment);
		resultIntent.setClass(this, InsurancePhotoActivity.class);
		setResult(2,resultIntent);
	
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(bitmap!=null)
			bitmap.recycle();
	}

}

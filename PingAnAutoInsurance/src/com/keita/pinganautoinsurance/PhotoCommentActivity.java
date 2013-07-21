package com.keita.pinganautoinsurance;

import com.keita.painganautoinsurance.entity.TextImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class PhotoCommentActivity extends Activity {
	private ImageView imageView = null;
	private TextImage textImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_comment);
		imageView = (ImageView) findViewById(R.id.photo_add_comment);
		Intent intent = this.getIntent();
		textImage = (TextImage) intent.getParcelableExtra("TextImage");
		String imagePath = null;
		if (textImage != null)
			imagePath = textImage.getImagePath();
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		imageView.setImageBitmap(bitmap);
		

	}

}

package com.keita.pinganautoinsurance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class InsurancePhotoActivity extends Activity {
	private Button camera_btn = null;
	private String SDPath = null;
	private File photoDir = null;
	private boolean isSDExist = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_photo);
		camera_btn = (Button) findViewById(R.id.camera_btn);
		/* 检测SD卡存在 */
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			Log.v("SD", "exist");
			SDPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			Log.v("SDPath", SDPath);
			String fileName = "/PingAn/image";
			photoDir = new File(SDPath + "/PingAn/image");
			photoDir.mkdirs();
			isSDExist = true;
		}
		else
			isSDExist =false;
		camera_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
			}
			
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			
			if (isSDExist){
				
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap)bundle.get("data");
				File image = new File(photoDir.getAbsoluteFile()+"/1.jpg");
				FileOutputStream fos = null;
				try {
					 fos = new FileOutputStream(image);
					 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						fos.flush();
						fos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				ImageView iv = (ImageView)findViewById(R.id.image_view);
				iv.setImageBitmap(bitmap);
			}else{
				Toast.makeText(this, "SD卡不存在", Toast.LENGTH_SHORT);
			}
		}
	}
	// 判断文件是否存在
	public boolean isFileExist(String fileName) {
		File file = new File(SDPath + fileName);
		return file.exists();
	}
}

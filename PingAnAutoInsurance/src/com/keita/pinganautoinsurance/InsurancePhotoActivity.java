package com.keita.pinganautoinsurance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keita.painganautoinsurance.entity.TextImage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class InsurancePhotoActivity extends Activity {
	private Button camera_btn = null;
	private String SDPath = null;
	private ListView photo_listview = null;
	private File photoDir = null;
	private boolean isSDExist = false;
	private TextImageAdapter adapter = null;
	private List<TextImage> textImage_list = null;
	private String photo_abs_dir = null;
	private int TAKE_PICTURE = 1;
	private Uri imageUri = null;
	private int currentPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_photo);
		camera_btn = (Button) findViewById(R.id.camera_btn);
		photo_listview = (ListView) findViewById(R.id.photo_list);

		textImage_list = new ArrayList<TextImage>();
		adapter = new TextImageAdapter(this, textImage_list);

		// 检测SD卡存在
		isSDExist();
		// 照片列表的监听
		photo_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				currentPosition = position;
				Intent intent = new Intent();
				intent.setClass(InsurancePhotoActivity.this,
						PhotoCommentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("TextImage", textImage_list.get(position));
				intent.putExtras(bundle);
				startActivityForResult(intent, 2);
			}

		});
		// 拍照按钮的监听
		camera_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Date now = new Date();
				String photo_name = Long.toString(now.getTime());
				photo_abs_dir = photoDir.getAbsoluteFile() + "/" + photo_name
						+ ".jpg";
				imageUri = Uri.fromFile(new File(photo_abs_dir));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, 1);
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {

			if (isSDExist) {
				TextImage textImage = new TextImage();
				/*
				 * Bundle bundle = data.getExtras(); Bitmap bitmap = (Bitmap)
				 * bundle.get("data"); Date now = new Date();
				 * 
				 * String photo_name = Long.toString(now.getTime()); File image
				 * = new File(photoDir.getAbsoluteFile() + "/" + photo_name +
				 * ".jpg"); FileOutputStream fos = null; try { fos = new
				 * FileOutputStream(image);
				 * bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); }
				 * catch (Exception e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); } finally { try { fos.flush();
				 * fos.close(); } catch (Exception e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); }
				 * 
				 * }
				 */

				Bitmap bitmap = BitmapFactory.decodeFile(photo_abs_dir);
				// 解决手竖屏抓取照片会翻转90度的问题
				int result = 0;
				try {
					ExifInterface exif = new ExifInterface(photo_abs_dir);
					result = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_UNDEFINED);
					int rotate = 0;
					switch (result) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						rotate = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						rotate = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						rotate = 270;
						break;
					default:
						break;
					}
					Log.v("orientation", "" + rotate);
					if (rotate > 0) {
						Matrix martix = new Matrix();
						martix.setRotate(rotate);
						Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								bitmap.getWidth(), bitmap.getHeight(), martix,
								true);
						if (rotateBitmap != null) {
							bitmap.recycle();
							bitmap = rotateBitmap;
						}
						// 重新把bitmap写到本地
						File file = new File(photo_abs_dir);
						FileOutputStream fos = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						fos.flush();
						fos.close();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Bitmap newBitmap = ThumbnailUtils.extractThumbnail(bitmap, 120,
						120);
				bitmap.recycle();
				textImage.setImage(newBitmap);

				textImage.setImagePath(photo_abs_dir);
				textImage_list.add(textImage);
				photo_listview.setAdapter(adapter);

			} else {
				Toast.makeText(this, "SD卡不存在", Toast.LENGTH_SHORT);
			}
		}
		// 评论后的返回处理
		if (resultCode == 2) {
			Bundle bundle = data.getExtras();
			String comment = bundle.getString("comment");
			if (comment == null)
				Log.v("comment", "comment is null");
			textImage_list.get(currentPosition).setText(comment);
			adapter.notifyDataSetChanged();
		}

	}

	// 检测SD卡存在
	public void isSDExist() {
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
		} else
			isSDExist = false;

	}

	/*
	 * // 判断文件是否存在 public boolean isFileExist(String fileName) { File file = new
	 * File(SDPath + fileName); return file.exists(); }
	 */

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
			textView_time.setText("X年X月X日X时X分");
			return itemView;
		}

	}
}

package com.keita.pinganautoinsurance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.keita.painganautoinsurance.entity.TextImage;
import com.keita.pinganautoinsurance.database.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnLongClickListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class InsurancePhotoActivity extends Activity {

	private Button cameraBtn = null;
	private Button continueBtn = null;
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
	private int photoSumNum = 0;
	private String insurance_photo_id = null;
	private String photo_name = null;
	private String location = "";
	//数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;
	
	private SimpleDateFormat dateformat = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_photo);
		ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		previous_button =(ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InsurancePhotoActivity.this.finish();
			}
			
		});
		cameraBtn = (Button) findViewById(R.id.camera_btn);
		photo_listview = (ListView) findViewById(R.id.photo_list);
		continueBtn = (Button) findViewById(R.id.continue_btn);
		textImage_list = new ArrayList<TextImage>();
		dbHelper = new DBHelper(this);
   	 	dataBase = dbHelper.getWritableDatabase();
		adapter = new TextImageAdapter(this, textImage_list);
		location = this.getIntent().getStringExtra("location");
		//格式化时间
		dateformat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
		// 照片列表长按事件的监听 长按可删除当前照片
		photo_listview
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long id) {
						// TODO Auto-generated method stub
						final int pos = position;
						System.out.println(position);
						new AlertDialog.Builder(InsurancePhotoActivity.this)
								.setTitle("删除照片").setMessage("你确定要删除当前照片")
								.setNegativeButton("取消", null)
								.setPositiveButton("确定", new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										File file = new File(textImage_list
												.get(pos).getImagePath());
										if (file.exists())
											file.delete();
										//从数据库中删去照片
										String[] args ={textImage_list.get(pos).getImageId()};
										dbHelper.deleteData(dataBase, "id=?", args, "text_image_table");
										
										textImage_list.get(pos).recycle();
										textImage_list.remove(pos);
										photoSumNum--;
										adapter.notifyDataSetChanged();

									}

								}).show();
						return false;
					}

				});
		// 拍照按钮的监听
		cameraBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (photoSumNum <= 5) {
					photoSumNum++;
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					Date now = new Date();
					photo_name = Long.toString(now.getTime());
					photo_abs_dir = photoDir.getAbsoluteFile() + "/IMG_"
							+ photo_name + ".jpg";
					imageUri = Uri.fromFile(new File(photo_abs_dir));
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, 1);
				} else
					Toast.makeText(InsurancePhotoActivity.this, "最多只能存储5张图片",
							Toast.LENGTH_SHORT).show();
			}

		});
		// 下一步按钮的监听
		continueBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//把所有的照片插入一个当数据库的保单图片集表中
				if(textImage_list.size() >0){
				ContentValues cv  = new ContentValues();
				for(int i=0;i<textImage_list.size();i++){
					String imgId = "img" +(i+1)+"_id";
					cv.put(imgId, textImage_list.get(i).getImageId());
				}
				dbHelper.insertData(dataBase, cv, "insurance_photo_table");
				String[] id = {"id"};
				Cursor cur = dbHelper.queryByCol(dataBase, "insurance_photo_table",id);
				if(cur.moveToLast()) {
						cur.moveToLast();
						insurance_photo_id = cur.getString(cur.getColumnIndex("id"));
						System.out.println("照片ID"+insurance_photo_id);
				
				}
				Intent intent = new Intent();
				intent.putExtra("location", location);
				intent.putExtra("insurance_photo_id", insurance_photo_id);
				intent.setClass(InsurancePhotoActivity.this,
						InsuranceTextActivity.class);
				startActivity(intent);
				InsurancePhotoActivity.this.finish();
				}else
					Toast.makeText(InsurancePhotoActivity.this, "请拍摄最少一张照片", Toast.LENGTH_SHORT).show();

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
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				InputStream is = null;
				Bitmap bitmap = null;
				try {
					is = new FileInputStream(photo_abs_dir);
					bitmap = BitmapFactory.decodeStream(is, null, options);
					is.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 解决手竖屏抓取照片会翻转90度的问题
				int result = 0;
				try {
					int rotate = 0;
					ExifInterface exif = new ExifInterface(photo_abs_dir);
					result = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_UNDEFINED);

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
				bitmap = null;
				textImage.setImageId(photo_name);
				textImage.setImage(newBitmap);
				textImage.setImagePath(photo_abs_dir);
				
				textImage.setImageDate(dateformat.format(new Date()));
				textImage_list.add(textImage);
				//插入数据库的表中
				ContentValues cv = new ContentValues();
				cv.put("id", textImage.getImageId());
				cv.put("img_path", textImage.getImagePath());
				cv.put("img_text", textImage.getText());
				cv.put("img_date", textImage.getImageDate());
				dbHelper.insertData(dataBase, cv, "text_image_table");
				photo_listview.setAdapter(adapter);

			} else {
				Toast.makeText(this, "SD卡不存在", Toast.LENGTH_SHORT).show();
			}
		}
		// 评论后的返回处理
		if (resultCode == 2) {
			Bundle bundle = data.getExtras();
			String comment = bundle.getString("comment");
			if (comment == null)
				Log.v("comment", "comment is null");
			textImage_list.get(currentPosition).setText(comment);
			String[] id = {textImage_list.get(currentPosition).getImageId()};
			ContentValues cv = new ContentValues();
			cv.put("img_text", comment);
			dbHelper.updateData(dataBase, "text_image_table", cv, "id = ?", id);
			adapter.notifyDataSetChanged();
			
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for (TextImage ti : textImage_list)
			ti.recycle();
		dataBase.close();
		dbHelper.close();
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
}

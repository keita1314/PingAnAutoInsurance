package com.keita.pinganautoinsurance;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.keita.painganautoinsurance.entity.InsurancePolicy;
import com.keita.pinganautoinsurance.database.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InsuranceListActivity extends Activity {
	private ListView listView = null;
	private ListAdapter listAdapter = null;
	private ArrayList<InsurancePolicy> insurancePolicy_list = null;
	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;
	
	private int page_start = 0;
	private int page_end = 5;
	
	Bitmap bitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_list);
		listView = (ListView) findViewById(R.id.insurance_list);
		insurancePolicy_list = new ArrayList<InsurancePolicy>();
		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getWritableDatabase();
		//列表要显示图片 避免OOM 所以于数据库中的保单分页显示 每页面只显示5个表单
		loadListView(page_start,page_end);
		listAdapter = new ListAdapter(this,insurancePolicy_list);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("insurancePolicyId",insurancePolicy_list.get(position).getId());
				intent.putExtra("date", insurancePolicy_list.get(position).getDate());
				intent.putExtra("location", insurancePolicy_list.get(position).getLocation());
				intent.setClass(InsuranceListActivity.this, InsuranceViewActivity.class);
				startActivity(intent);
				
			}
			
		});
	}
	//加载列表
	public void loadListView(int start,int end){
		Cursor cur = dbHelper.query(dataBase, "insurance_policy_table",null,null,null,null,null,"date desc");
		int i = start;
		if(cur.moveToPosition(start)){
			do{
				
				Log.v("load", "loadlistview");
				InsurancePolicy ip = new InsurancePolicy();
				ip.setId(cur.getString(cur.getColumnIndex("policy_id")));
				ip.setInsurancePhotoId(cur.getString(cur.getColumnIndex("photos_id")));
				System.out.println("loc"+cur.getString(cur.getColumnIndex("location")));
				ip.setLocation(cur.getString(cur.getColumnIndex("location")));
				ip.setDate(cur.getString(cur.getColumnIndex("date")));
				insurancePolicy_list.add(ip);
			}while(cur.moveToNext() && i < end);
		}
		else
		Toast.makeText(InsuranceListActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
	} 
	// 定义adapter
	class ListAdapter extends BaseAdapter {
		private Activity context;
		private List<InsurancePolicy> list;

		public ListAdapter(Activity context, List<InsurancePolicy> list) {
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
					.inflate(R.layout.insurance_list_item, null);
			InsurancePolicy currentInsurancePolicy = list.get(position);

			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.insurance_item_img);
			TextView textViewLoaction = (TextView) itemView
					.findViewById(R.id.insurance_item_location);
			TextView textViewDate = (TextView) itemView
					.findViewById(R.id.insurance_item_date);
			// 设置item的内容
			bitmap = getImage(currentInsurancePolicy.getInsurancePhotoId());
			if(bitmap != null)
				imageView.setImageBitmap(bitmap);
			textViewLoaction.setText(currentInsurancePolicy.getLocation());
			textViewDate.setText(currentInsurancePolicy.getDate());
			return itemView;
		}

	}
	//根据保单Id获得第一张图作为缩略图
	public Bitmap getImage(String photoId){
		
		Cursor cur = dbHelper.query(dataBase, "insurance_photo_table", new String[]{"img1_id"},"id=?",new String[]{photoId},null,null,null);
		String img_id = null;
		if(cur.moveToFirst())
			img_id = cur.getString(cur.getColumnIndex("img1_id"));
		cur.close();
		//根据Id查询得到图片
		
		cur = dbHelper.query(dataBase, "text_image_table", new String[]{"img_path"},"id=?",new String[]{img_id},null,null,null);
		String img_path =null;
		if(cur.moveToFirst())
			img_path= cur.getString(cur.getColumnIndex("img_path"));
		
		//根据路径得到图片
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		InputStream is = null;
		Bitmap source = null;
		;
		
		try {
			is = new FileInputStream(img_path);
			source = BitmapFactory.decodeStream(is, null, options);
			bitmap = ThumbnailUtils.extractThumbnail(source, 100,
					100);
			if(source != null && !source.isRecycled())
			source.recycle();
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dataBase.close();
		dbHelper.close();
		if(bitmap != null )
			bitmap.recycle();
	}
	
}

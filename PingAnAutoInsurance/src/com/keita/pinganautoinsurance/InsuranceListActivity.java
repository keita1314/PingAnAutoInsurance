package com.keita.pinganautoinsurance;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.keita.painganautoinsurance.entity.CaseListItem;
import com.keita.pinganautoinsurance.database.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/*
 * 案件列表页面
 */
public class InsuranceListActivity extends ActionBarActivity {
	private ListView listView = null;
	private EditText searchEditText = null;
	private ListAdapter listAdapter = null;
	private ListAdapter serachAdapter = null;
	private ArrayList<CaseListItem> caseList = null;
	// private ArrayList<String> driver = null;
	private ArrayList<CaseListItem> searchCaseList = null;
	private ActionBar actionBar = null;
	
	// 数据库操作
	DBHelper dbHelper = null;
	SQLiteDatabase dataBase = null;
	private int IMG_COUNT = 6;
	String search = "";
	Bitmap bitmap = null;
	//分页 每次只显示6个案件
	private int perPageNum = 6;
	private int start = 0;
	private int end = 0;
	private boolean isSearched = false;
	private CaseListItem curCaseListItem = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_list);
		searchEditText = (EditText) findViewById(R.id.SearchEditText);
		setTopBar();
		listView = (ListView) findViewById(R.id.insurance_list);
		caseList = new ArrayList<CaseListItem>();
		searchCaseList = new ArrayList<CaseListItem>();
		dbHelper = new DBHelper(this);
		dataBase = dbHelper.getWritableDatabase();
		// 首次加载数据
		end = end + perPageNum;
		loadListView(start, end);
		listAdapter = new ListAdapter(this, caseList);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//判断是否搜索
				if (isSearched == false) {
					curCaseListItem = caseList.get(position);
				} else
					curCaseListItem = searchCaseList.get(position);

				Intent intent = new Intent();
				intent.putExtra("insuranceCaseId", curCaseListItem.getId());
				intent.putExtra("date", curCaseListItem.getDate());
				intent.putExtra("location", curCaseListItem.getLocation());
				intent.setClass(InsuranceListActivity.this,
						InsuranceViewActivity.class);
				startActivity(intent);

			}

		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				if (isSearched == false) {
					curCaseListItem = caseList.get(position);
				} else
					curCaseListItem = searchCaseList.get(position);

				final int pos = position;
				new AlertDialog.Builder(InsuranceListActivity.this)
						.setTitle("删除案件")
						.setMessage("你确定要删除当前案件")
						.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// 删除案件
										deleteInsurance(
												curCaseListItem.getId(),
												curCaseListItem.getTextId(),
												curCaseListItem
														.getInsurancePhotoId());

										// driver.remove(pos);
										if (isSearched == false) {
											listAdapter.notifyDataSetChanged();
											caseList.remove(pos);
										} else {
											serachAdapter
													.notifyDataSetChanged();
											searchCaseList.remove(pos);
										}

									}

								}).show();
				return false;
			}

		});
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable edit) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				search = searchEditText.getText().toString();
				findResult(search);

			}

		});
	}

	// 从数据库分页加载
	public void loadListView(int start, int end) {

		Cursor cur = dbHelper.query(dataBase, "insurance_case_table", null,
				null, null, null, null, "date desc");

		if (cur.moveToPosition(start)) {
			if (end > cur.getCount())
				end = cur.getCount();
			do {
				Log.v("load", "loadlistview");
				CaseListItem ic = new CaseListItem();
				ic.setId(cur.getString(cur.getColumnIndex("case_id")));
				ic.setInsurancePhotoId(cur.getString(cur
						.getColumnIndex("photos_id")));
				ic.setLocation(cur.getString(cur.getColumnIndex("location")));
				ic.setDate(cur.getString(cur.getColumnIndex("date")));
				ic.setTextId(cur.getString(cur.getColumnIndex("text_id")));
				// 找出驾驶员姓名
				Cursor cur_text = dbHelper.query(dataBase,
						"insurance_text_table", null, "text_id=?",
						new String[] { ic.getTextId() }, null, null, null);
				if (cur_text.moveToFirst()) {
					ic.setDriverName(cur_text.getString(cur_text
							.getColumnIndex("case_driver")));
				}
				cur_text.close();
				caseList.add(ic);
				start++;
			} while (cur.moveToNext() && start < end);

		} else
			Toast.makeText(InsuranceListActivity.this, "没有数据",
					Toast.LENGTH_SHORT).show();
		cur.close();
	}

	public void findResult(String search) {
		if (searchCaseList.size() != 0)
			searchCaseList.clear();
		if (!search.equals("")) {
			for (int i = 0; i < caseList.size(); i++) {
				String driverName = caseList.get(i).getDriverName();
				// 按照驾驶员姓名找
				if (driverName.contains(search)) {
					searchCaseList.add(caseList.get(i));
				}
				// 按地点搜索
				String location = caseList.get(i).getLocation();
				if (location.contains(search)) {
					searchCaseList.add(caseList.get(i));
				}

			}
			serachAdapter = new ListAdapter(this, searchCaseList);
			listView.setAdapter(serachAdapter);
			isSearched = true;
		} else {
			listView.setAdapter(listAdapter);
			isSearched = false;
		}

	}

	// 定义adapter
	class ListAdapter extends BaseAdapter {
		private Activity context;
		private List<CaseListItem> list;

		public ListAdapter(Activity context, List<CaseListItem> list) {
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
			CaseListItem currentCaseItem = list.get(position);
			// String driverStr = driver.get(position);
			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.insurance_item_img);
			TextView textViewDriver = (TextView) itemView
					.findViewById(R.id.insurance_driver);
			TextView textViewLoaction = (TextView) itemView
					.findViewById(R.id.insurance_item_location);
			TextView textViewDate = (TextView) itemView
					.findViewById(R.id.insurance_item_date);
			// 设置item的内容

			bitmap = getImage(currentCaseItem.getInsurancePhotoId());
			if (bitmap != null)
				imageView.setImageBitmap(bitmap);
			textViewLoaction.setText(currentCaseItem.getLocation());
			textViewDate.setText(currentCaseItem.getDate());
			textViewDriver.setText(currentCaseItem.getDriverName());
			return itemView;
		}

	}

	// 标题栏退出按钮
	public void setTopBar() {

		/*ImageButton previous_button = null;
		ImageButton load_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("案件列表");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		load_button = (ImageButton) view.findViewById(R.id.top_bar_index);
		load_button.setVisibility(View.VISIBLE);
		load_button.setImageResource(R.drawable.update_btn_normal);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InsuranceListActivity.this.finish();
			}

		});
		// 加载数据
		load_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(InsuranceListActivity.this, "加载更多数据",
						Toast.LENGTH_SHORT).show();
				start = end;
				end += perPageNum;
				loadListView(start, end);
				listAdapter.notifyDataSetChanged();
				listView.setSelection(listAdapter.getCount());
			}

		});*/
		actionBar = getSupportActionBar();
		actionBar.show();
		actionBar.setTitle("事故列表");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	// 根据保单Id获得第一张图作为缩略图
	public Bitmap getImage(String photoId) {

		Cursor cur = dbHelper.query(dataBase, "insurance_photo_table",
				new String[] { "img1_id" }, "photos_id=?",
				new String[] { photoId }, null, null, null);
		String img_id = null;
		if (cur.moveToFirst())
			img_id = cur.getString(cur.getColumnIndex("img1_id"));
		cur.close();
		// 根据Id查询得到图片
		String img_path = null;
		if (img_id != null) {
			cur = dbHelper.query(dataBase, "text_image_table",
					new String[] { "img_path" }, "text_img_id=?",
					new String[] { img_id }, null, null, null);

			if (cur.moveToFirst())
				img_path = cur.getString(cur.getColumnIndex("img_path"));
		}
	

		try {
			if (img_path != null) {
				//对图片进去处理
				
				bitmap = BitMapUtil.getSmallBitmap(img_path);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;

	}

	// 删除案件
	public void deleteInsurance(String case_id, String text_id, String photo_id) {
		Cursor cur = dbHelper.query(dataBase, "insurance_case_table", null,
				"case_id = ?", new String[] { case_id }, null, null, null);

		String record_path = null;
		if (cur.moveToFirst()) {
			// 删除录音
			record_path = cur.getString(cur.getColumnIndex("record_path"));
			if (record_path != null && !record_path.equals("")) {
				File file_record = new File(record_path);
				if (file_record.exists())
					file_record.delete();
			}
		}
		cur.close();
		// 删除照片
		cur = dbHelper.query(dataBase, "insurance_photo_table", null,
				"photos_id=?", new String[] { photo_id }, null, null, null);
		String img_id[] = new String[IMG_COUNT];
		String img_path[] = new String[IMG_COUNT];
		if (cur.moveToFirst()) {
			for (int i = 0; i < IMG_COUNT; i++) {
				img_id[i] = cur.getString(cur.getColumnIndex("img" + (i + 1)
						+ "_id"));
			}

		}
		cur.close();
		// 根据Id查询得到图片
		for (int i = 0; i < IMG_COUNT; i++) {
			if (img_id[i] != null) {
				cur = dbHelper.query(dataBase, "text_image_table",
						new String[] { "img_path" }, "text_img_id=?",
						new String[] { img_id[i] }, null, null, null);
			}
			if (cur.moveToFirst()) {
				img_path[i] = cur.getString(cur.getColumnIndex("img_path"));
				if (img_path[i] != null && !img_path[i].equals("")) {
					File file = new File(img_path[i]);
					if (file.exists())
						file.delete();
				}
			}
			// 从数据中删除具体每张照片
			dbHelper.deleteData(dataBase, "text_image_table", "text_img_id=?",
					new String[] { img_id[i] });
		}

		// 从数据中删除与保单对应的照片
		dbHelper.deleteData(dataBase, "insurance_photo_table", "photos_id=?",
				new String[] { photo_id });
		dbHelper.deleteData(dataBase, "insurance_case_table", "case_id=?",
				new String[] { case_id });
		Log.v("delete", "删除成功");
		cur.close();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dataBase.close();
		dbHelper.close();
		if (bitmap != null)
			bitmap.recycle();
	}

}

package com.keita.pinganautoinsurance;

import java.util.ArrayList;
import java.util.HashMap;

import com.keita.pinganautoinsurance.database.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



/*
 * 主菜单页 以girdvie的形式显示
 * 作者关基达
 */
public class MainActivity extends ActionBarActivity {
	private Button create_btn = null;
	private Button view_btn = null;
	private Button photo_btn = null;
	private Button record_btn = null;
	private Button situation_btn = null;
	private GridView gridView = null;
	private ImageButton previous_button = null;
	private ArrayList<HashMap<String, Object>> lstImageItem = null;
	private SimpleAdapter simpleAdapter = null;
	private ActionBar actionBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//设置标题栏
		
		setTopBar();
		gridView = (GridView) findViewById(R.id.gridview);
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		getData();
		simpleAdapter = new SimpleAdapter(this, lstImageItem,
				R.layout.gridview_item,
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.ItemImage, R.id.ItemText });

		gridView.setAdapter(simpleAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (position) {
				case 0:
					intent.setClass(MainActivity.this,
							InsuranceLocationActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent.setClass(MainActivity.this,
							InsuranceListActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent.setClass(MainActivity.this, PhotoListActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent.setClass(MainActivity.this, RecordListActivity.class);
					startActivity(intent);
					break;
				case 4:
					intent.setClass(MainActivity.this,
							TemplateListActivity.class);
					startActivity(intent);
					break;
				case 5:
					intent.setClass(MainActivity.this,
							TemplateListActivity.class);
					startActivity(intent);
					break;
				}

			}

		});
	}

	public void getData() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemImage", R.drawable.open_case);
		map.put("itemText", "创建案件");
		lstImageItem.add(map);

		map = new HashMap<String, Object>();
		map.put("itemImage", R.drawable.case_view);
		map.put("itemText", "查看案件");
		lstImageItem.add(map);

		map = new HashMap<String, Object>();
		map.put("itemImage", R.drawable.photo);
		map.put("itemText", "相片库");
		lstImageItem.add(map);

		map = new HashMap<String, Object>();
		map.put("itemImage", R.drawable.record);
		map.put("itemText", "录音库");
		lstImageItem.add(map);

		map = new HashMap<String, Object>();
		map.put("itemImage", R.drawable.tempalte);
		map.put("itemText", "情景模板");
		lstImageItem.add(map);
		
	}

	// 标题栏退出按钮
	public void setTopBar() {

		/*ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("理赔快捷通");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setVisibility(View.INVISIBLE);*/
		
		actionBar = getSupportActionBar();
		actionBar.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.menu_about:
	            Intent intent = new Intent();
	            intent.setClass(MainActivity.this, AboutMeActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
}

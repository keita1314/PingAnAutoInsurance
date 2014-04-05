package com.keita.pinganautoinsurance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.keita.painganautoinsurance.entity.Template;
import com.keita.painganautoinsurance.entity.TextImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/*
 * 情景模板页面
 */
public class TemplateListActivity extends ActionBarActivity {
	private Button create_Btn = null;
	private Button add_btn = null;
	private ListView listView = null;
	private ArrayList<Template> list = null;
	private MyApplication application = null;
	private TemplateAdapter adapter = null;
	private ActionBar actionBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_list);

		application = (MyApplication) this.getApplication();
		// 设置标题栏
		setTopBar();
		list = application.getTemplateList();
		/*create_Btn = (Button) findViewById(R.id.create_btn);
		add_btn = (Button) findViewById(R.id.add_event);*/
		listView = (ListView) findViewById(R.id.template_list);
		adapter = new TemplateAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("view");
				intent.setClass(TemplateListActivity.this,
						CreateTemplateActivity.class);
				intent.putExtra("position", position);
				startActivity(intent);
				TemplateListActivity.this.finish();
			}

		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				final int pos = position;
				new AlertDialog.Builder(TemplateListActivity.this)
						.setTitle("删除模板")
						.setMessage("你确定要删除当前模板")
						.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										list.remove(pos);
										adapter.notifyDataSetChanged();
									}

								}).show();
				return false;
			}

		});
		/*create_Btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("create");
				intent.setClass(TemplateListActivity.this,
						CreateTemplateActivity.class);
				startActivity(intent);
				TemplateListActivity.this.finish();

			}

		});
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TemplateListActivity.this,
						AddEventActivity.class);
				startActivity(intent);
			}

		});*/
	}

	// 定义adapter
	class TemplateAdapter extends BaseAdapter {
		private Activity context;
		private List<Template> list;

		public TemplateAdapter(Activity context, List<Template> list) {
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
			View itemView = inflater.inflate(R.layout.template_list_item, null);
			Template currentTemplate = list.get(position);
			TextView textView_name = (TextView) itemView
					.findViewById(R.id.template_name);
			TextView textView_date = (TextView) itemView
					.findViewById(R.id.template_date);
			textView_name.setText(currentTemplate.getTemplateName());
			textView_date.setText(currentTemplate.getDate());
			return itemView;
		}

	}

	// 标题栏退出按钮
	public void setTopBar() {

		/*ImageButton previous_button = null;
		View view = findViewById(R.id.top_bar);
		TextView title = (TextView) view.findViewById(R.id.top_title);
		title.setText("模板列表");
		previous_button = (ImageButton) view.findViewById(R.id.top_bar_back);
		previous_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TemplateListActivity.this.finish();
			}

		});*/
		actionBar = getSupportActionBar();
		actionBar.show();
		actionBar.setTitle("情景模版");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_item, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		
		switch(item.getItemId()){
		case R.id.menu_new:
			intent.setAction("create");
			intent.setClass(TemplateListActivity.this,
					CreateTemplateActivity.class);
			startActivity(intent);
			return true;
			
		case R.id.menu_edit:
			intent.setClass(TemplateListActivity.this,
					AddEventActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
}

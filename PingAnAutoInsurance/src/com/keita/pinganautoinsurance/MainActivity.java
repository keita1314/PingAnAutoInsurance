package com.keita.pinganautoinsurance;

import com.keita.pinganautoinsurance.database.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button create_btn = null;
	private Button view_btn = null;
	private Button photo_btn = null;
	private Button record_btn = null;
	private Button situation_btn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   	 	
        create_btn = (Button)findViewById(R.id.create_insurance_policy_btn);
        view_btn = (Button)findViewById(R.id.all_insurance_policy_btn);
        photo_btn = (Button)findViewById(R.id.picture_repo_btn);
        record_btn = (Button)findViewById(R.id.record_repo_btn);
        situation_btn = (Button)findViewById(R.id.situation_template_btn);
        
        //开启理赔案件
        create_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,InsuranceLocationActivity.class);
				startActivity(intent);
			}
        	
        });
        
        view_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,InsuranceListActivity.class);
				startActivity(intent);
				
			}
        	
        });
        photo_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,PhotoListActivity.class);
				startActivity(intent);
				
			}
        	
        });
        record_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,RecordListActivity.class);
				startActivity(intent);
			}
        	
        });
    }
 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}

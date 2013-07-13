package com.keita.pinganautoinsurance;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
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
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}

package com.huadev.search.activity;

//import com.huadev.search.R;
import com.huadev.search.R;
import com.huadev.search.dao.WndMgrServiceDao;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main, null);

		//setContentView(R.layout.activity_main);
		Intent intent = new Intent();
		intent.setClass(this,WndMgrServiceDao.class);
		startService(intent);
        finish();	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

package com.morningtel.kidsedu;

import com.morningtel.kidsedu.main.TabMainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {
	
	LinearLayout splash_layout=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		init();
	}
	
	private void init() {
		splash_layout=(LinearLayout) findViewById(R.id.splash_layout);
		splash_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SplashActivity.this, TabMainActivity.class);
				startActivity(intent);
				finish();
			}});
	}
}

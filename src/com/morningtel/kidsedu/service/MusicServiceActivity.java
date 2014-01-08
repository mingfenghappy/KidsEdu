package com.morningtel.kidsedu.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.morningtel.kidsedu.BaseActivity;

public class MusicServiceActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent intent=new Intent(MusicServiceActivity.this, MusicBackgroundService.class);
		Bundle bundle=new Bundle();
		bundle.putString("name", "");
		bundle.putString("url", "");
		bundle.putBoolean("isNewStartFlag", false);
		intent.putExtras(bundle);
		startService(intent);
		
		finish();
	}

}

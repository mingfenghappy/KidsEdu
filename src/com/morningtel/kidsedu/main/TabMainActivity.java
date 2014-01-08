package com.morningtel.kidsedu.main;

import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.applist.AppTabsActivity;
import com.morningtel.kidsedu.musiclist.MusicTabsActivity;
import com.morningtel.kidsedu.service.MusicBackgroundService;
import com.morningtel.kidsedu.videolist.VideoTabsActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabMainActivity extends TabActivity {

	TabHost host=null;
	
	LinearLayout music_layout=null;
	LinearLayout video_layout=null;
	LinearLayout game_layout=null;
	EditText tab_search=null;
	ImageView tab_account=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tabmain);
		
		init();
	}
	
	public void init() {
		music_layout=(LinearLayout) findViewById(R.id.music_layout);
		music_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTab(0);
			}});
		video_layout=(LinearLayout) findViewById(R.id.video_layout);
		video_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTab(1);
			}});
		game_layout=(LinearLayout) findViewById(R.id.game_layout);
		game_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTab(2);
			}});
		host=getTabHost();
		addTab("spec1", MusicTabsActivity.class, R.drawable.ic_launcher);
		addTab("spec2", VideoTabsActivity.class, R.drawable.ic_launcher);
		addTab("spec3", AppTabsActivity.class, R.drawable.ic_launcher);
		setTab(0);
		tab_search=(EditText) findViewById(R.id.tab_search);
		tab_account=(ImageView) findViewById(R.id.tab_account);
	}
	
	public void addTab(String tag, Class<?> cls, int drawable) {
		TabSpec spec=host.newTabSpec(tag);
		spec.setContent(new Intent(TabMainActivity.this, cls));
		spec.setIndicator("", getResources().getDrawable(drawable));
		host.addTab(spec);
	}
	
	public void setTab(int index) {
		switch(index) {
		case 0:
			host.setCurrentTabByTag("spec1");
			break;
		case 1:
			host.setCurrentTabByTag("spec2");
			break;
		case 2:
			host.setCurrentTabByTag("spec3");
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Intent intent=new Intent(TabMainActivity.this, MusicBackgroundService.class);
		stopService(intent);
	}
}

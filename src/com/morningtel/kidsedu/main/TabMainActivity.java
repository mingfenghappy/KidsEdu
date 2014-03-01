package com.morningtel.kidsedu.main;

import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.account.AccountActivity;
import com.morningtel.kidsedu.applist.AppListActivity;
import com.morningtel.kidsedu.musiclist.MusicTabsActivity;
import com.morningtel.kidsedu.search.SearchActivity;
import com.morningtel.kidsedu.service.MusicBackgroundService;
import com.morningtel.kidsedu.videolist.VideoTabsActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView.OnEditorActionListener;

public class TabMainActivity extends TabActivity {

	TabHost host=null;
	
	LinearLayout music_layout=null;
	LinearLayout video_layout=null;
	LinearLayout read_layout=null;
	LinearLayout study_layout=null;
	LinearLayout game_layout=null;
	EditText tab_search=null;
	ImageView tab_account=null;
	ImageView tab_back=null;
	
	static TabMainActivity instance=null;
	
	public static TabMainActivity getInstance() {
		return instance;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tabmain);
		
		instance=TabMainActivity.this;
		
		init();
	}
	
	public void init() {
		video_layout=(LinearLayout) findViewById(R.id.video_layout);
		video_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTab(0);
			}});
		music_layout=(LinearLayout) findViewById(R.id.music_layout);
		music_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTab(1);
			}});
		read_layout=(LinearLayout) findViewById(R.id.read_layout);
		read_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTab(2);
			}});
		study_layout=(LinearLayout) findViewById(R.id.study_layout);
		study_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTab(3);
			}});
		game_layout=(LinearLayout) findViewById(R.id.game_layout);
		game_layout.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTab(4);
			}});
		host=getTabHost();
		addTab("spec1", VideoTabsActivity.class, R.drawable.ic_launcher);
		addTab("spec2", MusicTabsActivity.class, R.drawable.ic_launcher);
		addTab("spec3", AppListActivity.class, R.drawable.ic_launcher);
		addTab("spec4", AppListActivity.class, R.drawable.ic_launcher);
		addTab("spec5", AppListActivity.class, R.drawable.ic_launcher);
		setTab(0);
		tab_search=(EditText) findViewById(R.id.tab_search);
		tab_search.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_DONE) {
					Intent intent=new Intent(TabMainActivity.this, SearchActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("searchKey", tab_search.getText().toString());
					intent.putExtras(bundle);
					startActivity(intent);
				}
				return false;
			}
		});
		tab_account=(ImageView) findViewById(R.id.tab_account);
		tab_account.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TabMainActivity.this, AccountActivity.class);
				startActivity(intent);
			}});
		tab_back=(ImageView) findViewById(R.id.tab_back);
		tab_back.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TabMainActivity.this, MusicBackgroundService.class);
				stopService(intent);
				finish();
			}});
	}
	
	public void addTab(String tag, Class<?> cls, int drawable) {
		TabSpec spec=host.newTabSpec(tag);
		Intent intent=new Intent(TabMainActivity.this, cls);
		if(tag.equals("spec3")) {
			Bundle bundle=new Bundle();
			bundle.putInt("id", 9);
			intent.putExtras(bundle);
		}
		else if(tag.equals("spec4")) {
			Bundle bundle=new Bundle();
			bundle.putInt("id", 8);
			intent.putExtras(bundle);
		}
		else if(tag.equals("spec5")) {
			Bundle bundle=new Bundle();
			bundle.putInt("id", 10);
			intent.putExtras(bundle);
		}
		spec.setContent(intent);
		spec.setIndicator("", getResources().getDrawable(drawable));
		host.addTab(spec);
	}
	
	public void setTab(int index) {
		music_layout.setBackgroundResource(R.drawable.main_tab_indicator_background);
		video_layout.setBackgroundResource(R.drawable.main_tab_indicator_background);
		read_layout.setBackgroundResource(R.drawable.main_tab_indicator_background);
		study_layout.setBackgroundResource(R.drawable.main_tab_indicator_background);
		game_layout.setBackgroundResource(R.drawable.main_tab_indicator_background);
		switch(index) {
		case 0:
			video_layout.setBackgroundResource(R.drawable.main_tab_indicator_bg_choice);
			host.setCurrentTabByTag("spec1");
			break;
		case 1:
			music_layout.setBackgroundResource(R.drawable.main_tab_indicator_bg_choice);
			host.setCurrentTabByTag("spec2");
			break;
		case 2:
			read_layout.setBackgroundResource(R.drawable.main_tab_indicator_bg_choice);
			host.setCurrentTabByTag("spec3");
			break;
		case 3:
			study_layout.setBackgroundResource(R.drawable.main_tab_indicator_bg_choice);
			host.setCurrentTabByTag("spec4");
			break;
		case 4:
			game_layout.setBackgroundResource(R.drawable.main_tab_indicator_bg_choice);
			host.setCurrentTabByTag("spec5");
			break;
		}
	}
	
}

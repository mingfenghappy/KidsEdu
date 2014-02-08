package com.morningtel.kidsedu.account;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.account.AccountMusicAdapter.OnRefreshListener;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.service.MusicBackgroundService;

public class AccountMusicActivity extends SherlockActivity {
	
	SwipeListView app_list=null;
	AccountMusicAdapter adapter=null;
	
	ArrayList<AppModel> model_list=null;
	//当前第一个item
	int firstItem=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_app);
		
		getSupportActionBar().setTitle("账户信息");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		model_list=Conn.getInstance(getApplicationContext()).getAppModelList("music");
		
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		
		return true;
	}
	
	public void init() {
		app_list=(SwipeListView) findViewById(R.id.app_list);
		app_list.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		app_list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				firstItem=view.getFirstVisiblePosition(); 
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		if(Build.VERSION.SDK_INT>=11) {
			app_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		}
		adapter=new AccountMusicAdapter(model_list, AccountMusicActivity.this, new OnRefreshListener() {

			@Override
			public void refreshListView() {
				// TODO Auto-generated method stub
				app_list.setAdapter(adapter);
				app_list.setSelection(firstItem);
			}});
		app_list.setAdapter(adapter);
		app_list.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onClickFrontView(int position) {
				// TODO Auto-generated method stub
				super.onClickFrontView(position);
				Intent intent=new Intent(AccountMusicActivity.this, MusicBackgroundService.class);
				Bundle bundle=new Bundle();
				bundle.putString("image", ((KEApplication) getApplicationContext()).kidsIconUrl+CommonUtils.getIconAdd(model_list.get(position).getIconUrl()));
				bundle.putString("name", model_list.get(position).getName());
				bundle.putString("url", ((KEApplication) getApplicationContext()).kidsIconUrl+model_list.get(position).getFileUrl());
				bundle.putBoolean("isNewStartFlag", true);
				intent.putExtras(bundle);
				startService(intent);
			}
		});
	}
	
	public int convertDpToPixel(float dp) {
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        float px=dp*(metrics.densityDpi/160f);
        return (int) px;
    }
}

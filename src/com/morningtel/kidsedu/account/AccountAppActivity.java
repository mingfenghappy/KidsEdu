package com.morningtel.kidsedu.account;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.receiver.AppReceiver;

public class AccountAppActivity extends SherlockActivity {
	
	SwipeListView app_list=null;
	AccountAppAdapter adapter=null;
	
	ArrayList<AppModel> model_list=null;
	//当前第一个item
	int firstItem=0;
	//应用类型
	int resourceType=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_app);
		
		getSupportActionBar().setTitle("账户信息");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		resourceType=getIntent().getExtras().getInt("resourceType");
		model_list=new ArrayList<AppModel>();
		ArrayList<AppModel> model_list_temp=Conn.getInstance(getApplicationContext()).getAppManagerModelList();
		for(int i=0;i<model_list_temp.size();i++) {
			if(model_list_temp.get(i).getResourceType()==resourceType) {
				model_list.add(model_list_temp.get(i));
			}
		}
		
		init();
		
		IntentFilter filter=new IntentFilter();
    	filter.addAction(AppReceiver.appChange);
    	registerReceiver(receiver, filter);
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
		adapter=new AccountAppAdapter(model_list, AccountAppActivity.this);
		app_list.setAdapter(adapter);
		app_list.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onClickFrontView(int position) {
				// TODO Auto-generated method stub
				super.onClickFrontView(position);
				if(CommonUtils.checkAppInstall(model_list.get(position).getPackageName(), AccountAppActivity.this)) {
					CommonUtils.openApp(AccountAppActivity.this, model_list.get(position).getPackageName());
				}
			}
		});
	}
	
	public int convertDpToPixel(float dp) {
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        float px=dp*(metrics.densityDpi/160f);
        return (int) px;
    }
	
	BroadcastReceiver receiver=new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(AppReceiver.appChange)) {
				adapter.notifyDataSetChanged();
			}
			else if(intent.getAction().equals(AppReceiver.appUpdate)) {
				
			}
		}};
		
	@Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	unregisterReceiver(receiver);
    }	
}

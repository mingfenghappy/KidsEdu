package com.morningtel.kidsedu.account;

import java.util.ArrayList;

import android.content.Intent;
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
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.account.AccountVideoAdapter.OnRefreshListener;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.mediaplayer.PlayerActivity;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.model.VideoItemModel;

public class AccountVideoActivity extends SherlockActivity {
		
	SwipeListView app_list=null;
	AccountVideoAdapter adapter=null;
	
	ArrayList<AppModel> model_list=null;
	//��ǰ��һ��item
	int firstItem=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_app);
		
		getSupportActionBar().setTitle("�˻���Ϣ");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		model_list=Conn.getInstance(getApplicationContext()).getAppModelList("video");
		
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
		app_list.setOffsetLeft(CommonUtils.dip2px(AccountVideoActivity.this, 200));
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
		adapter=new AccountVideoAdapter(model_list, AccountVideoActivity.this, new OnRefreshListener() {

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
				
				ArrayList<VideoItemModel> item_list=model_list.get(position).getModel_list();
				String[] versionCode_array=new String[item_list.size()];
				String[] fileUrl_array=new String[item_list.size()];
				for(int i=0;i<item_list.size();i++) {
					versionCode_array[i]=item_list.get(i).getVersionCode();
					fileUrl_array[i]=item_list.get(i).getFileUrl();
				}
				
				Intent intent=new Intent(AccountVideoActivity.this, PlayerActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("name", model_list.get(position).getName());
				bundle.putStringArray("VersionCode", versionCode_array);
				bundle.putStringArray("FileUrl", fileUrl_array);
				bundle.putString("url", ((KEApplication) getApplicationContext()).kidsVideoUrl+item_list.get(position).getFileUrl().substring(6, item_list.get(position).getFileUrl().length())+".mp4");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	public int convertDpToPixel(float dp) {
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        float px=dp*(metrics.densityDpi/160f);
        return (int) px;
    }
}

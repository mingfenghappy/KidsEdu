package com.morningtel.kidsedu.account;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.account.AccountVideoAdapter.OnRefreshListener;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.videolist.VideoDetailActivity;

public class AccountVideoActivity extends BaseActivity {
	
	TextView nav_title=null;
	
	SwipeListView app_list=null;
	AccountVideoAdapter adapter=null;
	
	ArrayList<AppModel> model_list=null;
	//当前第一个item
	int firstItem=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_app);
		
		model_list=Conn.getInstance(getApplicationContext()).getAppModelList("video");
		
		init();
	}
	
	public void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("账户信息");
		nav_title.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		app_list=(SwipeListView) findViewById(R.id.app_list);
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
				Intent intent=new Intent(AccountVideoActivity.this, VideoDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("id", model_list.get(position).getId());
				bundle.putString("name", model_list.get(position).getName());
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

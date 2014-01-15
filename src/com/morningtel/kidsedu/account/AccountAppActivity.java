package com.morningtel.kidsedu.account;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.receiver.AppReceiver;

public class AccountAppActivity extends BaseActivity {
	
	TextView nav_title=null;
	
	SwipeListView app_list=null;
	AccountAppAdapter adapter=null;
	
	ArrayList<AppModel> model_list=null;
	//��ǰ��һ��item
	int firstItem=0;
	//Ӧ������
	int resourceType=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_app);

		resourceType=getIntent().getExtras().getInt("resourceType");
		model_list=new ArrayList<AppModel>();
		ArrayList<AppModel> model_list_temp=Conn.getInstance(getApplicationContext()).getAppModelList("app");
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
	
	public void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("�˻���Ϣ");
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
		adapter=new AccountAppAdapter(model_list, AccountAppActivity.this);
		app_list.setAdapter(adapter);
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
				model_list.clear();
				ArrayList<AppModel> model_list_temp=Conn.getInstance(getApplicationContext()).getAppModelList("app");
				for(int i=0;i<model_list_temp.size();i++) {
					if(model_list_temp.get(i).getResourceType()==resourceType) {
						model_list.add(model_list_temp.get(i));
					}
				}
				app_list.setAdapter(adapter);
				app_list.setSelection(firstItem);
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

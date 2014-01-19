package com.morningtel.kidsedu.applist;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.model.AppsFilterModel;
import com.morningtel.kidsedu.model.JsonParse;
import com.morningtel.kidsedu.receiver.AppReceiver;

public class AppListActivity extends BaseActivity {
	
	PullToRefreshListView fragment_apptabs_listview=null;
	AppListAdapter adapter=null;
	
	ArrayList<AppsFilterModel> appfilter_list=null;
	//当前包名对象集合
	ArrayList<String> packageList=null;
    private int id=0;
    //页码
    int page=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_apptabs);
		
		id=getIntent().getExtras().getInt("id");
        packageList=new ArrayList<String>();
        appfilter_list=new ArrayList<AppsFilterModel>();
        adapter=new AppListAdapter(appfilter_list, AppListActivity.this);
        
        IntentFilter filter=new IntentFilter();
    	filter.addAction(AppReceiver.appChange);
    	registerReceiver(receiver, filter);
    	
    	init();
	}
	
	public void init() {
		fragment_apptabs_listview=(PullToRefreshListView) findViewById(R.id.fragment_apptabs_listview);
        fragment_apptabs_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				page=1;
				getAppsFilter();
			}
		});
        fragment_apptabs_listview.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if(appfilter_list.size()%20==0) {
					page++;
					getAppsFilter();
				}
			}
		});
        ListView actualListView = fragment_apptabs_listview.getRefreshableView();
        fragment_apptabs_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent(AppListActivity.this, AppDetailActivity.class);
//				startActivity(intent);
			}
		});
        actualListView.setAdapter(adapter);
        getAppsFilter();
	}
	
	@Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	unregisterReceiver(receiver);
    }
    
    public void getAppsFilter() {
    	final Handler handler=new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			super.handleMessage(msg);
    			if(msg.obj==null) {
    				CommonUtils.showCustomToast(AppListActivity.this, "网络异常，请稍后再试");
				}
				else {
					String str=msg.obj.toString();
					if(CommonUtils.convertNull(str).equals("")) {
						CommonUtils.showCustomToast(AppListActivity.this, "网络异常，请稍后再试");
					}
					else {
						ArrayList<AppsFilterModel> appfilter_list_temp=JsonParse.getAppsFilterModelList(str);
						for(int i=0;i<appfilter_list_temp.size();i++) {
							packageList.add(appfilter_list_temp.get(i).getPackageName());
						}
						if(page==1) {
							appfilter_list.clear();
							fragment_apptabs_listview.onRefreshComplete();
						}
						appfilter_list.addAll(appfilter_list_temp);
						if(appfilter_list.size()==0) {
							CommonUtils.showCustomToast(AppListActivity.this, "暂无相关信息");
						}
						else {
							adapter.notifyDataSetChanged();
						}					
					}
				}
    		}
    	};
    	
    	new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message m=new Message();
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("rid", ""+id);
				map.put("page", ""+page);
				map.put("size", "20");
				map.put("isOk", "1");
				String result=CommonUtils.getWebData(map, ((KEApplication) getApplicationContext()).kidsDataUrl+"/data/json/app/AppsFilter");
				m.obj=result;
				handler.sendMessage(m);
			}}).start();
    }
    
    BroadcastReceiver receiver=new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(AppReceiver.appChange)) {
				String packageName=intent.getExtras().getString("packageName");
				System.out.println("收到广播："+packageName);
				if(packageList.contains(packageName)) {
					adapter.notifyDataSetChanged();
				}
			}
		}};
}

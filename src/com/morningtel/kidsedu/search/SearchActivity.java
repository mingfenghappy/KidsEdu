package com.morningtel.kidsedu.search;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;
import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.commons.DownloadMusicTask;
import com.morningtel.kidsedu.model.AppsFilterModel;
import com.morningtel.kidsedu.model.JsonParse;
import com.morningtel.kidsedu.receiver.AppReceiver;

public class SearchActivity extends BaseActivity {
	
	TextView nav_title=null;
	ImageView dataLoadingImage=null;
	PinnedSectionListView search_listview=null; 
	SearchAdapter adapter=null;
	
	String searchKey="";
	
	HashMap<String, ArrayList<AppsFilterModel>> appfilter_map=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		
		searchKey=getIntent().getExtras().getString("searchKey");
		appfilter_map=new HashMap<String, ArrayList<AppsFilterModel>>();
		
		init();
		
		IntentFilter filter=new IntentFilter();
        filter.addAction(AppReceiver.appChangeSearch);
        filter.addAction(DownloadMusicTask.musicChangeSearch);
        registerReceiver(receiver, filter);
	}
	
	public void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText(" ◊“≥");
		nav_title.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		dataLoadingImage=(ImageView) findViewById(R.id.dataLoadingImage);
		AnimationDrawable animationDrawable = (AnimationDrawable) dataLoadingImage.getDrawable();  
        animationDrawable.start();
		search_listview=(PinnedSectionListView) findViewById(R.id.search_listview);
		
		searchData();
	}
	
	public void searchData() {
    	final Handler handler=new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			super.handleMessage(msg);
    			if(msg.obj==null) {
    				CommonUtils.showCustomToast(SearchActivity.this, "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘");
    				dataLoadingImage.setImageResource(R.drawable.blank_page_network_fail);
				}
				else {
					String str=msg.obj.toString();
					if(CommonUtils.convertNull(str).equals("")) {
						CommonUtils.showCustomToast(SearchActivity.this, "Õ¯¬Á“Ï≥££¨«Î…‘∫Û‘Ÿ ‘");
	    				dataLoadingImage.setImageResource(R.drawable.blank_page_network_fail);
					}
					else {
						dataLoadingImage.setVisibility(View.GONE);
						ArrayList<AppsFilterModel> appfilter_list_temp=JsonParse.getAppsFilterModelList(str);
						for(int i=0;i<appfilter_list_temp.size();i++) {
							if(appfilter_list_temp.get(i).getResourceType()==8||
									appfilter_list_temp.get(i).getResourceType()==9||
									appfilter_list_temp.get(i).getResourceType()==10||
									appfilter_list_temp.get(i).getResourceType()==3||
									appfilter_list_temp.get(i).getResourceType()==4) {
								ArrayList<AppsFilterModel> model_list=null;
								if(appfilter_map.containsKey(""+appfilter_list_temp.get(i).getResourceType())) {
									model_list=appfilter_map.get(""+appfilter_list_temp.get(i).getResourceType());
								}	
								else {
									model_list=new ArrayList<AppsFilterModel>();
								}
								model_list.add(appfilter_list_temp.get(i));
								appfilter_map.put(""+appfilter_list_temp.get(i).getResourceType(), model_list);
							}
							else {
								continue;
							}
						}
						adapter=new SearchAdapter(SearchActivity.this, appfilter_map);
						search_listview.setAdapter(adapter);
						
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
				map.put("searchby", "name");
				map.put("orderby", "rid");
				map.put("size", "1000");
				map.put("keyword", searchKey);
				String result=CommonUtils.getWebData(map, ((KEApplication) getApplicationContext()).kidsDataUrl+"/data/json/app/AppsFilter");
				m.obj=result;
				handler.sendMessage(m);
			}}).start();
    }
	
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	};
	
	BroadcastReceiver receiver=new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(AppReceiver.appChangeSearch)||intent.getAction().equals(DownloadMusicTask.musicChangeSearch)) {
				if(adapter!=null) {
					adapter.notifyDataSetChanged();
				}				
			}
		}};
}

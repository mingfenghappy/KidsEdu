package com.morningtel.kidsedu.musiclist;

import java.util.ArrayList;
import java.util.HashMap;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.applist.AppListActivity;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.commons.DownloadMusicTask;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.model.AppsFilterModel;
import com.morningtel.kidsedu.model.JsonParse;
import com.morningtel.kidsedu.service.MusicBackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MusicFragment extends Fragment {
	
	PullToRefreshListView fragment_apptabs_listview=null;
	MusicListAdapter adapter=null;
	ListView actualListView=null;
	
	ArrayList<AppsFilterModel> appfilter_list=null;
    private int id=0;
    //页码
    int page=1;
    //正在加载标志位
    boolean isLoad=false;
    
    private View view;
    //底部footview
    View v=null;

    public static MusicFragment newInstance(int id) {
    	MusicFragment fragment = new MusicFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id=getArguments().getInt("id");
        appfilter_list=new ArrayList<AppsFilterModel>();
        adapter=new MusicListAdapter(appfilter_list, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if(view==null) {
    		v=LayoutInflater.from(getActivity()).inflate(R.layout.view_footer, null);
    		view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_apptabs, null);
    		fragment_apptabs_listview=(PullToRefreshListView) view.findViewById(R.id.fragment_apptabs_listview);
            fragment_apptabs_listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
    			@Override
    			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
    				if(isLoad) {
    					CommonUtils.showCustomToast(getActivity(), "正在加载中，请稍后");
    					return;
    				}
    				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
    						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
    				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
    				page=1;
    				getMusicFilter();
    			}
    		});
            fragment_apptabs_listview.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

    			@Override
    			public void onLastItemVisible() {
    				if(isLoad) {
    					CommonUtils.showCustomToast(getActivity(), "正在加载中，请稍后");
    					return;
    				}
    				if(appfilter_list.size()%20==0) {
    					page++;
    					getMusicFilter();
    				}
    			}
    		});
            actualListView = fragment_apptabs_listview.getRefreshableView();
            fragment_apptabs_listview.setOnItemClickListener(new OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
    				// TODO Auto-generated method stub
    				if(((KEApplication) getActivity().getApplicationContext()).isMusicPlay) {
    					playMusic(appfilter_list.get(position-1).getId(), appfilter_list.get(position-1).getName(), appfilter_list.get(position-1).getIconUrl());
    					((KEApplication) getActivity().getApplicationContext()).musicName=appfilter_list.get(position-1).getName();
    				}
    				else {
    					CommonUtils.showCustomToast(getActivity(), "正在加载中，请稍后");
    				}
    			}
    		});
            actualListView.setAdapter(adapter);
            getMusicFilter();
    	}
    	ViewGroup parent=(ViewGroup) view.getParent();  
        if (parent!=null) {  
            parent.removeView(view);  
        } 
        
        IntentFilter filter=new IntentFilter();
        filter.addAction(DownloadMusicTask.musicChange);
        getActivity().registerReceiver(receiver, filter);
        
        return view;
    }
    
    public void getMusicFilter() {
    	isLoad=true;
    	final Handler handler=new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			isLoad=false;
    			super.handleMessage(msg);
    			if(msg.obj==null) {
    				CommonUtils.showCustomToast(getActivity(), "网络异常，请稍后再试");
				}
				else {
					String str=msg.obj.toString();
					if(CommonUtils.convertNull(str).equals("")) {
						CommonUtils.showCustomToast(getActivity(), "网络异常，请稍后再试");
					}
					else {
						ArrayList<AppsFilterModel> appfilter_list_temp=JsonParse.getAppsFilterModelList(str);
						if(page==1) {
							appfilter_list.clear();
							fragment_apptabs_listview.onRefreshComplete();
							if(actualListView.getFooterViewsCount()>0) {
								actualListView.removeFooterView(v);
							}
				            actualListView.addFooterView(v);
						}
						appfilter_list.addAll(appfilter_list_temp);
						if(appfilter_list.size()!=0) {
							adapter.notifyDataSetChanged();
						}	
						if(appfilter_list.size()%20!=0||appfilter_list.size()==0) {
							actualListView.removeFooterView(v);							
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
				map.put("atid", ""+id);
				map.put("page", ""+page);
				map.put("size", "20");
				map.put("isOk", "1");
				map.put("orderby", "");
				String result=CommonUtils.getWebData(map, ((KEApplication) getActivity().getApplicationContext()).kidsDataUrl+"/data/json/app/AppsFilter");
				m.obj=result;
				handler.sendMessage(m);
			}}).start();
    }
    
    /**
     * 播放音乐
     * @param id
     * @param name
     */
	private void playMusic(final int id, final String name, final String imageUrl) {
		((KEApplication) getActivity().getApplicationContext()).isMusicPlay=false;
		final Handler handler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.obj==null) {
					((KEApplication) getActivity().getApplicationContext()).isMusicPlay=true;
    				CommonUtils.showCustomToast(getActivity(), "网络异常，请稍后再试");
				}
				else {
					Intent intent=new Intent(getActivity(), MusicBackgroundService.class);
					Bundle bundle=new Bundle();
					bundle.putString("image", ((KEApplication) getActivity().getApplicationContext()).kidsIconUrl+CommonUtils.getIconAdd(imageUrl));
					bundle.putString("name", name);
					AppModel model=JsonParse.getAppModelByAid(msg.obj.toString());
					bundle.putString("url", ((KEApplication) getActivity().getApplicationContext()).kidsIconUrl+model.getFileUrl());
					bundle.putBoolean("isNewStartFlag", true);
					intent.putExtras(bundle);
					getActivity().startService(intent);
					Conn.getInstance(getActivity()).insertMusicModel(model);
					Conn.getInstance(getActivity()).insertOtherPlatformByMusic(model.getId(), name, ((KEApplication) getActivity().getApplicationContext()).kidsIconUrl+CommonUtils.getIconAdd(model.getIconUrl()), ((KEApplication) getActivity().getApplicationContext()).kidsIconUrl+model.getFileUrl());
				}
			}
		};
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("aid", ""+id);
				String webResult=CommonUtils.getWebData(map, ((KEApplication) getActivity().getApplicationContext()).kidsDataUrl+"/data/json/app/AppByAid");
				Message m=new Message();
				m.obj=webResult;
				handler.sendMessage(m);
			}
		}).start();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getActivity().unregisterReceiver(receiver);
	}

    BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			for(int i=0;i<appfilter_list.size();i++) {
				if(appfilter_list.get(i).getName().equals(intent.getExtras().getString("name"))) {
					adapter.notifyDataSetChanged();
					break;
				}
			}
		}
	};
}

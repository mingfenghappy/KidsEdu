package com.morningtel.kidsedu.musiclist;

import java.util.ArrayList;
import java.util.HashMap;

import com.lidroid.xutils.BitmapUtils;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.BitmapHelp;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.model.AppTypesModel;
import com.morningtel.kidsedu.model.JsonParse;
import com.morningtel.kidsedu.service.MusicBackgroundService;
import com.viewpagerindicator.TabPageIndicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MusicTabsActivity extends FragmentActivity {
	
	ViewPager apptab_pager=null; 
	TabPageIndicator indicator=null;
	FragmentPagerAdapter adapter=null;
	LinearLayout view_play_ctrl=null;
	ImageView music_control_image=null;
	TextView music_control_play_name=null;
	ImageView music_control_play_state=null;
	
	public static BitmapUtils bitmapUtils;
	
	//UI是否加载完毕
	boolean isUILoadOK=false;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        bitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        
        IntentFilter filter=new IntentFilter();
        filter.addAction(MusicBackgroundService.MusicBackgroundStartAction);
        filter.addAction(MusicBackgroundService.MusicBackgroundStopAction);
        registerReceiver(receiver, filter);
        
        loadMusicTypesByRid();
    }
    
    BroadcastReceiver receiver=new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(MusicBackgroundService.MusicBackgroundStartAction.equals(intent.getAction())) {
				if(!isUILoadOK) {
					return;
				}
				bitmapUtils.display(music_control_image, intent.getExtras().getString("image"));
				music_control_play_name.setText(intent.getExtras().getString("name"));
				music_control_play_state.setImageResource(R.drawable.pause_sel);
			}
			else {
				music_control_play_state.setImageResource(R.drawable.play_sel);
			}
			music_control_play_state.setEnabled(true);
			music_control_play_state.setClickable(true);
		}};
    
    public void init() {    
    	isUILoadOK=true;
        apptab_pager=(ViewPager)findViewById(R.id.apptab_pager);
        indicator=(TabPageIndicator)findViewById(R.id.apptab_indicator);
        view_play_ctrl=(LinearLayout) findViewById(R.id.view_play_ctrl);
        view_play_ctrl.setVisibility(View.VISIBLE);
        music_control_image=(ImageView) findViewById(R.id.music_control_image);
        music_control_play_name=(TextView) findViewById(R.id.music_control_play_name);
        music_control_play_state=(ImageView) findViewById(R.id.music_control_play_state);
        music_control_play_state.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MusicTabsActivity.this, MusicBackgroundService.class);
				Bundle bundle=new Bundle();
				bundle.putString("image", "");
				bundle.putString("name", "");
				bundle.putString("url", "");
				bundle.putBoolean("isNewStartFlag", false);
				intent.putExtras(bundle);
				startService(intent);
				music_control_play_state.setEnabled(false);
				music_control_play_state.setClickable(false);
			}});
        music_control_play_state.setEnabled(false);
		music_control_play_state.setClickable(false);
    }
    
    /**
     * 加载顶部动态菜单
     */
    public void loadMusicTypesByRid() {
    	final Handler handler=new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			super.handleMessage(msg);
    			if(msg.obj==null) {
    				CommonUtils.showCustomToast(MusicTabsActivity.this, "网络异常，请稍后再试");
				}
				else {
					String str=msg.obj.toString();
					if(CommonUtils.convertNull(str).equals("")) {
						CommonUtils.showCustomToast(MusicTabsActivity.this, "网络异常，请稍后再试");
					}
					else {
						ArrayList<AppTypesModel> app_types_list_temp=JsonParse.getAppTypesModelList(str);
						if(app_types_list_temp.size()==0) {
							CommonUtils.showCustomToast(MusicTabsActivity.this, "暂无相关信息");
						}
						else {
							setContentView(R.layout.activity_apptab);
							init();
					        adapter=new AppListAdapter(getSupportFragmentManager(), app_types_list_temp);
							apptab_pager.setAdapter(adapter);
					        indicator.setViewPager(apptab_pager);
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
				map.put("rid", "4");
				String result=CommonUtils.getWebData(map, ((KEApplication) getApplicationContext()).kidsDataUrl+"/data/json/app/AppTypesByRid");
				m.obj=result;
				handler.sendMessage(m);
			}}).start();
    }

    class AppListAdapter extends FragmentPagerAdapter {
    	
    	ArrayList<AppTypesModel> app_types_list=null;
    	
        public AppListAdapter(FragmentManager fm) {
            super(fm);
        }
        
        public AppListAdapter(FragmentManager fm, ArrayList<AppTypesModel> app_types_list) {
        	super(fm);
        	this.app_types_list=app_types_list;
        }

        @Override
        public Fragment getItem(int position) {
            return MusicFragment.newInstance(app_types_list.get(position).getId());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return app_types_list.get(position).getName();
        }

        @Override
        public int getCount() {
            return app_types_list.size();
        }

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			super.finishUpdate(container);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return super.instantiateItem(container, position);
		}

    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	unregisterReceiver(receiver);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			Intent intent=new Intent(MusicTabsActivity.this, MusicBackgroundService.class);
			stopService(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}

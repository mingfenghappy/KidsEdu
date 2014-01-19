package com.morningtel.kidsedu.musiclist;

import java.util.ArrayList;
import java.util.HashMap;

import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.model.AppTypesModel;
import com.morningtel.kidsedu.model.JsonParse;
import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.Window;

public class MusicTabsActivity extends FragmentActivity {
	
	ViewPager apptab_pager=null; 
	TabPageIndicator indicator=null;
	FragmentPagerAdapter adapter=null;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        loadMusicTypesByRid();
    }
    
    public void init() {    	
        apptab_pager=(ViewPager)findViewById(R.id.apptab_pager);
        indicator=(TabPageIndicator)findViewById(R.id.apptab_indicator);
    }
    
    /**
     * ���ض�����̬�˵�
     */
    public void loadMusicTypesByRid() {
    	final Handler handler=new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			super.handleMessage(msg);
    			if(msg.obj==null) {
    				CommonUtils.showCustomToast(MusicTabsActivity.this, "�����쳣�����Ժ�����");
				}
				else {
					String str=msg.obj.toString();
					if(CommonUtils.convertNull(str).equals("")) {
						CommonUtils.showCustomToast(MusicTabsActivity.this, "�����쳣�����Ժ�����");
					}
					else {
						ArrayList<AppTypesModel> app_types_list_temp=JsonParse.getAppTypesModelList(str);
						if(app_types_list_temp.size()==0) {
							CommonUtils.showCustomToast(MusicTabsActivity.this, "���������Ϣ");
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

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			// TODO Auto-generated method stub
			//super.setPrimaryItem(container, position, object);
		}
        
        
    }
}

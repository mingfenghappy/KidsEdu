package com.morningtel.kidsedu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.commons.NotificationActivity;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.model.JsonParse;
import com.morningtel.kidsedu.receiver.AppReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

public class UpdateService extends Service {
	
	NotificationManager manager=null;
	Notification no=null;
	RemoteViews view=null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		checkAppUpdate();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void checkAppUpdate() {
		final Handler handler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.obj==null) {
					CommonUtils.showCustomToast(UpdateService.this, "网络异常，请稍后再试");
				}
				else {
					((KEApplication) getApplicationContext()).update_maps.clear();
					((KEApplication) getApplicationContext()).update_maps.putAll(JsonParse.getUpdateAppModelList(msg.obj.toString()));
					Iterator<Entry<String, String>> it=((KEApplication) getApplicationContext()).update_maps.entrySet().iterator();
					ArrayList<String> package_map=new ArrayList<String>();
					while(it.hasNext()) {
						Entry<String, String> entry=it.next();
						package_map.add(entry.getKey());
					}
					if(package_map.size()>0) {
						Intent intent=new Intent();
						intent.setAction(AppReceiver.appUpdate);
						sendBroadcast(intent);					
						manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
						no=new Notification();
						no.flags=Notification.FLAG_AUTO_CANCEL;
						no.icon=R.drawable.ic_launcher;
						no.when=System.currentTimeMillis();
						no.tickerText="易迪乐园更新提示";
						view=new RemoteViews(getPackageName(), R.layout.view_update);
						
						switch(package_map.size()) {
						case 1:
							view.setImageViewBitmap(R.id.update_image1, CommonUtils.drawableToBitmap(UpdateService.this, CommonUtils.getAPPIcon(UpdateService.this, package_map.get(0))));
							break;
						case 2:
							view.setImageViewBitmap(R.id.update_image1, CommonUtils.drawableToBitmap(UpdateService.this, CommonUtils.getAPPIcon(UpdateService.this, package_map.get(0))));
							view.setImageViewBitmap(R.id.update_image2, CommonUtils.drawableToBitmap(UpdateService.this, CommonUtils.getAPPIcon(UpdateService.this, package_map.get(1))));
							break;
						default:
							view.setImageViewBitmap(R.id.update_image1, CommonUtils.drawableToBitmap(UpdateService.this, CommonUtils.getAPPIcon(UpdateService.this, package_map.get(0))));
							view.setImageViewBitmap(R.id.update_image2, CommonUtils.drawableToBitmap(UpdateService.this, CommonUtils.getAPPIcon(UpdateService.this, package_map.get(1))));
							view.setImageViewBitmap(R.id.update_image3, CommonUtils.drawableToBitmap(UpdateService.this, CommonUtils.getAPPIcon(UpdateService.this, package_map.get(2))));
						}
						no.contentView=view;
						Intent intent_new=new Intent(UpdateService.this, NotificationActivity.class);
						PendingIntent pi=PendingIntent.getActivity(UpdateService.this, 0, intent_new, 0);
						no.contentIntent=pi;
						manager.notify(0, no);
					}
				}
				stopSelf();
			}
		};
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message m=new Message();
				String info="";
				ArrayList<AppModel> model_list=Conn.getInstance(UpdateService.this).getAppModelList("app");
				HashMap<String, Integer> map_app=CommonUtils.getWholeAPPInfo(UpdateService.this);
				for(int i=0;i<model_list.size();i++) {
					if(map_app.containsKey(model_list.get(i).getPackageName())) {
						info+=model_list.get(i).getId()+","+map_app.get(model_list.get(i).getPackageName())+"|";
					}
				}
				if(!info.equals("")) {
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("code", info);
					String result=CommonUtils.getWebData(map, ((KEApplication) getApplicationContext()).kidsDataUrl+"/data/json/app/AppsLatestFile_appFiles");
					m.obj=result;
					handler.sendMessage(m);
				}				
			}}).start();
	}

}

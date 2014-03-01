package com.morningtel.kidsedu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.service.GuardService;
import com.morningtel.kidsedu.service.UpdateService;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;

public class KEApplication extends Application {
	
	public String kidsIconUrl="http://res.kidsedu.com/";	
	public String kidsDataUrl="http://www.kidsedu.com";
	public String kidsVideoUrl="http://res.kidsedu.com/mp4video";
	
	//����Ӧ����Ϣ��¼
	public HashMap<String, Integer> download_app_maps=null;
	//������Ϣ
	public HashMap<String, String> update_maps=null;
	//�����л����ű�־
	public boolean isMusicPlay=true;
	//��ǰ���ڲ��ŵ�����
	public String musicName="";
	//����������Ϣ��¼
	public HashMap<String, Integer> download_music_maps=null;
	//��Ҫֹͣ���б��¼
	public ArrayList<String> download_stop_list=null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file=new File(Environment.getExternalStorageDirectory().getPath()+"/kidsedu/temp");
			if(!file.exists()) {
				file.mkdirs();
			}
			File file_=new File(Environment.getExternalStorageDirectory().getPath()+"/kidsedu/download_pic");
			if(!file_.exists()) {
				file_.mkdirs();
			}
			File file_cache=new File("/data/data/"+getApplicationContext().getPackageName()+"/kidsedu");
			if(!file_cache.exists()) {
				file_cache.mkdirs();
			}
		}
		download_app_maps=new HashMap<String, Integer>();
		update_maps=new HashMap<String, String>();
		download_music_maps=new HashMap<String, Integer>();
		download_stop_list=new ArrayList<String>();
		
		//�������·���
		Intent intent=new Intent(getApplicationContext(), UpdateService.class);
		startService(intent);		
		
		sendUserInfo();
		
		//����ͯģʽ�������ݸ��ƽ���
		copyOtherPlatformData("CachedAppItem.sqlite");
		copyOtherPlatformData("CachedAudiobookItem2-iPad.sqlite");
		copyOtherPlatformData("ChildMovieItem2-iPad.sqlite");
		
		Intent intent_time=new Intent(getApplicationContext(), GuardService.class);
		startService(intent_time);
	}
	
	/**
	 * ��ȡֹͣ�����б�
	 * @return
	 */
	public synchronized ArrayList<String> getDownload_stop_list() {
		return download_stop_list;
	}
	
	public void sendUserInfo() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("deviceToken.token", CommonUtils.getMacAddress(getApplicationContext()));
				map.put("token", CommonUtils.getUserInfo(getApplicationContext()).get("userToken"));
				if(!CommonUtils.getMacAddress(getApplicationContext()).equals("")&&!CommonUtils.getUserInfo(getApplicationContext()).get("userToken").equals("")) {
					CommonUtils.getWebData(map, kidsDataUrl+"/api/json/deviceToken_checkDeviceToken_feedback");
				}
				
			}
		}).start();
	}
	
	/**
	 * ����ͯģʽ�������ݸ��ƽ���
	 * @param sqliteName
	 */
	public void copyOtherPlatformData(String sqliteName) {
		File file=new File("/data/data/"+getApplicationContext().getPackageName()+"/"+sqliteName);
		if(!file.exists()) {
			CommonUtils.copyAssetsFile(sqliteName, file.getPath(), getApplicationContext());
			if(sqliteName.equals("CachedAudiobookItem2-iPad.sqlite")) {
				Conn.getInstance(getApplicationContext()).readOtherPlatformByMusic();
			}
			if(sqliteName.equals("CachedAppItem.sqlite")) {
				Conn.getInstance(getApplicationContext()).readOtherPlatformByApp();
			}
		}
	}
}

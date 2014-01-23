package com.morningtel.kidsedu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
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

			//���δ�����Ϣ
			AppModel model=new AppModel();
			model.setFileSize(0);
			model.setPackageName("com.morningtel.kidsedu");
			model.setFileUrl("appfiles/8650d94c-0670-4f7a-b21b-46132a9dd366.apk");
			model.setName("�׵���԰");
			model.setId(13471);
			model.setIconUrl("pics/6d0cb871-c6f5-4512-af8b-5a2c6aab4fa3.png");
			model.setMobiledesc("");
			model.setDownloadCount(1036);
			model.setResourceType(8);
			Conn.getInstance(getApplicationContext()).insertAppModel(model);
			Conn.getInstance(getApplicationContext()).updateModel("com.morningtel.kidsedu", 1);
		}
		download_app_maps=new HashMap<String, Integer>();
		update_maps=new HashMap<String, String>();
		download_music_maps=new HashMap<String, Integer>();
		download_stop_list=new ArrayList<String>();
		
		//�������·���
		Intent intent=new Intent(getApplicationContext(), UpdateService.class);
		startService(intent);		
	}
	
	/**
	 * ��ȡֹͣ�����б�
	 * @return
	 */
	public synchronized ArrayList<String> getDownload_stop_list() {
		return download_stop_list;
	}
}

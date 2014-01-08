package com.morningtel.kidsedu;

import java.io.File;
import java.util.HashMap;

import com.morningtel.kidsedu.db.Conn;

import android.app.Application;
import android.os.Environment;

public class KEApplication extends Application {
	
	public String kidsIconUrl="http://res.kidsedu.com/";	
	public String kidsDataUrl="http://www.kidsedu.com";
	
	//下载信息记录
	public HashMap<String, Integer> download_maps=null;
	//允许切换播放标志
	public boolean isMusicPlay=true;
	//当前正在播放的音乐
	public String musicName="";

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
		}
		System.out.println(Conn.getInstance(getApplicationContext()).getAppModelList().size());
		download_maps=new HashMap<String, Integer>();
	}
}

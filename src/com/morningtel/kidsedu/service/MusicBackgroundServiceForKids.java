package com.morningtel.kidsedu.service;

import java.io.File;
import java.io.FileInputStream;

import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicBackgroundServiceForKids extends Service {

	public final static String START="action_start";
	public final static String PAUSE="action_pause";
	public final static String STOP="action_stop";
	public final static String RESUME="action_resume";	
	
	public static String currentAction=STOP;
	
	MediaPlayer mediaPlayer=null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent.getExtras()==null) {
			return 0;
		}
		String action=intent.getExtras().getString("action");
		if(action.equals(MusicBackgroundServiceForKids.START)) {
			if(mediaPlayer!=null){
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer=null;
	        }
			mediaPlayer=new MediaPlayer();
			mediaPlayer.setLooping(false);
			AppModel model=Conn.getInstance(MusicBackgroundServiceForKids.this).getSingleMusicModel(Integer.parseInt(intent.getExtras().getString("id")));
			if(model==null) {
				CommonUtils.showCustomToast(MusicBackgroundServiceForKids.this, "未找到相应的资源信息");
				return super.onStartCommand(intent, flags, startId);
			}
			String filePath=model.getFileUrl().substring(model.getFileUrl().lastIndexOf("/"), model.getFileUrl().length());
			try {
				File file=new File("/data/data/"+getPackageName()+"/kidsedu"+filePath); 
				FileInputStream fis=new FileInputStream(file); 
				mediaPlayer.setDataSource(fis.getFD()); 
				if(!mediaPlayer.isPlaying()) {				
					if(mediaPlayer!=null) {
						mediaPlayer.prepare();
						mediaPlayer.start();
						currentAction=START;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(action.equals(MusicBackgroundServiceForKids.PAUSE)) {
			if(mediaPlayer!=null&&mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				currentAction=PAUSE;
			}
			else {
				CommonUtils.showCustomToast(MusicBackgroundServiceForKids.this, "请先使用音频播放功能");
				return super.onStartCommand(intent, flags, startId);
			}
		}
		else if(action.equals(MusicBackgroundServiceForKids.RESUME)) {
			if(mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
				mediaPlayer.start();
				currentAction=START;
			}
			else {
				CommonUtils.showCustomToast(MusicBackgroundServiceForKids.this, "请先暂停音频播放功能");
				return super.onStartCommand(intent, flags, startId);
			}
		}
		else if(action.equals(MusicBackgroundServiceForKids.STOP)) {
			if(mediaPlayer!=null){
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer=null;
				currentAction=STOP;
	        }
			else {
				CommonUtils.showCustomToast(MusicBackgroundServiceForKids.this, "请先使用音频播放功能");
				return super.onStartCommand(intent, flags, startId);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
}

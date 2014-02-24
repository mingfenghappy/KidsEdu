package com.morningtel.kidsedu.service;

import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;

public class MusicBackgroundServiceForKids extends Service {

	public final static String START="action_start";
	public final static String PAUSE="action_pause";
	public final static String STOP="action_stop";
	
	MediaPlayer mediaPlayer=null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		String action=intent.getExtras().getString("action");
		if(action.equals(MusicBackgroundServiceForKids.START)) {
			if(mediaPlayer!=null){
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer=null;
	        }
			mediaPlayer=new MediaPlayer();
			mediaPlayer.setLooping(false);
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mediaPlayer.start();
				}
			});
			mediaPlayer.reset();
			AppModel model=Conn.getInstance(MusicBackgroundServiceForKids.this).getSingleMusicModel(Integer.parseInt(intent.getExtras().getString("id")));
			if(model==null) {
				CommonUtils.showCustomToast(MusicBackgroundServiceForKids.this, "δ�ҵ���Ӧ����Դ��Ϣ");
				return super.onStartCommand(intent, flags, startId);
			}
			String filePath=model.getFileUrl().substring(model.getFileUrl().lastIndexOf("/"), model.getFileUrl().length());
			try {
				mediaPlayer.setDataSource("/data/data/"+getPackageName()+"/kidsedu"+filePath);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
				if(!mediaPlayer.isPlaying()) {				
					if(mediaPlayer!=null) {
						mediaPlayer.prepareAsync();							
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
			}
			else {
				CommonUtils.showCustomToast(MusicBackgroundServiceForKids.this, "����ʹ����Ƶ���Ź���");
				return super.onStartCommand(intent, flags, startId);
			}
		}
		else if(action.equals(MusicBackgroundServiceForKids.STOP)) {
			if(mediaPlayer!=null){
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer=null;
	        }
			else {
				CommonUtils.showCustomToast(MusicBackgroundServiceForKids.this, "����ʹ����Ƶ���Ź���");
				return super.onStartCommand(intent, flags, startId);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

}
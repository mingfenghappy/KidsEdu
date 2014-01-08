package com.morningtel.kidsedu.service;

import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.RemoteViews;

public class MusicBackgroundService extends Service {
	
	NotificationManager manager=null;
	Notification no=null;
	RemoteViews view=null;
	
	public MediaPlayer mediaPlayer=null;
	
	//首次加载
	boolean isFirstLoad=false;
	//相关缓存数据
	String name="";
	String url="";
	boolean isNewStartFlag=false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isFirstLoad=true;
		
		mediaPlayer=new MediaPlayer();
		mediaPlayer.setLooping(false);
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				view.setImageViewResource(R.id.play_state, R.drawable.play_sel);
				no.contentView=view;
				manager.notify(0, no);
			}});
		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mediaPlayer.start();
				
				view.setImageViewResource(R.id.play_state, R.drawable.pause_sel);
				no.contentView=view;
				manager.notify(0, no);
			}
		});
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try {
			((KEApplication) getApplicationContext()).isMusicPlay=true;
			
			//判断服务点击来源
			isNewStartFlag=intent.getExtras().getBoolean("isNewStartFlag");
			if(isNewStartFlag) {
				name=intent.getExtras().getString("name");
				url=intent.getExtras().getString("url");
				
				if(isFirstLoad) {
					manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
					no=new Notification();
					no.flags=Notification.FLAG_ONGOING_EVENT;
					no.icon=R.drawable.ic_launcher;
					no.when=System.currentTimeMillis();
					no.tickerText="易迪乐园故事开始播放";
					view=new RemoteViews(getPackageName(), R.layout.view_play);
					view.setTextViewText(R.id.play_name, name);
					view.setImageViewResource(R.id.play_state, R.drawable.play_sel);
					no.contentView=view;
					Intent intent_new=new Intent(MusicBackgroundService.this, MusicServiceActivity.class);
					PendingIntent pi=PendingIntent.getActivity(MusicBackgroundService.this, 0, intent_new, 0);
					no.contentIntent=pi;
					manager.notify(0, no);
					
					isFirstLoad=false;
				}
				else {
					view.setTextViewText(R.id.play_name, name);
					view.setImageViewResource(R.id.play_state, R.drawable.play_sel);
					no.contentView=view;
					manager.notify(0, no);
				}
				
				try {
					mediaPlayer.reset();
					mediaPlayer.setDataSource(url);
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
					if(!mediaPlayer.isPlaying()) {				
						if(mediaPlayer!=null) {
							mediaPlayer.prepareAsync();
							
						}
					}
				} catch (Exception e) {
					view.setImageViewResource(R.id.play_state, R.drawable.play_sel);
					no.contentView=view;
					manager.notify(0, no);
					
					e.printStackTrace();
				}
			}
			else {
				if(!mediaPlayer.isPlaying()) {
					mediaPlayer.start();
					
					view.setImageViewResource(R.id.play_state, R.drawable.pause_sel);
					no.contentView=view;
					manager.notify(0, no);
				}
				else {
					mediaPlayer.pause();
					
					view.setImageViewResource(R.id.play_state, R.drawable.play_sel);
					no.contentView=view;
					manager.notify(0, no);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manager.cancel(0);
		if(mediaPlayer!=null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;
         }
	}

}

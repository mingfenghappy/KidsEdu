package com.morningtel.kidsedu.service;

import com.morningtel.kidsedu.commons.CommonUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GuardService extends Service {
	
	AlarmManager mAlarmManager = null;
	PendingIntent mPendingIntent = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(getApplicationContext(), GuardService.class);        
		mAlarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
		mPendingIntent=PendingIntent.getService(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 3000, mPendingIntent);
		
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(CommonUtils.isNeedStopLimit(GuardService.this)) {
			System.out.println("¸æ¾¯¿ªÊ¼");
			Intent intent_=new Intent();
			intent_.setAction("guard_service");
			sendBroadcast(intent_);
		}
        return START_STICKY;
	}
	
}

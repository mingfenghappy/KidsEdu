package com.morningtel.kidsedu.receiver;

import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.service.GuardService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GuardReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//全广播服务均可打开守护服务
		System.out.println("由 "+intent.getAction()+" 打开守护服务");
		//重复打开
		if(!CommonUtils.isServiceWorked(context, "com.morningtel.kidsedu.service.GuardService")) {
			System.out.println("守护服务不存在，重启守护服务");
			Intent intent_guard = new Intent(context, GuardService.class);
			intent_guard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(intent_guard);
		}
	}
	
}

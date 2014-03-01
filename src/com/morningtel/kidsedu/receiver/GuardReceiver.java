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
		//ȫ�㲥������ɴ��ػ�����
		System.out.println("�� "+intent.getAction()+" ���ػ�����");
		//�ظ���
		if(!CommonUtils.isServiceWorked(context, "com.morningtel.kidsedu.service.GuardService")) {
			System.out.println("�ػ����񲻴��ڣ������ػ�����");
			Intent intent_guard = new Intent(context, GuardService.class);
			intent_guard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(intent_guard);
		}
	}
	
}

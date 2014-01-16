package com.morningtel.kidsedu.receiver;

import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AppReceiver extends BroadcastReceiver {
	
	public final static String appChange="app_change";
	public final static String appUpdate="app_update";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//���չ㲥��ϵͳ������ɺ����г���      
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {      
             
        }      
        //���չ㲥���豸���°�װ��һ��Ӧ�ó�������Զ������°�װӦ�ó���      
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {      
            String packageName=intent.getDataString().substring(8);      
            System.out.println("---------------" + packageName);  
            sendBroadCast(context, packageName);
            Conn.getInstance(context).updateModel(packageName, 1);
            //ɾ���ļ�
            CommonUtils.deleteFile(Conn.getInstance(context).getFileName(packageName));
        }      
        //���չ㲥���豸��ɾ����һ��Ӧ�ó������      
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {      
        	String packageName=intent.getDataString().substring(8);      
            System.out.println("---------------" + packageName); 
            ((KEApplication) context.getApplicationContext()).download_maps.remove(packageName);
            sendBroadCast(context, packageName);
            Conn.getInstance(context).updateModel(packageName, 0);
            if(((KEApplication) context.getApplicationContext()).update_maps.containsKey(packageName)) {
            	((KEApplication) context.getApplicationContext()).update_maps.remove(packageName);
            }
        } 
	}
	
	/**
	 * ���͹㲥
	 * @param context
	 */
	public void sendBroadCast(Context context, String packageName) {
		Intent intent=new Intent();
		intent.setAction(appChange);
		Bundle bundle=new Bundle();
		bundle.putString("packageName", packageName);
		intent.putExtras(bundle);
		context.sendBroadcast(intent);
	}

}

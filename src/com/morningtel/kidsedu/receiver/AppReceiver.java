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
		//接收广播：系统启动完成后运行程序      
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {      
             
        }      
        //接收广播：设备上新安装了一个应用程序包后自动启动新安装应用程序。      
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {      
            String packageName=intent.getDataString().substring(8);      
            System.out.println("---------------" + packageName);  
            sendBroadCast(context, packageName);
            Conn.getInstance(context).updateModel(packageName, 1);
            //删除文件
            CommonUtils.deleteFile(Conn.getInstance(context).getFileName(packageName));
        }      
        //接收广播：设备上删除了一个应用程序包。      
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
	 * 发送广播
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

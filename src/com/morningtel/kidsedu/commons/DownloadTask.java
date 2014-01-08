package com.morningtel.kidsedu.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.model.JsonParse;
import com.morningtel.kidsedu.receiver.AppReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.RemoteViews;

public class DownloadTask extends AsyncTask<String, Integer, String> {
	
	Context context=null;
	int id=0;
	String name="";
	String packageName="";
	
	NotificationManager manager=null;
	Notification no=null;
	RemoteViews view=null;
	
	public void setParams(Context context, int id, String name, String packageName) {
		this.context=context;
		this.id=id;
		this.name=name;
		this.packageName=packageName;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		((KEApplication) context.getApplicationContext()).download_maps.put(packageName, 0);
		
		manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		no=new Notification();
		no.flags=Notification.FLAG_AUTO_CANCEL;
		no.icon=R.drawable.ic_launcher;
		no.when=System.currentTimeMillis();
		no.tickerText="易迪乐园下载提示";
		view=new RemoteViews(context.getPackageName(), R.layout.view_download_withfile);
		view.setProgressBar(R.id.download_pb, 100, 0, false);
		view.setTextViewText(R.id.download_rate, "0%");
		view.setTextViewText(R.id.download_name, name+"准备下载");
		no.contentView=view;
		Intent intent=new Intent(context, NotificationActivity.class);
		PendingIntent pi=PendingIntent.getActivity(context, 0, intent, 0);
		no.contentIntent=pi;
		manager.notify(id, no);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		switch(Integer.parseInt(result)) {
		case 2:
			break;
		case 1:
			break;
		case -1:
			CommonUtils.showCustomToast(context, "获取下载文件信息出现异常");
			sendBroadCast(packageName);
			break;
		case -2:
			CommonUtils.showCustomToast(context, "解析下载文件信息出现异常");
			sendBroadCast(packageName);
			break;
		case -3:
			CommonUtils.showCustomToast(context, "下载文件出现异常");
			sendBroadCast(packageName);
			break;
		case -4:
			CommonUtils.showCustomToast(context, "下载文件出现异常");
			sendBroadCast(packageName);
			break;
		}
		no.defaults=Notification.DEFAULT_SOUND;
		manager.cancel(id);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		System.out.println("已经下载进度"+values[0]);
		view.setProgressBar(R.id.download_pb, 100, values[0], false);
		view.setTextViewText(R.id.download_rate, values[0]+"%");
		view.setTextViewText(R.id.download_name, name+"正在下载");
		no.contentView=view;
		manager.notify(id, no);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		String result="";
		
		String id=params[0];
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("aid", id);
		String webResult=CommonUtils.getWebData(map, ((KEApplication) context.getApplicationContext()).kidsDataUrl+"/data/json/app/AppByAid");
		if(CommonUtils.convertNull(webResult).equals("")) {
			result="-1";
			return result;
		}
		AppModel model=JsonParse.getAppModelByAid(webResult);
		if(model==null) {
			result="-2";
			return result;
		}

		Conn.getInstance(context).insertModel(model);
		
		String url_=((KEApplication) context.getApplicationContext()).kidsIconUrl+model.getFileUrl();
		final String filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/kidsedu/temp/"+model.getFileUrl().substring(model.getFileUrl().indexOf("/")+1, model.getFileUrl().length());
		File file=new File(filePath);
		int completeSize=0;
		if(file.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				completeSize=fis.available();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		}				
		long fileSize=init(url_);
		HttpURLConnection conn=null;
		RandomAccessFile raf=null;
		InputStream is=null;
		
		try {
			URL u = new URL(url_);
			conn=(HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");
			//文件已经下载完毕或者已经存在
			if(completeSize==fileSize) {
				result="1";
			}
			else {
				//conn.setRequestProperty("Range", "bytes="+completeSize+"-"+(fileSize-1));
				// 设置范围，格式为Range：bytes x-y;
				conn.setRequestProperty("User-Agent", "NetFox");
				String sProperty = "bytes=" + completeSize + "-" + fileSize;
				conn.setRequestProperty("RANGE", sProperty);
				
				File file_new=new File(filePath);
            	raf=new RandomAccessFile(file_new, "rw");
            	raf.seek(completeSize);
            	is=conn.getInputStream();
            	byte[] b=new byte[1024];
            	long total=completeSize;
            	int count=0;
            	//记录上一次下载的百分比
				int downloadPercent=0;
				while((count=is.read(b, 0, 1024))!=-1) {
            		raf.write(b, 0, count);
            		total+=count;
					int percent=(int) (total*100/fileSize);
					if(percent-downloadPercent>5) {
						publishProgress(percent);
						downloadPercent=percent;
						((KEApplication) context.getApplicationContext()).download_maps.put(model.getPackageName(), percent);
					}
            	}
				publishProgress(100);
				result="2";
				((KEApplication) context.getApplicationContext()).download_maps.put(model.getPackageName(), 100);
			}
			//下载完成之后直接安装
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					CommonUtils.install(filePath, context);
				}}).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result="-3";
		} finally {
			try {
				if(is!=null) {
					is.close();
				}
				if(raf!=null) {
					raf.close();
				}
				if(conn!=null) {
					conn.disconnect();
				}						
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result="-4";
			}
		}
		return result;
	}
	
	private long init(String url_) {
		long fileSize=0;
		try {
			URL url = new URL(url_);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			fileSize = connection.getContentLength();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileSize;
	}
	
	/**
	 * 发送广播
	 * @param context
	 */
	public void sendBroadCast(String packageName) {
		((KEApplication) context.getApplicationContext()).download_maps.remove(packageName);
		Intent intent=new Intent();
		intent.setAction(AppReceiver.appChange);
		Bundle bundle=new Bundle();
		bundle.putString("packageName", packageName);
		intent.putExtras(bundle);
		context.sendBroadcast(intent);
	}

}

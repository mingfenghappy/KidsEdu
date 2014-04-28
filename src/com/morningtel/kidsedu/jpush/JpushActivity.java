package com.morningtel.kidsedu.jpush;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import cn.jpush.android.api.JPushInterface;

import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.commons.CommonUtils;

public class JpushActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		init();
		
		finish();
		
	}
	
	private void init() {
		
		final Handler handler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what) {
				case 2:
					
				case 1:
					CommonUtils.showCustomToast(JpushActivity.this, "文件下载完成");
					break;
				case -1:
					CommonUtils.showCustomToast(JpushActivity.this, "获取下载文件信息出现异常");
					break;
				case -2:
					CommonUtils.showCustomToast(JpushActivity.this, "解析下载文件信息出现异常");
					break;
				case -3:
					
				case -4:
					CommonUtils.showCustomToast(JpushActivity.this, "下载文件出现异常");
					break;
				}
			}
		};
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String flag="";
				String url_="";
				try {
					JSONObject obj=new JSONObject(getIntent().getExtras().getString(JPushInterface.EXTRA_EXTRA));
					flag=obj.getString("flag");
					url_=obj.getString("url");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            
				if(flag.equals("update")) {			
					Message m=new Message();
					final String filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/kidsedu/temp/"+url_.substring(url_.lastIndexOf("/")+1, url_.length());
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
							m.what=1;
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
							while((count=is.read(b, 0, 1024))!=-1) {
								raf.write(b, 0, count);
			            		total+=count;
			            	}
							if(total>fileSize) {
								m.what=-3;
								file_new.delete();
								return;
							}				
						}
						//下载完成之后直接安装
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								CommonUtils.install(filePath, JpushActivity.this);
							}}).start();			
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						m.what=-3;
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
							m.what=-4;
						}
					}
					handler.sendMessage(m);
				}
			}
			
		}).start();
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
}

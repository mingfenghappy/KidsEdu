package com.morningtel.kidsedu.commons;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.service.MusicBackgroundServiceForKids;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class CommonUtils {
	
	public static String getWebData(HashMap<String, String> map, String url) {
		ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> it=map.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> entry=it.next();
			params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		HttpPost post=new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));//����post���� �����ñ����ʽ
			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client=new DefaultHttpClient();
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			HttpResponse resp=client.execute(post);
			if(resp.getStatusLine().getStatusCode()==200) {
				return EntityUtils.toString(resp.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * ��Ƶ�ߴ�Ŵ�
	 * @param width
	 * @param height
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static float getScale(int width, int height, int screenWidth, int screenHeight) {
		float scaleW=(float) screenWidth/width;
		float scaleH=(float) screenHeight/height;
		return scaleH>scaleW?scaleW:scaleH;
	}
	
	/**
	 * ����������
	 * @param time
	 * @return
	 */
	public static String toTime(int time) {
		time/=1000;
		int minute=time/60;
		int second=time%60;
		minute%=60;
		return String.format("%02d:%02d", minute, second);
	}
	
	public static String convertNull(String returnValue) {
        try {
            returnValue = (returnValue==null||(returnValue!=null&&returnValue.equals("null")))?"":returnValue;
        } catch (Exception e) {
            returnValue = "";
        }
        return returnValue;
    }
	
	/**
	 * ��ȡ��Ļ�ܶ���Ϣ
	 * @param context
	 * @return
	 */
	public static float getDisplayParams(Context context) {
		DisplayMetrics dm=new DisplayMetrics();
		dm=context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.density;
	}
	
	/**
	 * �Զ��嵯����
	 * @param context
	 * @param str
	 */
	public static void showCustomToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * ��ȡappͼ��
	 * @param iconName
	 * @return
	 */
	public static String getIconAdd(String iconName) {
		return iconName.replaceAll("(?:.jpg|.JPG|.png|.PNG)","s80x80.png");
	}
	
	/**
	 * ��װ
	 * @param path ����·��
	 * @param context
	 */
	public static void install(final String path, final Context context) {
		System.out.println("��ʼ��װ"+path);
		boolean flag=CommonUtils.getRootAhth();
		Log.d("Execute", "�Ƿ���ROOTȨ��"+flag);
		//��Ҫ��װAPK��·��
		boolean b=false;
		if(flag){
			File newFile = new File(path);
			System.out.println("pm install "+newFile.getAbsolutePath());
			b=CommonUtils.Execute("pm install "+newFile.getAbsolutePath());
			//newFile.delete();
			Log.d("Execute", b?"�Ƿ�װ�ɹ�"+ "��":"�Ƿ�װ�ɹ�"+ "��");
			
		}
		else {
			Intent intent=new Intent(Intent.ACTION_VIEW); 
			intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive"); 
			context.startActivity(intent);
		}
	}

	/**
	 * �ж��Ƿ��ȡrootȨ��
	 * @return
	 */
    public static boolean getRootAhth() {  
        Process process=null;  
        DataOutputStream os=null;  
        try {  
            process=Runtime.getRuntime().exec("su");  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes("exit\n");  
            os.flush();  
            int exitValue=process.waitFor();  
            if (exitValue==0) {  
                return true;  
            } else {  
                return false;  
            }  
        } catch (Exception e) {  
            return false;  
        } finally {  
            try {  
                if (os != null) {  
                    os.close();  
                }  
                process.destroy();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    /**
     * ж��
     * @param packageName ����
     * @param context
     */
    public static void uninstall(final String packageName, final Context context) {
    	Uri packageURI = Uri.parse("package:"+packageName);   
    	Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);   
    	context.startActivity(uninstallIntent);
    }
    
    /**
     * ��apk
     * @param context
     */
    public static void openApp(Context context, String packageName) {
    	PackageManager packageManager=context.getPackageManager(); 
    	Intent intent=new Intent(); 
    	try { 
    	    intent=packageManager.getLaunchIntentForPackage(packageName); 
    	} catch (Exception e) { 
    		e.printStackTrace();
    	} 
    	context.startActivity(intent); 
    }    

    /**
     * ִ��cmd����
     * @param command
     * @return
     */
    private static boolean Execute(String command) {
         Process process=null;
         DataOutputStream os=null;
         String s = "";
         try {
              process=Runtime.getRuntime().exec("su");
              os=new DataOutputStream(process.getOutputStream());
              os.writeBytes(command + "\n");
              os.writeBytes("exit\n");
              os.flush();
              process.waitFor();
              BufferedReader in=new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
              String line = null;
              while ((line=in.readLine())!=null) {
                   s+=line+"/n";
              }
              Log.d("Execute", "-----------"+s);
              if (s.toLowerCase().contains("success")) {
                   return true;
              }
         } catch (Exception e) {
              Log.d("Execute", "ROOT REE" + e.getMessage());
              return false;
         } finally {
              try {
                   if (os!=null) {
                        os.close();
                   }
                   process.exitValue();
              } catch (Exception e) {
            	  
              }
         }
         Log.d("Execute", "Root SUC ");
         return true;
    }
    
    /**
     * ͨ�������ж��ļ��Ƿ����
     * @param packageName
     * @return
     */
    public static boolean checkAppInstall(String packageName, Context context) {  
        if (packageName==null||"".equals(packageName))  
            return false;  
        PackageInfo packageInfo=null;
        try {
            packageInfo=context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo==null?false:true;
    }
    
    /**
     * ��ȡȫ����װ��Ϣ
     * @param context
     * @return
     */
    public static HashMap<String, Integer> getWholeAPPInfo(Context context) {
    	HashMap<String, Integer> appMap=new HashMap<String, Integer>(); //�����洢��ȡ��Ӧ����Ϣ����
    	List<PackageInfo> packages=context.getPackageManager().getInstalledPackages(0);
    	for(int i=0;i<packages.size();i++) { 
	    	PackageInfo packageInfo=packages.get(i); 
	    	if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0) {
	    		//�����ϵͳӦ�ã��������appMap
		    	appMap.put(packageInfo.packageName, packageInfo.versionCode); 
	    	}
    	}
    	return appMap;
    }
    
    public static Drawable getAPPIcon(Context context, String packageName) {
    	List<PackageInfo> packages=context.getPackageManager().getInstalledPackages(0);
    	for(int i=0;i<packages.size();i++) { 
	    	PackageInfo packageInfo=packages.get(i); 
	    	if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0) {
	    		//�����ϵͳӦ�ã��������appMap
	    		if(packageInfo.packageName.equals(packageName)) {
	    			return packageInfo.applicationInfo.loadIcon(context.getPackageManager());
	    		}
	    	}
    	}
    	return null;
    }
    
    /**
     * ��Drawableת��ΪBitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Context context, Drawable drawable) {       
    	if(drawable==null) {
    		return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
    	}
    	Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity()!=PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
    	Canvas canvas = new Canvas(bitmap);
    	drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    	drawable.draw(canvas);
    	return bitmap;
    }
    
    public static void deleteFile(String fileName) {
    	File file=new File(Environment.getExternalStorageDirectory().getPath()+"/kidsedu/temp"+File.separator+fileName);
    	if(file.exists()) {
    		file.delete();
    	}
    }
    
    
    final static int NETWORKSTATE_NULL=0;
    final static int NETWORKSTATE_MOBILE=1;
    final static int NETWORKSTATE_WIFI=2;
    /**
     * �ж�����״̬
     * @param context
     * @return
     */
    public static int checkNetWorkState(Context context) {
    	ConnectivityManager connManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if(connManager.getActiveNetworkInfo()!=null) {
        	 NetworkInfo ni_wifi=connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        	 if(ni_wifi.isAvailable()&&ni_wifi.isConnected()) {
        		 return NETWORKSTATE_WIFI;
        	 }
        	 NetworkInfo ni_mobile=connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        	 if(ni_mobile.isAvailable()&&ni_mobile.isConnected()) {
        		 return NETWORKSTATE_MOBILE;
        	 }
        	 return NETWORKSTATE_NULL;
        }
        else {
        	return NETWORKSTATE_NULL;
        }
    }
    
    /** 
     * ���Ƶ����ļ� 
     * @param oldPath 
     * @param newPath 
     * @return  
     */ 
    public static void copyFile(String oldPath, String newPath) { 
    	try { 
    		int bytesum=0; 
    		int byteread=0; 
    		File oldfile=new File(oldPath); 
    		if (oldfile.exists()) { 
    			InputStream inStream=new FileInputStream(oldPath); 
    			FileOutputStream fs=new FileOutputStream(newPath); 
    			byte[] buffer=new byte[1444]; 
    			while ((byteread = inStream.read(buffer))!=-1) { 
    				bytesum+=byteread; 
    				fs.write(buffer, 0, byteread); 
    			} 
    			inStream.close(); 
    		} 
    	} catch (Exception e) { 
           e.printStackTrace(); 
       } 
    } 
    
    /**
     * ͨ��assets�����ļ�
     * @param oldName
     * @param newPath
     * @param context
     */
    public static void copyAssetsFile(String oldName, String newPath, Context context) {
    	AssetManager manager=context.getAssets();
    	try {
    		int bytesum=0; 
    		int byteread=0; 
			InputStream inStream=manager.open(oldName);
			FileOutputStream fs=new FileOutputStream(newPath); 
			byte[] buffer=new byte[1444]; 
			while ((byteread = inStream.read(buffer))!=-1) { 
				bytesum+=byteread; 
				fs.write(buffer, 0, byteread); 
			} 
			inStream.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * ������Ա��Ϣ
     * @param context
     * @param userId
     * @param userName
     * @param userIcon
     */
    public static void insertUserInfo(Context context, int userId, String userName, String userIcon, String userToken) {
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor=sp.edit();
    	editor.putInt("userId", userId);
    	editor.putString("userName", userName);
    	editor.putString("userIcon", userIcon);
    	editor.putString("userToken", userToken);
    	editor.commit();
    }
    
    /**
     * ��ȡ��Ա��Ϣ
     * @param context
     * @return
     */
    public static HashMap<String, String> getUserInfo(Context context) {
    	HashMap<String, String> map=new HashMap<String, String>();
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	map.put("userId", ""+sp.getInt("userId", 0));
    	map.put("userName", sp.getString("userName", ""));
    	map.put("userIcon", ""+sp.getString("userIcon", ""));
    	map.put("userToken", ""+sp.getString("userToken", ""));
    	return map;
    }
    
    /**
     * ��ȡwifi mac��ַ
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
    	try {
    		String macAddress = "";
    	    WifiManager wifiMgr=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	    WifiInfo info=(null==wifiMgr?null:wifiMgr.getConnectionInfo());
    	    if (null!=info) {
    	        macAddress=info.getMacAddress();
    	    }
    	    if(!convertNull(macAddress).equals("")) {
    	    	if(macAddress.indexOf(":")!=-1) {
    	    		macAddress=macAddress.replace(":", "");
    	    		return macAddress;
    	    	}
    	    }
    	    return "";
    	} catch(Exception e) {
    		return "";
    	}   	
	}
    
    /**
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
     */
    public static int dip2px(Context context, float dpValue) {
    	float scale=context.getResources().getDisplayMetrics().density;
    	return (int) (dpValue*scale+0.5f);
    }
     
    /**
     * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
     */
    public static int px2dip(Context context, float pxValue) {
    	float scale=context.getResources().getDisplayMetrics().density;
    	return (int) (pxValue/scale+0.5f);
    }
    
    /**
     * �ж��ļ��Ƿ�װ��������
     * @param context
     * @param path
     * @return
     */
    public static boolean checkAPKState(Context context, String path) {
		PackageInfo pi=null;
		try {
			PackageManager pm=context.getPackageManager();
			pi=pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
			return pi==null?false:true;
		} catch(Exception e) {
			return false;
		}
	}
    
    /**
     * ���ԭ�����õ�home״̬
     * @param context
     */
    public static void clearHome(Context context) {
    	//���ԭ�����õ�home״̬
    	context.getPackageManager().clearPackagePreferredActivities(context.getPackageName());
    }
    
    /**
	 * �жϷ����Ƿ����
	 * @param context
	 * @return
	 */
	public static boolean isServiceWorked(Context context, String serviceName) {  
		ActivityManager myManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);  
		for(int i = 0 ; i<runningService.size();i++) {  
			if(runningService.get(i).service.getClassName().toString().equals(serviceName)) {  
				return true;  
			}  
		}  
		return false;  
	}
    
    /**
     * ��ͯģʽ���ŷ���ʼ
     * @param context
     * @param id
     */
    public static void kidsMusicStart(Context context, String id) {
    	Intent intent=new Intent(context, MusicBackgroundServiceForKids.class);
    	Bundle bundle=new Bundle();
    	bundle.putString("action", MusicBackgroundServiceForKids.START);
    	bundle.putString("id", id);
    	intent.putExtras(bundle);
    	context.startService(intent);
    }
    
    /**
     * ��ͯģʽ���ŷ�����ͣ
     * @param context
     */
    public static void kidsMusicPause(Context context) {
    	Intent intent=new Intent(context, MusicBackgroundServiceForKids.class);
    	Bundle bundle=new Bundle();
    	bundle.putString("action", MusicBackgroundServiceForKids.PAUSE);
    	intent.putExtras(bundle);
    	context.startService(intent);
    }
    
    /**
     * ��ͯģʽ���ŷ���ֹͣ
     * @param context
     */
    public static void kidsMusicStop(Context context) {
    	Intent intent=new Intent(context, MusicBackgroundServiceForKids.class);
    	Bundle bundle=new Bundle();
    	bundle.putString("action", MusicBackgroundServiceForKids.STOP);
    	intent.putExtras(bundle);
    	context.startService(intent);
    }
    
    /**
     * ��ͯģʽ���ŷ���ָ�
     * @param context
     */
    public static void kidsMusicResume(Context context) {
    	Intent intent=new Intent(context, MusicBackgroundServiceForKids.class);
    	Bundle bundle=new Bundle();
    	bundle.putString("action", MusicBackgroundServiceForKids.RESUME);
    	intent.putExtras(bundle);
    	context.startService(intent);
    }
    
    /**
     * ��ǰ��ͯģʽ����״̬
     * @return
     */
    public static String kidsMusicState() {
    	return MusicBackgroundServiceForKids.currentAction;
    }
    
    /**
     * ����/�ر�ʱ����������
     */
    public static void setTimeLimitState(Context context, boolean isStart) {
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor=sp.edit();
    	editor.putBoolean("isStart", isStart);
    	editor.putInt("minute", sp.getInt("minute", 1));
    	if(isStart) {
    		editor.putLong("limitStateStartTime", System.currentTimeMillis());
        	//�����澯��־λ
        	editor.putBoolean("isStartWarm", true);
    	}
    	else {
    		editor.putLong("lastTime", 0);
    		//�رո澯��־λ
        	editor.putBoolean("isStartWarm", false);
    	}
    	editor.commit();
    }
    
    /**
     * ʱ����������
     */
    public static void setTimeLimitMinute(Context context, int minute) {
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor=sp.edit();
    	editor.putInt("minute", minute);
    	editor.commit();
    }
    
    /**
     * ��ȡ���úõ�ʱ��
     * @param context
     * @return
     */
    public static int getTimeLimit(Context context) {
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	if(sp.getBoolean("isStart", false)) {
    		return sp.getInt("minute", -1);
    	}
    	else {
    		return -1;
    	}
    }
    
    /**
     * �ָ�����ʱ���ʱ
     * @param context
     */
    public static void resumeLimitState(Context context) {
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	if(sp.getBoolean("isStart", false)) {
    		SharedPreferences.Editor editor=sp.edit();
    		editor.putLong("limitStateStartTime", System.currentTimeMillis());
        	editor.putBoolean("isStartWarm", true);
        	editor.commit();
    	}
    }
    
    /**
     * ��ͣ����ʱ���ʱ
     * @param context
     */
    public static void pauseLimitState(Context context) {
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor=sp.edit();
    	long startTime=sp.getLong("limitStateStartTime", 0);
    	editor.putLong("lastTime", System.currentTimeMillis()-startTime);
    	editor.putBoolean("isStartWarm", false);
    	editor.commit();
    }
    
    /**
     * ��������ʱ���ʱ
     * @param context
     */
    public static void resetLimitState(Context context) {
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor=sp.edit();
    	editor.putBoolean("isStartWarm", true);
    	editor.putLong("limitStateStartTime", System.currentTimeMillis());
    	editor.putLong("lastTime", 0);
    	editor.commit();
    }
    
    /**
     * �ж��Ƿ��Ѿ���ʱ
     * @param context
     * @return
     */
    public static boolean isNeedStopLimit(Context context) {
    	SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
    	//�ж�ʱ�����÷�����û�п��������û�п���������
    	if(sp.getBoolean("isStart", false)) {
    		//�Ƿ��Ѿ������澯�����û�п���������
    		if(sp.getBoolean("isStartWarm", false)) {
    			long startTime=sp.getLong("limitStateStartTime", 0);
    			long lastTime=sp.getLong("lastTime", 0)+(System.currentTimeMillis()-startTime);
            	//�ڴ򿪼�ص�����£������ǰ�ۼ�ʱ��������úõ�ʱ�䣬���ظ澯
            	if(lastTime>=1000*60*sp.getInt("minute", 1)) {
        			SharedPreferences.Editor editor=sp.edit();
                	editor.putBoolean("isStartWarm", false);
                	editor.commit();
                	System.out.println("���ڸ澯");
            		return true;
            	}
            	else {
            		System.out.println("�澯���������");
            		return false;
            	}
    		}
    		else {
    			System.out.println("�澯�����������أ������Ƿ�رո澯�����Ѿ��������澯");
            	return false;
    		}    		
    	}
    	else {
    		System.out.println("�澯δ����");
    		return false;
    	}
    }
    
}

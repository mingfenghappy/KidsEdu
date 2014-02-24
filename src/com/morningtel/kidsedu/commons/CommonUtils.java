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
			post.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));//设置post参数 并设置编码格式
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
	 * 视频尺寸放大
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
	 * 将毫秒重组
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
	 * 获取屏幕密度信息
	 * @param context
	 * @return
	 */
	public static float getDisplayParams(Context context) {
		DisplayMetrics dm=new DisplayMetrics();
		dm=context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.density;
	}
	
	/**
	 * 自定义弹出框
	 * @param context
	 * @param str
	 */
	public static void showCustomToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 获取app图标
	 * @param iconName
	 * @return
	 */
	public static String getIconAdd(String iconName) {
		return iconName.replaceAll("(?:.jpg|.JPG|.png|.PNG)","s80x80.png");
	}
	
	/**
	 * 安装
	 * @param path 绝对路径
	 * @param context
	 */
	public static void install(final String path, final Context context) {
		System.out.println("开始安装"+path);
		boolean flag=CommonUtils.getRootAhth();
		Log.d("Execute", "是否有ROOT权限"+flag);
		//需要安装APK的路径
		boolean b=false;
		if(flag){
			File newFile = new File(path);
			System.out.println("pm install "+newFile.getAbsolutePath());
			b=CommonUtils.Execute("pm install "+newFile.getAbsolutePath());
			//newFile.delete();
			Log.d("Execute", b?"是否安装成功"+ "是":"是否安装成功"+ "否");
			
		}
		else {
			Intent intent=new Intent(Intent.ACTION_VIEW); 
			intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive"); 
			context.startActivity(intent);
		}
	}

	/**
	 * 判断是否获取root权限
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
     * 卸载
     * @param packageName 包名
     * @param context
     */
    public static void uninstall(final String packageName, final Context context) {
    	Uri packageURI = Uri.parse("package:"+packageName);   
    	Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);   
    	context.startActivity(uninstallIntent);
    }
    
    /**
     * 打开apk
     * @param context
     */
    public static void openApp(Context context, String packageName) {
    	PackageManager packageManager=context.getPackageManager(); 
    	Intent intent=new Intent(); 
    	try { 
    	    intent =packageManager.getLaunchIntentForPackage(packageName); 
    	} catch (Exception e) { 
    		e.printStackTrace();
    	} 
    	context.startActivity(intent); 
    }    

    /**
     * 执行cmd命令
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
     * 通过包名判断文件是否存在
     * @param packageName
     * @return
     */
    public static boolean checkAppInstall(String packageName, Context context) {  
        if (packageName==null||"".equals(packageName))  
            return false;  
        try {  
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);  
            return true;  
        } catch (NameNotFoundException e) {  
            return false;  
        }  
    }
    
    /**
     * 获取全部安装信息
     * @param context
     * @return
     */
    public static HashMap<String, Integer> getWholeAPPInfo(Context context) {
    	HashMap<String, Integer> appMap=new HashMap<String, Integer>(); //用来存储获取的应用信息数据
    	List<PackageInfo> packages=context.getPackageManager().getInstalledPackages(0);
    	for(int i=0;i<packages.size();i++) { 
	    	PackageInfo packageInfo=packages.get(i); 
	    	if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0) {
	    		//如果非系统应用，则添加至appMap
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
	    		//如果非系统应用，则添加至appMap
	    		if(packageInfo.packageName.equals(packageName)) {
	    			return packageInfo.applicationInfo.loadIcon(context.getPackageManager());
	    		}
	    	}
    	}
    	return null;
    }
    
    /**
     * 将Drawable转化为Bitmap
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
     * 判断网络状态
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
     * 复制单个文件 
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
     * 通过assets复制文件
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
     * 更新人员信息
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
     * 获取人员信息
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
     * 获取wifi mac地址
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
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
    	float scale=context.getResources().getDisplayMetrics().density;
    	return (int) (dpValue*scale+0.5f);
    }
     
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
    	float scale=context.getResources().getDisplayMetrics().density;
    	return (int) (pxValue/scale+0.5f);
    }
    
    /**
     * 儿童模式播放服务开始
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
     * 儿童模式播放服务暂停
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
     * 儿童模式播放服务停止
     * @param context
     */
    public static void kidsMusicStop(Context context) {
    	Intent intent=new Intent(context, MusicBackgroundServiceForKids.class);
    	Bundle bundle=new Bundle();
    	bundle.putString("action", MusicBackgroundServiceForKids.STOP);
    	intent.putExtras(bundle);
    	context.startService(intent);
    }
}

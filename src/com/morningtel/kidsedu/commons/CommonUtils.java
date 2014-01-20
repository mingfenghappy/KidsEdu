package com.morningtel.kidsedu.commons;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
    	    intent =packageManager.getLaunchIntentForPackage(packageName); 
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
        try {  
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);  
            return true;  
        } catch (NameNotFoundException e) {  
            return false;  
        }  
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
    
}

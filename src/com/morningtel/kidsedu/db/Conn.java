package com.morningtel.kidsedu.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.model.AppModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conn extends SQLiteOpenHelper {
	
	final static String DATABASE_NAME="kids";
	final static int DATABASE_VERSION=1;
	
	final static String _ID="_id";
	
	private static final String APP_TABLE="apptable";
	private static final String APP_PACKAGENAME="apppackagename";
	private static final String APP_INFO="appinfo";
	private static final String APP_FLAG="appflag";
	
	private static final String	MUSIC_TABLE="musictable";
	private static final String MUSIC_NAME="musicname";
	private static final String MUSIC_INFO="musicinfo";
	private static final String MUSIC_FLAG="musicflag";
	
	private static final String	VIDEO_TABLE="videotable";
	private static final String VIDEO_NAME="videoname";
	private static final String VIDEO_INFO="videoinfo";
	private static final String VIDEO_NUM="videonum";
	
	Context context=null;
	
	static Conn conn=null;
	
	public static Conn getInstance(Context context) {
		if(conn==null) {
			conn=new Conn(context);
		}
		return conn;
	}

	private Conn(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists "+APP_TABLE+"("+_ID+" integer primary key autoincrement not null, "+APP_INFO+" blob, "+APP_PACKAGENAME+" text, "+APP_FLAG+" INTEGER)");
		db.execSQL("create table if not exists "+MUSIC_TABLE+"("+_ID+" integer primary key autoincrement not null, "+MUSIC_INFO+" blob, "+MUSIC_NAME+" text, "+MUSIC_FLAG+" INTEGER)");
		db.execSQL("create table if not exists "+VIDEO_TABLE+"("+_ID+" integer primary key autoincrement not null, "+VIDEO_INFO+" blob, "+VIDEO_NAME+" text, "+VIDEO_NUM+" INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 将应用对象保存到数据库中
	 * @param model
	 */
	public void insertAppModel(Object obj) {
		synchronized (this) {
			SQLiteDatabase db_check=this.getReadableDatabase();
			Cursor cs=db_check.query(APP_TABLE, null, APP_PACKAGENAME+"=?", new String[]{((AppModel) obj).getPackageName()}, null, null, null);
			cs.moveToFirst();
			if(cs.getCount()>0) {
				cs.close();
				db_check.close();
				return;
			}
			else {
				cs.close();
				db_check.close();
			}
			ContentValues values=null;
			byte[] bytes=serialize((AppModel) obj);
			values=new ContentValues(3);
			values.put(APP_INFO, bytes);
			values.put(APP_PACKAGENAME, ((AppModel) obj).getPackageName());
			values.put(APP_FLAG, 0);
			SQLiteDatabase db=this.getWritableDatabase();
			db.beginTransaction();
			db.insert(APP_TABLE, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();	
		}			
	}
	
	/**
	 * 将音乐对象保存到数据库中
	 * @param model
	 */
	public void insertMusicModel(Object obj) {
		synchronized (this) {
			SQLiteDatabase db_check=this.getReadableDatabase();
			Cursor cs=db_check.query(MUSIC_TABLE, null, MUSIC_NAME+"=?", new String[]{((AppModel) obj).getName()}, null, null, null);
			cs.moveToFirst();
			if(cs.getCount()>0) {
				cs.close();
				db_check.close();
				return;
			}
			else {
				cs.close();
				db_check.close();
			}
			ContentValues values=null;
			byte[] bytes=serialize((AppModel) obj);
			values=new ContentValues(3);
			values.put(MUSIC_INFO, bytes);
			values.put(MUSIC_NAME, ((AppModel) obj).getName());
			values.put(MUSIC_FLAG, 0);
			SQLiteDatabase db=this.getWritableDatabase();
			db.beginTransaction();
			db.insert(MUSIC_TABLE, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();	
		}			
	}
	
	/**
	 * 将视频对象保存到数据库中
	 * @param model
	 */
	public void insertVideoModel(Object obj, int num) {
		synchronized (this) {
			SQLiteDatabase db_check=this.getReadableDatabase();
			Cursor cs=db_check.query(VIDEO_TABLE, null, VIDEO_NAME+"=?", new String[]{((AppModel) obj).getName()}, null, null, null);
			cs.moveToFirst();
			if(cs.getCount()>0) {
				cs.close();
				db_check.close();
				ContentValues values=new ContentValues(1);
				values.put(VIDEO_NUM, num);
				SQLiteDatabase db=this.getWritableDatabase();
				db.update(VIDEO_TABLE, values, VIDEO_NAME+"=?", new String[]{((AppModel) obj).getName()});
				db.close();
			}
			else {
				cs.close();
				db_check.close();
				ContentValues values=new ContentValues(3);
				byte[] bytes=serialize((AppModel) obj);
				values.put(VIDEO_INFO, bytes);
				values.put(VIDEO_NAME, ((AppModel) obj).getName());
				values.put(VIDEO_NUM, num);
				SQLiteDatabase db=this.getWritableDatabase();
				db.beginTransaction();
				db.insert(VIDEO_TABLE, null, values);
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();	
			}
			
		}			
	}
	
	/**
	 * 序列化
	 * @param model
	 * @return
	 */
	public static byte[] serialize(Object obj) { 
        try { 
        	ByteArrayOutputStream mem_out = new ByteArrayOutputStream(); 
        	ObjectOutputStream out = new ObjectOutputStream(mem_out);  
        	if(obj instanceof AppModel) {
        		out.writeObject((AppModel) obj); 
        	}
            out.close(); 
            mem_out.close();  
            byte[] bytes =  mem_out.toByteArray(); 
            return bytes; 
        } catch (IOException e) { 
        	e.printStackTrace();
            return null; 
        } 
    } 
 
	/**
	 * 反序列化AppModel
	 * @param bytes
	 * @return
	 */
	public static AppModel deserializeModel(byte[] bytes){ 
		try { 
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes); 
			ObjectInputStream in = new ObjectInputStream(mem_in);  
			AppModel model = (AppModel)in.readObject();  
			in.close(); 
			mem_in.close();  
			return model; 
		} catch (StreamCorruptedException e) { 

		} catch (ClassNotFoundException e) { 

		} catch (IOException e) { 
		
		} 
		return null; 
	}
	
	/**
	 * 通过安装、删除修改数据库数据
	 * @param packageName
	 * @param flag
	 */
	public void updateModel(String packageName, int flag) {
		synchronized (this) {
			SQLiteDatabase db=this.getWritableDatabase();
			ContentValues cv=new ContentValues();
			cv.put(APP_FLAG, flag);
			db.update(APP_TABLE, cv, APP_PACKAGENAME+"=?", new String[]{packageName});
			db.close();
		}
	}
	
	/**
	 * 音乐下载复制完成更新数据库
	 * @param name
	 * @param flag
	 */
	public void updateMusic(String name, int flag) {
		synchronized (this) {
			SQLiteDatabase db=this.getWritableDatabase();
			ContentValues cv=new ContentValues();
			cv.put(MUSIC_FLAG, flag);
			db.update(MUSIC_TABLE, cv, MUSIC_NAME+"=?", new String[]{name});
			db.close();
		}
	}
	
	/**
	 * 根据包名查找文件的下载url，供删除使用
	 * @param packageName
	 * @return
	 */
	public String getFileName(String packageName) {
		synchronized (this) {
			String result=null;
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cs=db.query(APP_TABLE, null, APP_PACKAGENAME+"=?", new String[]{packageName}, null, null, null);
			cs.moveToFirst();
			if(cs.getCount()>0) {
				cs.moveToFirst();
				AppModel model=deserializeModel(cs.getBlob(1));
				result=model.getFileUrl().substring(model.getFileUrl().indexOf("/")+1, model.getFileUrl().length());
			}
			cs.close();
			db.close();
			return result;
		}
	}
	
	/**
	 * 根据类型获取全部已安装类型的相应信息
	 * @return
	 */
	public ArrayList<AppModel> getAppModelList(String type) {
		synchronized (this) {
			ArrayList<AppModel> model_list=new ArrayList<AppModel>();
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cs=null;
			if(type.equals("app")) {
				cs=db.query(APP_TABLE, null, APP_FLAG+"=?", new String[]{"1"}, null, null, null);
			}
			else if(type.equals("video")) {
				cs=db.query(VIDEO_TABLE, null, null, null, null, null, null);
			}
			else if(type.equals("music")) {
				cs=db.query(MUSIC_TABLE, null, MUSIC_FLAG+"=?", new String[]{"1"}, null, null, null);
			}
			cs.moveToFirst();
			for(int i=0;i<cs.getCount();i++) {
				cs.moveToPosition(i);
				model_list.add(deserializeModel(cs.getBlob(1)));
			}
			cs.close();
			db.close();
			return model_list;
		}
	}
	
	/**
	 * 获取全部应用相应信息
	 * @return
	 */
	public ArrayList<AppModel> getAppManagerModelList() {
		synchronized (this) {
			ArrayList<AppModel> model_list=new ArrayList<AppModel>();
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cs=db.query(APP_TABLE, null, null, null, null, null, null);
			cs.moveToFirst();
			for(int i=0;i<cs.getCount();i++) {
				cs.moveToPosition(i);
				model_list.add(deserializeModel(cs.getBlob(1)));
			}
			cs.close();
			db.close();
			return model_list;
		}
	}
	
	/**
	 * 通过包名，判断数据库是否已经记录安装
	 * @param packageName
	 * @return
	 */
	public boolean getDBInstallFlag(String packageName) {
		synchronized (this) {
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cs=db.query(APP_TABLE, null, APP_PACKAGENAME+"=?", new String[]{packageName}, null, null, null);
			cs.moveToFirst();
			if(cs.getCount()>0) {
				boolean flag=cs.getInt(3)==1?true:false;
				cs.close();
				db.close();
				return flag;
			}
			cs.close();
			db.close();
			return false;
		}
	}
	
	/**
	 * 获取单一音乐播放信息
	 * @param id
	 * @return
	 */
	public AppModel getSingleMusicModel(int id) {
		synchronized (this) {
			AppModel model=null;
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cs=db.query(MUSIC_TABLE, null, MUSIC_FLAG+"=?", new String[]{"1"}, null, null, null);
			cs.moveToFirst();
			for(int i=0;i<cs.getCount();i++) {
				cs.moveToPosition(i);
				AppModel temp=deserializeModel(cs.getBlob(1));
				if(temp.getId()==id) {
					model=temp;
				}
			}
			cs.close();
			db.close();
			return model;
		}
	}
	
	/**
	 * 获取单一视频播放信息
	 * @param id
	 * @return
	 */
	public AppModel getSingleAppModel(int id) {
		synchronized (this) {
			AppModel model=null;
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cs=db.query(VIDEO_TABLE, null, null, null, null, null, null);
			cs.moveToFirst();
			for(int i=0;i<cs.getCount();i++) {
				cs.moveToPosition(i);
				AppModel temp=deserializeModel(cs.getBlob(1));
				if(temp.getId()==id) {
					model=temp;
				}
			}
			cs.close();
			db.close();
			return model;
		}
	}
	
	/**
	 * 根据类型删除某一个数据
	 * @param name
	 */
	public void deleteAppModel(String name, String type) {
		synchronized (this) {
			SQLiteDatabase db=this.getWritableDatabase();
			if(type.equals("app")) {
				db.delete(APP_TABLE, APP_PACKAGENAME+"=?", new String[]{name});
			}
			else if(type.equals("video")) {
				db.delete(VIDEO_TABLE, VIDEO_NAME+"=?", new String[]{name});
			}
			else if(type.equals("music")) {
				db.delete(MUSIC_TABLE, MUSIC_NAME+"=?", new String[]{name});
			}
			db.close();
		}
	}
	
	/**
	 * 判断音乐是否在存在
	 * @param name
	 * @return
	 */
	public AppModel isMusicExists(String name) {
		synchronized (this) {
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cs=db.query(MUSIC_TABLE, null, MUSIC_NAME+"=? and "+MUSIC_FLAG+"=?", new String[]{name, "1"}, null, null, null);
			cs.moveToFirst();
			AppModel model=null;
			if(cs.getCount()>0) {
				model=deserializeModel(cs.getBlob(1));
			}
			cs.close();
			db.close();
			return model;
		}
	}
	
	/**
	 * 儿童界面数据操作-音乐
	 */
	public void insertOtherPlatformByMusic(int id, String name, String url, String fileUrl) {
		synchronized (this) {
			try {
				File file=new File("/data/data/"+context.getPackageName()+"/CachedAudiobookItem2-iPad.sqlite");
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
				Cursor cs=db.query("ZCACHEDAUDIOBOOKITEM2", null, "ZAID=?", new String[]{""+id}, null, null, null);
				cs.moveToFirst();
				if(cs.getCount()>0) {
					cs.close();
					db.close();
					return;
				}
				else {
					cs.close();
				}
				ContentValues cv=new ContentValues();
				cv.put("ZAID", id);
				cv.put("ZICONURL", url.substring(url.lastIndexOf("/")+1));
				cv.put("ZNAME", name);
				cv.put("ZDESC", "");
				cv.put("ZPATH", fileUrl.substring(fileUrl.lastIndexOf("/")+1));
				cv.put("ZMP3URL", fileUrl);
				cv.put("ZDOWNPER", 1.0);
				cv.put("ZMODIFYTIME", Integer.parseInt((""+System.currentTimeMillis()).substring(0, 10)));
				cv.put("ZSTATUS", 1);
				cv.put("ZUSERID", 0);
				db.insert("ZCACHEDAUDIOBOOKITEM2", null, cv);
				db.close();	
			} catch(Exception e) {
				
			}
		}				
	}
	
	/**
	 * 将儿童模式的数据库信息写入到后台
	 */
	public void readOtherPlatformByMusic() {
		synchronized (this) {
			File file=new File("/data/data/"+context.getPackageName()+"/CachedAudiobookItem2-iPad.sqlite");
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
			Cursor cs=db.query("ZCACHEDAUDIOBOOKITEM2", null, null, null, null, null, null);
			cs.moveToFirst();
			for(int i=0;i<cs.getCount();i++) {
				cs.moveToPosition(i);
				
				AppModel model=new AppModel();
				model.setFileSize(0);
				model.setPackageName("");
				int startFileUrlPos=(((KEApplication) context.getApplicationContext())).kidsIconUrl.length();			
				model.setFileUrl(cs.getString(cs.getColumnIndex("ZMP3URL")).substring(startFileUrlPos));
				model.setName(cs.getString(cs.getColumnIndex("ZNAME")));
				model.setId(cs.getInt(cs.getColumnIndex("ZAID")));
				model.setIconUrl("audiopic/"+cs.getString(cs.getColumnIndex("ZICONURL")));
				model.setMobiledesc("");
				model.setDownloadCount(0);
				model.setResourceType(4);
				Conn.getInstance(context).insertMusicModel(model);
				Conn.getInstance(context).updateMusic(cs.getString(cs.getColumnIndex("ZNAME")), 1);
			}
			cs.close();
			db.close();
		}		
	}
	
	/**
	 * 将儿童模式的数据库信息写入到后台
	 */
	public void readOtherPlatformByApp() {
		synchronized (this) {
			File file=new File("/data/data/"+context.getPackageName()+"/CachedAppItem.sqlite");
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
			Cursor cs=db.query("CachedAppItem", null, null, null, null, null, null);
			cs.moveToFirst();
			for(int i=0;i<cs.getCount();i++) {
				cs.moveToPosition(i);
				
				AppModel model=new AppModel();
				model.setFileSize(0);
				model.setPackageName(cs.getString(cs.getColumnIndex("appInternalUrl")));
				model.setName(cs.getString(cs.getColumnIndex("appName")));
				model.setId(cs.getInt(cs.getColumnIndex("appID")));
				model.setMobiledesc("");
				model.setDownloadCount(0);
				model.setResourceType(cs.getInt(cs.getColumnIndex("appType")));
				switch(cs.getInt(cs.getColumnIndex("appType"))) {
				case 8:
					model.setIconUrl("studypics/"+cs.getString(cs.getColumnIndex("appStoreUrl")));
					break;
				case 9:
					model.setIconUrl("readingpics/"+cs.getString(cs.getColumnIndex("appStoreUrl")));
					break;
				case 10:
					model.setIconUrl("playpics/"+cs.getString(cs.getColumnIndex("appStoreUrl")));
					break;
				}
				Conn.getInstance(context).insertAppModel(model);
				if(CommonUtils.checkAppInstall(cs.getString(cs.getColumnIndex("appInternalUrl")), context)) {
					Conn.getInstance(context).updateModel(cs.getString(cs.getColumnIndex("appInternalUrl")), 1);
				}				
			}
			cs.close();
			db.close();
		}
	}
	
	/**
	 * 儿童界面数据操作-视频
	 */
	public void insertOtherPlatformByVideo(int id, String name, String url) {
		synchronized (this) {
			try {
				File file=new File("/data/data/"+context.getPackageName()+"/ChildMovieItem2-iPad.sqlite");
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
				Cursor cs=db.query("ZCHILDMOVIEITEM2", null, "ZAID=?", new String[]{""+id}, null, null, null);
				cs.moveToFirst();
				if(cs.getCount()>0) {
					cs.close();
					db.close();
					return;
				}
				else {
					cs.close();
				}
				ContentValues cv=new ContentValues();
				cv.put("ZUSERID", 0);
				cv.put("ZAID", id);
				cv.put("ZMODIFYTIME", Integer.parseInt((""+System.currentTimeMillis()).substring(0, 10)));
				cv.put("ZLASTSET", 0);
				cv.put("ZSTATUS", 1);
				cv.put("ZLASTTIME", 0.0);
				cv.put("ZNAME", name);
				cv.put("ZICONURL", url.substring(url.lastIndexOf("/")+1));
				db.insert("ZCHILDMOVIEITEM2", null, cv);
				db.close();
			} catch(Exception e) {
				
			}
		}				
	}
	
	/**
	 * 儿童界面数据操作-应用
	 */
	public void insertOtherPlatformByApp(int id, int type, String packageName,  String name, String url) {
		synchronized (this) {
			try {
				File file=new File("/data/data/"+context.getPackageName()+"/CachedAppItem.sqlite");
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
				Cursor cs=db.query("CachedAppItem", null, "appID=?", new String[]{""+id}, null, null, null);
				cs.moveToFirst();
				if(cs.getCount()>0) {
					cs.close();
					db.close();
					return;
				}
				else {
					cs.close();
				}
				ContentValues cv=new ContentValues();
				cv.put("appID", id);
				cv.put("appType", type);
				cv.put("appPriority", 0);
				cv.put("appInternalUrl", packageName);
				cv.put("appStoreUrl", url.substring(url.lastIndexOf("/")+1));
				cv.put("appName", name);
				db.insert("CachedAppItem", null, cv);
				db.close();	
			} catch(Exception e) {
				
			}
		}				
	}
	
	public void updateOtherPlatformByApp(String packageName, int type) {
		synchronized (this) {
			try {
				File file=new File("/data/data/"+context.getPackageName()+"/CachedAppItem.sqlite");
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
				ContentValues cv=new ContentValues();
				cv.put("appPriority", type);
				db.update("CachedAppItem", cv, "appInternalUrl=?" , new String[]{packageName});
				db.close();			
			} catch(Exception e) {
				
			}
		}		
	}
	
	/**
	 * 删除儿童界面视频播放
	 * @param id
	 */
	public void deleteOtherPlatformByVideo(int id) {
		synchronized (this) {
			try {
				File file=new File("/data/data/"+context.getPackageName()+"/ChildMovieItem2-iPad.sqlite");
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
				db.delete("ZCHILDMOVIEITEM2", "ZAID=?", new String[]{""+id});
			} catch(Exception e) {
				
			}
		}				
	}
	
	/**
	 * 删除儿童界面音乐播放
	 * @param id
	 */
	public void deleteOtherPlatformByMusic(int id) {
		synchronized (this) {
			try {
				File file=new File("/data/data/"+context.getPackageName()+"/CachedAudiobookItem2-iPad.sqlite");
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null); 
				db.delete("ZCACHEDAUDIOBOOKITEM2", "ZAID=?", new String[]{""+id});
			} catch(Exception e) {
				
			}
		}		
	}
	
}

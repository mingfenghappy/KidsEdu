package com.morningtel.kidsedu.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

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
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 将对象保存到数据库中
	 * @param model
	 */
	public void insertModel(Object obj) {
		synchronized (this) {
			SQLiteDatabase db_check=this.getReadableDatabase();
			Cursor cs=db_check.query(APP_TABLE, null, APP_PACKAGENAME+"=?", new String[]{((AppModel) obj).getPackageName()}, null, null, null);
			cs.moveToFirst();
			if(cs.getCount()>0) {
				cs.close();
				db_check.close();
				return;
			}
			
			ContentValues values=null;
			if(obj instanceof AppModel) {
				byte[] bytes=serialize((AppModel) obj);
				values=new ContentValues(3);
				values.put(APP_INFO, bytes);
				values.put(APP_PACKAGENAME, ((AppModel) obj).getPackageName());
				values.put(APP_FLAG, 0);
			}
			SQLiteDatabase db=this.getWritableDatabase();
			db.beginTransaction();
			if(obj instanceof AppModel) {
				db.insert(APP_TABLE, null, values);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();	
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
	 * 获取全部安装信息
	 * @return
	 */
	public ArrayList<AppModel> getAppModelList() {
		synchronized (this) {
			ArrayList<AppModel> model_list=new ArrayList<AppModel>();
			SQLiteDatabase db_check=this.getReadableDatabase();
			Cursor cs=db_check.query(APP_TABLE, null, null, null, null, null, null);
			cs.moveToFirst();
			for(int i=0;i<cs.getCount();i++) {
				cs.moveToPosition(i);
				model_list.add(deserializeModel(cs.getBlob(1)));
			}
			return model_list;
		}
	}

}

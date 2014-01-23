package com.morningtel.kidsedu.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParse {

	/**
	 * 获取app列表等
	 * @param str
	 * @return
	 */
	public static ArrayList<AppTypesModel> getAppTypesModelList(String str) {
		ArrayList<AppTypesModel> model_list=new ArrayList<AppTypesModel>();
		try {
			JSONObject obj=new JSONObject(str);
			JSONArray appTypes_array=obj.getJSONArray("appTypes");
			for(int i=0;i<appTypes_array.length();i++) {
				JSONObject model_obj=appTypes_array.getJSONObject(i);
				AppTypesModel model=new AppTypesModel();
				model.setId(model_obj.getInt("id"));
				model.setName(model_obj.getString("name"));
				model_list.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model_list;
	}
	
	/**
	 * 根据id获取列表
	 * @param str
	 * @return
	 */
	public static ArrayList<AppsFilterModel> getAppsFilterModelList(String str) {
		ArrayList<AppsFilterModel> model_list=new ArrayList<AppsFilterModel>();
		try {
			JSONObject obj=new JSONObject(str);
			JSONArray apps_array=obj.getJSONArray("apps");
			for(int i=0;i<apps_array.length();i++) {
				JSONObject model_obj=apps_array.getJSONObject(i);
				AppsFilterModel model=new AppsFilterModel();
				model.setCommentCount(model_obj.getInt("commentCount"));
				model.setCommentGrade(model_obj.getDouble("commentGrade"));
				model.setDownloadCount(model_obj.getInt("downloadCount"));
				model.setIconUrl(model_obj.getString("iconUrl"));
				model.setId(model_obj.getInt("id"));
				model.setLastUpdateTime(model_obj.getLong("lastUpdateTime"));
				model.setMobiledesc(model_obj.getString("mobiledesc"));
				model.setMoney(model_obj.getInt("money"));
				model.setName(model_obj.getString("name"));
				model.setOriginalMoney(model_obj.getInt("originalMoney"));
				model.setPackageName(model_obj.getString("packageName"));
				model.setProvider(model_obj.getString("provider"));
				model.setViewCount(model_obj.getInt("viewCount"));
				JSONObject model_obj_resourceType=new JSONObject(model_obj.getString("resourceType"));
				model.setResourceType(model_obj_resourceType.getInt("id"));
				model_list.add(model);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model_list;
	}
	
	/**
	 * 获取应用下载以及音乐播放列表元素信息
	 * @param str
	 * @return
	 */
	public static AppModel getAppModelByAid(String str) {
		AppModel model=new AppModel();
		try {
			JSONObject obj=new JSONObject(str);
			JSONObject app_obj=obj.getJSONObject("app");
			JSONArray appFiles_array=app_obj.getJSONArray("appFiles");
			JSONObject model_obj=appFiles_array.getJSONObject(0);
			model.setFileSize(model_obj.getLong("fileSize"));
			model.setPackageName(model_obj.getString("packageName"));
			model.setFileUrl(model_obj.getString("fileUrl"));
			model.setName(app_obj.getString("name"));
			model.setId(app_obj.getInt("id"));
			model.setIconUrl(app_obj.getString("iconUrl"));
			model.setMobiledesc(app_obj.getString("mobiledesc"));
			model.setDownloadCount(app_obj.getInt("downloadCount"));
			JSONObject model_obj_resourceType=new JSONObject(app_obj.getString("resourceType"));
			model.setResourceType(model_obj_resourceType.getInt("id"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model=null;
		}
		return model;
	}
	
	/**
	 * 视频播放列表
	 * @param str
	 * @return
	 */
	public static AppModel getVideoModelByAid(String str) {
		AppModel model=new AppModel();
		try {
			JSONObject obj=new JSONObject(str);
			JSONObject app_obj=obj.getJSONObject("app");
			JSONArray appFiles_array=app_obj.getJSONArray("appFiles");
			ArrayList<VideoItemModel> item_list=new ArrayList<VideoItemModel>();
			for(int i=0;i<appFiles_array.length();i++) {
				JSONObject model_obj=appFiles_array.getJSONObject(i);
				VideoItemModel item=new VideoItemModel();
				item.setVersionCode(model_obj.getString("versionCode"));
				item.setFileUrl(model_obj.getString("fileUrl"));
				item_list.add(item);
			}
			model.setIconUrl(app_obj.getString("iconUrl"));
			model.setModel_list(item_list);
			model.setName(app_obj.getString("name"));
			model.setId(app_obj.getInt("id"));
			model.setProvider(app_obj.getString("provider"));
			model.setCommentGrade(app_obj.getDouble("commentGrade"));
			model.setMobiledesc(app_obj.getString("mobiledesc"));
			model.setDownloadCount(app_obj.getInt("downloadCount"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model=null;
		}
		return model;
	}
	
	/**
	 * 获取版本更新
	 * @param str
	 * @return
	 */
	public static HashMap<String, String> getUpdateAppModelList(String str) {
		HashMap<String, String> model_map=new HashMap<String, String>();
		try {
			JSONObject obj=new JSONObject(str);
			JSONArray obj_array=obj.getJSONArray("appFiles");
			for(int i=0;i<obj_array.length();i++) {
				JSONObject obj_model=obj_array.getJSONObject(i);
				model_map.put(obj_model.getString("packageName"), obj_model.getString("fileUrl"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model_map;
	} 
}

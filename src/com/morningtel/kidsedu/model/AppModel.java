package com.morningtel.kidsedu.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AppModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int id=0;
	long fileSize=0;
	String fileUrl="";
	String packageName="";
	int versionCode=0;
	String versionName="";
	ArrayList<String> appScreenShots=null;
	int commentCount=0;
	double commentGrade=0;
	int downloadCount=0;
	String iconUrl="";
	long lastUpdateTime=0;
	String mobiledesc="";
	int money=0;
	String name="";
	ArrayList<VideoItemModel> model_list=null;
	String provider="";
	
	public ArrayList<VideoItemModel> getModel_list() {
		return model_list;
	}
	public void setModel_list(ArrayList<VideoItemModel> model_list) {
		this.model_list = model_list;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public ArrayList<String> getAppScreenShots() {
		return appScreenShots;
	}
	public void setAppScreenShots(ArrayList<String> appScreenShots) {
		this.appScreenShots = appScreenShots;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public double getCommentGrade() {
		return commentGrade;
	}
	public void setCommentGrade(double commentGrade) {
		this.commentGrade = commentGrade;
	}
	public int getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getMobiledesc() {
		return mobiledesc;
	}
	public void setMobiledesc(String mobiledesc) {
		this.mobiledesc = mobiledesc;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

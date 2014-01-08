package com.morningtel.kidsedu.model;

import java.io.Serializable;

public class AppsFilterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int commentCount=0;
	double commentGrade=0;
	long downloadCount=0;
	String iconUrl="";
	int id=0;
	long lastUpdateTime=0;
	String mobiledesc="";
	int money=0;
	String name="";
	int originalMoney=0;
	String packageName="";
	String provider="";
	int viewCount=0;
	//音乐播放器的时候作为正在播放的标志
	boolean isPlay=false;
	
	public boolean isPlay() {
		return isPlay;
	}
	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
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
	public long getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(long downloadCount) {
		this.downloadCount = downloadCount;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getOriginalMoney() {
		return originalMoney;
	}
	public void setOriginalMoney(int originalMoney) {
		this.originalMoney = originalMoney;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
}

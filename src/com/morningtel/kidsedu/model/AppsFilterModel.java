package com.morningtel.kidsedu.model;

import java.io.Serializable;

public class AppsFilterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int ITEM=0;
	public static final int SECTION=1;
	
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
	int resourceType=0;
	//���ֲ�������ʱ����Ϊ���ڲ��ŵı�־
	boolean isPlay=false;
	//����ʱ��������ʶ�Ǳ�ͷ���Ǳ�����
	int search_type=1;
	
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	public int getSearch_type() {
		return search_type;
	}
	public void setSearch_type(int search_type) {
		this.search_type = search_type;
	}
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

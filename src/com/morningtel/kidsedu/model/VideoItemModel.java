package com.morningtel.kidsedu.model;

import java.io.Serializable;

public class VideoItemModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String versionCode="";
	String fileUrl="";
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	
}

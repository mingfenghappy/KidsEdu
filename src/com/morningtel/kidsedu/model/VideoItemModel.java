package com.morningtel.kidsedu.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoItemModel implements Parcelable {
	
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
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(fileUrl);
		dest.writeString(versionCode);
	}
	
	public static final Parcelable.Creator<VideoItemModel> CREATOR = new Creator<VideoItemModel>() {

		@Override
		public VideoItemModel createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			VideoItemModel model=new VideoItemModel();
			model.fileUrl=source.readString();
			model.versionCode=source.readString();
			return model;
		}

		@Override
		public VideoItemModel[] newArray(int size) {
			// TODO Auto-generated method stub
			return new VideoItemModel[size];
		}};
	
}

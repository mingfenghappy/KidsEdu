package com.morningtel.kidsedu.yxapi;

import java.util.Date;

import im.yixin.sdk.api.YXAPIBaseBroadcastReceiver;
import im.yixin.sdk.channel.YXMessageProtocol;

public class AppRegister extends YXAPIBaseBroadcastReceiver {

	@Override
	protected String getAppId() {
		// TODO Auto-generated method stub
		return "yxf448b99c04f24aaea62792ccfcdf7a29";
	}
	
	@Override
	protected void onAfterYixinStart(YXMessageProtocol protocol) {
		// TODO Auto-generated method stub
		super.onAfterYixinStart(protocol);
		System.out.println("ClientonAfterYixinStart@"+(new Date())+",AppId="+protocol.getAppId()+",Command="+protocol.getCommand()+",SdkVersion="+protocol.getSdkVersion()+",appPackage="+protocol.getAppPackage());
	}

}

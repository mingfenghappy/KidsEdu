package com.morningtel.kidsedu.wxapi;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final IWXAPI api = WXAPIFactory.createWXAPI(context, "wx75db9bd256c4c9fa");

		// 将该app注册到微信
		api.registerApp("wx75db9bd256c4c9fa");
	}

}

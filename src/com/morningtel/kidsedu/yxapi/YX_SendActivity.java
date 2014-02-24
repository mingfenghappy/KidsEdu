package com.morningtel.kidsedu.yxapi;

import im.yixin.sdk.api.IYXAPI;
import im.yixin.sdk.api.SendMessageToYX;
import im.yixin.sdk.api.YXAPIFactory;
import im.yixin.sdk.api.YXMessage;
import im.yixin.sdk.api.YXWebPageMessageData;
import im.yixin.sdk.util.BitmapUtil;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;

public class YX_SendActivity extends Activity {
	
	private IYXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		api=YXAPIFactory.createYXAPI(this, "yxf448b99c04f24aaea62792ccfcdf7a29");
		api.registerApp();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		sendText(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("path"), getIntent().getExtras().getBoolean("isFriend"));
	}
	
	private void sendText(String text, String path, boolean isFriend) {
		YXWebPageMessageData webpage=new YXWebPageMessageData();
		webpage.webPageUrl="http://www.kidsedu.com";
		YXMessage msg=new YXMessage(webpage);
		msg.title="Ò×µÏÀÖÔ°ÓÅÐã¶¯»­Æ¬ÍÆ¼ö";
		msg.description=text;
		msg.thumbData=BitmapUtil.bmpToByteArray(BitmapFactory.decodeFile(path), true);
		SendMessageToYX.Req req=new SendMessageToYX.Req();
		req.transaction=buildTransaction("webpage");
		req.message=msg;
		req.scene=isFriend?SendMessageToYX.Req.YXSceneTimeline:SendMessageToYX.Req.YXSceneSession;
		api.sendRequest(req);
		finish();
	}
	
	private String buildTransaction(final String type) {
		return (type ==null)?String.valueOf(System.currentTimeMillis()):type+System.currentTimeMillis();
	}

}

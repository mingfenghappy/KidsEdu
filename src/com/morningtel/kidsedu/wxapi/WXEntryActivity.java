package com.morningtel.kidsedu.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;

import com.morningtel.kidsedu.commons.CommonUtils;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
    private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(getIntent().getExtras().getString("text")==null) {
			finish();
			return;
		}
		
		// ͨ��WXAPIFactory��������ȡIWXAPI��ʵ��
    	api=WXAPIFactory.createWXAPI(this, "wx75db9bd256c4c9fa", false);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		api.handleIntent(getIntent(), this);
		
		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion >= 0x21020001) {
			api.registerApp("wx75db9bd256c4c9fa");    
			sendText(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("path"), getIntent().getExtras().getBoolean("isFriend"));
		} else {
			CommonUtils.showCustomToast(WXEntryActivity.this, "����ǰʹ�õ�΢�Ű汾���ͣ��׵���԰����ʧ��");
		}
	}
	
	private void sendText(String text, String path, boolean isFriend) {
		WXWebpageObject webpage=new WXWebpageObject();
		webpage.webpageUrl="http://www.kidsedu.com";
		WXMediaMessage msg=new WXMediaMessage(webpage);
		msg.title="�׵���԰���㶯��Ƭ�Ƽ�";
		msg.description=text;
		msg.thumbData=Util.bmpToByteArray(BitmapFactory.decodeFile(path), true);
		
		SendMessageToWX.Req req=new SendMessageToWX.Req();
		req.transaction=buildTransaction("webpage");
		req.message=msg;
		req.scene=isFriend?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
		
		finish();
	}
	
	private String buildTransaction(final String type) {
		return (type ==null)?String.valueOf(System.currentTimeMillis()):type+System.currentTimeMillis();
	}

	// ΢�ŷ������󵽵�����Ӧ��ʱ����ص����÷���
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	// ������Ӧ�÷��͵�΢�ŵ�����������Ӧ�������ص����÷���
	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			CommonUtils.showCustomToast(WXEntryActivity.this, "����ɹ�");
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			CommonUtils.showCustomToast(WXEntryActivity.this, "����ȡ��");
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			CommonUtils.showCustomToast(WXEntryActivity.this, "��û��΢�ŷ���Ȩ��");
			break;
		default:
			CommonUtils.showCustomToast(WXEntryActivity.this, "δ֪���󣬷���ʧ��");
			break;
		}
		finish();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

}

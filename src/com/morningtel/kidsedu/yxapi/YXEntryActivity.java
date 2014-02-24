package com.morningtel.kidsedu.yxapi;

import com.morningtel.kidsedu.commons.CommonUtils;

import im.yixin.sdk.api.BaseReq;
import im.yixin.sdk.api.BaseResp;
import im.yixin.sdk.api.BaseYXEntryActivity;
import im.yixin.sdk.api.IYXAPI;
import im.yixin.sdk.api.SendMessageToYX;
import im.yixin.sdk.api.YXAPIFactory;
import im.yixin.sdk.util.YixinConstants;

public class YXEntryActivity extends BaseYXEntryActivity {

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		switch (resp.getType()) {
		case YixinConstants.RESP_SEND_MESSAGE_TYPE:
			SendMessageToYX.Resp resp1 = (SendMessageToYX.Resp) resp;
			switch (resp1.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				CommonUtils.showCustomToast(YXEntryActivity.this, "分享成功");
				break;
			case BaseResp.ErrCode.ERR_COMM:
				CommonUtils.showCustomToast(YXEntryActivity.this, "分享失败");
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				CommonUtils.showCustomToast(YXEntryActivity.this, "用户取消");
				break;
			case BaseResp.ErrCode.ERR_SENT_FAILED:
				CommonUtils.showCustomToast(YXEntryActivity.this, "发送失败");
				break;
			}
			finish();
		}
	}

	@Override
	protected IYXAPI getIYXAPI() {
		// TODO Auto-generated method stub
		return YXAPIFactory.createYXAPI(this, "yxf448b99c04f24aaea62792ccfcdf7a29");
	}

}

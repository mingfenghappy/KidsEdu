package com.morningtel.kidsedu.qqapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.morningtel.kidsedu.commons.CommonUtils;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class QQActivity extends Activity {
	
	public Tencent mTencent;
	public static String mAppid="101020681";
	
	private int shareType=Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mTencent=Tencent.createInstance(mAppid, getApplicationContext());
		if(!mTencent.isSupportSSOLogin(QQActivity.this)) {
			CommonUtils.showCustomToast(QQActivity.this, "�����Ȱ�װ�ֻ���QQ��ִ�з������");
			finish();
			return ;
		}
				
		HashMap<String, String> map=readQQ(QQActivity.this);
		if((Long.parseLong(map.get("expires_in"))-System.currentTimeMillis())/1000<=0) {
        	onClickLogin();
		}
		else {
			mTencent.setOpenId(map.get("openid"));
			mTencent.setAccessToken(map.get("access_token"), ""+(Long.parseLong(map.get("expires_in"))-System.currentTimeMillis())/1000);
			if(getIntent().getExtras().getString("type").equals("qqkj")) {
				shareType=Tencent.SHARE_TO_QQ_NO_SHARE_TYPE;
				sendTextKJ(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("send_imageUrl"));
			}
			else if(getIntent().getExtras().getString("type").equals("qq")) {
				shareType=Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
				sendText(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("send_imageUrl"));
			}
			else if(getIntent().getExtras().getString("type").equals("tweibo")) {
				sendWeiboWithPic(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("path"));
			}
		}
	}
	
	private void onClickLogin() {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject response) {
					System.out.println(response.toString());
					//�����û���¼��Ϣ
            		keepQQ(QQActivity.this, getQQOpenId(response.toString()).get("openid"), 
            				getQQOpenId(response.toString()).get("access_token"), 
            				System.currentTimeMillis()+Long.parseLong(getQQOpenId(response.toString()).get("expires_in"))*1000);
            		mTencent.setOpenId(getQQOpenId(response.toString()).get("openid"));
        			mTencent.setAccessToken(getQQOpenId(response.toString()).get("access_token"), ""+(Long.parseLong(getQQOpenId(response.toString()).get("expires_in"))));
        			if(getIntent().getExtras().getString("type").equals("qqkj")) {
        				shareType=Tencent.SHARE_TO_QQ_NO_SHARE_TYPE;
        				sendTextKJ(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("send_imageUrl"));
        			}
        			else if(getIntent().getExtras().getString("type").equals("qq")) {
        				shareType=Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
        				sendText(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("send_imageUrl"));
        			}
        			else if(getIntent().getExtras().getString("type").equals("tweibo")) {
        				sendWeiboWithPic(getIntent().getExtras().getString("text"), getIntent().getExtras().getString("path"));
        			}				
    			}
			};
			mTencent.login(this, "all", listener);
		} else {
			mTencent.logout(this);
		}
	}
	
	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			CommonUtils.showCustomToast(QQActivity.this, "��¼�ɹ�");
			System.out.println(response.toString());
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			CommonUtils.showCustomToast(QQActivity.this, "onError: "+e.errorDetail);
		}

		@Override
		public void onCancel() {
			CommonUtils.showCustomToast(QQActivity.this, "onCancel: ");
		}
	}
	
	/**
	 * QQ������Ϣ����
	 * @param context
	 * @param openid
	 * @param access_token
	 * @param expires_in
	 */
	public static void keepQQ(Context context, String openid, String access_token, long expires_in) {
		SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("openid", openid);
		editor.putString("access_token", access_token);
		editor.putLong("expires_in", expires_in);
		editor.commit();
	}

	/**
	 * ���openId
	 * @param str
	 * @return
	 */
	public static HashMap<String, String> getQQOpenId(String str) {
		HashMap<String, String> map=null;
		try {
			map=new HashMap<String, String>();
			JSONObject obj=new JSONObject(str);
			map.put("openid", obj.getString("openid"));
			map.put("expires_in", obj.getString("expires_in"));
			map.put("access_token", obj.getString("access_token"));
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ��ȡQQ��¼��Ϣ
	 * @param context
	 * @return
	 */
	public static HashMap<String, String> readQQ(Context context) {
		SharedPreferences sp=context.getSharedPreferences("kidsedu", Activity.MODE_PRIVATE);
		String openid=sp.getString("openid", "");
		String access_token=sp.getString("access_token", "");
		long expires_in=sp.getLong("expires_in", 0); 
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("openid", openid);
		map.put("access_token", access_token);
		map.put("expires_in", ""+expires_in);
		return map;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // must call mTencent.onActivityResult.
    	if(resultCode==RESULT_CANCELED) {
    		finish();
    		return;
    	}
    }
	
	public void sendText(String text, String imageUrl) {
		final Bundle params=new Bundle();
		params.putString(Tencent.SHARE_TO_QQ_TITLE, "�׵���԰���㶯��Ƭ�Ƽ�");
        params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://www.kidsedu.com");
        params.putString(Tencent.SHARE_TO_QQ_SUMMARY, text);
        params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putString(Tencent.SHARE_TO_QQ_APP_NAME, "�׵���԰");
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(Tencent.SHARE_TO_QQ_EXT_INT, 0x00);
        doShareToQQ(params);
	}
	
	public void sendTextKJ(String text, String imageUrl) {
        ArrayList<String> imageUrls=new ArrayList<String>();
        imageUrls.add(imageUrl);
		final Bundle params=new Bundle();
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putString(Tencent.SHARE_TO_QQ_TITLE, "�׵���԰���㶯��Ƭ�Ƽ�");
        params.putString(Tencent.SHARE_TO_QQ_SUMMARY, text);
        params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://www.kidsedu.com");
        params.putStringArrayList(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        doShareToQzone(params);
	}
	
	/**
     * ���첽��ʽ��������QQ
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent=Tencent.createInstance(mAppid, QQActivity.this);        
        final Activity activity=QQActivity.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                tencent.shareToQQ(activity, params, new IUiListener() {

                    @Override
                    public void onComplete(JSONObject response) {
                        // TODO Auto-generated method stub
                        CommonUtils.showCustomToast(activity, "����ɹ�");
                        finish();
                    }

                    @Override
                    public void onError(UiError e) {
                        CommonUtils.showCustomToast(activity, "����ʧ��"+e.errorMessage);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                    	if(shareType!=Tencent.SHARE_TO_QQ_TYPE_IMAGE){
                    		CommonUtils.showCustomToast(activity, "����ȡ��");
                    		finish();
                    	}
                    }

                });
            }
        }).start();
    }
    
    /**
     * ���첽��ʽ��������QQ�ռ�
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
        final Activity activity=QQActivity.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
            	mTencent.shareToQzone(activity, params, new IUiListener() {

                    @Override
                    public void onCancel() {
                    	CommonUtils.showCustomToast(activity, "����ȡ��");
                    	finish();
                    }

                    @Override
                    public void onComplete(JSONObject response) {
                        // TODO Auto-generated method stub
                    	CommonUtils.showCustomToast(activity, "����ɹ�");
                    	finish();
                    }

                    @Override
                    public void onError(UiError e) {
                        // TODO Auto-generated method stub
                    	CommonUtils.showCustomToast(activity, "����ʧ��"+e.errorMessage);
                    	finish();
                    }

                });
            }
        }).start();
    }
    
    /**
	 * ���ʹ�ͼ΢��
	 */
	private void sendWeiboWithPic(String text, String path) {
		if(mTencent.ready(QQActivity.this)) {
			Bundle bundle=new Bundle();
			bundle.putString("format", "json");
			bundle.putString("content", "�׵���԰���㶯��Ƭ�Ƽ���"+text);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			Bitmap bmp=BitmapFactory.decodeFile(path);
			bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] buff=baos.toByteArray();
			bundle.putByteArray("pic", buff);
			mTencent.requestAsync(Constants.GRAPH_ADD_PIC_T, bundle, Constants.HTTP_POST, new TQQApiListener("add_pic_t", false, QQActivity.this), null);
			bmp.recycle();
		}
	}
	
	private class TQQApiListener extends BaseApiListener {
		
		private String mScope="all";
        private Boolean mNeedReAuth=false;
        private Activity mActivity;
        
    	public TQQApiListener(String scope, boolean needReAuth, Activity activity) {
			super(scope, needReAuth, activity);
			this.mScope = scope;
			this.mNeedReAuth = needReAuth;
			this.mActivity = activity;
		}

		@Override
		public void onComplete(final JSONObject response, Object state) {
			final Activity activity=QQActivity.this;
			try {
				int ret=response.getInt("ret");
				if(ret==0) {
					Message msg=mHandler.obtainMessage(0, mScope);
					Bundle data=new Bundle();
					data.putString("response", response.toString());
					msg.setData(data);
					mHandler.sendMessage(msg);
				} else if(ret == 100030) {
					if(mNeedReAuth) {
						Runnable r=new Runnable() {
							public void run() {
								mTencent.reAuth(activity, mScope, new BaseUiListener());
							}
						};
						QQActivity.this.runOnUiThread(r);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				finish();
			}
		}
		
	  	@Override
        public void onIOException(final IOException e, Object state) {
            CommonUtils.showCustomToast(QQActivity.this, "����ʧ�ܣ�onIOException: "+e.getMessage());
            finish();
        }

        @Override
        public void onMalformedURLException(final MalformedURLException e,
                Object state) {
        	CommonUtils.showCustomToast(QQActivity.this, "����ʧ�ܣ�onMalformedURLException: "+e.getMessage());
        	finish();
        }

        @Override
        public void onJSONException(final JSONException e, Object state) {
        	CommonUtils.showCustomToast(QQActivity.this, "����ʧ�ܣ�onJSONException: "+e.getMessage());
        	finish();
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e,
                Object arg1) {
        	CommonUtils.showCustomToast(QQActivity.this, "����ʧ�ܣ�onConnectTimeoutException: "+e.getMessage());
        	finish();
        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e,
                Object arg1) {
        	CommonUtils.showCustomToast(QQActivity.this, "����ʧ�ܣ�onSocketTimeoutException: "+e.getMessage());
        	finish();
        }

        @Override
        public void onUnknowException(Exception e, Object arg1) {
        	CommonUtils.showCustomToast(QQActivity.this, "����ʧ�ܣ�onUnknowException: "+e.getMessage());
        	finish();
        }

        @Override
        public void onHttpStatusException(HttpStatusException e, Object arg1) {
        	CommonUtils.showCustomToast(QQActivity.this, "����ʧ�ܣ�onHttpStatusException: "+e.getMessage());
        	finish();
        }

        @Override
        public void onNetworkUnavailableException(
                NetworkUnavailableException e, Object arg1) {
        	CommonUtils.showCustomToast(QQActivity.this, "����ʧ�ܣ�onNetworkUnavailableException: "+e.getMessage());
        	finish();
        }
	}
	
	/**
	 * �첽��ʾ���
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String response=msg.getData().getString("response");
			if(response!=null) {
				// ������ʾ
				response=response.replace(",", "\r\n");
				CommonUtils.showCustomToast(QQActivity.this, "����ɹ�");
				System.out.println(response);
				finish();
			}
		};
	};
}

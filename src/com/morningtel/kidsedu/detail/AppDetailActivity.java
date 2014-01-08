package com.morningtel.kidsedu.detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.BitmapHelp;

public class AppDetailActivity extends BaseActivity {
	
	ImageView appdetail_image=null;
	
	public static BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_appdetail);
		
		bitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		
		init();
	}
	
	public void init() {
		appdetail_image=(ImageView) findViewById(R.id.appdetail_image);
		bitmapUtils.display(appdetail_image, "http://res.kidsedu.com/pics/840d8299-1ed1-429e-992f-f3ad65b5bcd8.jpg");

	}
	
	
	
}

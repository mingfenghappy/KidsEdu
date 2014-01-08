package com.morningtel.kidsedu.commons;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class NotificationActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		finish();
	}

}

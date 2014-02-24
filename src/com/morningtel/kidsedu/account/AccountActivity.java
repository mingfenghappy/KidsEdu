package com.morningtel.kidsedu.account;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.myview.MyTextView;

public class AccountActivity extends SherlockActivity {
		
	ImageView account_avatar=null;
	TextView user_nickname=null;
	MyTextView account_coin=null;
	Button account_coin_recharge=null;
	TextView account_tt=null;
	TextView account_kk=null;
	TextView account_dd=null;
	TextView account_xx=null;
	TextView account_ww=null;
	
	HashMap<String, String> userInfo_map=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_profile);
		
		getSupportActionBar().setTitle("Ê×Ò³");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		userInfo_map=CommonUtils.getUserInfo(AccountActivity.this);
		
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		
		return true;
	}

	public void init() {
		account_avatar=(ImageView) findViewById(R.id.account_avatar);
		user_nickname=(TextView) findViewById(R.id.user_nickname);
		user_nickname.setText(userInfo_map.get("userName"));
		account_coin=(MyTextView) findViewById(R.id.account_coin);
		account_coin.setMaxNum(0);
		account_coin.setStart();
		account_coin_recharge=(Button) findViewById(R.id.account_coin_recharge);
		account_coin_recharge.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AccountActivity.this, WebInfoActivity.class);
				startActivity(intent);
			}});
		account_tt=(TextView) findViewById(R.id.account_tt);
		account_tt.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AccountActivity.this, AccountMusicActivity.class);
				startActivity(intent);
			}});
		account_kk=(TextView) findViewById(R.id.account_kk);
		account_kk.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AccountActivity.this, AccountVideoActivity.class);
				startActivity(intent);
			}});
		account_dd=(TextView) findViewById(R.id.account_dd);
		account_dd.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AccountActivity.this, AccountAppActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("resourceType", 9);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		account_xx=(TextView) findViewById(R.id.account_xx);
		account_xx.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AccountActivity.this, AccountAppActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("resourceType", 8);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
		account_ww=(TextView) findViewById(R.id.account_ww);
		account_ww.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(AccountActivity.this, AccountAppActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("resourceType", 10);
				intent.putExtras(bundle);
				startActivity(intent);
			}});
	}
}

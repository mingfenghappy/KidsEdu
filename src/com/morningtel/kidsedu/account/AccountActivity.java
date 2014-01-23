package com.morningtel.kidsedu.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.R;

public class AccountActivity extends BaseActivity {
	
	TextView nav_title=null;
	
	ImageView account_avatar=null;
	TextView user_nickname=null;
	TextView account_coin=null;
	Button account_coin_recharge=null;
	TextView account_tt=null;
	TextView account_kk=null;
	TextView account_dd=null;
	TextView account_xx=null;
	TextView account_ww=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_profile);
		
		init();
	}

	public void init() {
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setText("Ê×Ò³");
		nav_title.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		account_avatar=(ImageView) findViewById(R.id.account_avatar);
		user_nickname=(TextView) findViewById(R.id.user_nickname);
		account_coin=(TextView) findViewById(R.id.account_coin);
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

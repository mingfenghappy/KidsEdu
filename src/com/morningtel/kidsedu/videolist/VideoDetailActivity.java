package com.morningtel.kidsedu.videolist;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.lidroid.xutils.BitmapUtils;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.BitmapHelp;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.mediaplayer.PlayerActivity;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.model.JsonParse;
import com.morningtel.kidsedu.model.VideoItemModel;
import com.morningtel.kidsedu.qqapi.QQActivity;
import com.morningtel.kidsedu.sinaweiboapi.WBMainActivity;
import com.morningtel.kidsedu.wxapi.WXEntryActivity;
import com.morningtel.kidsedu.yxapi.YX_SendActivity;

public class VideoDetailActivity extends SherlockActivity {
	
	ArrayList<VideoItemModel> item_list=null;
	
	AudioManager mAudioManager=null;
	
	ImageView video_detail_image=null;
	ImageView video_detail_grade=null;
	TextView video_detail_num=null;
	TextView video_detail_provider=null;
	TextView video_detail_resolution=null;
	Button video_detail_playButton=null;
	Button video_detail_add=null;
	TextView video_detail_reviews=null;
	TextView video_detail_summary=null;
	TextView video_detail_desp=null;
	GridView video_detail_gridview=null;
	SimpleAdapter adapter=null;
	ArrayList<HashMap<String, Object>> ui_list=null;
	//当前页面对象
	AppModel model=null;
	//第一集播放url
	String firstUrl="";
	
	//第三方分享信息
	String send_text="";
	String send_path="";
	String send_imageUrl="";
	
	public static BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_detail);
		
		getSupportActionBar().setTitle("看看");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		bitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		
		item_list=new ArrayList<VideoItemModel>();
		ui_list=new ArrayList<HashMap<String, Object>>();
		
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		SubMenu s_menu=menu.addSubMenu("分享");
		MenuItem item1=s_menu.add(0, 1, 0, "易信好友");
		item1.setIcon(R.drawable.yixin_logo);
		MenuItem item2=s_menu.add(0, 2, 1, "易信朋友圈");
		item2.setIcon(R.drawable.yixin_pengyou_logo);
		MenuItem item3=s_menu.add(0, 3, 2, "微信好友");
		item3.setIcon(R.drawable.weixin_logo);
		MenuItem item4=s_menu.add(0, 4, 3, "微信朋友圈");
		item4.setIcon(R.drawable.weixin_pengyou_logo);
		MenuItem item5=s_menu.add(0, 5, 4, "QQ好友");
		item5.setIcon(R.drawable.qq_logo);
		MenuItem item6=s_menu.add(0, 6, 5, "QQ空间");
		item6.setIcon(R.drawable.qqkj_logo);
		MenuItem item7=s_menu.add(0, 7, 6, "腾讯微博");
		item7.setIcon(R.drawable.tencentweibo_logo);
		MenuItem item8=s_menu.add(0, 8, 7, "新浪微博");
		item8.setIcon(R.drawable.sinaweibo_logo);
		MenuItem mItem=s_menu.getItem();
		mItem.setIcon(R.drawable.abs__ic_menu_share_holo_light);
		mItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT|MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent=null;
		Bundle bundle=null;
		switch(item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			case 1:
				intent=new Intent(VideoDetailActivity.this, YX_SendActivity.class);
				bundle=new Bundle();
				bundle.putString("text", send_text);
				bundle.putString("path", "/data/data/"+getPackageName()+"/kidsedu/"+send_path);
				bundle.putBoolean("isFriend", false);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 2:
				intent=new Intent(VideoDetailActivity.this, YX_SendActivity.class);
				bundle=new Bundle();
				bundle.putString("text", send_text);
				bundle.putString("path", "/data/data/"+getPackageName()+"/kidsedu/"+send_path);
				bundle.putBoolean("isFriend", true);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 3:
				intent=new Intent(VideoDetailActivity.this, WXEntryActivity.class);
				bundle=new Bundle();
				bundle.putString("text", send_text);
				bundle.putString("path", "/data/data/"+getPackageName()+"/kidsedu/"+send_path);
				bundle.putBoolean("isFriend", false);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 4:
				intent=new Intent(VideoDetailActivity.this, WXEntryActivity.class);
				bundle=new Bundle();
				bundle.putString("text", send_text);
				bundle.putString("path", "/data/data/"+getPackageName()+"/kidsedu/"+send_path);
				bundle.putBoolean("isFriend", true);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 5:
				intent=new Intent(VideoDetailActivity.this, QQActivity.class);
				bundle=new Bundle();
				bundle.putString("text", send_text);
				bundle.putString("send_imageUrl", send_imageUrl);
				bundle.putString("type", "qq");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 6:
				intent=new Intent(VideoDetailActivity.this, QQActivity.class);
				bundle=new Bundle();
				bundle.putString("text", send_text);
				bundle.putString("send_imageUrl", send_imageUrl);
				bundle.putString("type", "qqkj");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 7:
				intent=new Intent(VideoDetailActivity.this, QQActivity.class);
				bundle=new Bundle();
				bundle.putString("text", send_text);
				bundle.putString("path", "/data/data/"+getPackageName()+"/kidsedu/"+send_path);
				bundle.putString("type", "tweibo");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case 8:
				intent=new Intent(VideoDetailActivity.this, WBMainActivity.class);
				bundle=new Bundle();
				bundle.putString("text", send_text);
				bundle.putString("path", "/data/data/"+getPackageName()+"/kidsedu/"+send_path);
				bundle.putString("title", "易迪乐园优秀动画片推荐");
				bundle.putString("url", "www.kidsedu.com");
				bundle.putString("defaultText", "易迪乐园");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
		}
		
		return true;
	}
	
	public void init() {
		video_detail_image=(ImageView) findViewById(R.id.video_detail_image);
		video_detail_grade=(ImageView) findViewById(R.id.video_detail_grade);
		video_detail_num=(TextView) findViewById(R.id.video_detail_num);
		video_detail_provider=(TextView) findViewById(R.id.video_detail_provider);
		video_detail_resolution=(TextView) findViewById(R.id.video_detail_resolution);
		video_detail_playButton=(Button) findViewById(R.id.video_detail_playButton);
		video_detail_playButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadPlayData(0);
			}});
		video_detail_add=(Button) findViewById(R.id.video_detail_add);
		if(Conn.getInstance(VideoDetailActivity.this).getSingleAppModel(getIntent().getExtras().getInt("id"))==null) {
			video_detail_add.setText("添加");
		}
		else {
			video_detail_add.setText("删除");
		}
		video_detail_reviews=(TextView) findViewById(R.id.video_detail_reviews);
		video_detail_reviews.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(video_detail_gridview.getVisibility()==View.GONE) {
					video_detail_gridview.setVisibility(View.VISIBLE);
					video_detail_desp.setVisibility(View.GONE);
					video_detail_reviews.setBackgroundResource(R.drawable.segment_item_bg_checked);
					video_detail_reviews.setTextColor(getResources().getColor(R.color.playchoice));
					video_detail_summary.setBackgroundResource(R.drawable.segment_item_bg);
					video_detail_summary.setTextColor(getResources().getColor(R.color.playunchoice));
				}
			}});
		video_detail_summary=(TextView) findViewById(R.id.video_detail_summary);
		video_detail_summary.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(video_detail_desp.getVisibility()==View.GONE) {
					video_detail_gridview.setVisibility(View.GONE);
					video_detail_desp.setVisibility(View.VISIBLE);
					video_detail_summary.setBackgroundResource(R.drawable.segment_item_bg_checked);
					video_detail_summary.setTextColor(getResources().getColor(R.color.playchoice));
					video_detail_reviews.setBackgroundResource(R.drawable.segment_item_bg);
					video_detail_reviews.setTextColor(getResources().getColor(R.color.playunchoice));
				}
			}});
		video_detail_desp=(TextView) findViewById(R.id.video_detail_desp);
		video_detail_gridview=(GridView) findViewById(R.id.video_detail_gridview);
		video_detail_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				loadPlayData(position);
			}
		});
		adapter=new SimpleAdapter(VideoDetailActivity.this, ui_list, R.layout.video_detail_grid_item, new String[]{"itemText"}, new int[]{R.id.itemText});
		video_detail_gridview.setAdapter(adapter);
		playVideo(getIntent().getExtras().getInt("id"));
	}
	
	private void playVideo(final int id) {
		final Handler handler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.obj==null) {
					((KEApplication) getApplicationContext()).isMusicPlay=true;
    				CommonUtils.showCustomToast(VideoDetailActivity.this, "网络异常，请稍后再试");
				}
				else {
					model=JsonParse.getVideoModelByAid(msg.obj.toString());
					
					send_text=model.getName();
					send_path=model.getIconUrl().substring(model.getIconUrl().lastIndexOf("/")+1, model.getIconUrl().length());
					send_imageUrl=((KEApplication) getApplicationContext()).kidsIconUrl+model.getIconUrl();
					
					bitmapUtils.display(video_detail_image, ((KEApplication) getApplicationContext()).kidsIconUrl+model.getIconUrl());
					video_detail_num.setText(""+model.getCommentGrade());
					video_detail_provider.setText("提供者："+model.getProvider());
					video_detail_resolution.setText("剧集：共"+model.getModel_list().size()+"集");
					video_detail_playButton.setVisibility(View.VISIBLE);
					video_detail_add.setVisibility(View.VISIBLE);
					item_list.addAll(model.getModel_list());
					for(int i=0;i<item_list.size();i++) {
						VideoItemModel item=item_list.get(i);
						HashMap<String, Object> map=new HashMap<String, Object>();
						map.put("itemText", "第"+item.getVersionCode()+"集");
						ui_list.add(map);
					}
					BigDecimal bd=new BigDecimal(""+model.getCommentGrade()).setScale(1, BigDecimal.ROUND_HALF_UP);
					switch(bd.intValue()) {
					case 5:
						video_detail_grade.setImageResource(R.drawable.fanqie_icon_red);
						break;
					case 4:
						video_detail_grade.setImageResource(R.drawable.fanqie_icon_red);
						break;
					case 3:
						video_detail_grade.setImageResource(R.drawable.fanqie_icon_orange);
						break;
					case 2:
						video_detail_grade.setImageResource(R.drawable.fanqie_icon_green);
						break;
					case 1:						
						video_detail_grade.setImageResource(R.drawable.fanqie_icon_gray);
						break;
					case 0:
						video_detail_grade.setImageResource(R.drawable.fanqie_icon_gray);
						break;
					}
					video_detail_desp.setText(model.getMobiledesc());
					video_detail_add.setOnClickListener(new Button.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(Conn.getInstance(VideoDetailActivity.this).getSingleAppModel(getIntent().getExtras().getInt("id"))==null) {
								Conn.getInstance(VideoDetailActivity.this).insertVideoModel(model, 1);
								Conn.getInstance(VideoDetailActivity.this).insertOtherPlatformByVideo(getIntent().getExtras().getInt("id"), getIntent().getExtras().getString("name"), ((KEApplication) getApplicationContext()).kidsIconUrl+model.getIconUrl());	
								video_detail_add.setText("删除");
							}
							else {
								Conn.getInstance(VideoDetailActivity.this).deleteAppModel(model.getName(), "video");
								Conn.getInstance(VideoDetailActivity.this).deleteOtherPlatformByVideo(getIntent().getExtras().getInt("id"));
								video_detail_add.setText("添加");
							}
						}});
					adapter.notifyDataSetChanged();
				}
			}
		};
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("aid", ""+id);
				String webResult=CommonUtils.getWebData(map, ((KEApplication) getApplicationContext()).kidsDataUrl+"/data/json/app/AppByAid");
				Message m=new Message();
				m.obj=webResult;
				handler.sendMessage(m);
			}
		}).start();
	}
	
	/**
	 * 调用播放器前准备
	 * @param position
	 */
	public void loadPlayData(int position) {

		String[] versionCode_array=new String[item_list.size()];
		String[] fileUrl_array=new String[item_list.size()];
		for(int i=0;i<item_list.size();i++) {
			versionCode_array[i]=item_list.get(i).getVersionCode();
			fileUrl_array[i]=item_list.get(i).getFileUrl();
		}
		
		Intent intent=new Intent(VideoDetailActivity.this, PlayerActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString("name", getIntent().getExtras().getString("name"));
		bundle.putStringArray("VersionCode", versionCode_array);
		bundle.putStringArray("FileUrl", fileUrl_array);
		bundle.putString("url", ((KEApplication) getApplicationContext()).kidsVideoUrl+item_list.get(position).getFileUrl().substring(6, item_list.get(position).getFileUrl().length())+".mp4");
		bundle.putString("source", "adult");
		intent.putExtras(bundle);
		startActivity(intent);
		Conn.getInstance(VideoDetailActivity.this).insertVideoModel(model, Integer.parseInt(item_list.get(position).getVersionCode()));
		Conn.getInstance(VideoDetailActivity.this).insertOtherPlatformByVideo(getIntent().getExtras().getInt("id"), getIntent().getExtras().getString("name"), ((KEApplication) getApplicationContext()).kidsIconUrl+model.getIconUrl());	
	}
}

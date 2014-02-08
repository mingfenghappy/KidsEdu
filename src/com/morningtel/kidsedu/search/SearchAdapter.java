package com.morningtel.kidsedu.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.lidroid.xutils.BitmapUtils;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.BitmapHelp;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.commons.DownloadAppTask;
import com.morningtel.kidsedu.commons.DownloadMusicTask;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;
import com.morningtel.kidsedu.model.AppsFilterModel;
import com.morningtel.kidsedu.service.MusicBackgroundService;
import com.morningtel.kidsedu.videolist.VideoDetailActivity;

public class SearchAdapter extends BaseAdapter implements
		PinnedSectionListAdapter {
	
	private static final int[] COLORS=new int[]{R.color.green_light, R.color.orange_light, R.color.blue_light, R.color.red_light};
	
	Context context=null;
	ArrayList<AppsFilterModel> appfilter_list=null;
	
	int currentResourceType=-1;
	
	public static BitmapUtils bitmapUtils;
	
	public SearchAdapter(Context context, HashMap<String, ArrayList<AppsFilterModel>> map) {
		this.context=context;
		
		appfilter_list=new ArrayList<AppsFilterModel>();
		
		Iterator<Entry<String, ArrayList<AppsFilterModel>>> it=map.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, ArrayList<AppsFilterModel>> entry=it.next();
			AppsFilterModel model_temp=new AppsFilterModel();
			switch(Integer.parseInt(entry.getKey())) {
			case 3:
				model_temp.setName("看看");
				break;
			case 4:
				model_temp.setName("听听");
				break;
			case 8:
				model_temp.setName("学学");
				break;
			case 9:
				model_temp.setName("读读");
				break;
			case 10:
				model_temp.setName("玩玩");
				break;
			}
			model_temp.setSearch_type(AppsFilterModel.SECTION);
			model_temp.setResourceType(Integer.parseInt(entry.getKey()));
			appfilter_list.add(model_temp);
			ArrayList<AppsFilterModel> appfilter_list_temp=entry.getValue();
			for(int i=0;i<appfilter_list_temp.size();i++) {
				AppsFilterModel model=appfilter_list_temp.get(i);
				model.setSearch_type(AppsFilterModel.ITEM);
				appfilter_list.add(model);
			}
		}
		
		bitmapUtils = BitmapHelp.getBitmapUtils(context.getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appfilter_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appfilter_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int position_=position;
		SearchAppListHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_appfilter, null);
			holder=new SearchAppListHolder();
			holder.appfilter_image=(ImageView) convertView.findViewById(R.id.appfilter_image);
			holder.appfilter_name=(TextView) convertView.findViewById(R.id.appfilter_name);
			holder.appfilter_star=(ImageView) convertView.findViewById(R.id.appfilter_star);
			holder.appfilter_otherinfo=(TextView) convertView.findViewById(R.id.appfilter_otherinfo);
			holder.appfilter_download=(ImageView) convertView.findViewById(R.id.appfilter_download);
			holder.appfilter_detailinfo=(TextView) convertView.findViewById(R.id.appfilter_detailinfo);
			holder.search_section_layout=(LinearLayout) convertView.findViewById(R.id.search_section_layout);
			convertView.setTag(holder);
		}
		else {
			holder=(SearchAppListHolder) convertView.getTag();
		}
		if (appfilter_list.get(position).getSearch_type()==AppsFilterModel.SECTION) {
			switch(appfilter_list.get(position).getResourceType()) {
			case 3:
				convertView.setBackgroundColor(parent.getResources().getColor(COLORS[0]));
				break;
			case 4:
				convertView.setBackgroundColor(parent.getResources().getColor(COLORS[1]));
				break;
			case 8:
				convertView.setBackgroundColor(parent.getResources().getColor(COLORS[2]));
				break;
			case 9:
				convertView.setBackgroundColor(parent.getResources().getColor(COLORS[2]));
				break;
			case 10:
				convertView.setBackgroundColor(parent.getResources().getColor(COLORS[2]));
				break;
			}			
			holder.search_section_layout.setVisibility(View.GONE);
			holder.appfilter_detailinfo.setText(""+appfilter_list.get(position).getName());
			holder.appfilter_detailinfo.setTextColor(Color.WHITE);
			holder.appfilter_detailinfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			holder.appfilter_name.setText("");
        }
		else {
			convertView.setBackgroundColor(Color.WHITE);
			holder.search_section_layout.setVisibility(View.VISIBLE);
			holder.appfilter_name.setText(""+appfilter_list.get(position).getName());
			holder.appfilter_otherinfo.setText(appfilter_list.get(position).getDownloadCount()+"次下载");
			bitmapUtils.display(holder.appfilter_image, ((KEApplication) context.getApplicationContext()).kidsIconUrl+CommonUtils.getIconAdd(appfilter_list.get(position).getIconUrl()));
			int index=appfilter_list.get(position).getMobiledesc().indexOf("。");
			if(index==-1) {
				holder.appfilter_detailinfo.setText(appfilter_list.get(position).getMobiledesc());
			}
			else {
				holder.appfilter_detailinfo.setText(appfilter_list.get(position).getMobiledesc().substring(0, index));
			}
			holder.appfilter_detailinfo.setTextColor(context.getResources().getColor(R.color.playunchoice));
			holder.appfilter_detailinfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			BigDecimal bd=new BigDecimal(""+appfilter_list.get(position).getCommentGrade()).setScale(1, BigDecimal.ROUND_HALF_UP);
			switch(bd.intValue()) {
			case 5:
				holder.appfilter_star.setImageResource(R.drawable.star5);
				break;
			case 4:
				if(bd.doubleValue()>4.5) {
					holder.appfilter_star.setImageResource(R.drawable.star4h);
				}
				else {
					holder.appfilter_star.setImageResource(R.drawable.star4);
				}
				break;
			case 3:
				if(bd.doubleValue()>3.5) {
					holder.appfilter_star.setImageResource(R.drawable.star3h);
				}
				else {
					holder.appfilter_star.setImageResource(R.drawable.star3);
				}
				break;
			case 2:
				if(bd.doubleValue()>2.5) {
					holder.appfilter_star.setImageResource(R.drawable.star3h);
				}
				else {
					holder.appfilter_star.setImageResource(R.drawable.star3);
				}
				break;
			case 1:
				if(bd.doubleValue()>1.5) {
					holder.appfilter_star.setImageResource(R.drawable.star1h);
				}
				else {
					holder.appfilter_star.setImageResource(R.drawable.star1);
				}
				break;
			case 0:
				if(bd.doubleValue()>0.5) {
					holder.appfilter_star.setImageResource(R.drawable.star0h);
				}
				else {
					holder.appfilter_star.setImageResource(R.drawable.star0);
				}
				break;
			}
			final ImageView imageview=holder.appfilter_download;
			if(appfilter_list.get(position).getResourceType()==8||appfilter_list.get(position).getResourceType()==9||appfilter_list.get(position).getResourceType()==10) {
				if(CommonUtils.checkAppInstall(appfilter_list.get(position_).getPackageName(), context)) {
					holder.appfilter_download.setImageResource(R.drawable.app_open_icon);
					holder.appfilter_download.setOnClickListener(new ImageView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							CommonUtils.openApp(context, appfilter_list.get(position_).getPackageName());
						}});
				}
				else if(!CommonUtils.checkAppInstall(appfilter_list.get(position_).getPackageName(), context)&&((KEApplication) context.getApplicationContext()).download_app_maps.containsKey(appfilter_list.get(position_).getPackageName())) {
					holder.appfilter_download.setImageResource(R.drawable.download_cancle);
					holder.appfilter_download.setOnClickListener(new ImageView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(!((KEApplication) context.getApplicationContext()).getDownload_stop_list().contains(appfilter_list.get(position_).getPackageName())) {
								((KEApplication) context.getApplicationContext()).getDownload_stop_list().add(appfilter_list.get(position_).getPackageName());
								CommonUtils.showCustomToast(context, "即将停止下载"+appfilter_list.get(position_).getName());
								notifyDataSetChanged();
							}
							else {
								CommonUtils.showCustomToast(context, "正在停止下载"+appfilter_list.get(position_).getName()+"，请稍后");
							}
						}});
				}
				else {
					holder.appfilter_download.setImageResource(R.drawable.myapp_item_action_download_image);
					holder.appfilter_download.setOnClickListener(new ImageView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(((KEApplication) context.getApplicationContext()).download_app_maps.containsKey(appfilter_list.get(position_).getPackageName())) {
								return;
							}
							DownloadAppTask task=new DownloadAppTask();
							task.setParams(context, appfilter_list.get(position_).getId(), appfilter_list.get(position_).getName(), appfilter_list.get(position_).getPackageName());
							task.execute(""+appfilter_list.get(position_).getId());
							imageview.setImageResource(R.drawable.myapp_item_action_redownload_image);	
							((KEApplication) context.getApplicationContext()).download_app_maps.put(appfilter_list.get(position_).getPackageName(), 0);
							notifyDataSetChanged();				
						}});
				}
			}
			else if(appfilter_list.get(position).getResourceType()==3) {
				holder.appfilter_download.setImageResource(R.drawable.media_video_icon);
				holder.appfilter_download.setOnClickListener(new ImageView.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(context, VideoDetailActivity.class);
						Bundle bundle=new Bundle();
						bundle.putInt("id", appfilter_list.get(position_).getId());
						bundle.putString("name", appfilter_list.get(position_).getName());
						intent.putExtras(bundle);
						context.startActivity(intent);
					}});
			}
			else if(appfilter_list.get(position).getResourceType()==4) {
				holder.search_section_layout.setOnClickListener(new LinearLayout.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}});
				final AppModel model=Conn.getInstance(context).isMusicExists(appfilter_list.get(position_).getName());
				if(model!=null) {
					holder.appfilter_download.setImageResource(R.drawable.media_play_icon);
					imageview.setOnClickListener(new ImageView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(context, MusicBackgroundService.class);
							Bundle bundle=new Bundle();
							bundle.putString("image", ((KEApplication) context.getApplicationContext()).kidsIconUrl+CommonUtils.getIconAdd(appfilter_list.get(position_).getIconUrl()));
							bundle.putString("name", appfilter_list.get(position_).getName());
							bundle.putString("url", Environment.getExternalStorageDirectory().getAbsolutePath()+"/kidsedu/temp/"+model.getFileUrl().substring(model.getFileUrl().lastIndexOf("/")+1));
							bundle.putBoolean("isNewStartFlag", true);
							intent.putExtras(bundle);
							context.startService(intent);
						}});
				}
				else if(((KEApplication) context.getApplicationContext()).download_music_maps.containsKey(appfilter_list.get(position_).getName())) {
					holder.appfilter_download.setImageResource(R.drawable.download_cancle);
					holder.appfilter_download.setOnClickListener(new ImageView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(!((KEApplication) context.getApplicationContext()).getDownload_stop_list().contains(appfilter_list.get(position_).getName())) {
								((KEApplication) context.getApplicationContext()).getDownload_stop_list().add(appfilter_list.get(position_).getName());
								CommonUtils.showCustomToast(context, "即将停止下载"+appfilter_list.get(position_).getName());
								notifyDataSetChanged();
							}
							else {
								CommonUtils.showCustomToast(context, "正在停止下载"+appfilter_list.get(position_).getName()+"，请稍后");
							}
						}});
				}
				else {
					holder.appfilter_download.setImageResource(R.drawable.media_add_icon);
					imageview.setOnClickListener(new ImageView.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(((KEApplication) context.getApplicationContext()).download_music_maps.containsKey(appfilter_list.get(position_).getName())) {
								CommonUtils.showCustomToast(context, appfilter_list.get(position_).getName()+"正在下载中");
								return;
							}
							DownloadMusicTask task=new DownloadMusicTask();
							task.setParams(context, appfilter_list.get(position_).getId(), appfilter_list.get(position_).getName());
							task.execute(""+appfilter_list.get(position_).getId());			
							imageview.setImageResource(R.drawable.myapp_item_action_redownload_image);	
							((KEApplication) context.getApplicationContext()).download_music_maps.put(appfilter_list.get(position_).getName(), 0);					
							notifyDataSetChanged();	
						}});
				}
			}
		}
		
		return convertView;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		return viewType==AppsFilterModel.SECTION;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(appfilter_list.get(position).getSearch_type()==AppsFilterModel.ITEM) {
			return AppsFilterModel.ITEM;
		}
		else {
			return AppsFilterModel.SECTION;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}

class SearchAppListHolder {
	ImageView appfilter_image=null;
	TextView appfilter_name=null;
	ImageView appfilter_star=null;
	TextView appfilter_otherinfo=null;
	ImageView appfilter_download=null;
	TextView appfilter_detailinfo=null;
	LinearLayout search_section_layout=null;
}

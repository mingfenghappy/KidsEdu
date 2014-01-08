package com.morningtel.kidsedu.applist;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.lidroid.xutils.BitmapUtils;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.BitmapHelp;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.commons.DownloadTask;
import com.morningtel.kidsedu.model.AppsFilterModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListAdapter extends BaseAdapter {
	
	ArrayList<AppsFilterModel> appfilter_list=null;
	Context context=null;
	
	public static BitmapUtils bitmapUtils;
	
	public AppListAdapter(ArrayList<AppsFilterModel> appfilter_list, Context context) {
		this.appfilter_list=appfilter_list;
		this.context=context;
		
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
		AppListHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_appfilter, null);
			holder=new AppListHolder();
			holder.appfilter_image=(ImageView) convertView.findViewById(R.id.appfilter_image);
			holder.appfilter_name=(TextView) convertView.findViewById(R.id.appfilter_name);
			holder.appfilter_star=(ImageView) convertView.findViewById(R.id.appfilter_star);
			holder.appfilter_otherinfo=(TextView) convertView.findViewById(R.id.appfilter_otherinfo);
			holder.appfilter_download=(ImageView) convertView.findViewById(R.id.appfilter_download);
			holder.appfilter_detailinfo=(TextView) convertView.findViewById(R.id.appfilter_detailinfo);
			convertView.setTag(holder);
		}
		else {
			holder=(AppListHolder) convertView.getTag();
		}
		
		bitmapUtils.display(holder.appfilter_image, ((KEApplication) context.getApplicationContext()).kidsIconUrl+CommonUtils.getIconAdd(appfilter_list.get(position).getIconUrl()));
		holder.appfilter_name.setText(appfilter_list.get(position).getName());
		holder.appfilter_otherinfo.setText(appfilter_list.get(position).getDownloadCount()+"´ÎÏÂÔØ");
		int index=appfilter_list.get(position).getMobiledesc().indexOf("¡£");
		if(index==-1) {
			holder.appfilter_detailinfo.setText(appfilter_list.get(position).getMobiledesc());
		}
		else {
			holder.appfilter_detailinfo.setText(appfilter_list.get(position).getMobiledesc().substring(0, index));
		}
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
		if(CommonUtils.checkAppInstall(appfilter_list.get(position_).getPackageName(), context)) {
			holder.appfilter_download.setImageResource(R.drawable.myapp_item_action_uninstall_image);
			holder.appfilter_download.setOnClickListener(new ImageView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CommonUtils.uninstall(appfilter_list.get(position_).getPackageName(), context);
				}});
		}
		else if(!CommonUtils.checkAppInstall(appfilter_list.get(position_).getPackageName(), context)&&((KEApplication) context.getApplicationContext()).download_maps.containsKey(appfilter_list.get(position_).getPackageName())) {
			holder.appfilter_download.setImageResource(R.drawable.myapp_item_action_resume_image);
		}
		else {
			holder.appfilter_download.setImageResource(R.drawable.myapp_item_action_download_image);
			holder.appfilter_download.setOnClickListener(new ImageView.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DownloadTask task=new DownloadTask();
					task.setParams(context, appfilter_list.get(position_).getId(), appfilter_list.get(position_).getName(), appfilter_list.get(position_).getPackageName());
					task.execute(""+appfilter_list.get(position_).getId());
					imageview.setImageResource(R.drawable.myapp_item_action_resume_image);
					
				}});
		}
		
		return convertView;
	}

}

class AppListHolder {
	ImageView appfilter_image=null;
	TextView appfilter_name=null;
	ImageView appfilter_star=null;
	TextView appfilter_otherinfo=null;
	ImageView appfilter_download=null;
	TextView appfilter_detailinfo=null;
}

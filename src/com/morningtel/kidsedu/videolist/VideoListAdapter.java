package com.morningtel.kidsedu.videolist;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.lidroid.xutils.BitmapUtils;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.BitmapHelp;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.model.AppsFilterModel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter {
	
	ArrayList<AppsFilterModel> appfilter_list=null;
	Context context=null;
	
	public static BitmapUtils bitmapUtils;
	
	public VideoListAdapter(ArrayList<AppsFilterModel> appfilter_list, Context context) {
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
		VideoListHolder holder=null;
		if(convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_appfilter, null);
			holder=new VideoListHolder();
			holder.appfilter_image=(ImageView) convertView.findViewById(R.id.appfilter_image);
			holder.appfilter_name=(TextView) convertView.findViewById(R.id.appfilter_name);
			holder.appfilter_star=(ImageView) convertView.findViewById(R.id.appfilter_star);
			holder.appfilter_otherinfo=(TextView) convertView.findViewById(R.id.appfilter_otherinfo);
			holder.appfilter_download=(ImageView) convertView.findViewById(R.id.appfilter_download);
			holder.appfilter_detailinfo=(TextView) convertView.findViewById(R.id.appfilter_detailinfo);
			convertView.setTag(holder);
		}
		else {
			holder=(VideoListHolder) convertView.getTag();
		}
		
		bitmapUtils.display(holder.appfilter_image, ((KEApplication) context.getApplicationContext()).kidsIconUrl+CommonUtils.getIconAdd(appfilter_list.get(position).getIconUrl()));
		holder.appfilter_name.setText(appfilter_list.get(position).getName());
		holder.appfilter_otherinfo.setText(appfilter_list.get(position).getDownloadCount()+"´Î²¥·Å");
		int index=appfilter_list.get(position).getMobiledesc().indexOf("¡£");
		if(index==-1) {
			if(appfilter_list.get(position).getMobiledesc().equals("")) {
				holder.appfilter_detailinfo.setVisibility(View.GONE);
			}
			else {
				holder.appfilter_detailinfo.setText(appfilter_list.get(position).getMobiledesc());
				holder.appfilter_detailinfo.setVisibility(View.VISIBLE);
			}
		}
		else {
			holder.appfilter_detailinfo.setText(appfilter_list.get(position).getMobiledesc().substring(0, index));
			holder.appfilter_detailinfo.setVisibility(View.VISIBLE);
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
		
		return convertView;
	}

}

class VideoListHolder {
	ImageView appfilter_image=null;
	TextView appfilter_name=null;
	ImageView appfilter_star=null;
	TextView appfilter_otherinfo=null;
	ImageView appfilter_download=null;
	TextView appfilter_detailinfo=null;
}

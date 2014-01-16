package com.morningtel.kidsedu.account;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.lidroid.xutils.BitmapUtils;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.BitmapHelp;
import com.morningtel.kidsedu.commons.CommonUtils;
import com.morningtel.kidsedu.db.Conn;
import com.morningtel.kidsedu.model.AppModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountMusicAdapter extends BaseAdapter {
	
	ArrayList<AppModel> model_list=null;
	Context context=null;
	OnRefreshListener lis=null;
	
	public static BitmapUtils bitmapUtils;
	
	public AccountMusicAdapter(ArrayList<AppModel> model_list, Context context, OnRefreshListener lis) {
		this.model_list=model_list;
		this.context=context;
		this.lis=lis;
		
		bitmapUtils = BitmapHelp.getBitmapUtils(context.getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return model_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return model_list.get(position);
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
		Account_MusicList_Holder holder=null;
		if(convertView==null) {
			holder=new Account_MusicList_Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.adapter_account_music, null);
			holder.button_remove=(Button) convertView.findViewById(R.id.button_remove);
			holder.account_image=(ImageView) convertView.findViewById(R.id.account_image);
			holder.account_title=(TextView) convertView.findViewById(R.id.account_title);
			holder.account_otherinfo=(TextView) convertView.findViewById(R.id.account_otherinfo);
			holder.account_star=(ImageView) convertView.findViewById(R.id.account_star);
			convertView.setTag(holder);
		}
		else {
			holder=(Account_MusicList_Holder)convertView.getTag();
		}
		
		((SwipeListView)parent).recycle(convertView, position);
		 
		bitmapUtils.display(holder.account_image, ((KEApplication) context.getApplicationContext()).kidsIconUrl+CommonUtils.getIconAdd(model_list.get(position).getIconUrl()));
		holder.account_title.setText(model_list.get(position).getName());
		holder.account_otherinfo.setText(model_list.get(position).getDownloadCount()+"´Î²¥·Å");
		int index=model_list.get(position).getMobiledesc().indexOf("¡£");
		if(index==-1) {
			if(model_list.get(position).getMobiledesc().equals("")) {
				holder.account_otherinfo.setVisibility(View.GONE);
			}
			else {
				holder.account_otherinfo.setText(model_list.get(position).getMobiledesc());
				holder.account_otherinfo.setVisibility(View.VISIBLE);
			}
		}
		else {
			holder.account_otherinfo.setText(model_list.get(position).getMobiledesc().substring(0, index));
			holder.account_otherinfo.setVisibility(View.VISIBLE);
		}
		BigDecimal bd=new BigDecimal(""+model_list.get(position).getCommentGrade()).setScale(1, BigDecimal.ROUND_HALF_UP);
		switch(bd.intValue()) {
		case 5:
			holder.account_star.setImageResource(R.drawable.star5);
			break;
		case 4:
			if(bd.doubleValue()>4.5) {
				holder.account_star.setImageResource(R.drawable.star4h);
			}
			else {
				holder.account_star.setImageResource(R.drawable.star4);
			}
			break;
		case 3:
			if(bd.doubleValue()>3.5) {
				holder.account_star.setImageResource(R.drawable.star3h);
			}
			else {
				holder.account_star.setImageResource(R.drawable.star3);
			}
			break;
		case 2:
			if(bd.doubleValue()>2.5) {
				holder.account_star.setImageResource(R.drawable.star3h);
			}
			else {
				holder.account_star.setImageResource(R.drawable.star3);
			}
			break;
		case 1:
			if(bd.doubleValue()>1.5) {
				holder.account_star.setImageResource(R.drawable.star1h);
			}
			else {
				holder.account_star.setImageResource(R.drawable.star1);
			}
			break;
		case 0:
			if(bd.doubleValue()>0.5) {
				holder.account_star.setImageResource(R.drawable.star0h);
			}
			else {
				holder.account_star.setImageResource(R.drawable.star0);
			}
			break;
		}
		holder.button_remove.setText("É¾³ý");
		holder.button_remove.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Conn.getInstance(context).deleteAppModel(model_list.get(position_).getName(), "music");
				model_list.remove(position_);
				lis.refreshListView();
			}});
		return convertView;
	}
	

	public interface OnRefreshListener {
		public void refreshListView();
	}

}


class Account_MusicList_Holder {
	Button button_remove=null;
	ImageView account_image=null;
	TextView account_title=null;
	TextView account_otherinfo=null;
	ImageView account_star=null;
}

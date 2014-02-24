package com.morningtel.kidsedu.myview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {
	
	//������ʾ���
	int maxNum=0;
	//ÿ�ε������
	int perNum=0;
	//��ǰ���
	int currentNum=0;

	public double getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(currentNum<maxNum) {
				currentNum+=perNum;
			}
			else {
				currentNum=maxNum;
			}
			setText(""+currentNum+"�ױ�");
			handler.sendMessageDelayed(new Message(), 50);
		}
	};
	
	public void setStart() {
		perNum=maxNum/20;
		handler.sendMessage(new Message());
	}

}

package com.morningtel.kidsedu.mediaplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.KEApplication;
import com.morningtel.kidsedu.R;
import com.morningtel.kidsedu.commons.CommonUtils;

public class PlayerActivity extends BaseActivity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {
	
	TextView nav_title=null;
	ImageView playerlightbutton=null;
	ImageView playerplaybutton=null;
	ImageView playervolumebutton=null;
	TextView playercurrentplaytime=null;
	TextView playertotalplaytime=null;
	SeekBar playerplayprogressseekbar=null;
	TextView player_choice=null;
	ListView anthology_list=null;
	SimpleAdapter adapter=null;
	SurfaceView surface=null;
	ImageView playLoadingImage=null;
	LinearLayout playLoadingLayout=null;
	TextView playLoadingPersent=null;
	SeekBar light_seekbar=null;
	SeekBar voice_seekbar=null;
	LinearLayout player_title_control=null;
	RelativeLayout player_bottom_control=null;
	
	AudioManager mAudioManager=null;
	MediaPlayer mMediaPlayer=null;
	SurfaceHolder holder=null;
	
	private boolean mIsVideoSizeKnown=false;
	private boolean mIsVideoReadyToBePlayed=false;
	private int mVideoWidth=0;
	private int mVideoHeight=0;
	private String path="";
	private int screenWidth=0;
	private int screenHeight=0;
	//是否已经暂停
	private boolean isPause=false;
	//当前播放的位置
	long playPos=0;
	//总播放时间
	long duration=0;
	//最低亮度
	int MINIMUM_LIGHT=30;
	//最高亮度
	int MAXIMUM_LIGHT=255;
	String[] versionCode_array;
	String[] fileUrl_array;
	
	Handler handler_voice_bar=null;
	Handler handler_contro_screen=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (!LibsChecker.checkVitamioLibs(this)) {
			return;
		}
		
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth=dm.widthPixels;
		screenHeight=dm.heightPixels;
		
		mAudioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		setContentView(R.layout.player_layout);

		path=getIntent().getExtras().getString("url");
		
		init();
	}
	
	public void init() {
		handler_voice_bar=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==1) {
					if(mMediaPlayer!=null) {
						playerplayprogressseekbar.setProgress((int) (((float) mMediaPlayer.getCurrentPosition()/duration)*100));
						playercurrentplaytime.setText(""+CommonUtils.toTime((int) mMediaPlayer.getCurrentPosition()));
					}
					handler_voice_bar.sendEmptyMessageDelayed(1, 1000);
				}
			}
		};
		
		handler_contro_screen=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==2) {
					player_title_control.setVisibility(View.GONE);
					player_bottom_control.setVisibility(View.GONE);
					voice_seekbar.setVisibility(View.GONE);
					light_seekbar.setVisibility(View.GONE);
					player_choice.setVisibility(View.GONE);
					anthology_list.setVisibility(View.GONE);
				}
			}
		};
		
		player_title_control=(LinearLayout) findViewById(R.id.player_title_control);
		player_title_control.setVisibility(View.GONE);
		player_bottom_control=(RelativeLayout) findViewById(R.id.player_bottom_control);
		player_bottom_control.setVisibility(View.GONE);
		nav_title=(TextView) findViewById(R.id.nav_title);
		nav_title.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		nav_title.setText(getIntent().getExtras().getString("name"));
		playerlightbutton=(ImageView) findViewById(R.id.playerlightbutton);
		playerlightbutton.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(light_seekbar.getVisibility()==View.VISIBLE) {
					light_seekbar.setVisibility(View.GONE);
				}
				else {
					light_seekbar.setVisibility(View.VISIBLE);
				}
			}});
		playerplaybutton=(ImageView) findViewById(R.id.playerplaybutton);
		playerplaybutton.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isPause) {
					playerplaybutton.setImageResource(R.drawable.player_playbtn_selector);
					mMediaPlayer.start();
				}
				else {
					playerplaybutton.setImageResource(R.drawable.player_pause_selector);
					mMediaPlayer.pause();
				}
				isPause=!isPause;
			}});
		playervolumebutton=(ImageView) findViewById(R.id.playervolumebutton);
		playervolumebutton.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(voice_seekbar.getVisibility()==View.VISIBLE) {
					voice_seekbar.setVisibility(View.GONE);
				}
				else {
					voice_seekbar.setVisibility(View.VISIBLE);
				}
			}});
		playercurrentplaytime=(TextView) findViewById(R.id.playercurrentplaytime);
		playertotalplaytime=(TextView) findViewById(R.id.playertotalplaytime);
		playerplayprogressseekbar=(SeekBar) findViewById(R.id.playerplayprogressseekbar);
		playerplayprogressseekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				handler_voice_bar.sendEmptyMessageDelayed(1, 2000);
				handler_contro_screen.sendEmptyMessageDelayed(2, 4000);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				handler_voice_bar.removeMessages(1);
				handler_contro_screen.removeMessages(2);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser) {
					mMediaPlayer.seekTo((int) ((float) progress/100*duration));
				}
			}
		});
		anthology_list=(ListView) findViewById(R.id.anthology_list);
		versionCode_array=getIntent().getExtras().getStringArray("VersionCode");
		fileUrl_array=getIntent().getExtras().getStringArray("FileUrl");
		ArrayList<HashMap<String, String>> model_list=new ArrayList<HashMap<String, String>>();
		for(int i=0;i<versionCode_array.length;i++) {
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("item", "第"+versionCode_array[i]+"集");
			model_list.add(map);
		}
		adapter=new SimpleAdapter(PlayerActivity.this, model_list, R.layout.video_detail_grid_item, new String[]{"item"}, new int[]{R.id.itemText});
		anthology_list.setAdapter(adapter);
		anthology_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(mMediaPlayer!=null) {
					mMediaPlayer.stop();
				}
				releaseMediaPlayer();
				doCleanUp();
				path=((KEApplication) getApplicationContext()).kidsVideoUrl+fileUrl_array[position].substring(6, fileUrl_array[position].length())+".mp4";
				play();
			}
		});
		anthology_list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch(scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					handler_contro_screen.sendEmptyMessageDelayed(2, 4000);
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					handler_contro_screen.removeMessages(2);
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		player_choice=(TextView) findViewById(R.id.player_choice);
		player_choice.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(anthology_list.getVisibility()==View.GONE) {
					anthology_list.setVisibility(View.VISIBLE);
				}
				else {
					anthology_list.setVisibility(View.GONE);
				}
			}});
		surface=(SurfaceView) findViewById(R.id.surface);
		surface.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(player_title_control.getVisibility()==View.VISIBLE||player_bottom_control.getVisibility()==View.VISIBLE) {
					player_title_control.setVisibility(View.GONE);
					player_bottom_control.setVisibility(View.GONE);
					voice_seekbar.setVisibility(View.GONE);
					light_seekbar.setVisibility(View.GONE);
					player_choice.setVisibility(View.GONE);
					anthology_list.setVisibility(View.GONE);
					handler_contro_screen.removeMessages(2);
				}
				else {
					player_title_control.setVisibility(View.VISIBLE);
					player_bottom_control.setVisibility(View.VISIBLE);
					player_choice.setVisibility(View.VISIBLE);
					handler_contro_screen.sendEmptyMessageDelayed(2, 4000);
				}
			}});	
		holder=surface.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888); 
		playLoadingLayout=(LinearLayout) findViewById(R.id.playLoadingLayout);
		playLoadingPersent=(TextView) findViewById(R.id.playLoadingPersent);
		playLoadingImage=(ImageView) findViewById(R.id.playLoadingImage);
		AnimationDrawable animationDrawable = (AnimationDrawable) playLoadingImage.getDrawable();  
        animationDrawable.start();
        
        light_seekbar=(SeekBar) findViewById(R.id.light_seekbar);
        light_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				handler_contro_screen.sendEmptyMessageDelayed(2, 4000);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				handler_contro_screen.removeMessages(2);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = getWindow().getAttributes();  
                lp.screenBrightness =(float) ((((float) (progress+MINIMUM_LIGHT)) / MAXIMUM_LIGHT * 1.0));  
                getWindow().setAttributes(lp);  
			}
		});
        light_seekbar.setMax(MAXIMUM_LIGHT-MINIMUM_LIGHT);
        int nowBrightnessValue = 0;  
        boolean automicBrightness=false;
        ContentResolver resolver=getContentResolver();  
        try { 
        	automicBrightness = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE)==Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC; 
            nowBrightnessValue=Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);  
        } catch (Exception e) { 
        
        } 
        if(automicBrightness) {
        	light_seekbar.setProgress(100);
        }
        else {
        	light_seekbar.setProgress(nowBrightnessValue);
        }
        
        voice_seekbar=(SeekBar) findViewById(R.id.voice_seekbar);
        voice_seekbar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        voice_seekbar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        voice_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				handler_contro_screen.sendEmptyMessageDelayed(2, 4000);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				handler_contro_screen.removeMessages(2);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0); //tempVolume:音量绝对值
			}
		});
	}
	
	private void playVideo() {
		doCleanUp();
		// Create a new media player and set the listeners
		
		try {
			mMediaPlayer=new MediaPlayer(this);
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepare();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void doCleanUp() {
		mVideoWidth=0;
		mVideoHeight=0;
		mIsVideoReadyToBePlayed=false;
		mIsVideoSizeKnown=false;
	}
	
	private void releaseMediaPlayer() {
		if(mMediaPlayer!=null) {
			mMediaPlayer.release();
			mMediaPlayer=null;
		}

		if(handler_voice_bar!=null) {
			handler_voice_bar.removeMessages(1);
		}
	}
	
	private void startVideoPlayback() {
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		if(playPos>0) {
			mMediaPlayer.seekTo(playPos);			
			playPos=0;
		}
		if(isPause) {
			mMediaPlayer.pause();
		}
		else {
			mMediaPlayer.start();			
		}
		
	}
	
	public void play() {
		
		final Handler handler_play=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				playLoadingLayout.setVisibility(View.VISIBLE);
			}
		};
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				playVideo();
				handler_play.sendMessage(new Message());
			}}).start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		play();		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// TODO Auto-generated method stub
		if(width==0||height==0) {
			Toast.makeText(PlayerActivity.this, "资源获取失败，请稍后再试", Toast.LENGTH_SHORT).show();
		}
		mIsVideoSizeKnown = true;
		float scale=CommonUtils.getScale(width, height, screenWidth, screenHeight);
		LayoutParams lp=surface.getLayoutParams();
		lp.width=(int) (scale*width);
		lp.height=(int) (scale*height);
		surface.setLayoutParams(lp);
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
			playLoadingLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		duration=mMediaPlayer.getDuration();
		playertotalplaytime.setText(""+CommonUtils.toTime((int) duration));
		handler_voice_bar.sendEmptyMessage(1);
		mIsVideoReadyToBePlayed=true;
		if(mIsVideoReadyToBePlayed&&mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mMediaPlayer.seekTo(0);
		mMediaPlayer.pause();
		playerplaybutton.setImageResource(R.drawable.player_pause_selector);
		isPause=true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		//System.out.println(percent);
		playLoadingPersent.setText(""+percent);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		playLoadingLayout.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mMediaPlayer!=null) {
			playPos=mMediaPlayer.getCurrentPosition();
			mMediaPlayer.stop();
		}
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_VOLUME_UP||keyCode==KeyEvent.KEYCODE_VOLUME_DOWN) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

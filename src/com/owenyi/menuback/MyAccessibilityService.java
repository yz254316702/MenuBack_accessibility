package com.owenyi.menuback;

import java.util.ArrayList;
import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View;
import android.view.MotionEvent;;

public class MyAccessibilityService extends AccessibilityService {

	public static int INVOKE_TYPE = 0;
	public static final int TYPE_ACTION_BACK = 1;
	public static final int TYPE_ACTION_MENU = 2;

	public static void reset() {
		INVOKE_TYPE = 0;
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		this.processAccessibilityEnvent(event);
	}

	private void processAccessibilityEnvent(AccessibilityEvent event) {

		Log.d("test", event.eventTypeToString(event.getEventType()));
		if (event.getSource() == null) {
			Log.d("test", "the source = null");
		}
//		{
//			Log.d("test", "event = " + event.toString());
//			switch (INVOKE_TYPE) {
//			case TYPE_ACTION_BACK:
//					performGlobalAction (GLOBAL_ACTION_BACK);
//					reset();
//				break;
//			case TYPE_ACTION_MENU:
//					performGlobalAction (GLOBAL_ACTION_RECENTS);
//					reset();
//				break;
//			default:
//				break;
//			}
//		}
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		createFloatView();
		
		Thread t = new Thread(new Runnable()  
        {  
            @Override  
            public void run()  
            {  
                while(true) {
                	if(MyAccessibilityService.INVOKE_TYPE == MyAccessibilityService.TYPE_ACTION_MENU) {
                		MyAccessibilityService.this.performGlobalAction(GLOBAL_ACTION_RECENTS);
                		MyAccessibilityService.INVOKE_TYPE = 0;
                	}
                	else if(MyAccessibilityService.INVOKE_TYPE == MyAccessibilityService.TYPE_ACTION_BACK) {
                		MyAccessibilityService.this.performGlobalAction(GLOBAL_ACTION_BACK);
                		MyAccessibilityService.INVOKE_TYPE = 0;
                	}
                	try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }  
        });
        t.start();
	}
	@Override
	protected boolean onKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return true;

	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}
	
	//定义浮动窗口布局  
	//LinearLayout mFloatLayout;  
	WindowManager.LayoutParams wmParams;  
	//创建浮动窗口设置布局参数的对象  
	WindowManager mWindowManager;  
	LinearLayout mFloatView; 
	Button mButtonMenu;
	Button mButtonBack;
	private void createFloatView()  
	{  
		wmParams = new WindowManager.LayoutParams();  
		//获取的是WindowManagerImpl.CompatModeWrapper  
		mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);  
		Log.i("test", "mWindowManager--->" + mWindowManager);  
		//设置window type  
		wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR;//TYPE_SYSTEM_ERROR;//TYPE_PHONE;   
		//设置图片格式，效果为背景透明  
		wmParams.format = PixelFormat.RGBA_8888;   
		//设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）  
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_LAYOUT_IN_SCREEN ;        
		//调整悬浮窗显示的停靠位置为左侧置顶  
		wmParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;         
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity  
		wmParams.x = 0;  
		wmParams.y = 0;  

		//设置悬浮窗口长宽数据    
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int STATUS_BAR_HEIGHT = (int) Math.ceil( 25 * metrics.density);
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
		wmParams.height = STATUS_BAR_HEIGHT;//WindowManager.LayoutParams.WRAP_CONTENT;  

		/*// 设置悬浮窗口长宽数据 
	        wmParams.width = 200; 
	      wmParams.height = 80;*/  

		//获取浮动窗口视图所在布局  
		//LayoutInflater inflater = LayoutInflater.from(getApplication());  
		//mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);  
		//添加mFloatLayout  
		//mWindowManager.addView(mFloatLayout, wmParams);  
		//浮动窗口按钮  
		mFloatView = new LinearLayout(getApplicationContext());
		mButtonMenu = new Button(getApplicationContext());
		mButtonBack = new Button(getApplicationContext());
		mButtonMenu.setWidth(20);mButtonMenu.setBackgroundColor(Color.BLUE);
		mButtonBack.setWidth(20);mButtonBack.setBackgroundColor(Color.RED);
		mButtonMenu.setAlpha((float) 0.5);
		mButtonBack.setAlpha((float) 0.5);
		mButtonBack.setContentDescription("Button Back");
		mButtonMenu.setContentDescription("Button Menu");
		mButtonBack.setText("很低");
		mButtonMenu.setText("速度");
		mFloatView.addView(mButtonMenu);
		mFloatView.addView(mButtonBack);
		mFloatView.setLayoutParams(new LayoutParams(20, STATUS_BAR_HEIGHT));
		mWindowManager.addView(mFloatView, wmParams); 
		
		Log.i("test", "Width/2--->" + mFloatView.getMeasuredWidth()/2);  
		Log.i("test", "Height/2--->" + mFloatView.getMeasuredHeight()/2);  
		//设置监听浮动窗口的触摸移动  
	

		mButtonMenu.setOnClickListener(new OnClickListener()   
		{  

			@Override  
			public void onClick(View v)   
			{ Log.d("test","  mButtonMenu onClick");
				MyAccessibilityService.INVOKE_TYPE = MyAccessibilityService.TYPE_ACTION_MENU;
				//mButtonMenu.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
				// TODO Auto-generated method stub  
			}  
		});  
		mButtonBack.setOnClickListener(new OnClickListener()   
		{  

			@Override  
			public void onClick(View v)   
			{ Log.d("test","  mButtonBack onClick");
				MyAccessibilityService.INVOKE_TYPE = MyAccessibilityService.TYPE_ACTION_BACK;
				//mButtonBack.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
				// TODO Auto-generated method stub  
			}  
		}); 
	} 
}

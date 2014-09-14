package com.huadev.search.dao;

import java.util.Timer;
import java.util.TimerTask;

import com.huadev.search.wnd.WndSearchDesk;
import com.huadev.utils.AppRes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.WindowManager;


public class WndMgrServiceDao extends Service{
	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler m_handler = new Handler();	
	private WndSearchDesk m_wndSearchDesk;

	/**
	 * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
	 */
	private Timer m_timer;

	class UIRefreshTask extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			m_handler.post(new Runnable() {
				@Override
				public void run() {
					
				
					
					if(AppRes.getInstantce().isHome())
					{
						if(m_wndSearchDesk == null)
						{
							m_wndSearchDesk = new WndSearchDesk(AppRes.getInstantce().getApplicationContext());
							m_wndSearchDesk.ShowWindowInCenter();							
						}
						
					}
					else
					{
						if(m_wndSearchDesk != null)
						{
							AppRes.getInstantce().removeView(m_wndSearchDesk);
							m_wndSearchDesk = null;						
						}
						
					}										
				}
			});
	
			
			
		}
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if(null == m_timer){
			m_timer = new Timer();
			m_timer.scheduleAtFixedRate(new UIRefreshTask(), 0, 500);
		}
		// 开启定时器，每隔0.5秒刷新一次		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service被终止的同时也停止定时器继续运行
	}
	


}

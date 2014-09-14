package com.huadev.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class AppRes extends Application {

	private static AppRes m_inst = null;
	private int m_statusBarHeight = 0;
	private WindowManager m_wndMgr = null;

	public static AppRes getInstantce() {
		return m_inst;
	}

	@Override
	public void onCreate() {
		Log.v("ApplicationActivity", "onCreate");
		super.onCreate();
		m_inst = this;
	}

	@Override
	public void onTerminate() {
		Log.v("ApplicationActivity", "onTerminate");
		super.onTerminate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.v("ApplicationActivity", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		Log.v("ApplicationActivity", "onLowMemory");
		super.onLowMemory();
	}

	public int getStatusBarHeight() {
		if (m_statusBarHeight != 0) {
			return m_statusBarHeight;
		}

		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object o = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = (Integer) field.get(o);
			m_statusBarHeight = new android.view.View(null).getResources()
					.getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return m_statusBarHeight;
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	public List<String> getHomePackNames() {
		List<String> lstPckNames = new ArrayList<String>();
		Context appContext = getApplicationContext();
		PackageManager packManager = appContext.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);

		List<ResolveInfo> lstResolveInfo = packManager.queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (int i = 0; i < lstResolveInfo.size(); i++) {
			ResolveInfo rsInfo = lstResolveInfo.get(i);
			lstPckNames.add(rsInfo.activityInfo.packageName);
		}

		return lstPckNames;
	}

	public boolean isHome() {
		ActivityManager activityMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> lstRunningTask = activityMgr.getRunningTasks(1);
		List<String> lstPckNames = getHomePackNames();
		return lstPckNames.contains(lstRunningTask.get(0).topActivity
				.getPackageName());
	}

	public WindowManager getWindowManager() {
		if (m_wndMgr == null) {
			m_wndMgr = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		}
		return m_wndMgr;
	}

	/**
	 * 将小悬浮窗从屏幕上移除。
	 */
	public void removeView(View view) {
		if (view != null) {
			WindowManager windowManager = getWindowManager();
			windowManager.removeView(view);
		}
	}
	
	public void startApp(PackageInfo info)
	{
		 // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent  
	    Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);  
	    resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
	    resolveIntent.setPackage(info.packageName);  
	  
	    // 通过getPackageManager()的queryIntentActivities方法遍历  
	    List<ResolveInfo> resolveinfoList = getPackageManager()  
	            .queryIntentActivities(resolveIntent, 0);  
	  
	    ResolveInfo resolveinfo = resolveinfoList.iterator().next();  
	    if (resolveinfo != null) {  
	        // packagename = 参数packname  
	        String packageName = resolveinfo.activityInfo.packageName;  
	        // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]  
	        String className = resolveinfo.activityInfo.name;  
	        // LAUNCHER Intent  
	      //  Intent intent = new Intent(Intent.ACTION_MAIN);  
	        //intent.addCategory(Intent.CATEGORY_LAUNCHER);  
	        Intent intent = new Intent();
	        // 设置ComponentName参数1:packagename参数2:MainActivity路径  
	        ComponentName cn = new ComponentName(packageName, className);  	  
	        intent.setComponent(cn);  
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);  
	    }  

  
	}

	public List<PackageInfo> GetInstalledPackages() {

		List<PackageInfo> packageInfos = getPackageManager()
				.getInstalledPackages(0);
		// for (int i = 0; i < packageInfos.size(); i++) {
		// PackageInfo pInfo=packageInfos.get(i);
		// AllAppInfo allAppInfo=new AllAppInfo();
		// allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(getPackageManager()).toString());//应用程序的名称
		// allAppInfo.setPackagename(pInfo.packageName);//应用程序的包
		// allAppInfo.setVersionCode(pInfo.versionCode);//版本号
		// allAppInfo.setLastInstal(pInfo.firstInstallTime);
		// //allAppInfo.setProvider(pInfo.providers);
		// allAppInfo.setInstalPath(pInfo.applicationInfo.sourceDir);
		// allAppInfo.setAppicon(pInfo.applicationInfo.loadIcon(getPackageManager()));
		// appList.add(allAppInfo);
		// }
		return packageInfos;
	}

}

package com.huadev.search.wnd;

import java.util.LinkedList;
import java.util.List;

import com.huadev.search.R;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huadev.utils.*;

public class WndSearchDesk extends LinearLayout {

	private static final String LinearLayout = null;
	private LinearLayout m_layoutBg;
	private android.view.WindowManager.LayoutParams m_layoutParam;
	private int m_iPosXInClient;
	private int m_iPosYInClinet;

	public WndSearchDesk(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.wndsearchdesk, this);
		m_layoutBg = (LinearLayout) findViewById(R.id.wndsearchdeskbg);
		findViewById(R.id.btnsearch).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView text = (TextView) findViewById(R.id.txtkeyword);
				String strSearchKeyword = text.getText().toString();
				if(strSearchKeyword != null
						&& strSearchKeyword!="" )
				{
					List<PackageInfo> lstInstalledPack = AppRes.getInstantce()
							.GetInstalledPackages();
					List<PackageInfo> lstResult = new LinkedList<PackageInfo>();
					for (int i = 0; i < lstInstalledPack.size(); i++) {
						PackageInfo info = lstInstalledPack.get(i);
						String strPackName = (String) info.applicationInfo
								.loadLabel(AppRes.getInstantce()
										.getPackageManager());
						if (strPackName.toLowerCase().indexOf(strSearchKeyword) >= 0) {
							lstResult.add(info);
						}

					}
					
					if(lstResult.size() == 1)
					{
						AppRes.getInstantce().startApp(lstResult.get(0));
					}

					
				}
			
			}

		});
		// LinearLayout searchDeskBg = (android.widget.LinearLayout)
		// findViewById(R.id.wndsearchdeskbg);
		// searchDeskBg.

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			m_iPosXInClient = (int) event.getX();
			m_iPosYInClinet = (int) event.getY();
		}
			break;
		case MotionEvent.ACTION_MOVE: {
			int iPosXInScreen = (int) event.getRawX();
			int iPosYInScreen = (int) event.getRawY()
					- AppRes.getInstantce().getStatusBarHeight();
			updatePos(iPosXInScreen, iPosYInScreen);
		}
			break;
		}
		return super.onTouchEvent(event);
	}

	private void updatePos(int iPosXScreen, int iPosYScreen) {
		m_layoutParam.x = (int) (iPosXScreen - m_iPosXInClient);
		m_layoutParam.y = (int) (iPosYScreen - m_iPosYInClinet);
		AppRes.getInstantce().getWindowManager()
				.updateViewLayout(this, m_layoutParam);

	}

	public void ShowWindowInCenter() {
		int iScreenWidth = AppRes.getInstantce().getWindowManager()
				.getDefaultDisplay().getWidth();
		int iScreenHeight = AppRes.getInstantce().getWindowManager()
				.getDefaultDisplay().getHeight();

		if (m_layoutParam == null) {
			m_layoutParam = new android.view.WindowManager.LayoutParams();
			m_layoutParam.type = android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			// m_layoutParam.format = PixelFormat.TRANSPARENT;
			m_layoutParam.x = iScreenWidth;
			m_layoutParam.y = 20;
			m_layoutParam.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;// |android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

			m_layoutParam.width = m_layoutBg.getLayoutParams().width;
			m_layoutParam.height = m_layoutBg.getLayoutParams().height;
			m_layoutParam.gravity = Gravity.LEFT | Gravity.TOP;
			AppRes.getInstantce().getWindowManager()
					.addView(this, m_layoutParam);

		}

	}

}

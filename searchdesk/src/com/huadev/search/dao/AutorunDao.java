package com.huadev.search.dao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutorunDao extends BroadcastReceiver {

	static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION_BOOT_COMPLETED)) {

			Intent searchIntent = new Intent(context, WndMgrServiceDao.class);

			context.startService(searchIntent);
		}

	}

}

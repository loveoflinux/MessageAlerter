package com.messagealerter;


import com.messagealerter.service.AlertThread;
import com.messagealerter.service.Alerter;
import com.messagealerter.service.IAlerter;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MessageAlerter extends Application implements ServiceConnection {

	private static MessageAlerter sInstance;
	
	private IAlerter mService;
	
	private volatile AlertThread mAlertThread;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		synchronized (MessageAlerter.class) {
			sInstance = this;	
		}
		
		bindService(new Intent(this, Alerter.class), this, BIND_AUTO_CREATE);

	}

	public static MessageAlerter getInstance() {
		synchronized (MessageAlerter.class) {
			return sInstance;
		}
	}

	public DBOpenHelper getDBOpenHelper() {
		return DBOpenHelper.getInstance(this);
	}

	public void setAlertThread(AlertThread alertThread) {
		synchronized (this) {
			mAlertThread = alertThread;
		}
	}
	
	public AlertThread getAlertThread() {
		synchronized (this) {
			return mAlertThread;
		}
	}
	
	public IAlerter getService() {
		return mService;
	}
	
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = (IAlerter) service;
	}

	public void onServiceDisconnected(ComponentName name) {
		mService = null;
	}

}

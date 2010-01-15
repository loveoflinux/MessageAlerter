package com.messagealerter.service;

import com.messagealerter.MessageAlerter;
import com.messagealerter.data.TwoDByteArray;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class Alerter extends Service {

	private SMSReceiverThread mSMSReceiverThread;
	private AlertThread mAlertThread;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mAlertThread = new AlertThread(this);
		((MessageAlerter)getApplication()).setAlertThread(mAlertThread);
		
		mSMSReceiverThread = new SMSReceiverThread(this, mAlertThread,
				((MessageAlerter)getApplication()).getDBOpenHelper().getReadableDatabase());
		
		mAlertThread.start();
		mSMSReceiverThread.start();
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		((MessageAlerter)getApplication()).setAlertThread(null);
		mSMSReceiverThread.interrupt();
		mAlertThread.interrupt();
	}
	
	private final IAlerter.Stub mBinder = new IAlerter.Stub() {
		
		public void feedPDUs(TwoDByteArray pdus) throws RemoteException {
			
			mSMSReceiverThread.feedPDUs(pdus.getData());
			
		}

		public void acknowledge(int alert) throws RemoteException {
			
			mAlertThread.acknowledgeAlert(alert);
			
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

}

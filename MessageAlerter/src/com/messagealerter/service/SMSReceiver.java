package com.messagealerter.service;

import com.messagealerter.data.TwoDByteArray;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

public class SMSReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		IAlerter alerter = (IAlerter) peekService(context, new Intent(context, Alerter.class));
		
		if (alerter != null) {
			
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			int count = pdus.length;
			byte[][] pduBytes = new byte[count][];
			
			for (int i = 0; i < count; i++) {
				pduBytes[i] = (byte[])pdus[i];
			}
			
			try {
				alerter.feedPDUs(new TwoDByteArray(pduBytes));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}

}

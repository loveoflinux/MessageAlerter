package com.messagealerter.data;

import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class Alert {
	
	private static final String KEY_SMS_PDU = "com.messagealerter.SMS_PDU";
	private static final String KEY_RINGTONE = "com.messagealerter.RINGTONE";
	private static final String KEY_VOLUME = "com.messagealerter.VOLUME";
	private static final String KEY_INTERVAL = "com.messagealerter.INTERVAL";
	private static final String KEY_ALERT_ID = "com.messagealerter.ALERT_ID";
	
	private static int sId = 0;
	
	private final SmsMessage mSmsMessage;
	private final Uri mRingtone;
	private final int mVolume;
	private final int mInterval;
	private final int mId;
	
	private Alert(byte[] pdu, String ringtone, int volume, int interval, int id) {
		mSmsMessage = SmsMessage.createFromPdu(pdu);
		mRingtone = Uri.parse(ringtone);
		mVolume = volume;
		mInterval = interval;
		mId = id;
	}

	public Alert(SmsMessage smsMessage, Uri ringtone, int volume, int interval) {
		mSmsMessage = smsMessage;
		mRingtone = ringtone;
		mVolume = volume;
		mInterval = interval;
		mId = sId;
		sId++;
	}

	public SmsMessage getSmsMessage() {
		return mSmsMessage;
	}

	public Uri getRingtone() {
		return mRingtone;
	}

	public int getVolume() {
		return mVolume;
	}

	public int getInterval() {
		return mInterval;
	}

	public int getId() {
		return mId;
	}

	public Bundle toBundle() {
		Bundle bundle = new Bundle();
		bundle.putByteArray(KEY_SMS_PDU, mSmsMessage.getPdu());
		bundle.putString(KEY_RINGTONE, mRingtone.toString());
		bundle.putInt(KEY_VOLUME, mVolume);
		bundle.putInt(KEY_INTERVAL, mInterval);
		bundle.putInt(KEY_ALERT_ID, mId);
		return bundle;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o instanceof Alert) return mId == ((Alert)o).mId;
		return false;
	}

	@Override
	public int hashCode() {
		return mId;
	}
	
	public static Alert fromBundle(Bundle bundle) {
		return new Alert(bundle.getByteArray(KEY_SMS_PDU),
			bundle.getString(KEY_RINGTONE),
			bundle.getInt(KEY_VOLUME),
			bundle.getInt(KEY_INTERVAL),
			bundle.getInt(KEY_ALERT_ID));
	}

}

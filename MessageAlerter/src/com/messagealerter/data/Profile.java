package com.messagealerter.data;

import com.messagealerter.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;

public class Profile {

	private static final int VOLUME_LEVEL_LOW = 0;
	private static final int VOLUME_LEVEL_MEDIUM = 1;
	private static final int VOLUME_LEVEL_HIGH = 2;
	
	private static final int VOLUME_TRESHOLD_LOW = 33;
	private static final int VOLUME_TRESHOLD_MEDIUM = 66;
	
	private String mName;
	private int mRingtone;
	private int mVolume;
	private int mInterval;
	private String mIcon;
	
	private final RingtoneManager mRingtoneManager;
	
	public Profile(RingtoneManager ringtoneManager) {
		mRingtoneManager = ringtoneManager;
	}

	public Profile(RingtoneManager ringtoneManager, Cursor cursor) {
		this(ringtoneManager);
		load(cursor);
	}

	public void load(Cursor cursor) {
		mName = cursor.getString(cursor.getColumnIndex(DB.PROFILES.NAME));
		mRingtone = cursor.getInt(cursor.getColumnIndex(DB.PROFILES.RINGTONE));
		mVolume = cursor.getInt(cursor.getColumnIndex(DB.PROFILES.VOLUME));
		mInterval = cursor.getInt(cursor.getColumnIndex(DB.PROFILES.INTERVAL));
		mIcon = cursor.getString(cursor.getColumnIndex(DB.PROFILES.ICON));
	}
	
	public void load(SQLiteDatabase db, long id) {
		Cursor cursor = db.query(DB.PROFILES.TABLE, null, DB.PROFILES.ID + '=' + id, null, null, null, null);
		if (cursor.moveToFirst()) load(cursor);
		cursor.close();
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public int getRingtonePosition() {
		return mRingtone;
	}
	
	public String getRingtoneName() {
		Cursor cursor = mRingtoneManager.getCursor();
		if (cursor.moveToPosition(mRingtone)) {
			return cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
		}
		return null;
	}
	
	public Ringtone getRingtone() {
		
		return mRingtoneManager.getRingtone(mRingtone);
	}
	
	public void setRingtone(int ringtone) {
		mRingtone = ringtone;
	}
	
	public int getVolume() {
		return mVolume;
	}
	
	public int getVolumeLevel() {
		
		if (mVolume <= VOLUME_TRESHOLD_LOW) return VOLUME_LEVEL_LOW;
		if (mVolume > VOLUME_TRESHOLD_LOW && mVolume <= VOLUME_TRESHOLD_MEDIUM) return VOLUME_LEVEL_MEDIUM;
		return VOLUME_LEVEL_HIGH;
		
	}
	
	public void setVolume(int volume) {
		mVolume = volume;
	}
	
	public int getInterval() {
		return mInterval;
	}
	
	public void setInterval(int interval) {
		mInterval = interval;
	}
	
	public String getIconString() {
		return mIcon;
	}
	
	public int getIcon() {
		if (mIcon != null)
			try {
				return android.R.drawable.class.getField(mIcon).getInt(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		return -1;
	}
	
	public void setIcon(String icon) {
		mIcon = icon;
	}

	public float getSeverity() {
		return mVolume / mInterval;
	}
	
	public long save(SQLiteDatabase db, long id) {
		ContentValues values = new ContentValues();
		
		values.put(DB.PROFILES.NAME, mName);
		values.put(DB.PROFILES.RINGTONE, mRingtone);
		values.put(DB.PROFILES.VOLUME, mVolume);
		values.put(DB.PROFILES.INTERVAL, mInterval);
		values.put(DB.PROFILES.ICON, mIcon);		
		
		if (db.update(DB.PROFILES.TABLE, values, DB.PROFILES.ID + '=' + id, null) > 0)
			return id;
		
		return db.insert(DB.PROFILES.TABLE, DB.PROFILES.NAME, values);
	}
	
	public static long findId(SQLiteDatabase db, Profile profile) {
		
		StringBuilder where = new StringBuilder();
		where.append(DB.PROFILES.NAME);
		where.append(" = '");
		where.append(profile.mName);
		where.append("' AND ");
		where.append(DB.PROFILES.RINGTONE);
		where.append(" = '");
		where.append(profile.mRingtone);
		where.append("' AND ");
		where.append(DB.PROFILES.VOLUME);
		where.append(" = '");
		where.append(profile.mVolume);
		where.append("' AND ");
		where.append(DB.PROFILES.INTERVAL);
		where.append(" = '");
		where.append(profile.mInterval);
		where.append("' AND ");
		where.append(DB.PROFILES.ICON);
		where.append(" = '");
		where.append(profile.mIcon);
		where.append('\'');

		Cursor cursor = db.query(DB.PROFILES.TABLE, new String[] {DB.PROFILES.ID},
				where.toString(), null, null, null, null);
		
		if (cursor.moveToFirst()) return cursor.getLong(0);
		
		return -1;
		
	}
}

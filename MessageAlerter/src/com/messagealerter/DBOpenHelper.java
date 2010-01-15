package com.messagealerter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String CREATE_TABLE = "CREATE TABLE ";
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
	private static final String START_TABLE = " (", SEPARATE_COLUMN = ", ", END_TABLE = ");";
	private static final String TYPE_TEXT = " TEXT";
	private static final String TYPE_INTEGER = " INTEGER";
	private static final String TYPE_ID = " INTEGER PRIMARY KEY AUTOINCREMENT";
	
	private static DBOpenHelper sInstance;
	
	private DBOpenHelper(Context context) {
		super(context, DB.NAME, null, DB.VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		StringBuilder query = new StringBuilder(CREATE_TABLE);
		
		query.append(DB.PROFILES.TABLE);
		query.append(START_TABLE);
		query.append(DB.PROFILES.ID);
		query.append(TYPE_ID);
		query.append(SEPARATE_COLUMN);
		query.append(DB.PROFILES.NAME);
		query.append(TYPE_TEXT);
		query.append(SEPARATE_COLUMN);
		query.append(DB.PROFILES.RINGTONE);
		query.append(TYPE_TEXT);
		query.append(SEPARATE_COLUMN);
		query.append(DB.PROFILES.VOLUME);
		query.append(TYPE_INTEGER);
		query.append(SEPARATE_COLUMN);
		query.append(DB.PROFILES.INTERVAL);
		query.append(TYPE_INTEGER);
		query.append(SEPARATE_COLUMN);
		query.append(DB.PROFILES.ICON);
		query.append(TYPE_TEXT);
		query.append(END_TABLE);
		
		db.execSQL(query.toString());
		query.setLength(0);
		
		query.append(CREATE_TABLE);
		query.append(DB.RULES.TABLE);
		query.append(START_TABLE);
		query.append(DB.RULES.ID);
		query.append(TYPE_ID);
		query.append(SEPARATE_COLUMN);
		query.append(DB.RULES.NAME);
		query.append(TYPE_TEXT);
		query.append(SEPARATE_COLUMN);
		query.append(DB.RULES.KEYWORDS);
		query.append(TYPE_TEXT);
		query.append(SEPARATE_COLUMN);
		query.append(DB.RULES.PROFILE);
		query.append(TYPE_INTEGER);
		query.append(SEPARATE_COLUMN);
		query.append(DB.RULES.ENABLED);
		query.append(TYPE_INTEGER);
		query.append(END_TABLE);
		
		db.execSQL(query.toString());
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		StringBuilder query = new StringBuilder();
		
		if (oldVersion < 3 && newVersion == 3) {
			
			query.append(DROP_TABLE);
			query.append(DB.PROFILES.TABLE);
			query.append(';');
			
			db.execSQL(query.toString());
			query.setLength(0);
			
			query.append(CREATE_TABLE);
			query.append(DB.PROFILES.TABLE);
			query.append(START_TABLE);
			query.append(DB.PROFILES.ID);
			query.append(TYPE_ID);
			query.append(SEPARATE_COLUMN);
			query.append(DB.PROFILES.NAME);
			query.append(TYPE_TEXT);
			query.append(SEPARATE_COLUMN);
			query.append(DB.PROFILES.RINGTONE);
			query.append(TYPE_INTEGER);
			query.append(SEPARATE_COLUMN);
			query.append(DB.PROFILES.VOLUME);
			query.append(TYPE_INTEGER);
			query.append(SEPARATE_COLUMN);
			query.append(DB.PROFILES.INTERVAL);
			query.append(TYPE_INTEGER);
			query.append(SEPARATE_COLUMN);
			query.append(DB.PROFILES.ICON);
			query.append(TYPE_TEXT);
			query.append(END_TABLE);
			
			db.execSQL(query.toString());
	
		}
		
	}

	/* package */ static synchronized DBOpenHelper getInstance(Context context) {
		if (sInstance == null)
			sInstance = new DBOpenHelper(context);
		return sInstance;
	}
	
}

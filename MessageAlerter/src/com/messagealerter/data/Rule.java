package com.messagealerter.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.messagealerter.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;

public class Rule implements Comparable<Rule>, Cloneable {
	
	private String mName;
	private final List<Keyword> mKeywords;
	private long mProfile;
	private boolean mEnabled;
	
	private final StringBuilder mKeywordsList;
	private Profile mConvertProfile;
	
	public Rule() {
		mKeywords = new ArrayList<Keyword>();
		mKeywordsList = new StringBuilder();
	}
	
	public Rule(Cursor cursor) {
		this();
		load(cursor);
	}

	public Rule(String name, List<Keyword> keywords, long profile, boolean enabled) {
		this();
		mName = name;
		setKeywords(keywords);
		mProfile = profile;
		mEnabled = enabled;
	}

	public void load(Cursor cursor) {
		mName = cursor.getString(cursor.getColumnIndex(DB.RULES.NAME));
		try {
			setKeywords(parseKeywords(cursor.getString(cursor.getColumnIndex(DB.RULES.KEYWORDS))));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mProfile = cursor.getLong(cursor.getColumnIndex(DB.RULES.PROFILE));
		mEnabled = cursor.getInt(cursor.getColumnIndex(DB.RULES.ENABLED)) != 0;
	}
	
	public void load(SQLiteDatabase db, long id) {
		Cursor cursor = db.query(DB.RULES.TABLE, null, DB.RULES.ID + '=' + id, null, null, null, null);
		if (cursor.moveToFirst()) load(cursor);
		cursor.close();
	}
	
	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public List<Keyword> getKeywords() {
		return Collections.unmodifiableList(mKeywords);
	}

	public void setKeywords(List<Keyword> keywords) {
		mKeywords.clear();
		mKeywords.addAll(keywords);
		mKeywordsList.setLength(0);
		int count = mKeywords.size();
		for (int i = 0; i < count; i++) {
			mKeywordsList.append(mKeywords.get(i));
			if (i < count - 1) mKeywordsList.append(", ");
		}
	}

	public Profile getProfile(RingtoneManager ringtoneManager, SQLiteDatabase db) {
		
		if (mConvertProfile == null)
			mConvertProfile = new Profile(ringtoneManager);
		
		mConvertProfile.load(db, mProfile);
		
		return mConvertProfile;
	}
	
	public long getProfileId() {
		return mProfile;
	}

	public void setProfile(long profile) {
		mProfile = profile;
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}
	
	public String getKeywordsList() {
		return mKeywordsList.toString();
	}
	
	public long save(SQLiteDatabase db, long id) {
		ContentValues values = new ContentValues();
		
		values.put(DB.RULES.NAME, mName);
		try {
			values.put(DB.RULES.KEYWORDS, getKeywordsJSON(mKeywords));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		values.put(DB.RULES.PROFILE, mProfile);
		values.put(DB.RULES.ENABLED, mEnabled ? 1 : 0);
		
		if (db.update(DB.RULES.TABLE, values, DB.RULES.ID + '=' + id, null) > 0)
			return id;
		
		return db.insert(DB.RULES.TABLE, DB.RULES.KEYWORDS, values);
	}
	
	public static List<Keyword> parseKeywords(String json) throws JSONException {
		JSONArray jarray = new JSONArray(json);
		int count = jarray.length();
		ArrayList<Keyword> keywords = new ArrayList<Keyword>(count);
		
		for (int i = 0; i < count; i++)
			keywords.add(new Keyword(jarray.getJSONObject(i)));
			
		return keywords;
	}
	
	private static String getKeywordsJSON(List<Keyword> keywords) throws JSONException {
		
		JSONArray json = new JSONArray();
		
		for (Keyword keyword : keywords)
			json.put(keyword.toJSON());
			
		return json.toString();
	}

	public int compareTo(Rule another) {
		if (mConvertProfile != null && another.mConvertProfile != null) {
			float severity = mConvertProfile.getSeverity();
			float severityAnother = another.mConvertProfile.getSeverity();
			
			if (severity < severityAnother) return -1;
			if (severity == severityAnother) return 0;
			return 1;
			
		}
		return 0;
	}

	@Override
	public Rule clone() {
		Rule rule = new Rule();
		rule.mName = mName;
		rule.setKeywords(mKeywords);
		rule.mProfile = mProfile;
		rule.mEnabled = mEnabled;
		return rule;
	}
}

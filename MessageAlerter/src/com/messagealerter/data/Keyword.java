package com.messagealerter.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Keyword {
	
	private static final String KEYWORD = "k";
	private static final String CASE_SENSITIVE = "c";

	private String mKeyword;
	private boolean mCaseSensitive;
	
	public Keyword() {
		mKeyword = "";
		mCaseSensitive = false;
	}
	
	public Keyword(String keyword, boolean caseSensitive) {
		mKeyword = keyword;
		mCaseSensitive = caseSensitive;
	}
	
	/* package */ Keyword(JSONObject json) throws JSONException {
		mKeyword = json.getString(KEYWORD);
		mCaseSensitive = json.getBoolean(CASE_SENSITIVE);
	}

	public String getKeyword() {
		return mKeyword;
	}

	public void setKeyword(String keyword) {
		mKeyword = keyword;
	}

	public boolean isCaseSensitive() {
		return mCaseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		mCaseSensitive = caseSensitive;
	}
	
	/* package */ JSONObject toJSON() throws JSONException {
		
		JSONObject json = new JSONObject();
		
		json.put(KEYWORD, mKeyword);
		json.put(CASE_SENSITIVE, mCaseSensitive);
		
		return json;
		
	}

	@Override
	public String toString() {
		return mKeyword;
	}
	
}

package com.messagealerter;

public interface DB {

	public static final String NAME = "messagealerter.db";
	public static final int VERSION = 3;
	
	public static interface RULES {
		
		public static final String TABLE = "rules";
		public static final String ID = "_id";
		public static final String NAME = "name";
		public static final String KEYWORDS = "keywords";
		public static final String PROFILE = "profile";
		public static final String ENABLED = "enabled";
		
	}
	
	public static interface PROFILES {
		
		public static final String TABLE = "profiles";
		public static final String ID = "_id";
		public static final String NAME = "name";
		public static final String RINGTONE = "ringtone";
		public static final String VOLUME = "volume";
		public static final String INTERVAL = "interval";
		public static final String ICON = "icon";
		
	}
	
}

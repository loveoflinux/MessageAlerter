package com.messagealerter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;

import com.messagealerter.DB;
import com.messagealerter.data.Alert;
import com.messagealerter.data.Keyword;
import com.messagealerter.data.Profile;
import com.messagealerter.data.Rule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.telephony.SmsMessage;

public class SMSReceiverThread extends Thread {

	private final SQLiteDatabase mDb;
	private final AlertThread mAlertThread;
	private final RingtoneManager mRingtoneManager;
	private final Cursor mRulesKeywords;
	private final Map<Long, List<Keyword>> mKeywords = new HashMap<Long, List<Keyword>>();
	private final LinkedBlockingQueue<byte[][]> mPDUs = new LinkedBlockingQueue<byte[][]>();
	
	public SMSReceiverThread(Context context, AlertThread alertThread, SQLiteDatabase db) {
		super();
		mAlertThread = alertThread;
		mDb = db;
		mRingtoneManager = new RingtoneManager(context);
		mRulesKeywords = db.query(DB.RULES.TABLE, new String[]
			{DB.RULES.ID, DB.RULES.KEYWORDS}, null, null, null, null, null);
	}

	private void populateKeywords() {
		
		mRulesKeywords.requery();
		
		mKeywords.clear();
		
		if (mRulesKeywords.moveToFirst()) {
			
			try {
			
				do {
					long id = mRulesKeywords.getLong(0);
					String keywords = mRulesKeywords.getString(1);
					mKeywords.put(id, Rule.parseKeywords(keywords));
				} while (mRulesKeywords.moveToNext());
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private List<Rule> resolveRules(String body) {
		
		List<Rule> rules = new ArrayList<Rule>();
		
		if (body == null) return rules;
		
		String lowercaseBody = body.toLowerCase();
		
		Rule rule = new Rule();		
		loopRules: for (long id : mKeywords.keySet()) {
			
			rule.load(mDb, id);
			for (Keyword keyword : rule.getKeywords()) {
				if (keyword.isCaseSensitive()) {
					String kw = keyword.getKeyword();
					if (body.contains(kw)) {
						rules.add(rule.clone());
						continue loopRules;
					}
				} else {
					String kw = keyword.getKeyword().toLowerCase();
					if (lowercaseBody.contains(kw)) {
						rules.add(rule.clone());
						continue loopRules;
					}
				}
			}
			
		}
		
		// make sure the profiles are constructed to be able to sort by severity
		for (Rule r : rules)
			r.getProfile(mRingtoneManager, mDb);
		
		Collections.sort(rules);
		
		return rules;
		
	}
	
	@Override
	public void run() {

		try {
			while (true) {
				
				byte[][] pdus = mPDUs.take();
				populateKeywords();
				
				for (byte[] pdu : pdus) {
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					List<Rule> rules = resolveRules(sms.getDisplayMessageBody());
					
					if (!rules.isEmpty()) {
						
						Profile profile = rules.get(0).getProfile(mRingtoneManager, mDb);
						
						Alert alert = new Alert(sms,
							mRingtoneManager.getRingtoneUri(profile.getRingtonePosition()),
							profile.getVolume(), profile.getInterval());
						
						mAlertThread.feedAlert(alert);
					}
					
				}
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			mRulesKeywords.close();
		}
		
	}
	
	public void feedPDUs(byte[][] pdus) {
		mPDUs.offer(pdus);
	}
	
}

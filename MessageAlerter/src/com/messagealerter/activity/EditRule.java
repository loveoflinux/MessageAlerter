package com.messagealerter.activity;

import com.messagealerter.DB;
import com.messagealerter.MessageAlerter;
import com.messagealerter.R;
import com.messagealerter.adapter.KeywordAdapter;
import com.messagealerter.adapter.ProfileAdapter;
import com.messagealerter.data.Keyword;
import com.messagealerter.data.Rule;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class EditRule extends Activity implements View.OnClickListener {
	
	public static final String EXTRA_RULE_ID = "com.messagealerter.RULE_ID";
	
	private EditText mEditName;
	private ListView mListKeywords;
	private Spinner mSpinnerProfile;
	private CheckBox mCheckEnabled;
	private Button mButtonSave;
	private Button mButtonRevert;
	private Button mButtonCancel;
	
	private View mKeywordsFooter;
	
	private KeywordAdapter mKeywordAdapter;
	private ProfileAdapter mProfileAdapter;
	
	private long mRuleId;
	private Rule mRule;
	
	private SQLiteDatabase mDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDb = ((MessageAlerter)getApplication()).getDBOpenHelper().getWritableDatabase();
		
		mRule = new Rule();
		mRuleId = getIntent().getLongExtra(EXTRA_RULE_ID, -1);
		mRule.load(mDb, mRuleId);
		
		setContentView(R.layout.activity_edit_rule);
		
		mEditName = (EditText) findViewById(R.id.activity_edit_rule_name);
		mListKeywords = (ListView) findViewById(R.id.activity_edit_rule_keywords);
		mSpinnerProfile = (Spinner) findViewById(R.id.activity_edit_rule_profile);
		mCheckEnabled = (CheckBox) findViewById(R.id.activity_edit_rule_enabled);
		mButtonSave = (Button) findViewById(R.id.activity_edit_rule_save);
		mButtonRevert = (Button) findViewById(R.id.activity_edit_rule_revert);
		mButtonCancel = (Button) findViewById(R.id.activity_edit_rule_cancel);

		mKeywordsFooter = View.inflate(this, R.layout.keyword_footer, null);
		mListKeywords.addFooterView(mKeywordsFooter);
		
		mKeywordAdapter = new KeywordAdapter(this);
		
		mKeywordsFooter.findViewById(R.id.keyword_footer_add).setOnClickListener(this);
		mButtonSave.setOnClickListener(this);
		mButtonRevert.setOnClickListener(this);
		mButtonCancel.setOnClickListener(this);
		
		mListKeywords.setAdapter(mKeywordAdapter);
		
		Cursor profilesCursor = (mDb.query(DB.PROFILES.TABLE, null, null, null, null, null, null));
        startManagingCursor(profilesCursor);
		mProfileAdapter = new ProfileAdapter(this, profilesCursor);
		
		mSpinnerProfile.setAdapter(mProfileAdapter);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateUi();
	}
	
	private void updateUi() {
		mEditName.setText(mRule.getName());
		mKeywordAdapter.setKeywords(mRule.getKeywords());
		mCheckEnabled.setChecked(mRule.isEnabled());
		mSpinnerProfile.setSelection(mProfileAdapter.findPosition(mRule.getProfileId()));
	}
	
	private void updateRule() {
		mRule.setName(mEditName.getText().toString());
		mRule.setKeywords(mKeywordAdapter.getKeywords());
		mRule.setEnabled(mCheckEnabled.isChecked());
		mRule.setProfile(mSpinnerProfile.getSelectedItemId());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.keyword_footer_add:
			mKeywordAdapter.append(new Keyword());
			break;
			
		case R.id.activity_edit_rule_save:
			updateRule();
			mRuleId = mRule.save(mDb, mRuleId);
			Toast.makeText(this, R.string.toast_rule_saved, Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.activity_edit_rule_revert:
			mRule.load(mDb, mRuleId);
			updateUi();
			break;
			
		case R.id.activity_edit_rule_cancel:
			finish();
			break;
		
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.options_menu, menu);
		
		menu.findItem(R.id.options_menu_rules).setVisible(false);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.options_menu_profiles:
			startActivity(new Intent(this, Profiles.class));
			break;
			
		case R.id.options_menu_settings:
			startActivity(new Intent(this, Settings.class));			
			break;
			
		case R.id.options_menu_help:
			startActivity(new Intent(this, Help.class));
			break;
		
		}
		
		return true;
	}

}

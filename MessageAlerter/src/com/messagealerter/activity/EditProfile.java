package com.messagealerter.activity;

import com.messagealerter.MessageAlerter;
import com.messagealerter.R;
import com.messagealerter.adapter.IconsAdapter;
import com.messagealerter.data.Profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class EditProfile extends Activity implements View.OnClickListener,
	AdapterView.OnItemClickListener {
	
	public static final String EXTRA_PROFILE_ID = "com.messagealerter.PROFILE_ID";
	
	private static final int REQUEST_RINGTONE = 100;
	
	private static final int DIALOG_ICONS = 101;
	
	private EditText mEditName;
	private ImageButton mButtonIcon;
	private Button mButtonRingtone;
	private SeekBar mSeekVolume;
	private EditText mEditInterval;
	private Button mButtonSave;
	private Button mButtonRevert;
	private Button mButtonCancel;
	
	private Profile mProfile;
	private long mProfileId;
	
	private SQLiteDatabase mDb;
	
	private RingtoneManager mRingtoneManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDb = ((MessageAlerter)getApplication()).getDBOpenHelper().getWritableDatabase();
		
		mRingtoneManager = new RingtoneManager(this);
				
		mProfile = new Profile(mRingtoneManager);
		mProfileId = getIntent().getLongExtra(EXTRA_PROFILE_ID, -1);
		mProfile.load(mDb, mProfileId);
		
		setContentView(R.layout.activity_edit_profile);
		
		mEditName = (EditText) findViewById(R.id.activity_edit_profile_name);
		mButtonIcon = (ImageButton) findViewById(R.id.activity_edit_profile_icon);
		mButtonRingtone = (Button) findViewById(R.id.activity_edit_profile_ringtone);
		mSeekVolume = (SeekBar) findViewById(R.id.activity_edit_profile_volume);
		mEditInterval = (EditText) findViewById(R.id.activity_edit_profile_interval);
		mButtonSave = (Button) findViewById(R.id.activity_edit_profile_save);
		mButtonRevert = (Button) findViewById(R.id.activity_edit_profile_revert);
		mButtonCancel = (Button) findViewById(R.id.activity_edit_profile_cancel);
		
		mButtonIcon.setOnClickListener(this);
		mButtonRingtone.setOnClickListener(this);
		mButtonSave.setOnClickListener(this);
		mButtonRevert.setOnClickListener(this);
		mButtonCancel.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateUi();
	}

	private void updateUi() {
		mEditName.setText(mProfile.getName());
		mButtonIcon.setImageResource(mProfile.getIcon());
		mButtonRingtone.setText(mProfile.getRingtoneName());
		mSeekVolume.setProgress(mProfile.getVolume());
		mEditInterval.setText(String.valueOf(mProfile.getInterval()));
	}
	
	private void updateProfile() {
		mProfile.setName(mEditName.getText().toString());
		mProfile.setVolume(mSeekVolume.getProgress());
		mProfile.setInterval(Integer.parseInt(mEditInterval.getText().toString()));
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.activity_edit_profile_icon:
			showDialog(DIALOG_ICONS);
			break;
			
		case R.id.activity_edit_profile_ringtone:
			startActivityForResult(new Intent(RingtoneManager.ACTION_RINGTONE_PICKER), REQUEST_RINGTONE);
			break;
			
		case R.id.activity_edit_profile_save:
			updateProfile();
			mProfileId = mProfile.save(mDb, mProfileId);
			Toast.makeText(this, R.string.toast_profile_saved, Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.activity_edit_profile_revert:
			mProfile.load(mDb, mProfileId);
			updateUi();
			break;
			
		case R.id.activity_edit_profile_cancel:
			finish();			
			break;
			
		case R.id.dialog_icons_cancel:
			dismissDialog(DIALOG_ICONS);
			break;
		
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case REQUEST_RINGTONE:
			
			if (resultCode == RESULT_OK) {

				Uri ringtone = (Uri) data.getExtras().get((RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
				mProfile.setRingtone(mRingtoneManager.getRingtonePosition(ringtone));
				mButtonRingtone.setText(mProfile.getRingtoneName());
				
			}
			
			break;

		}
		
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		
		switch (id) {
		case DIALOG_ICONS:
			
			dialog = new Dialog(this);
			dialog.setTitle(R.string.dialog_icons_title);
			dialog.setContentView(R.layout.dialog_icons);
			
			GridView grid = (GridView) dialog.findViewById(R.id.dialog_icons_grid);
			grid.setAdapter(new IconsAdapter(this));
			
			grid.setOnItemClickListener(this);
			dialog.findViewById(R.id.dialog_icons_cancel).setOnClickListener(this);
			
			break;

		}
		
		return dialog;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		mButtonIcon.setImageResource((int)id);
		mProfile.setIcon(((IconsAdapter)parent.getAdapter()).getItem(position));
		dismissDialog(DIALOG_ICONS);
		
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.options_menu, menu);
		
		menu.findItem(R.id.options_menu_profiles).setVisible(false);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.options_menu_rules:
			startActivity(new Intent(this, Rules.class));
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

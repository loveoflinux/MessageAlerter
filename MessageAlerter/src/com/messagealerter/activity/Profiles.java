package com.messagealerter.activity;

import com.messagealerter.DB;
import com.messagealerter.MessageAlerter;
import com.messagealerter.R;
import com.messagealerter.adapter.ProfileAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class Profiles extends ListActivity implements View.OnClickListener {
	
	private ProfileAdapter mProfileAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Cursor profilesCursor = ((MessageAlerter)getApplication()).getDBOpenHelper().getReadableDatabase().query(DB.PROFILES.TABLE, null, null, null, null, null, null);
        startManagingCursor(profilesCursor);
        mProfileAdapter = new ProfileAdapter(this, profilesCursor, true);
		
		View footer = View.inflate(this, R.layout.profile_footer, null);
		
		getListView().addFooterView(footer);
		
		setListAdapter(mProfileAdapter);
		
		footer.findViewById(R.id.profile_footer_add).setOnClickListener(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent editProfile = new Intent(this, EditProfile.class);
		editProfile.putExtra(EditProfile.EXTRA_PROFILE_ID, id);
		startActivity(editProfile);
	}
	
	public void onClick(View v) {
		startActivity(new Intent(this, EditProfile.class));	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.options_menu, menu);
		
		menu.findItem(R.id.options_menu_profiles).setVisible(false);
		menu.findItem(R.id.options_menu_delete).setVisible(false);
		
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

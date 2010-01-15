package com.messagealerter.activity;

import com.messagealerter.R;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Settings extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_settings);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.options_menu, menu);
		
		menu.findItem(R.id.options_menu_settings).setVisible(false);
		menu.findItem(R.id.options_menu_delete).setVisible(false);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.options_menu_profiles:
			startActivity(new Intent(this, Profiles.class));
			break;
			
		case R.id.options_menu_rules:
			startActivity(new Intent(this, Rules.class));			
			break;
			
		case R.id.options_menu_help:
			startActivity(new Intent(this, Help.class));
			break;
		
		}
		
		return true;
	}
	
}

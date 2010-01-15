package com.messagealerter.activity;

import com.messagealerter.DB;
import com.messagealerter.MessageAlerter;
import com.messagealerter.R;
import com.messagealerter.adapter.RuleAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class Rules extends ListActivity implements View.OnClickListener {
	
	private RuleAdapter mRuleAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Cursor rulesCursor = ((MessageAlerter)getApplication()).getDBOpenHelper().getReadableDatabase().query(DB.RULES.TABLE, null, null, null, null, null, null);
        startManagingCursor(rulesCursor);
        mRuleAdapter = new RuleAdapter(this, rulesCursor, true);
        
        View footer = View.inflate(this, R.layout.rule_footer, null);
        
        getListView().addFooterView(footer);
        
        setListAdapter(mRuleAdapter);

        footer.findViewById(R.id.rule_footer_add).setOnClickListener(this);
        
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent editRule = new Intent(this, EditRule.class);
		editRule.putExtra(EditRule.EXTRA_RULE_ID, id);
		startActivity(editRule);
	}

	public void onClick(View v) {
		startActivity(new Intent(this, EditRule.class));		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.options_menu, menu);
		
		menu.findItem(R.id.options_menu_rules).setVisible(false);
		menu.findItem(R.id.options_menu_delete).setVisible(false);
		
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
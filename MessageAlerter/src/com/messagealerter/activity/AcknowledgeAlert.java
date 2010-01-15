package com.messagealerter.activity;

import com.messagealerter.MessageAlerter;
import com.messagealerter.R;
import com.messagealerter.adapter.AlertAdapter;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AcknowledgeAlert extends ListActivity {
	
	private AlertAdapter mAdapter;
	private MessageAlerter mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAdapter = new AlertAdapter(this);
		mApplication = (MessageAlerter) getApplication();
		
		try {
			mApplication.getAlertThread().registerWatcher(mAdapter);
		} catch (NullPointerException e) {}
		
		setContentView(R.layout.activity_acknowledge_alert);
		
		setListAdapter(mAdapter);
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		
		new AlertDialog.Builder(this).setTitle(R.string.alert_acknowledge_title)
			.setMessage(R.string.alert_acknowledge_message)
			.setPositiveButton(R.string.alert_acknowledge_positive,
				new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						try {
							mApplication.getAlertThread().acknowledgeAlert(mAdapter.getItem(position).getId());
						} catch (NullPointerException e) {}
						
					}
				}).setNegativeButton(R.string.alert_acknowledge_negative, null).show();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			mApplication.getAlertThread().unregisterWatcher(mAdapter);
		} catch (NullPointerException e) {}
	}

}

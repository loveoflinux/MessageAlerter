package com.messagealerter.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.messagealerter.R;
import com.messagealerter.data.Alert;
import com.messagealerter.service.AlertsWatcher;

import android.app.Activity;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlertAdapter extends BaseAdapter implements AlertsWatcher {

	private class Holder {
		TextView textSender;
		TextView textReceived;
		TextView textMessage;
	}
	
	private final Activity mActivity;
	private final List<Alert> mItems = new ArrayList<Alert>();
	
	private final Runnable mRefresh = new Runnable() {
		
		public void run() {
			notifyDataSetChanged();
		}
	};
	
	public AlertAdapter(Activity activity) {
		super();
		mActivity = activity;
	}

	public void onAlertsChanged(List<Alert> alerts) {
		
		synchronized (mItems) {
			mItems.clear();
			mItems.addAll(alerts);
		}
		
		mActivity.runOnUiThread(mRefresh);
	}

	public int getCount() {
		synchronized (mItems) {
			return mItems.size();			
		}
	}

	public Alert getItem(int position) {
		synchronized (mItems) {
			return mItems.get(position);
		}
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			
			convertView = View.inflate(mActivity, R.layout.alert_item, null);
			
			Holder holder = new Holder();
			
			holder.textSender = (TextView) convertView.findViewById(R.id.alert_item_sender);
			holder.textReceived = (TextView) convertView.findViewById(R.id.alert_item_received);
			holder.textMessage = (TextView) convertView.findViewById(R.id.alert_item_message);
			
			convertView.setTag(holder);
			
		}
		
		Holder holder = (Holder) convertView.getTag();
		
		SmsMessage item = getItem(position).getSmsMessage();
		
		holder.textSender.setText(item.getOriginatingAddress());
		holder.textReceived.setText(new Date(item.getTimestampMillis()).toString());
		holder.textMessage.setText(item.getMessageBody());
		
		return convertView;
	}

}

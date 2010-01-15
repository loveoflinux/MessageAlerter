package com.messagealerter.adapter;

import com.messagealerter.MessageAlerter;
import com.messagealerter.R;
import com.messagealerter.data.Rule;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RuleAdapter extends CursorAdapter {

	private static final class ViewHolder {
		ImageView icon;
		TextView name;
		TextView keywords;
	}
	
	private final Rule mConvertRule;
	private final RingtoneManager mRingtoneManager;
	private final SQLiteDatabase mDb;
	
	public RuleAdapter(Activity activity, Cursor c) {
		super(activity, c);
		mConvertRule = new Rule();
		mRingtoneManager = new RingtoneManager(activity);
		mDb = ((MessageAlerter)activity.getApplication()).getDBOpenHelper().getReadableDatabase();
	}

	public RuleAdapter(Activity activity, Cursor c, boolean autoRequery) {
		super(activity, c, autoRequery);
		mConvertRule = new Rule();
		mRingtoneManager = new RingtoneManager(activity);
		mDb = ((MessageAlerter)activity.getApplication()).getDBOpenHelper().getReadableDatabase();
	}

	@Override
	public Rule getItem(int position) {
		return new Rule((Cursor)super.getItem(position));
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		
		mConvertRule.load(cursor);
		
		holder.icon.setImageResource(mConvertRule.getProfile(mRingtoneManager, mDb).getIcon());
		holder.name.setText(mConvertRule.getName());
		holder.keywords.setText(mConvertRule.getKeywordsList());
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = View.inflate(context, R.layout.rule_item, null);
		
		ViewHolder holder = new ViewHolder();
		
		holder.icon = (ImageView) view.findViewById(R.id.rule_item_icon);
		holder.name = (TextView) view.findViewById(R.id.rule_item_name);
		holder.keywords = (TextView) view.findViewById(R.id.rule_item_keywords);
		
		view.setTag(holder);
		
		return view;
	}

}

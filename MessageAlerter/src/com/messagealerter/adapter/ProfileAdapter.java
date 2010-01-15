package com.messagealerter.adapter;

import com.messagealerter.DB;
import com.messagealerter.R;
import com.messagealerter.data.Profile;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.LevelListDrawable;
import android.media.RingtoneManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileAdapter extends CursorAdapter {

	private static final class ViewHolder {
		ImageView icon;
		TextView name;
		TextView ringtone;
		LevelListDrawable volume;
	}
	
	private final Profile mConvertProfile;
	private final RingtoneManager mRingtoneManager;
	
	public ProfileAdapter(Activity activity, Cursor c) {
		super(activity, c);
		mRingtoneManager = new RingtoneManager(activity);
		mConvertProfile = new Profile(mRingtoneManager);
		
	}
	
	public ProfileAdapter(Activity activity, Cursor c, boolean autoRequery) {
		super(activity, c, autoRequery);
		mRingtoneManager = new RingtoneManager(activity);
		mConvertProfile = new Profile(mRingtoneManager);
	}

	@Override
	public Profile getItem(int position) {
		return new Profile(mRingtoneManager, (Cursor)super.getItem(position));
	}
	
	public int findPosition(long id) {
		for (int i = 0; i < getCount(); i++) {
			Cursor cursor = (Cursor)super.getItem(i);
			if (id == cursor.getLong(cursor.getColumnIndex(DB.PROFILES.ID)))
				return i;
		}
		
		return -1;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		
		mConvertProfile.load(cursor);
		
		holder.icon.setImageResource(mConvertProfile.getIcon());
		holder.name.setText(mConvertProfile.getName());
		holder.ringtone.setText(mConvertProfile.getRingtoneName());
		holder.volume.setLevel(mConvertProfile.getVolumeLevel());
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = View.inflate(context, R.layout.profile_item, null);
		
		ViewHolder holder = new ViewHolder();
		
		holder.icon = (ImageView) view.findViewById(R.id.profile_item_icon);
		holder.name = (TextView) view.findViewById(R.id.profile_item_name);
		holder.ringtone = (TextView) view.findViewById(R.id.profile_item_ringtone);
		holder.volume = (LevelListDrawable) ((ImageView)view.findViewById(R.id.profile_item_volume)).getDrawable();
		
		view.setTag(holder);
		
		return view;
	}


}

package com.messagealerter.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IconsAdapter extends BaseAdapter {

	private final Context mContext;
	private final int[] mIcons;
	private final String[] mIconStrings;
	
	public IconsAdapter(Context context) {
		mContext = context;
		
		Field[] fields = android.R.drawable.class.getFields();
		
		ArrayList<Field> iconFields = new ArrayList<Field>();

		for (Field field : fields)
			if (field.getName().startsWith("ic_")) iconFields.add(field);
		
		int count = iconFields.size();
		
		mIcons = new int[count];
		mIconStrings = new String[count];
		
		try {
			for (int i = 0; i < count; i++) {
				Field iconField = iconFields.get(i);
				mIcons[i] = iconField.getInt(null);
				mIconStrings[i] = iconField.getName();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getCount() {
		return mIcons.length;
	}

	public String getItem(int position) {
		return mIconStrings[position];
	}

	public long getItemId(int position) {
		return mIcons[position];
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) convertView = new ImageView(mContext);
		
		((ImageView)convertView).setImageResource(mIcons[position]);
		
		return convertView;
	}

}

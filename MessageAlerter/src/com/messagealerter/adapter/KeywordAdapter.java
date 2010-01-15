package com.messagealerter.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.messagealerter.R;
import com.messagealerter.data.Keyword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

public class KeywordAdapter extends BaseAdapter implements View.OnClickListener,
	CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener, TextWatcher {

	private static final class ViewHolder {
		EditText keyword;
		CheckBox caseSensitive;
		ImageButton remove;
		
		void setPosition(Integer position) {
			keyword.setTag(position);
			caseSensitive.setTag(position);
			remove.setTag(position);
		}
		
	}
	
	private final Context mContext;
	private final List<Keyword> mKeywords;
	
	private EditText mFocused;
	
	public KeywordAdapter(Context context) {
		super();
		mContext = context;
		mKeywords = new ArrayList<Keyword>();
	}
	
	public void setKeywords(List<Keyword> keywords) {
		mKeywords.clear();
		mKeywords.addAll(keywords);
		notifyDataSetChanged();
	}
	
	public List<Keyword> getKeywords() {
		return Collections.unmodifiableList(mKeywords);
	}

	public void append(Keyword keyword) {
		mKeywords.add(keyword);
		notifyDataSetChanged();
	}

	public int getCount() {
		return mKeywords.size();
	}

	public Keyword getItem(int position) {
		return mKeywords.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.keyword_item, null);
			
			ViewHolder holder = new ViewHolder();
			
			holder.keyword = (EditText) convertView.findViewById(R.id.keyword_item_keyword);
			holder.caseSensitive = (CheckBox) convertView.findViewById(R.id.keyword_item_case_sensitive);
			holder.remove = (ImageButton) convertView.findViewById(R.id.keyword_item_remove);
			
			holder.keyword.setOnFocusChangeListener(this);
			holder.caseSensitive.setOnCheckedChangeListener(this);
			holder.remove.setOnClickListener(this);
			
			convertView.setTag(holder);
			
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		holder.setPosition(position);
		
		Keyword item = getItem(position);
		
		holder.keyword.setText(item.getKeyword());
		holder.caseSensitive.setChecked(item.isCaseSensitive());
		
		return convertView;
	}


	public void onClick(View v) {
		
		final int position = (Integer)v.getTag();

		new AlertDialog.Builder(mContext).setTitle(mContext.getString(R.string.alert_remove_keyword_title))
			.setMessage(mContext.getString(R.string.alert_remove_keyword_message))
			.setPositiveButton(mContext.getString(R.string.alert_remove_keyword_positive),
				new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						mKeywords.remove(position);
						notifyDataSetChanged();						
					}
				})
			.setNegativeButton(mContext.getString(R.string.alert_remove_keyword_negative), null).show();

	}
	
	public void onFocusChange(View v, boolean hasFocus) {
		
		if (hasFocus) {
			mFocused = (EditText)v;
			mFocused.addTextChangedListener(this);
		} else {
			((EditText)v).removeTextChangedListener(this);
		}
		
	}

	public void afterTextChanged(Editable s) {
		int position = (Integer)mFocused.getTag();
		
		getItem(position).setKeyword(s.toString());
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int position = (Integer)buttonView.getTag();
		
		getItem(position).setCaseSensitive(isChecked);
		
	}

}

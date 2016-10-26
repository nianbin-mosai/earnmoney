package com.mdxx.qmmz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdxx.qmmz.R;
import com.mdxx.qmmz.utils.BitMapUtils;

public class PullAdapter extends BaseAdapter{
	int[] mDrawables;
	String[] mTitle;
	String[] mMessage;
	Context mContext;
	public PullAdapter(Context context,int[] drawables,String[] title,String[] message) {
		mDrawables = drawables;
		mTitle = title;
		mMessage = message;
		mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTitle.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold viewHold = null;
		if(convertView == null){
			viewHold = new ViewHold();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.pull_list_item, null);
			viewHold.icon = (ImageView) convertView.findViewById(R.id.icon_image);
			viewHold.title = (TextView) convertView.findViewById(R.id.title);
			viewHold.message = (TextView) convertView.findViewById(R.id.message);
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}
		
		viewHold.icon.setImageBitmap(BitMapUtils.readBitMap(mContext, mDrawables[position]));
		viewHold.title.setText(mTitle[position]);
		viewHold.message.setText(mMessage[position]);
		return convertView;
	}
}

class ViewHold{
	ImageView icon;
	TextView title;
	TextView message;
}

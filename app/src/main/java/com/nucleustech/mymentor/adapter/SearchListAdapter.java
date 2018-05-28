package com.nucleustech.mymentor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.Student;

import java.util.ArrayList;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
/**
 * @author ritwik.rai
 */
public class SearchListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<Student> members = new ArrayList<Student>();


	public SearchListAdapter(Context mContext, ArrayList<Student> members) {
		this.mContext = mContext;
		this.members = members;
		mInflater = LayoutInflater.from(mContext);
		Log.d("Member", "member array size: " + members.size());
	}

	@Override
	public int getCount() {
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		return members.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View hView = convertView;
		if (convertView == null) {
			hView = mInflater.inflate(R.layout.list_item_mem_dir_member, null);
			ViewHolder holder = new ViewHolder();
			holder.memberName = (TextView) hView.findViewById(R.id.tv_item);
			holder.memberIcon = (ImageView) hView.findViewById(R.id.iv_profile_thumb);

			hView.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) hView.getTag();
		holder.memberName.setText(members.get(position).name);
		//ImageUtil.displayRoundImage(holder.memberIcon, members.get(position).profileImgURL, null);

		return hView;
	}


	class ViewHolder {
		TextView memberName;
		ImageView memberIcon;

	}
}

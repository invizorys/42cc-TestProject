package com.invizorys.cc.testproject.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.invizorys.cc.testproject.R;
import com.invizorys.cc.testproject.entity.Friend;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsListAdapter extends ArrayAdapter<Friend>
{
	private List<Friend> mListFriends = null;
	private Context context;
	private ArrayList<String> checkedIds = new ArrayList<String>();

	public FriendsListAdapter(Context context, List<Friend> friends)
	{
		super(context, R.layout.friends_list_item, friends);
		this.mListFriends = friends;
		this.context = context;
		
		checkedIds = Util.loadCheckedIds(context);
	}

	@Override
	public int getCount()
	{
		if (mListFriends != null)
		{
			return mListFriends.size();
		}
		return 0;
	}

	@Override
	public Friend getItem(int position)
	{
		if (mListFriends != null && position < mListFriends.size())
			return mListFriends.get(position);
		return null;
	}

	@Override
	public long getItemId(int id)
	{
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.friends_list_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.imageView_friend_photo);
			viewHolder.name = (TextView) convertView.findViewById(R.id.textView_friend_name_and_surname);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox_priority);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Friend friend = getItem(position);
		
		viewHolder.name.setText(friend.getName());

		String photoPath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName();
		String photoFileStr = photoPath + "/" + friend.getId() + ".jpg";
		File photoFile = new File(photoFileStr);
		if (photoFile.exists()) {
			viewHolder.photo.setImageBitmap(BitmapFactory.decodeFile(photoFileStr));
		}
		
		if(checkedIds.contains(friend.getId()))
			viewHolder.checkBox.setChecked(true);
		else {
			viewHolder.checkBox.setChecked(false);
		}

//		viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//		   @Override
//		   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			   
//		   }
//		});
		
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView photo;
		TextView name;
		CheckBox checkBox;
	}

}
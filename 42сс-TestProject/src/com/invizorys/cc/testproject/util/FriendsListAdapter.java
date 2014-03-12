package com.invizorys.cc.testproject.util;

import java.io.File;
import java.util.List;

import com.invizorys.cc.testproject.R;
import com.invizorys.cc.testproject.entity.Friend;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsListAdapter extends ArrayAdapter<Friend>
{
	private List<Friend> mListFriends = null;
	private Context context;

	public FriendsListAdapter(Context context, List<Friend> friends)
	{
		super(context, R.layout.friends_list_item, friends);
		this.mListFriends = friends;
		this.context = context;
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
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final Friend friend = getItem(position);
		
		viewHolder.name.setText(friend.getName());

		String photoPath = Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName();
		String photoFileStr = photoPath + "/" + friend.getId() + ".jpg";
		File photoFile = new File(photoFileStr);
		if (photoFile.exists()) {
			viewHolder.photo.setImageBitmap(BitmapFactory.decodeFile(photoFileStr));
		}
		
		final TextView textViewPriority = (TextView) convertView.findViewById(R.id.textView_priority);
		int friendPriority = Util.loadFriendPriority(context, friend.getId());
		textViewPriority.setText(String.valueOf(friendPriority));
		
		convertView.findViewById(R.id.button_increase_priority).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int priority = Integer.parseInt(textViewPriority.getText().toString());
				if(priority < 5)
				{
					Util.saveFriendPriority(context, friend.getId(), priority+1);
					textViewPriority.setText(String.valueOf(priority + 1));
				}
				if(priority == 5)
					Toast.makeText(context, "it's a maximum priority", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		convertView.findViewById(R.id.button_decrease_priority).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int priority = Integer.parseInt(textViewPriority.getText().toString());
						if (priority > 0) {
							Util.saveFriendPriority(context, friend.getId(), priority-1);
							textViewPriority.setText(String.valueOf(priority - 1));
						}
						if(priority == 0)
							Toast.makeText(context, "it's a minimum priority", Toast.LENGTH_SHORT).show();

					}
				});
		
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView photo;
		TextView name;
	}

}
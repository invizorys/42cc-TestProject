package com.invizorys.cc.testproject.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.invizorys.cc.testproject.R;
import com.invizorys.cc.testproject.entity.Friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsListAdapter extends ArrayAdapter<Friend>
{
	private List<Friend> mListFriends = null;
	private Context context;
	private ImageView friendPhoto;

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
		TextView friendNameAndSurname;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.friends_list_item, null);
		}
		
		friendPhoto = (ImageView) convertView.findViewById(R.id.imageView_friend_photo);
		friendNameAndSurname = (TextView) convertView.findViewById(R.id.textView_friend_name_and_surname);
		Friend friend = getItem(position);

		friendNameAndSurname.setText(friend.getName());
		new DownloadPhoto().execute(friend.getId());

		return convertView;
	}
	
	private class DownloadPhoto extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
				String url = String.format("https://graph.facebook.com/%s/picture", params[0]);
				
				InputStream inputStream;
				try {
					inputStream = new URL(url).openStream();
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
		}

		@Override
		protected void onPostExecute(Bitmap photo) {
			super.onPostExecute(photo);
			if(photo != null)
				friendPhoto.setImageBitmap(photo);
		}

	}

}
package com.invizorys.cc.testproject.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import com.invizorys.cc.testproject.util.FriendsListAdapter;
import com.invizorys.cc.testproject.util.Util;
import com.invizorys.cc.testproject.entity.Friend;
import com.invizorys.cc.testproject.R;

public class FriendsFragment extends SherlockFragment {
	private ListView lvFriends;
	private ArrayList<Friend> friends = new ArrayList<Friend>();
	private FriendsListAdapter adapter;
	private ProgressDialog dialog;
	private File imagesDir;
	private ArrayList<String> checkedIds = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.friends_fragment, container, false);
		lvFriends = (ListView) rootView.findViewById(R.id.listView_friends);
		
		adapter = new FriendsListAdapter(getActivity(), friends);
		lvFriends.setAdapter(adapter);
		lvFriends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if(Util.isNetworkConnected(getActivity()))
					startFriendsPage(adapter.getItem(position).getId().toString());
				else
					Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
			}

		});
		
		rootView.findViewById(R.id.button_update_friend_list).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateFriendList();
			}
		});

		String photoPath = Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName();
		imagesDir = new File(photoPath);
		if(Util.isNetworkConnected(getActivity()))
			showFriendsList();
		else
			Toast.makeText(getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();

		return rootView;

	}

	private void showFriendsList() {
		dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);
		if (Session.getActiveSession() != null) {
			Request request = Request.newMyFriendsRequest(Session.getActiveSession(),
					new Request.GraphUserListCallback() {
						@Override
						public void onCompleted(List<GraphUser> users, Response response) {
							if (getActivity() != null && lvFriends != null) {
								adapter.clear();
								for (GraphUser friend : users) {
									friends.add(new Friend(friend.getId(), friend.getName()));
								}
								if(imagesDir.listFiles().length != friends.size() + 1)
								{
									Friend[] friendsArr = friends.toArray(new Friend[friends.size()]);
									new PhotoLoader().execute(friendsArr);
								}
								else
								{
									checkedIds = Util.loadCheckedIds(getActivity());
									setPriorityForFriends(checkedIds);
									Collections.sort(friends, new FriendPriorityComparator());
									
									dialog.dismiss();
									adapter.notifyDataSetChanged();
								}
							}
						}
					});
			Request.executeBatchAsync(request);
		}
	}
	
	private void startFriendsPage(String friendId) {
		final String urlFb = "fb://profile/" + friendId;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(urlFb));

		final PackageManager packageManager = getActivity().getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (list.size() == 0) {
			final String urlBrowser = "https://www.facebook.com/pages/" + friendId;
			intent.setData(Uri.parse(urlBrowser));
		}
		startActivity(intent);
	}
	
	private void updateFriendList()
	{
		CheckBox cb;
		checkedIds = new ArrayList<String>();
	    for (int position = 0; position < lvFriends.getChildCount(); position++){
	        cb = (CheckBox) lvFriends.getChildAt(position).findViewById(R.id.checkBox_priority);
	        if(cb.isChecked()){
	        	checkedIds.add(adapter.getItem(position).getId());
	        }
	    }
	    Util.saveArray(getActivity(), checkedIds);
	    
		setPriorityForFriends(checkedIds);
		Collections.sort(friends, new FriendPriorityComparator());
		adapter = new FriendsListAdapter(getActivity(), friends);
		lvFriends.setAdapter(adapter);
	}
	
	private void setPriorityForFriends(ArrayList<String> checkedIds)
	{
		for (Friend friend : friends) {
			if (checkedIds.contains(friend.getId()))
				friend.setPriority(1);
			else
				friend.setPriority(0);		
		}
	}
	
	private class PhotoLoader extends AsyncTask<Friend, Void, Void> {

		@Override
		protected Void doInBackground(Friend... params) {
			for (Friend friend : params) {
				String url = String.format("https://graph.facebook.com/%s/picture", friend.getId());

				InputStream inputStream;
				try {
					inputStream = new URL(url).openStream();
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					Util.savePhotoOnSdcard(bitmap, friend.getId(), getActivity());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void arg) {
			super.onPostExecute(arg);
			
			checkedIds = Util.loadCheckedIds(getActivity());
			setPriorityForFriends(checkedIds);
			Collections.sort(friends, new FriendPriorityComparator());
			adapter.notifyDataSetChanged();
			dialog.dismiss();
		}

	}
	
	public class FriendPriorityComparator implements Comparator<Friend> {
	    @Override
	    public int compare(Friend friend1, Friend friend2) {
	        return  friend2.getPriority() - friend1.getPriority();
	    }
	}

}

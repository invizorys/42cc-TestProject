package com.invizorys.cc.testproject.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import com.invizorys.cc.testproject.util.FriendsListAdapter;
import com.invizorys.cc.testproject.entity.Friend;
import com.invizorys.cc.testproject.R;

public class FriendsFragment extends SherlockFragment {
	private ListView lvFriends;
	private ArrayList<Friend> friends = new ArrayList<Friend>();
	private FriendsListAdapter adapter;

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
				startFriendsPage(adapter.getItem(position).getId().toString());
			}

		});

		showFriendsList();

		return rootView;

	}

	private void showFriendsList() {
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
								adapter.notifyDataSetChanged();
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

}

package com.invizorys.test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.invizorys.cc.testproject.MainActivity;
import com.invizorys.cc.testproject.R;
import com.invizorys.cc.testproject.db.DBHelper;
import com.invizorys.cc.testproject.entity.Friend;
import com.invizorys.cc.testproject.entity.User;
import com.invizorys.cc.testproject.util.FriendsListAdapter;
import com.invizorys.cc.testproject.util.Util;

@RunWith(RobolectricTestRunner.class) @Config(reportSdk = 10)
public class MainActivityTest {
	private MainActivity activity;
	private TextView name, surname, dateOfBirth;
	private ListView lvFriends;
	private User user;

	@Before
    public void setUp() {
		activity = Robolectric.buildActivity(MainActivity.class).create().get();
		
		name = (TextView) activity.findViewById(R.id.textView_name);
		surname = (TextView) activity.findViewById(R.id.textView_surname);
		dateOfBirth = (TextView) activity.findViewById(R.id.textView_birthday);
		lvFriends = (ListView) activity.findViewById(R.id.listView_friends);
	}
	
	@Test
	public void testFieldsCreated() {
		assertNotNull(name);
		assertNotNull(surname);
		assertNotNull(dateOfBirth);
	}
	
	@Test
	public void testFieldsNotEmpty() {
		assertNotSame("", name.getText().toString());
		assertNotSame("", surname.getText().toString());
		assertNotSame("", dateOfBirth.getText().toString());
	}
	
	@Test
	public void testListView()
	{
		assertNotNull(lvFriends);
		assertNotSame(0, lvFriends.getCount());
	}
	
	@Test
	public void isDataFromDB()
	{
		DBHelper dbHelper = new DBHelper(activity);
		user = dbHelper.readUserData();
		assertEquals(user.getName(), name.getText().toString());
		assertEquals(user.getSurname(), surname.getText().toString());
		assertEquals(user.getBirthday(), dateOfBirth.getText().toString());
	}
	
	@Test
	public void testSortingFriends()
	{
		ArrayList<String> checkedIds = new ArrayList<String>();
		checkedIds = Util.loadCheckedIds(activity);
		
		if (lvFriends.getCount() > 1)
		{
			
			FriendsListAdapter adapter = (FriendsListAdapter) lvFriends.getAdapter();
			checkedIds = new ArrayList<String>();
		    
		    checkedIds.add(adapter.getItem(1).getId());
		    
		    ArrayList<Friend> friends = new ArrayList<Friend>();
			for (int i = 0; i < adapter.getCount(); i++) {
				friends.add(adapter.getItem(i));
			}
			
			for (Friend friend : friends) {
				if (checkedIds.contains(friend.getId()))
					friend.setPriority(1);
				else
					friend.setPriority(0);		
			}
			
		    Collections.sort(friends, new FriendPriorityComparator());
		    adapter.clear();
		    adapter = new FriendsListAdapter(activity, friends);
			lvFriends.setAdapter(adapter);
			
			CheckBox currentCb, nextCb;
			for (int position = 0; position < lvFriends.getChildCount() - 1; position++){
		        currentCb = (CheckBox) lvFriends.getChildAt(position).findViewById(R.id.checkBox_priority);
		        nextCb = (CheckBox) lvFriends.getChildAt(position).findViewById(R.id.checkBox_priority);
		        
		        int currentPriority = getPriority(currentCb);
		        int nextPriority = getPriority(nextCb);
		        assertTrue(currentPriority >= nextPriority);
		    }
			
		}
	}
	
	private int getPriority(CheckBox cb)
	{
		if(cb.isChecked())
			return 1;
		else return 0;
	}
	
	private static class FriendPriorityComparator implements Comparator<Friend> {
	    @Override
	    public int compare(Friend friend1, Friend friend2) {
	        return  friend2.getPriority() - friend1.getPriority();
	    }
	}
}
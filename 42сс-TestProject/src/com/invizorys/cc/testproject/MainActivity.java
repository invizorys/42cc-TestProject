package com.invizorys.cc.testproject;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;

import com.invizorys.cc.testproject.R;
import com.invizorys.cc.testproject.util.TabListener;
import com.invizorys.cc.testproject.fragment.DataFragment;
import com.invizorys.cc.testproject.fragment.FriendsFragment;
import com.invizorys.cc.testproject.fragment.AboutFragment;

public class MainActivity extends SherlockFragmentActivity {
	
	private ActionBar.Tab dataTab, friendsTab, aboutTab;
	private SherlockFragment dataFragment = new DataFragment();
	private SherlockFragment friendsFragment = new FriendsFragment();
	private SherlockFragment photofragment = new AboutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		dataTab = actionBar.newTab().setText("Main");
		friendsTab = actionBar.newTab().setText("Friends");
		aboutTab = actionBar.newTab().setText("About");

		dataTab.setTabListener(new TabListener(dataFragment));
		friendsTab.setTabListener(new TabListener(friendsFragment));
		aboutTab.setTabListener(new TabListener(photofragment));

		actionBar.addTab(dataTab);
		actionBar.addTab(friendsTab);
		actionBar.addTab(aboutTab);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}
    
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

		case R.id.logout:
			if(item.getItemId() == R.id.logout)
			{
				Session session = Session.getActiveSession();
				if (!session.isClosed()) {
					session.closeAndClearTokenInformation();
				}
			}
			finish();
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

    
}

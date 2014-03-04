package com.invizorys.cc.testproject;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.invizorys.cc.testproject.R;

public class MainActivity extends SherlockFragmentActivity {
	
	private ActionBar.Tab dataTab,photoTab;
	private SherlockFragment dataFragment = new DataFragment();
	private SherlockFragment photofragment = new AboutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		dataTab = actionBar.newTab().setText("FACEBOOK");
		photoTab = actionBar.newTab().setText("ABOUT");

		dataTab.setTabListener(new TabListener(dataFragment));
		photoTab.setTabListener(new TabListener(photofragment));

		actionBar.addTab(dataTab);
		actionBar.addTab(photoTab);
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

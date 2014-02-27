package com.invizorys.cc.testproject;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.invizorys.cc.testproject.R;

public class MainActivity extends SherlockFragmentActivity {
	
	private ActionBar.Tab dataTab,photoTab;
	private SherlockFragment dataFragment = new DataFragment();
	private SherlockFragment photofragment = new PhotoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		dataTab = actionBar.newTab().setText("Дані");
		photoTab = actionBar.newTab().setText("Фото");

		dataTab.setTabListener(new TabListener(dataFragment));
		photoTab.setTabListener(new TabListener(photofragment));

		actionBar.addTab(dataTab);
		actionBar.addTab(photoTab);
    }
    
}

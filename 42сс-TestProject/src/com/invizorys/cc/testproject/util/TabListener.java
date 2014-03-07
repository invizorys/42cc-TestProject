package com.invizorys.cc.testproject.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.invizorys.cc.testproject.R;

public class TabListener implements ActionBar.TabListener{

	private Fragment fragment;
	
	public TabListener(Fragment fragment) {
        this.fragment = fragment;
    }
 
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) { }

}

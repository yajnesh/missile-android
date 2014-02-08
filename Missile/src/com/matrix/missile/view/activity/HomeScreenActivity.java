package com.matrix.missile.view.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.NavBarAdapter;
import com.matrix.missile.model.NavigationDrawerModel;

public class HomeScreenActivity extends Activity {

	// Holds unique tags from database
	private ArrayList<NavigationDrawerModel> tags;
	private NavBarAdapter navigationListAdapter;
	// NavBar related variables
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mAppTitle;
	private ListView mDrawerList;
	private boolean isDrawerOpen = false;

	private NavigationDrawerModel getNavigationDrawerModel(String title) {
		NavigationDrawerModel navModel = new NavigationDrawerModel();
		navModel.setNavItem(title);
		return navModel;
	}

	private void setNavigationBar() {
		mAppTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {

				getActionBar().setTitle(mAppTitle);
				isDrawerOpen = false;
				invalidateOptionsMenu();

			}

			public void onDrawerOpened(View drawerView) {

				getActionBar().setTitle(mDrawerTitle);
				isDrawerOpen = true;
				invalidateOptionsMenu();

			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// mDrawerLayout.setDrawerListener(null);

	}

	/**
	 * Updates the Appdrawer with latest tags from
	 * ArrayList<NavigationDrawerModel> tags;
	 */
	private void setNavigationListAdapter() {

		navigationListAdapter = new NavBarAdapter(this,
				R.layout.drawer_list_item, tags);
		if (tags != null || tags.size() > 0) {
			mDrawerList.setAdapter(navigationListAdapter);
		}

	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mDrawerList.setItemChecked(position, true);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	/*
	 * private void replaceFragment(String fragmentArgsValue) {
	 * 
	 * // After changing to support.v4 , each time NoteListFragment has to be //
	 * instantiated. Any fixes? noteListFragment = new NoteListFragment();
	 * 
	 * Bundle bundle = new Bundle();
	 * bundle.putString(ConstantDefinitions.FRAGMENT_ARGS_TAGNAME_CALLER,
	 * fragmentArgsValue); noteListFragment.setArguments(bundle);
	 * 
	 * FragmentManager fragmentManager = getSupportFragmentManager();
	 * 
	 * fragmentManager.beginTransaction() .replace(R.id.frame_container,
	 * noteListFragment) .commitAllowingStateLoss(); }
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void setTitle(CharSequence title) {
		mAppTitle = title;
		getActionBar().setTitle(mAppTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen_layout);
		initialize();

		generateStaticNavBarItems();
		setNavigationBar();
		setNavigationListAdapter();

	}

	private void generateStaticNavBarItems() {
		tags.add(getNavigationDrawerModel("Live Missiles"));
		tags.add(getNavigationDrawerModel("My Missiles"));
		tags.add(getNavigationDrawerModel("#tags"));
	}

	private void initialize() {
		tags = new ArrayList<NavigationDrawerModel>();
	}

}

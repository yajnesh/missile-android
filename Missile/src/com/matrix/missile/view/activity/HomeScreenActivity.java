package com.matrix.missile.view.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.matrix.missile.R;
import com.matrix.missile.controller.adapter.NavBarAdapter;
import com.matrix.missile.controller.adapter.StartModule;
import com.matrix.missile.model.NavigationDrawerModel;

public class HomeScreenActivity extends FragmentActivity implements
		OnItemClickListener {

	// Holds unique tags from database
	private ArrayList<NavigationDrawerModel> tags;
	private NavBarAdapter navigationListAdapter;
	// NavBar related variables
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mAppTitle;
	private ListView mDrawerList;
	public boolean isDrawerOpen = false;
	private MenuItem searchItem;
	private Handler delaySearchHandler;

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

		mDrawerList.setOnItemClickListener(this);

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
		navigationListAdapter = new NavBarAdapter(this);
		navigationListAdapter.supportAddAll(tags);
		if (tags != null || tags.size() > 0) {
			mDrawerList.setAdapter(navigationListAdapter);
		}

	}

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
		// showMissileList();

		// By default selece first item
		setTitle(tags.get(0).getNavItem());
		showMissileList();

	}

	private void generateStaticNavBarItems() {
		tags.add(getNavigationDrawerModel("Live Missiles"));
		tags.add(getNavigationDrawerModel("My Missiles"));
		tags.add(getNavigationDrawerModel("#tags"));
		tags.add(getNavigationDrawerModel("Launch Missile"));
		tags.add(getNavigationDrawerModel("About"));
	}

	private void initialize() {
		tags = new ArrayList<NavigationDrawerModel>();
	}

	private void showMissileList() {
		ViewMissilesFragment viewMissilesFragment = new ViewMissilesFragment();
		Bundle bundle = new Bundle();
		bundle.putString("url", "missiles.json");
		StartModule.setFragmentLayout(getSupportFragmentManager(),
				viewMissilesFragment, bundle, "Live Missile");
	}

	@Override
	public void onBackPressed() {
		FragmentManager ft = getSupportFragmentManager();
		if (ft.getBackStackEntryCount() == 1) {
			ft.popBackStackImmediate();
			ft.executePendingTransactions();
		}
		super.onBackPressed();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
		navigationListAdapter.changeSelected(position);

		setTitle(tags.get(position).getNavItem());
		switch (position) {
		case 0:
			showMissileList();
			break;
		case 1:
			MyMissilesFragment myMissilesActivity = new MyMissilesFragment();
			StartModule.setFragmentLayout(getSupportFragmentManager(),
					myMissilesActivity, null, "My Missile");
			break;
		case 2:
			Toast.makeText(getApplicationContext(), "Not yet implemented",
					Toast.LENGTH_LONG).show();
			break;

		case 3:
			SendMissileFragment sendMissileActivity = new SendMissileFragment();
			StartModule.setFragmentLayout(getSupportFragmentManager(),
					sendMissileActivity, null, "Launch Missile");
			break;
		case 4:
			startActivity(new Intent(HomeScreenActivity.this,AboutAvtivity.class));
		}

	}

	public DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

}

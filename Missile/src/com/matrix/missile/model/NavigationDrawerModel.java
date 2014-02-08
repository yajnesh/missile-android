package com.matrix.missile.model;

public class NavigationDrawerModel {

	private String navItem;
	private boolean isSection=false;

	public String getNavItem() {
		return navItem;
	}

	public void setNavItem(String navItem) {
		this.navItem = navItem;
	}

	public boolean isSection() {
		return isSection;
	}

	public void setSection(boolean isSection) {
		this.isSection = isSection;
	}

}

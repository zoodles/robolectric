package com.xtremelabs.robolectric.tester.android.util;

import android.support.v4.app.FragmentManager.BackStackEntry;

public class TestBackStackEntry implements BackStackEntry {
	
	String name;

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getBreadCrumbTitleRes() {
		return 0;
	}

	@Override
	public int getBreadCrumbShortTitleRes() {
		return 0;
	}

	@Override
	public CharSequence getBreadCrumbTitle() {
		return null;
	}

	@Override
	public CharSequence getBreadCrumbShortTitle() {
		return null;
	}

	public TestBackStackEntry(String name) {
		super();
		this.name = name;
	}

}

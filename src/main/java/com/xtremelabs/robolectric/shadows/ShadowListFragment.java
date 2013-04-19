package com.xtremelabs.robolectric.shadows;

import android.support.v4.app.ListFragment;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

@Implements(ListFragment.class)
public class ShadowListFragment extends ShadowFragment {

	private ListView listView;
	private ListAdapter listAdapter;
	private int position;
	private long selectedItemId;
	
	public void __constructor__() {
		listView = new ListView( Robolectric.application );
	}
	
	@Implementation
	public ListView getListView() {
		return listView;
	}
	
	@Implementation
	public ListAdapter getListAdapter() {
		return listAdapter;
	}
	
	@Implementation
	public void setListAdapter(ListAdapter listAdapter) {
		this.listAdapter = listAdapter;
	}
	
	@Implementation
	public int getSelectedItemPosition() {
		return position;
	}
	
	@Implementation
	public void setSelection( int position ) {
		this.position = position;
	}
	
	@Implementation
	public long getSelectedItemId() {
		return selectedItemId;
	}
	
	/**
	 * Non-Android accessor.
	 * 
	 * @param id
	 */
	public void setSelectedItemId( long id ) {
		this.selectedItemId = id;
	}
}

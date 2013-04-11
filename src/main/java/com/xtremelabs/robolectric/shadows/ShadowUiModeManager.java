package com.xtremelabs.robolectric.shadows;

import android.app.UiModeManager;
import android.content.res.Configuration;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;

@Implements(UiModeManager.class)
public class ShadowUiModeManager {

	@RealObject
	UiModeManager realManager;
	
	private int mCurrentModeType = Configuration.UI_MODE_TYPE_NORMAL;
	
	@Implementation
	public void disableCarMode(int flag){
		mCurrentModeType = Configuration.UI_MODE_TYPE_NORMAL;
	}
	
	@Implementation
	public void enableCarMode( int flag ){
		mCurrentModeType = Configuration.UI_MODE_TYPE_CAR;
	}
	
	@Implementation
	public int getCurrentModeType(){
		return mCurrentModeType; 
	}
	
}

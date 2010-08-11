package com.xtremelabs.droidsugar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xtremelabs.droidsugar.view.FakeActivity;

class MyLayoutInflater extends LayoutInflater {
    public MyLayoutInflater(Context context) {
        super(context);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return null;
    }

    @Override
    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return FakeActivity.viewLoader.inflateView(getContext(), resource);
    }
}

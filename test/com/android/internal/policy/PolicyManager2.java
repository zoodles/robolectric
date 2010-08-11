package com.android.internal.policy;

import android.content.Context;
import android.view.Window;
import com.android.internal.policy.impl.PhoneWindow;

public class PolicyManager2 {
    public static Window makeNewWindow(Context context) {
        return new PhoneWindow(context);
    }
}

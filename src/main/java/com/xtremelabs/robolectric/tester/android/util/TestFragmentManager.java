package com.xtremelabs.robolectric.tester.android.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.*;
import android.view.View;
import android.view.ViewGroup;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.SerializedFragmentState;
import com.xtremelabs.robolectric.shadows.ShadowFragment;
import com.xtremelabs.robolectric.shadows.ShadowFragmentActivity;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.*;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;

public class TestFragmentManager extends FragmentManager {
    private Map<Integer, Fragment> fragmentsById = new HashMap<Integer, Fragment>();
    private Map<String, Fragment> fragmentsByTag = new HashMap<String, Fragment>();
    private FragmentActivity activity;
    private List<TestFragmentTransaction> transactions = new ArrayList<TestFragmentTransaction>();
    private List<Runnable> transactionsToRunLater = new ArrayList<Runnable>();
    
    private LinkedList<String> backStack = new LinkedList<String>();
    private OnBackStackChangedListener backStackListener;

    public TestFragmentManager(FragmentActivity activity) {
        this.activity = activity;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    @Override
    public FragmentTransaction beginTransaction() {
        return new TestFragmentTransaction(this);
    }

    @Override
    public boolean executePendingTransactions() {
        if (transactionsToRunLater.size() > 0) {
            for (Runnable runnable : new ArrayList<Runnable>(transactionsToRunLater)) {
                runnable.run();
                shadowOf(Looper.getMainLooper()).getScheduler().remove(runnable);
            }
            return true;
        }
        return false;
    }

    @Override
    public Fragment findFragmentById(int id) {
        return fragmentsById.get(id);
    }

    @Override
    public Fragment findFragmentByTag(String tag) {
        return fragmentsByTag.get(tag);
    }
    
    public void addToBackStack( String name ) {
    	backStack.addLast(name);
    }

    @Override
    public void popBackStack() {
    	backStack.removeLast();
    }

    @Override
    public boolean popBackStackImmediate() {
    	backStack.removeLast();
    	return false;
    }

    @Override
    public void popBackStack(String name, int flags) {
    	int size = backStack.size();
    	String current = backStack.getLast();
    	while( current != null && !current.equals(name) && !backStack.isEmpty() ) {
    		current = backStack.removeLast();
    	}
    	if ( flags == POP_BACK_STACK_INCLUSIVE && !backStack.isEmpty() && current.equals(name) ) {
    		backStack.removeLast();
    	}
    	if ( backStack.size() != size && backStackListener != null ) {
    		backStackListener.onBackStackChanged();
    	}
    }

    @Override
    public boolean popBackStackImmediate(String name, int flags) {
    	int size = backStack.size();
    	popBackStack(name, flags);
        return size != backStack.size();
    }

    @Override
    public void popBackStack(int id, int flags) {
    	// just toss them all
    	popBackStack(null, flags);
    }

    @Override
    public boolean popBackStackImmediate(int id, int flags) {
    	int size = backStack.size();
    	popBackStack(null, flags);
        return size != backStack.size();
    }

    @Override
    public int getBackStackEntryCount() {
        return backStack.size();
    }

    @Override
    public BackStackEntry getBackStackEntryAt(int index) {
    	String name = backStack.get(index);
        return new TestBackStackEntry( name );
    }

    @Override
    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
    	backStackListener = listener;
    }

    @Override
    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
    	backStackListener = null;
    }

    @Override
    public void putFragment(Bundle bundle, String key, Fragment fragment) {
    }

    @Override
    public Fragment getFragment(Bundle bundle, String key) {
        Object[] fragments = (Object[]) bundle.getSerializable(ShadowFragmentActivity.FRAGMENTS_TAG);
        for (Object object : fragments) {
            SerializedFragmentState fragment = (SerializedFragmentState) object;
            if (fragment.tag.equals(key)) {
                // TODO deserialize state
                return Robolectric.newInstanceOf(fragment.fragmentClass);
            }
        }
        return null;
    }

    @Override
    public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
        return null;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
    }

    public void addDialogFragment(String tag, DialogFragment fragment) {
        fragmentsByTag.put(tag, fragment);
    }
    
    public void removeDialogFragment(DialogFragment fragment) {
    	fragmentsByTag.values().remove(fragment);
    }

    public void addFragment(int containerViewId, String tag, Fragment fragment, boolean replace) {
        fragmentsById.put(containerViewId, fragment);
        fragmentsByTag.put(tag, fragment);

        ShadowFragment shadowFragment = shadowOf(fragment);
        shadowFragment.setTag(tag);
        shadowFragment.setContainerViewId(containerViewId);
        shadowFragment.setShouldReplace(replace);
        shadowFragment.setActivity(activity);

        fragment.onAttach(activity);
        fragment.onCreate(shadowFragment.getSavedInstanceState());
    }

    public void startFragment(Fragment fragment) {
        ViewGroup container = null;
        ShadowFragment shadowFragment = shadowOf(fragment);
        if (shadowOf(activity).getContentView() != null) {
            container = (ViewGroup) activity.findViewById(shadowFragment.getContainerViewId());
        }

        View view = fragment.onCreateView(activity.getLayoutInflater(), container, shadowFragment.getSavedInstanceState());
        shadowFragment.setView(view);

        fragment.onViewCreated(view, null);
        if (container != null) {
            if (shadowFragment.getShouldReplace()) {
                container.removeAllViews();
            }
            if (view != null) {
                container.addView(view);
            }
        }

        fragment.onActivityCreated(shadowFragment.getSavedInstanceState());
        fragment.onStart();
    }

    public HashMap<Integer, Fragment> getFragments() {
        return new HashMap<Integer, Fragment>(fragmentsById);
    }

    public List<TestFragmentTransaction> getCommittedTransactions() {
        return transactions;
    }

    public void commitTransaction(TestFragmentTransaction t) {
        transactions.add(t);
        if (t.isStarting()) {
            addFragment(t.getContainerViewId(), t.getTag(), t.getFragment(), t.isReplacing());
            startFragment(t.getFragment());
        }
        if (t.isRemoving()) {
            Fragment fragment = t.getFragmentToRemove();
            if (fragment instanceof DialogFragment) {
                ((DialogFragment)fragment).dismiss();
            }
        }
        if (t.isAttaching()) {
            shadowOf(t.getFragmentToAttach()).setAttached(true);
        }
    }

    void commitLater(final TestFragmentTransaction testFragmentTransaction) {
        Runnable transactionCommit = new Runnable() {
            @Override
            public void run() {
                commitTransaction(testFragmentTransaction);
                transactionsToRunLater.remove(this);
            }
        };
        transactionsToRunLater.add(transactionCommit);
        new Handler().post(transactionCommit);
    }
}

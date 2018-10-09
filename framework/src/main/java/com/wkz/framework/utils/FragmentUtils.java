package com.wkz.framework.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Fragment操作
 *
 * @author venshine
 */
public class FragmentUtils {

    /**
     * Replace an existing fragment that was added to a container.
     */
    public static void replaceFragment(FragmentActivity activity, int containerViewId,
                                       Fragment newFragment, Bundle bundle,
                                       boolean canBack) {
        if (newFragment == null) return;
        FragmentTransaction mFragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        newFragment.setArguments(bundle);
        mFragmentTransaction.replace(containerViewId, newFragment, newFragment.getClass().getName());
        if (canBack) {
            mFragmentTransaction.addToBackStack(null);
        }
        mFragmentTransaction.commit();
    }

    /**
     * Add a fragment to the activity state. This fragment may optionally also have its view (if
     * {@link Fragment#onCreateView Fragment.onCreateView} returns non-null) into a container view of the activity.
     */
    public static void addFragment(FragmentActivity activity, int containerViewId,
                                   Fragment newFragment, Bundle bundle,
                                   boolean canBack) {
        if (newFragment == null) return;
        FragmentTransaction mFragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        newFragment.setArguments(bundle);
        if (!newFragment.isAdded()) {
            mFragmentTransaction.add(containerViewId, newFragment, newFragment.getClass().getName());
        }
        if (canBack) {
            mFragmentTransaction.addToBackStack(null);
        }
        mFragmentTransaction.commit();
    }

    /**
     * Add a fragment to the fragment state. This fragment may optionally also have its view (if
     * {@link Fragment#onCreateView Fragment.onCreateView} returns non-null) into a container view of the fragment.
     */
    public static void addChildFragment(Fragment fragment, int containerViewId,
                                        Fragment newFragment, Bundle bundle,
                                        boolean canBack) {
        if (newFragment == null) return;
        FragmentTransaction mFragmentTransaction = fragment.getChildFragmentManager().beginTransaction();
        newFragment.setArguments(bundle);
        if (!newFragment.isAdded()) {
            mFragmentTransaction.add(containerViewId, newFragment, newFragment.getClass().getName());
        }
        if (canBack) {
            mFragmentTransaction.addToBackStack(null);
        }
        mFragmentTransaction.commit();
    }

    /**
     * Hides an existing fragment. This is only relevant for fragments whose views have been added to a container, as
     * this will cause the view to be hidden.
     */
    public static void hideAndShowFragment(FragmentActivity activity, int containerViewId,
                                           Fragment previousFragment, Fragment newFragment,
                                           Bundle bundle, boolean canBack) {
        if (newFragment == null) return;
        FragmentTransaction mFragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        if (null != previousFragment) {
            mFragmentTransaction.hide(previousFragment);
        }
        newFragment.setArguments(bundle);
        if (newFragment.isAdded()) {
            mFragmentTransaction.show(newFragment);
        } else {
            mFragmentTransaction
                    .add(containerViewId,
                            newFragment,
                            newFragment.getClass().getName())
                    .show(newFragment);
        }
        if (canBack && previousFragment != null) {
            mFragmentTransaction.addToBackStack(newFragment.getClass().getName());
        }
        mFragmentTransaction.commit();
    }

    /**
     * Hides an existing child fragment. This is only relevant for fragments whose views have been added to a container, as
     * this will cause the view to be hidden.
     */
    public static void hideAndShowChildFragment(Fragment fragment, int containerViewId,
                                                Fragment previousFragment, Fragment newFragment,
                                                Bundle bundle, boolean canBack) {
        if (newFragment == null) return;
        FragmentTransaction mFragmentTransaction = fragment.getChildFragmentManager().beginTransaction();
        if (null != previousFragment) {
            mFragmentTransaction.hide(previousFragment);
        }
        newFragment.setArguments(bundle);
        if (newFragment.isAdded()) {
            mFragmentTransaction.show(newFragment);
        } else {
            mFragmentTransaction
                    .add(containerViewId,
                            newFragment,
                            newFragment.getClass().getName())
                    .show(newFragment);
        }
        if (canBack && previousFragment != null) {
            mFragmentTransaction.addToBackStack(newFragment.getClass().getName());
        }
        mFragmentTransaction.commit();
    }

    /**
     * Remove an existing fragment. If it was added to a container, its view is also removed from that container.
     */
    public static void removeFragment(FragmentActivity activity, String... names) {
        FragmentManager manager = activity.getSupportFragmentManager();
        for (String name : names) {
            manager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = manager.beginTransaction();
        for (String name : names) {
            Fragment fragment = manager.findFragmentByTag(name);
            if (fragment != null) {
                transaction.remove(fragment);
            }
        }
        transaction.commit();
    }
}

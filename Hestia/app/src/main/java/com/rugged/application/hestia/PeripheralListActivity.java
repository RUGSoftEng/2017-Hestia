/*
This class is the host of the PeripheralListFragment.
 */

package com.rugged.application.hestia;

import android.support.v4.app.Fragment;

/**
 * The activity which presents a list containing all peripherals to the user. An activity is a
 * single, focused thing the user can do.
 */
public class PeripheralListActivity extends SingleFragmentActivity{
    private final static String TAG = "PeripheralListActivity";
    @Override
    protected Fragment createFragment() {
        return new PeripheralListFragment();
    }
}

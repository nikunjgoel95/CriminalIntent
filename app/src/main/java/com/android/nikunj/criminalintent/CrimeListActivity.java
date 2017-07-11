package com.android.nikunj.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by nikunjgoel on 13-06-2017.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        return new CrimeListFragment();

    }
}

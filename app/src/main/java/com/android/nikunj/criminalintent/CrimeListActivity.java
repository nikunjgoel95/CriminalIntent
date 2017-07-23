package com.android.nikunj.criminalintent;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

/**
 * Created by nikunjgoel on 13-06-2017.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {

        return new CrimeListFragment();

    }
    @LayoutRes
    protected int getLayoutResId()
    {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container) == null)
        {
            Intent intent = CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(intent);
        }
        else
        {
            Fragment newdetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,newdetail).commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}

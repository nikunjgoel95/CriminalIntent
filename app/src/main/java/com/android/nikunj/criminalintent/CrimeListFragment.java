package com.android.nikunj.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nikunjgoel on 13-06-2017.
 */

public class CrimeListFragment extends Fragment {
    private List<Crime> mCrimes;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private FrameLayout mnocrimescontainer;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks =(Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks =null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mnocrimescontainer = (FrameLayout) view.findViewById(R.id.no_crimes_container);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycle_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState!=null)
        {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                //Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                //startActivity(intent);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;

            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateSubtitle()
    {
        CrimeLab crimeLab =CrimeLab.get(getActivity());
        int Crimecount = crimeLab.getCrimes().size();
        //Toast.makeText(getContext(),"Crime Count is "+Crimecount,Toast.LENGTH_SHORT).show();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,Crimecount);
        //Toast.makeText(getContext(),subtitle+Crimecount,Toast.LENGTH_SHORT).show();
        subtitle = subtitle+" ";
        subtitle=subtitle+Crimecount;
        if(!mSubtitleVisible)
        {
            subtitle=null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
        displayCrimeContainer();
    }

    private void displayCrimeContainer() {
        if(mAdapter.getItemCount()==0)
        {
            mnocrimescontainer.setVisibility(View.VISIBLE);
        }
        else
        {
            mnocrimescontainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible)
        {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else
        {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {
        public CrimeAdapter(List<Crime> crimes)
        {
            mCrimes = crimes;
        }
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes)
        {
            mCrimes = crimes;
        }
    }
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder (View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView=(TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView=(TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox=(CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }
        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
        @Override
        public void onClick(View view)
        {
            //Toast.makeText(getActivity(),mCrime.getTitle()+" Clicked!",Toast.LENGTH_SHORT).show();
            //Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            //startActivity(intent);
            mCallbacks.onCrimeSelected(mCrime);
        }
    }
}

package com.android.nikunj.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.android.nikunj.criminalintent.database.CrimeBaseHelper;
import com.android.nikunj.criminalintent.database.CrimeCursorWrapper;
import com.android.nikunj.criminalintent.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nikunjgoel on 13-06-2017.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context)
    {
        if(sCrimeLab==null)
        {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab (Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        //mCrimes = new ArrayList<Crime>();
    }
    public void addCrime(Crime c)
    {
        //mCrimes.add(c);
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);
    }
    public List<Crime> getCrimes()
    {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);
        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return crimes;
        //return new ArrayList<>();
        //return mCrimes;
    }
    public Crime getCrime(UUID id)
    {
        /*for(Crime crime:mCrimes)
        {
            if(crime.getId().equals(id))
            {
                return crime;
            }
        }*/
        CrimeCursorWrapper cursor = queryCrimes(CrimeDbSchema.Cols.UUID+"=?",new String[] {id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime)
    {
        String uuidstring = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values, CrimeDbSchema.Cols.UUID + "= ?",new String[]{uuidstring});
    }
    private static ContentValues getContentValues(Crime crime)
    {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.Cols.UUID,crime.getId().toString());
        values.put(CrimeDbSchema.Cols.TITLE,crime.getTitle());
        values.put(CrimeDbSchema.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeDbSchema.Cols.SOLVED,crime.isSolved()?1:0);
        values.put(CrimeDbSchema.Cols.SUSPECT,crime.getSuspect());
        return values;
    }
    public void removeCrime(Crime crime) {
        //mCrimes.remove(crime);
        String uuidstring = crime.getId().toString();
        //ContentValues values = getContentValues(crime);

        mDatabase.delete(CrimeDbSchema.CrimeTable.NAME,CrimeDbSchema.Cols.UUID+"=?",new String[] {uuidstring});
    }

    private CrimeCursorWrapper queryCrimes (String whereclause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(CrimeDbSchema.CrimeTable.NAME,null,whereclause,whereArgs,null,null,null);
        //return cursor;
        return new CrimeCursorWrapper(cursor);
    }
}

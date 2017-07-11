package com.android.nikunj.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.android.nikunj.criminalintent.Crime;

import java.util.Date;
import java.util.UUID;

/**
 * Created by nikunjgoel on 05-07-2017.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Crime getCrime()
    {
        String uuidString = getString(getColumnIndex(CrimeDbSchema.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.Cols.DATE));
        int issolved = getInt(getColumnIndex(CrimeDbSchema.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeDbSchema.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(issolved!=0);
        crime.setSuspect(suspect);

        return crime;
    }
}

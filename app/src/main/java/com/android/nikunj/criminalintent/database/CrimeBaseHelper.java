package com.android.nikunj.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.nikunj.criminalintent.database.CrimeDbSchema.*;

import static android.R.attr.name;
import static android.R.attr.version;

/**
 * Created by nikunjgoel on 05-07-2017.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION= 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ CrimeTable.NAME+"("+"_id integer primary key autoincrement,"+Cols.UUID+","+Cols.TITLE+","+Cols.DATE+","+Cols.SOLVED+","+Cols.SUSPECT+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

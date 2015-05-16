package com.konst.glass.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DaysTable {

    private final Context mContext;
    final ContentResolver contentResolver;

    public static final String TABLE = "days";

    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_DAY = "day";



    private static final String[] All_COLUMN_TABLE = {
            KEY_ID,
            KEY_DAY};

    public static final String TABLE_CREATE = "create table "
            + TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_DAY + " text );";

    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    public DaysTable(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
    }

    public Uri insertNewEntry() {
        Date date = new Date();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DAY, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date));
        return contentResolver.insert(CONTENT_URI, contentValues);
    }

    public Cursor getAllEntries() {
        return mContext.getContentResolver().query(CONTENT_URI, null, null, null,null);
    }

}

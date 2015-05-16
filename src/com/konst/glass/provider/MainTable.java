package com.konst.glass.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class MainTable {

    private final Context mContext;
    final ContentResolver contentResolver;

    public static final String TABLE = "main";

    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_NAME = "name";
    public static final String KEY_CASH = "cash";



    private static final String[] All_COLUMN_TABLE = {
            KEY_ID,
            KEY_NAME,
            KEY_CASH};

    public static final String TABLE_CREATE = "create table "
            + TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null, "
            + KEY_CASH + " integer );";

    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    public MainTable(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
    }
    public int getCash(int _rowIndex) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null,null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(KEY_CASH));
        }catch (Exception e){
            return 0;
        }

    }

    public int getEntry(String name){
        Cursor cursor = contentResolver.query(CONTENT_URI, All_COLUMN_TABLE, KEY_NAME + "='" + name + "'", null, null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(KEY_ID));
        }catch (Exception e){
            return 0;
        }
    }

    public boolean updateCash(int _rowIndex, int cash) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CASH, cash);
            return contentResolver.update(uri, values, null, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor getAllEntries() {
        return mContext.getContentResolver().query(CONTENT_URI, null, null, null,null);
    }



}

package com.konst.glass.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * таблица вклада main в площадку
 *
 * @author Kostya
 */
public class LinkUnitTable {

    private final Context mContext;
    final ContentResolver contentResolver;

    public static final String TABLE = "linkUnit";

    public static final String KEY_ID = BaseColumns._ID;
    /**
     * Индекс main.
     */
    public static final String KEY_MAIN_ID = "mainId";
    /**
     * Индекс unit
     */
    public static final String KEY_UNIT_ID = "unitId";
    /**
     * Сумма денег
     */
    public static final String KEY_CASH = "cash";


    private static final String[] All_COLUMN_TABLE = {
            KEY_ID,
            KEY_MAIN_ID,
            KEY_UNIT_ID,
            KEY_CASH};

    public static final String TABLE_CREATE = "create table "
            + TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_MAIN_ID + " integer , "
            + KEY_UNIT_ID + " integer , "
            + KEY_CASH + " integer );";

    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    public LinkUnitTable(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
    }

    public Cursor getAllEntries() {
        return mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    public Uri insertNewEntry(int unitId, int cash) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_UNIT_ID, unitId);
        contentValues.put(KEY_CASH, cash);
        return contentResolver.insert(CONTENT_URI, contentValues);
    }

    public Uri insertNewEntry(int unitId, ContentValues values) {
        values.put(KEY_UNIT_ID, unitId);
        return contentResolver.insert(CONTENT_URI, values);
    }

    public boolean updateEntry(int _rowIndex, ContentValues values) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        try {
            return contentResolver.update(uri, values, null, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

}

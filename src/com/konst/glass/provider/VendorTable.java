package com.konst.glass.provider;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class VendorTable {

    private final Context mContext;
    final ContentResolver contentResolver;

    public static final String TABLE = "vendor";

    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_CONTACT_ID = "contactId";
    public static final String KEY_NAME = "name";
    public static final String KEY_CASH = "cash";


    private static final String[] All_COLUMN_TABLE = {
            KEY_ID,
            KEY_CONTACT_ID,
            KEY_NAME,
            KEY_CASH};

    public static final String TABLE_CREATE = "create table "
            + TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_CONTACT_ID + " integer , "
            + KEY_NAME + " text , "
            + KEY_CASH + " integer );";

    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    public VendorTable(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
    }

    public Uri insertNewEntry() {
        ContentValues contentValues = new ContentValues();
        contentValues.putNull(KEY_NAME);
        return contentResolver.insert(CONTENT_URI, contentValues);
    }

    public Uri insertNewEntry(ContentValues values) {
        return contentResolver.insert(CONTENT_URI, values);
    }

    public Cursor getAllEntries() {
        return mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

    public Cursor getItem(Uri uri) {
        return contentResolver.query(uri, null, null, null, null);
    }

    public Cursor getItem(int _rowIndex) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        return contentResolver.query(uri, null, null, null, null);
    }

    public ContentValues getEntry(int _rowIndex) {
        Cursor cursor = getItem(_rowIndex);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        Map<String, ContentValues> map = mQueryMap.getRows();
        return map.get(String.valueOf(_rowIndex));
    }

    public boolean updateEntry(int _rowIndex, ContentValues values) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        try {
            return contentResolver.update(uri, values, null, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateEntry(Uri uri, ContentValues values) {
        try {
            return contentResolver.update(uri, values, null, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getKeyName(int _rowIndex) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        try {
            Cursor cursor = getItem(uri);
            ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
            ContentValues values = mQueryMap.getValues(uri.getLastPathSegment());
            return values.getAsString(KEY_NAME);
        } catch (Exception e) {
            return "null";
        }
    }

}

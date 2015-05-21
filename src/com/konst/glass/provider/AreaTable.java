package com.konst.glass.provider;

import android.content.ContentQueryMap;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Таблица областей городоа.
 *  @author Kostya
 */
public class AreaTable {

    private final Context mContext;
    final ContentResolver contentResolver;

    public static final String TABLE = "area";

    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_AREA = "area";



    private static final String[] All_COLUMN_TABLE = {
            KEY_ID,
            KEY_AREA};

    public static final String TABLE_CREATE = "create table "
            + TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_AREA + " text );";

    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    public AreaTable(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
    }


    public Cursor getAllEntries() {
        return contentResolver.query(CONTENT_URI, null, null, null,null);
    }

}

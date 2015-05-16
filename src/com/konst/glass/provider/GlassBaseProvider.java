package com.konst.glass.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/*
 * Created with IntelliJ IDEA.
 * User: Kostya
 * Date: 08.12.13
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class GlassBaseProvider extends ContentProvider {

    private static final String DATABASE_NAME = "glass.db";
    private static final int DATABASE_VERSION = 1;
    static final String AUTHORITY = "com.konst.glass.glass";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    private static final int ALL_ROWS = 1;
    private static final int SINGLE_ROWS = 2;


    private enum TABLE_LIST {
        CITY_LIST,
        CITY_ID,
        AREA_LIST,
        AREA_ID,
        UNIT_LIST,
        UNIT_ID,
        MAIN_LIST,
        MAIN_ID,
        LINK_UNIT_LIST,
        LINK_UNIT_ID,
        DAYS_LIST,
        DAYS_ID,
        VENDOR_LIST,
        VENDOR_ID
    }

    private static final UriMatcher uriMatcher;
    private SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CityTable.TABLE, TABLE_LIST.CITY_LIST.ordinal());
        uriMatcher.addURI(AUTHORITY, CityTable.TABLE + "/#", TABLE_LIST.CITY_ID.ordinal());
        uriMatcher.addURI(AUTHORITY, AreaTable.TABLE, TABLE_LIST.AREA_LIST.ordinal());
        uriMatcher.addURI(AUTHORITY, AreaTable.TABLE + "/#", TABLE_LIST.AREA_ID.ordinal());
        uriMatcher.addURI(AUTHORITY, UnitTable.TABLE, TABLE_LIST.UNIT_LIST.ordinal());
        uriMatcher.addURI(AUTHORITY, UnitTable.TABLE + "/#", TABLE_LIST.UNIT_ID.ordinal());
        uriMatcher.addURI(AUTHORITY, MainTable.TABLE, TABLE_LIST.MAIN_LIST.ordinal());
        uriMatcher.addURI(AUTHORITY, MainTable.TABLE + "/#", TABLE_LIST.MAIN_ID.ordinal());
        uriMatcher.addURI(AUTHORITY, LinkUnitTable.TABLE, TABLE_LIST.LINK_UNIT_LIST.ordinal());
        uriMatcher.addURI(AUTHORITY, LinkUnitTable.TABLE + "/#", TABLE_LIST.LINK_UNIT_ID.ordinal());
        uriMatcher.addURI(AUTHORITY, DaysTable.TABLE, TABLE_LIST.DAYS_LIST.ordinal());
        uriMatcher.addURI(AUTHORITY, DaysTable.TABLE + "/#", TABLE_LIST.DAYS_ID.ordinal());
        uriMatcher.addURI(AUTHORITY, VendorTable.TABLE, TABLE_LIST.VENDOR_LIST.ordinal());
        uriMatcher.addURI(AUTHORITY, VendorTable.TABLE + "/#", TABLE_LIST.VENDOR_ID.ordinal());
    }

    /*public void vacuum(){
        db.execSQL("VACUUM");
    }*/

    private String getTable(Uri uri) {
        switch (TABLE_LIST.values()[uriMatcher.match(uri)]) {
            case CITY_LIST:
            case CITY_ID:
                return CityTable.TABLE; // return
            case AREA_LIST:
            case AREA_ID:
                return AreaTable.TABLE; // return
            case UNIT_LIST:
            case UNIT_ID:
                return UnitTable.TABLE; // return
            case MAIN_LIST:
            case MAIN_ID:
                return MainTable.TABLE; // return
            case LINK_UNIT_LIST:
            case LINK_UNIT_ID:
                return LinkUnitTable.TABLE; // return
            case DAYS_LIST:
            case DAYS_ID:
                return DaysTable.TABLE; // return
            case VENDOR_LIST:
            case VENDOR_ID:
                return VendorTable.TABLE; // return
            /** PROVIDE A DEFAULT CASE HERE **/
            default:
                // If the URI doesn't match any of the known patterns, throw an exception.
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        //db = dbHelper.getWritableDatabase();
        db = dbHelper.getReadableDatabase();
        if (db != null) {
            db.setLockingEnabled(false);
        }
        return true;
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (TABLE_LIST.values()[uriMatcher.match(uri)]) {
            case CITY_LIST: // общий Uri
                queryBuilder.setTables(CityTable.TABLE);
            break;
            case CITY_ID: // Uri с ID
                queryBuilder.setTables(CityTable.TABLE);
                queryBuilder.appendWhere(BaseColumns._ID + '=' + uri.getLastPathSegment());
            break;
            case AREA_LIST: // общий Uri
                queryBuilder.setTables(AreaTable.TABLE);
                break;
            case AREA_ID: // Uri с ID
                queryBuilder.setTables(AreaTable.TABLE);
                queryBuilder.appendWhere(BaseColumns._ID + '=' + uri.getLastPathSegment());
                break;
            case UNIT_LIST: // общий Uri
                queryBuilder.setTables(UnitTable.TABLE);
                break;
            case UNIT_ID: // Uri с ID
                queryBuilder.setTables(UnitTable.TABLE);
                queryBuilder.appendWhere(BaseColumns._ID + '=' + uri.getLastPathSegment());
                break;
            case MAIN_LIST: // общий Uri
                queryBuilder.setTables(MainTable.TABLE);
                break;
            case MAIN_ID: // Uri с ID
                queryBuilder.setTables(MainTable.TABLE);
                queryBuilder.appendWhere(BaseColumns._ID + '=' + uri.getLastPathSegment());
                break;
            case LINK_UNIT_LIST: // общий Uri
                queryBuilder.setTables(LinkUnitTable.TABLE);
                break;
            case LINK_UNIT_ID: // Uri с ID
                queryBuilder.setTables(LinkUnitTable.TABLE);
                queryBuilder.appendWhere(BaseColumns._ID + '=' + uri.getLastPathSegment());
                break;
            case DAYS_LIST: // общий Uri
                queryBuilder.setTables(DaysTable.TABLE);
                break;
            case DAYS_ID: // Uri с ID
                queryBuilder.setTables(DaysTable.TABLE);
                queryBuilder.appendWhere(BaseColumns._ID + '=' + uri.getLastPathSegment());
                break;
            case VENDOR_LIST: // общий Uri
                queryBuilder.setTables(VendorTable.TABLE);
                break;
            case VENDOR_ID: // Uri с ID
                queryBuilder.setTables(VendorTable.TABLE);
                queryBuilder.appendWhere(BaseColumns._ID + '=' + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sort);
        if (cursor == null) {
            return null;
        }
        Context context = getContext();
        if (context != null) {
            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver != null) {
                cursor.setNotificationUri(contentResolver, uri);
            }
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS:
                return "vnd.android.cursor.dir/vnd.";
            case SINGLE_ROWS:
                return "vnd.android.cursor.item/vnd.";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        long rowID = db.insert(getTable(uri), null, contentValues);
        if (rowID > 0L) {
            Uri resultUri = ContentUris.withAppendedId(uri, rowID);
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            }
        }
        throw new SQLiteException("Ошибка добавления записи " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArg) {
        int delCount;
        String id;

        switch (TABLE_LIST.values()[uriMatcher.match(uri)]) {
            case CITY_LIST: // общий Uri
                delCount = db.delete(CityTable.TABLE, where, whereArg);
                break;
            case CITY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                delCount = db.delete(CityTable.TABLE, where, whereArg);
                break;
            case AREA_LIST: // общий Uri
                delCount = db.delete(AreaTable.TABLE, where, whereArg);
                break;
            case AREA_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                delCount = db.delete(AreaTable.TABLE, where, whereArg);
                break;
            case UNIT_LIST: // общий Uri
                delCount = db.delete(UnitTable.TABLE, where, whereArg);
                break;
            case UNIT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                delCount = db.delete(UnitTable.TABLE, where, whereArg);
                break;
            case MAIN_LIST: // общий Uri
                delCount = db.delete(MainTable.TABLE, where, whereArg);
                break;
            case MAIN_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                delCount = db.delete(MainTable.TABLE, where, whereArg);
                break;
            case LINK_UNIT_LIST: // общий Uri
                delCount = db.delete(LinkUnitTable.TABLE, where, whereArg);
                break;
            case LINK_UNIT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                delCount = db.delete(LinkUnitTable.TABLE, where, whereArg);
                break;
            case DAYS_LIST: // общий Uri
                delCount = db.delete(DaysTable.TABLE, where, whereArg);
                break;
            case DAYS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                delCount = db.delete(DaysTable.TABLE, where, whereArg);
                break;
            case VENDOR_LIST: // общий Uri
                delCount = db.delete(VendorTable.TABLE, where, whereArg);
                break;
            case VENDOR_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                delCount = db.delete(VendorTable.TABLE, where, whereArg);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db.execSQL("VACUUM");
        if (delCount > 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String where, String[] whereArg) {
        int updateCount;
        String id;
        switch (TABLE_LIST.values()[uriMatcher.match(uri)]) {
            case CITY_LIST: // общий Uri
                updateCount = db.update(CityTable.TABLE, contentValues, where, whereArg);
            break;
            case CITY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                updateCount = db.update(CityTable.TABLE, contentValues, where, whereArg);
            break;
            case AREA_LIST: // общий Uri
                updateCount = db.update(AreaTable.TABLE, contentValues, where, whereArg);
                break;
            case AREA_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                updateCount = db.update(AreaTable.TABLE, contentValues, where, whereArg);
                break;
            case UNIT_LIST: // общий Uri
                updateCount = db.update(UnitTable.TABLE, contentValues, where, whereArg);
                break;
            case UNIT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                updateCount = db.update(UnitTable.TABLE, contentValues, where, whereArg);
                break;
            case MAIN_LIST: // общий Uri
                updateCount = db.update(MainTable.TABLE, contentValues, where, whereArg);
                break;
            case MAIN_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                updateCount = db.update(MainTable.TABLE, contentValues, where, whereArg);
                break;
            case LINK_UNIT_LIST: // общий Uri
                updateCount = db.update(LinkUnitTable.TABLE, contentValues, where, whereArg);
                break;
            case LINK_UNIT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                updateCount = db.update(LinkUnitTable.TABLE, contentValues, where, whereArg);
                break;
            case DAYS_LIST: // общий Uri
                updateCount = db.update(DaysTable.TABLE, contentValues, where, whereArg);
                break;
            case DAYS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                updateCount = db.update(DaysTable.TABLE, contentValues, where, whereArg);
                break;
            case VENDOR_LIST: // общий Uri
                updateCount = db.update(VendorTable.TABLE, contentValues, where, whereArg);
                break;
            case VENDOR_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(where)) {
                    where = BaseColumns._ID + " = " + id;
                } else {
                    where = where + " AND " + BaseColumns._ID + " = " + id;
                }
                updateCount = db.update(VendorTable.TABLE, contentValues, where, whereArg);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        if (updateCount > 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        return updateCount;
    }

    private class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CityTable.TABLE_CREATE);
            db.execSQL(AreaTable.TABLE_CREATE);
            db.execSQL(UnitTable.TABLE_CREATE);
            db.execSQL(MainTable.TABLE_CREATE);
            db.execSQL(LinkUnitTable.TABLE_CREATE);
            db.execSQL(DaysTable.TABLE_CREATE);
            db.execSQL(VendorTable.TABLE_CREATE);
            //Add default record to my table
            new CityTable(getContext()).addCityFromFile(db, "ukraine.txt");
            ContentValues contentValues = new ContentValues();
            contentValues.put(MainTable.KEY_NAME, "Glass");
            contentValues.put(MainTable.KEY_CASH, 100000);
            db.insert(MainTable.TABLE,null,contentValues);
            /*-----------------------------------------------*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(DROP_TABLE_IF_EXISTS + CityTable.TABLE);
            db.execSQL(DROP_TABLE_IF_EXISTS + AreaTable.TABLE);
            db.execSQL(DROP_TABLE_IF_EXISTS + UnitTable.TABLE);
            db.execSQL(DROP_TABLE_IF_EXISTS + MainTable.TABLE);
            db.execSQL(DROP_TABLE_IF_EXISTS + LinkUnitTable.TABLE);
            db.execSQL(DROP_TABLE_IF_EXISTS + DaysTable.TABLE);
            db.execSQL(DROP_TABLE_IF_EXISTS + VendorTable.TABLE);
            onCreate(db);
        }
    }
}

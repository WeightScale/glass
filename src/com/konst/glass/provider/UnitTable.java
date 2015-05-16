package com.konst.glass.provider;

import android.content.*;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UnitTable {

    private final Context mContext;
    final ContentResolver contentResolver;

    public static final String TABLE = "unit";

    public static final String KEY_ID           = BaseColumns._ID;
    public static final String KEY_DATE         = "date";           //дата создания площадки
    public static final String KEY_CITY_ID      = "cityId";         //индекс записи таблица city
    public static final String KEY_VENDOR_ID    = "vendorId";       //индекс записи таблица vendor
    //public static final String KEY_LINK_UNIT_ID = "linkUnitId";     //индекс записи таблица linkUnit
    public static final String KEY_DAYS         = "days";      //количество дней жизни площадки
    public static final String KEY_MONTHS       = "months";      //количество месяцев жизни площадки
    public static final String KEY_YEARS        = "years";      //количество дней жизни площадки
    public static final String KEY_GLASS        = "glass";       //количество стекла на площадке
    public static final String KEY_CASH         = "cash";           //сумма денег на площадке
    //public static final String KEY_PRICE        = "price";          //цена покупки стекла
    //public static final String KEY_PRICE_PACT   = "pricePact";      //цена продажи стекла
    public static final String KEY_RATE         = "rate";           //норма отгрузки в килограмах
    public static final String KEY_RATE_PRICE   = "ratePrice";      //расходы на отгрузку
    public static final String KEY_EXES         = "exes";           //общии расходы в месяц

    public static final String KEY_DATA1        = "data1";          //прочии данные
    public static final String KEY_DATA2        = "data2";          //прочии данные

    private static final String[] All_COLUMN_TABLE = {
            KEY_ID,
            KEY_DATE,
            KEY_CITY_ID,
            KEY_VENDOR_ID,
            //KEY_LINK_UNIT_ID,
            KEY_DAYS,
            KEY_MONTHS,
            KEY_YEARS,
            KEY_GLASS,
            KEY_CASH,
            //KEY_PRICE,
            //KEY_PRICE_PACT,
            KEY_RATE,
            KEY_RATE_PRICE,
            KEY_EXES,
            KEY_DATA1,
            KEY_DATA2};

    public static final String TABLE_CREATE = "create table "
            + TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_DATE + " text,"
            + KEY_CITY_ID + " integer,"
            + KEY_VENDOR_ID + " integer,"
            //+ KEY_LINK_UNIT_ID + " integer,"
            + KEY_DAYS + " integer,"
            + KEY_MONTHS + " integer,"
            + KEY_YEARS + " integer,"
            + KEY_GLASS + " real,"
            + KEY_CASH + " integer,"
            //+ KEY_PRICE + " integer,"
            //+ KEY_PRICE_PACT + " integer,"
            + KEY_RATE + " integer,"
            + KEY_RATE_PRICE + " integer,"
            + KEY_EXES + " integer,"
            + KEY_DATA1 + " integer,"
            + KEY_DATA2 + " text );";


    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    public UnitTable(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
    }

    public Uri insertNewEntry() {
        Date date = new Date();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date));
        return contentResolver.insert(CONTENT_URI, contentValues);
    }
    public Uri insertNewEntry(ContentValues values) {
        Date date = new Date();
        values.put(KEY_DATE, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date));
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

    public boolean updateEntry(Uri uri, ContentValues values) {
        try {
            return contentResolver.update(uri, values, null, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor getAllEntries() {
        return contentResolver.query(CONTENT_URI, All_COLUMN_TABLE, null, null, null);
    }

    public Map<String, ContentValues> getEntriesToCity(int cityId) {
        Cursor cursor = contentResolver.query(CONTENT_URI, All_COLUMN_TABLE, KEY_CITY_ID + " = " + cityId, null, null);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        return mQueryMap.getRows();
    }

    public Cursor getItem(Uri uri) {
            return contentResolver.query(uri, null, null, null, null);
    }


}


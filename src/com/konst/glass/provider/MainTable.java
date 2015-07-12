package com.konst.glass.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Map;

/**
 * The type Main table.
 */
public class MainTable {

    private final Context mContext;
    /**
     * The Content resolver.
     */
    final ContentResolver contentResolver;

    /**
     * Имя таблици.
     */
    public static final String TABLE = "main";

    /**
     * The constant KEY_ID.
     */
    public static final String KEY_ID = BaseColumns._ID;
    /**
     * Имя фирмы
     */
    public static final String KEY_NAME = "name";
    /**
     * Сумма денег.
     */
    public static final String KEY_CASH = "cash";
    /**
     * Количество стекла отгруженого
     */
    public static final String KEY_GLASS = "glass";

    private static final String[] All_COLUMN_TABLE = {
            KEY_ID,
            KEY_NAME,
            KEY_CASH,
            KEY_GLASS};

    /**
     * Константа создать таблицу.
     */
    public static final String TABLE_CREATE = "create table "
            + TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_NAME + " text not null, "
            + KEY_CASH + " integer, "
            + KEY_GLASS + " real );";

    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    /**
     * Конструктор нового экземпляра Таблици.
     *
     * @param context the context
     */
    public MainTable(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
    }

    /**
     * Получить сумму денег.
     *
     * @param _rowIndex Индекс записи.
     * @return Сумма денег.
     */
    public int getCash(int _rowIndex) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(KEY_CASH));
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * Получить записи по имени.
     *
     * @param name Имя фирмы
     * @return Индекс записи.
     */
    public int getEntry(String name) {
        Cursor cursor = contentResolver.query(CONTENT_URI, All_COLUMN_TABLE, KEY_NAME + "='" + name + "'", null, null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex(KEY_ID));
        } catch (Exception e) {
            return 0;
        }
    }

    public ContentValues getEntry(int _rowIndex) {
        Cursor cursor = contentResolver.query(CONTENT_URI, null, KEY_ID + " = " + _rowIndex, null, null);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        Map<String, ContentValues> map = mQueryMap.getRows();
        return map.get(String.valueOf(_rowIndex));
    }

    /**
     * Обновить в записи сумму денег.
     *
     * @param _rowIndex Индекс записи.
     * @param cash      Сумма денег.
     * @return true - Запись обновлена.
     */
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

    /**
     * Получить все записи.
     *
     * @return Курсор записей.
     */
    public Cursor getAllEntries() {
        return mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

}

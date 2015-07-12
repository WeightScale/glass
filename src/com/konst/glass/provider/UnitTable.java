package com.konst.glass.provider;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class UnitTable {

    private final Context mContext;
    final ContentResolver contentResolver;

    /**
     * Имя таблици
     */
    public static final String TABLE = "unit";

    public static final String KEY_ID = BaseColumns._ID;
    /**
     * Дата создания площадки.
     */
    public static final String KEY_DATE = "date";
    /**
     * Индекс записи таблица city
     */
    public static final String KEY_CITY_ID = "cityId";
    /**
     * индекс записи таблица vendor
     */
    public static final String KEY_VENDOR_ID = "vendorId";
    //public static final String KEY_LINK_UNIT_ID = "linkUnitId";     //индекс записи таблица linkUnit
    /**
     * Количество дней жизни площадки
     */
    public static final String KEY_DAYS = "days";
    /**
     * Количество месяцев жизни площадки
     */
    public static final String KEY_MONTHS = "months";
    /**
     * Количество дней жизни площадки
     */
    public static final String KEY_YEARS = "years";
    /**
     * Количество стекла на площадке
     */
    public static final String KEY_GLASS = "glass";
    /**
     * Стоимость стекла на площадке
     */
    public static final String KEY_GLASS_CASH = "glassCash";
    /**
     * Сумма денег на площадке
     */
    public static final String KEY_CASH = "cash";
    /** Расходы на площадке.
     *  Включается стоимость стекла, расходы в месяц  */
    //public static final String KEY_SPENDING     = "spending";
    /**
     * Норма отгрузки в килограмах
     */
    public static final String KEY_RATE = "rate";
    /**
     * Расходы на отгрузку
     */
    public static final String KEY_RATE_PRICE = "ratePrice";
    /**
     * Общии расходы в месяц
     */
    public static final String KEY_EXES = "exes";
    /**
     * Вклад денег в площадку Main
     */
    public static final String KEY_DEPOSIT_MAIN = "depositMain";
    /**
     * Вклад денег в площадку Unit
     */
    public static final String KEY_DEPOSIT_UNIT = "depositUnit";
    /**
     * Прочии данные
     */
    public static final String KEY_DATA1 = "data1";
    /**
     * Прочии данные
     */
    public static final String KEY_DATA2 = "data2";

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
            KEY_GLASS_CASH,
            KEY_CASH,
            KEY_RATE,
            KEY_RATE_PRICE,
            KEY_EXES,
            KEY_DEPOSIT_MAIN,
            KEY_DEPOSIT_UNIT,
            KEY_DATA1,
            KEY_DATA2};

    /**
     * Создать таблицу.
     */
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
            + KEY_GLASS_CASH + " integer,"
            + KEY_CASH + " integer,"
            //+ KEY_PRICE + " integer,"
            //+ KEY_PRICE_PACT + " integer,"
            + KEY_RATE + " integer,"
            + KEY_RATE_PRICE + " integer,"
            + KEY_EXES + " integer,"
            + KEY_DEPOSIT_MAIN + " integer,"
            + KEY_DEPOSIT_UNIT + " integer,"
            + KEY_DATA1 + " integer,"
            + KEY_DATA2 + " text );";


    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    /**
     * Конструктор таблици площадки.
     *
     * @param context Контекст.
     */
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

    /**
     * Вставить новую запись в таблицу.
     *
     * @param values Значения для добавления.
     * @return Uri добавленой записи.
     */
    public Uri insertNewEntry(ContentValues values) {
        Date date = new Date();
        values.put(KEY_DATE, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date));
        return contentResolver.insert(CONTENT_URI, values);
    }

    /**
     * Обновить запись в таблице.
     *
     * @param _rowIndex Индекс записи.
     * @param values    Значения для обновления
     * @return true - Запись таблици обновлена.
     */
    public boolean updateEntry(int _rowIndex, ContentValues values) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        try {
            return contentResolver.update(uri, values, null, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Обновить запись в таблице.
     *
     * @param uri    Uri записи.
     * @param values Значения для обновления
     * @return true - Запись таблици обновлена.
     */
    public boolean updateEntry(Uri uri, ContentValues values) {
        try {
            return contentResolver.update(uri, values, null, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получить все записи.
     *
     * @return Курсор с записями.
     */
    public Cursor getAllEntries() {
        return contentResolver.query(CONTENT_URI, null, null, null, null);
    }

    /**
     * Получить записи по отбору города.
     *
     * @param cityId Индекс города.
     * @return Значения в Map контейнере.
     */
    public Map<String, ContentValues> getEntriesToCity(int cityId) {
        Cursor cursor = contentResolver.query(CONTENT_URI, null, KEY_CITY_ID + " = " + cityId, null, null);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        return mQueryMap.getRows();
    }

    /**
     * Получить запись.
     *
     * @param uri Uri записи.
     * @return Курсор записи.
     */
    public Cursor getItem(Uri uri) {
        return contentResolver.query(uri, null, null, null, null);
    }


}


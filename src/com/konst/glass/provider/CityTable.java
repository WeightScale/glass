package com.konst.glass.provider;

import android.content.*;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import org.apache.http.client.utils.CloneUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class CityTable {

    private final Context mContext;
    final ContentResolver contentResolver;
    public static int day;

    public static final String TABLE = "city";

    public static final String KEY_ID           = BaseColumns._ID;
    public static final String KEY_CITY         = "city";               //город
    public static final String KEY_AREA         = "area";               //область
    public static final String KEY_UNIT_QTY     = "unitQty";          //количество созданых площадок
    public static final String KEY_POPULATION   = "population";         //население
    public static final String KEY_K_POPULATION = "kPopulation";        //норма прихода в колограмах на человека в месяц
    public static final String KEY_G_REAL       = "gReal";              //кол во реального прихода в городе кг
    public static final String KEY_AD_USED      = "adUsed";             //сумма расхода на рекламу в месяц
    public static final String KEY_AD_MAX       = "adMax";              //сумма расхода на рекламу необходимая для максимального эфекта в месяц
    public static final String KEY_PRICE_DOWN   = "priceDown";          //цена закупки стекла за тонну
    public static final String KEY_PRICE_UP     = "priceUP";            //цена продажи стекла за тонну
    public static final String KEY_K_SERVICE    = "kService";          //коэфициент сервиса (логистика удобство прочее)
    public static final String KEY_P_SERVICE    = "pService";          //сумма сервиса необходимая для максимального эфекта в месяц


    private static final String[] All_COLUMN_TABLE = {
            KEY_ID,
            KEY_CITY,
            KEY_AREA,
            KEY_UNIT_QTY,
            KEY_POPULATION,
            KEY_K_POPULATION,
            KEY_G_REAL,
            KEY_AD_USED,
            KEY_AD_MAX,
            KEY_PRICE_DOWN,
            KEY_PRICE_UP,
            KEY_K_SERVICE,
            KEY_P_SERVICE};

    public static final String TABLE_CREATE = "create table "
            + TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_CITY + " text,"
            + KEY_AREA + " text,"
            + KEY_UNIT_QTY + " integer,"
            + KEY_POPULATION + " integer,"
            + KEY_K_POPULATION + " float,"
            + KEY_G_REAL + " integer,"
            + KEY_AD_USED + " integer,"
            + KEY_AD_MAX + " integer,"
            + KEY_PRICE_DOWN + " integer,"
            + KEY_PRICE_UP + " integer,"
            + KEY_K_SERVICE + " float,"
            + KEY_P_SERVICE + " integer );";

    private static final Uri CONTENT_URI = Uri.parse("content://" + GlassBaseProvider.AUTHORITY + '/' + TABLE);

    public CityTable(Context context) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
    }

    public Cursor getValidCity(int population) {
        return contentResolver.query(CONTENT_URI, All_COLUMN_TABLE, KEY_POPULATION + "> "+ population + " or " + KEY_POPULATION + "= " + population, null, KEY_POPULATION + " DESC ");
    }

    public Map<String, ContentValues>  getValuesIfUnits(){
        Cursor cursor = contentResolver.query(CONTENT_URI, All_COLUMN_TABLE, KEY_UNIT_QTY + " > 0 ", null, KEY_POPULATION + " DESC ");
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        return mQueryMap.getRows();
    }

    public ContentValues getEntry(int _rowIndex){
        Cursor cursor = contentResolver.query(CONTENT_URI, All_COLUMN_TABLE, null, null, null);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        Map<String, ContentValues> map = mQueryMap.getRows();
        return map.get(String.valueOf(_rowIndex));
    }

    public Cursor getItem(int _rowIndex) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        return contentResolver.query(uri, null, null, null, null);
    }

    public Cursor getGroupEntries(String name) {
        return mContext.getContentResolver().query(CONTENT_URI, new String[]{KEY_ID,KEY_AREA}, name+" IS NOT NULL) GROUP BY ("+name, null,null);
    }

    public ContentValues updateGReal(Map.Entry<String, ContentValues> entry){
        ContentValues values = entry.getValue();
        float kAd = (float)values.getAsInteger(KEY_AD_USED) / (float)values.getAsInteger(KEY_AD_MAX);           //коэ. реклама
        float kPrice = (float)values.getAsInteger(KEY_PRICE_DOWN) / (float)values.getAsInteger(KEY_PRICE_UP);   //коэф. покупки
        int glassReal = (int)(values.getAsInteger(KEY_POPULATION) * values.getAsFloat(KEY_K_POPULATION));
        glassReal *= kAd;
        glassReal *= (kPrice + values.getAsFloat(KEY_K_SERVICE))/2;
        //ContentValues gReal = new ContentValues();
        values.put(KEY_G_REAL, glassReal);
        if (updateEntry(Integer.valueOf(entry.getKey()),values))
            return values;
        return null;
    }

    public boolean updateEntry(int _rowIndex, ContentValues values) {
        Uri uri = ContentUris.withAppendedId(CONTENT_URI, _rowIndex);
        try {
            return contentResolver.update(uri, values, null, null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void addCityFromFile(SQLiteDatabase db, String file){
        AssetManager manager = mContext.getAssets();
        InputStream input = null;
        String cityLine = new String();
        try {
            input = manager.open(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String[] columnNames = br.readLine().split("\t", -1);
            HashMap<Integer, ContentValues> city = new HashMap<Integer, ContentValues>();
            HashMap<String, Long> area = new HashMap<String, Long>();
            while ((cityLine = br.readLine()) != null){
                ContentValues contentValues = new ContentValues();
                String[] cityValues = cityLine.split("\t", -1);
                for (int i=0; i < columnNames.length; i++) {
                    if (columnNames[i].equals(KEY_K_POPULATION)){
                        contentValues.put(KEY_K_POPULATION, Float.valueOf(cityValues[i].trim()));
                        continue;
                    }
                    contentValues.put(columnNames[i], cityValues[i]);
                }
                contentValues.put(KEY_UNIT_QTY, 0);
                contentValues.put(KEY_G_REAL, 0);
                contentValues.put(KEY_AD_USED, 0);
                contentValues.put(KEY_AD_MAX, 0);
                contentValues.put(KEY_PRICE_DOWN, 0);
                contentValues.put(KEY_PRICE_UP, 0);
                contentValues.put(KEY_K_SERVICE, 0.0f);
                contentValues.put(KEY_P_SERVICE, 0);
                db.insert(TABLE, null, contentValues);
            }

        } catch (IOException e) { }
    }

    public void addSystemRow(SQLiteDatabase db) {

        AssetManager manager = mContext.getAssets();
        InputStream input = null;
        String cityLine = new String();
        try {
            input = manager.open("ukraine.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String[] columnNames = br.readLine().split("\t", -1);
            HashMap<Integer, ContentValues> city = new HashMap<Integer, ContentValues>();
            HashMap<String, Long> area = new HashMap<String, Long>();
            while ((cityLine = br.readLine()) != null){
                ContentValues contentValues = new ContentValues();
                String[] cityValues = cityLine.split("\t", -1);
                for (int i=0; i < columnNames.length; i++){
                    if(columnNames[i].equals(KEY_POPULATION)){
                        contentValues.put(KEY_POPULATION, Integer.valueOf(cityValues[i].trim()));
                        continue;
                    }else if(columnNames[i].equals(KEY_AREA)){
                        if(!area.containsKey(cityValues[i].trim())){
                            ContentValues values = new ContentValues();
                            values.put(KEY_AREA, cityValues[i].trim());
                            long row = db.insert(AreaTable.TABLE, null, values);
                            contentValues.put(KEY_AREA, String.valueOf(row));
                            area.put(cityValues[i].trim(),row);
                            continue;
                        }else {
                            contentValues.put(KEY_AREA, area.get(cityValues[i].trim()));
                            continue;
                        }
                    }
                    contentValues.put(KEY_CITY, cityValues[i]);
                }
                db.insert(TABLE, null, contentValues);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

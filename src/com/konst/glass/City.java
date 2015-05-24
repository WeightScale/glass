package com.konst.glass;

import android.content.ContentValues;
import android.content.Context;
import com.konst.glass.provider.CityTable;


/** Класс для работы с городом
 *
 */
public class City {
    Context context;
    ContentValues valuesCity;
    CityTable cityTable;

    City(Context context, int cityIndex){
        this.context = context;
        cityTable = new CityTable(context);
        valuesCity = cityTable.getEntry(cityIndex);
    }
}

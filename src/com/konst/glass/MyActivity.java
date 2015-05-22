package com.konst.glass;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.konst.glass.provider.CityTable;
import com.konst.glass.provider.DaysTable;
import com.konst.glass.provider.MainTable;
import com.konst.glass.provider.UnitTable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyActivity extends Activity implements Runnable, View.OnClickListener {
    MainTable mainTable;
    Button buttonNewUnit, buttonNewDay, buttonOpenCity;
    int mainId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mainTable = new MainTable(this);
        mainId = mainTable.getEntry("Glass");

        buttonNewUnit = (Button)findViewById(R.id.buttonNewUnit);
        buttonNewUnit.setOnClickListener(this);
        buttonNewUnit = (Button)findViewById(R.id.buttonNewDay);
        buttonNewUnit.setOnClickListener(this);
        buttonOpenCity = (Button)findViewById(R.id.buttonOpenCity);
        buttonOpenCity.setOnClickListener(this);

        /*Cursor cursor = new CityTable(getApplicationContext()).getGroupEntries(CityTable.KEY_AREA);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        Map<String, ContentValues> map = mQueryMap.getRows();
        int i = map.size();*/
    }


    void newDay() {
            Uri uriDay = new DaysTable(getApplicationContext()).insertNewEntry();
            CityTable cityTable = new CityTable(MyActivity.this);
            Map<String, ContentValues> cities = cityTable.getValuesIfUnits();

            for (Map.Entry<String, ContentValues> entryCity : cities.entrySet()){
                ContentValues valuesCity = cityTable.updateGReal(entryCity);
                UnitTable unitTable = new UnitTable(MyActivity.this);
                Map<String, ContentValues> units = unitTable.getEntriesToCity(Integer.valueOf(entryCity.getKey()));
                for (Map.Entry<String,ContentValues> entryUnit : units.entrySet()){

                    Unit unit = new Unit(this,entryUnit, valuesCity, handlerMain);
                    unit.buyGlass();
                    unit.shippingGlass();
                }
            }
            /*Cursor cursor = new UnitTable(getApplicationContext()).getAllEntries();
            ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
            Map<String, ContentValues> mapUnit = mQueryMap.getRows();
            for (Map.Entry<String, ContentValues> entry : mapUnit.entrySet()){
                Unit unit = new Unit(getApplicationContext(),entry);
                unit.buyGlass();
            }*/
            try {  TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {  e.printStackTrace(); }

    }

    @Override
    public void run() {
        while (true){
//            Uri uriDay = new DaysTable(this).insertNewEntry();
//            Cursor cursor = new UnitTable(this).getAllEntries();
//            ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
//            Map<String, ContentValues> mapUnit = mQueryMap.getRows();
//            for (Map.Entry<String, ContentValues> entry : mapUnit.entrySet()){
//                Unit unit = new Unit(getApplicationContext(),entry);
//                unit.buyGlass();
//                unit.saleGlass();
//            }
            try {  TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {  e.printStackTrace(); }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonNewDay:
                newDay();
            break;
            case R.id.buttonNewUnit:
                startActivity(new Intent(getApplicationContext(), UnitActivity.class));
            break;
            case R.id.buttonOpenCity:
                startActivity(new Intent(getApplicationContext(), CityListActivity.class));
                break;
            default:
        }
    }

    HandlerMain handlerMain = new HandlerMain() {

        @Override
        public int messageCash(int cash) {
            int mainCash = mainTable.getCash(mainId);
            //if (mainCash > cash){
                mainCash -= cash;
                mainTable.updateCash(mainId, mainCash);
                return cash;
            //}
            //return 0;
        }

        @Override
        public int messageSippingGlass(float glass, int sum) {
            int mainCash = mainTable.getCash(mainId);
            mainCash -= sum;
            mainTable.updateCash(mainId, mainCash);
            return sum;
        }

        @Override
        public int messageSippingGlass(Unit.Goods goods) {//todo
            int mainCash = mainTable.getCash(mainId);
            mainCash -= goods.getCash();
            mainTable.updateCash(mainId, mainCash);
            return goods.getCash();
        }
    };

    class City{
        String area;
        String city;
        String population;

        City(String city, String area, String population){
            this.city = city;
            this.area = area;
            this.population = population;

        }

        void setArea(String area){
            this.area = area;
        }

        void setCity(String city){
            this.city = city;
        }

        void setPopulation(String population){
            this.population = population;
        }
    }
}

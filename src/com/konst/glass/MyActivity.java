package com.konst.glass;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    TimerDay timerDay;
    Button buttonNewUnit, buttonNewDay, buttonNewMonth, buttonOpenCity;
    int mainId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mainTable = new MainTable(this);
        mainId = mainTable.getEntry("Glass");

        buttonNewUnit = (Button) findViewById(R.id.buttonNewUnit);
        buttonNewUnit.setOnClickListener(this);
        buttonNewDay = (Button) findViewById(R.id.buttonNewDay);
        buttonNewDay.setOnClickListener(this);
        buttonNewMonth = (Button) findViewById(R.id.buttonMonth);
        buttonNewMonth.setOnClickListener(this);
        buttonOpenCity = (Button) findViewById(R.id.buttonOpenCity);
        buttonOpenCity.setOnClickListener(this);

        /** Создаем таймер 30 раз по 1000 милисекунд*/
        timerDay = new TimerDay(30000, 1000);

        /*Cursor cursor = new CityTable(getApplicationContext()).getGroupEntries(CityTable.KEY_AREA);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        Map<String, ContentValues> map = mQueryMap.getRows();
        int i = map.size();*/
    }


    /**
     * Обработка нового дня
     */
    void newDay() {
        /** Создаем новый день*/
        Uri uriDay = new DaysTable(getApplicationContext()).insertNewEntry();
        /** Экземпляр таблици городов*/
        CityTable cityTable = new CityTable(MyActivity.this);
        /** Контейнер городов у которых есть площадки*/
        Map<String, ContentValues> cities = cityTable.getValuesIfUnits();
        /** Цикл обработки городов*/
        for (Map.Entry<String, ContentValues> entryCity : cities.entrySet()) {
            /** Обновляес реальный приход и получаем значения для города*/
            ContentValues valuesCity = cityTable.updateGReal(entryCity);
            /** Экземпляр таблици площадок*/
            UnitTable unitTable = new UnitTable(this);
            /** Контейнер площадок со значениями*/
            Map<String, ContentValues> units = unitTable.getEntriesToCity(Integer.valueOf(entryCity.getKey()));
            /** Цикл обработки площадок*/
            for (Map.Entry<String, ContentValues> entryUnit : units.entrySet()) {
                /** Эземпляр площадки*/
                Unit unit = new Unit(this, entryUnit, valuesCity, handlerMain);
                /** Обработать задание покупки стекла*/
                unit.buyGlass();
                /** Обработать задание продажи стекла*/
                unit.shippingGlass();
                /** Обрабатывам расходы*/
                unit.spending();
                /** Добавляем день*/
                unit.addDay();
            }
        }

        //try {  TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {  e.printStackTrace(); }

    }

    @Override
    public void run() {
        while (true) {
//            Uri uriDay = new DaysTable(this).insertNewEntry();
//            Cursor cursor = new UnitTable(this).getAllEntries();
//            ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
//            Map<String, ContentValues> mapUnit = mQueryMap.getRows();
//            for (Map.Entry<String, ContentValues> entry : mapUnit.entrySet()){
//                Unit unit = new Unit(getApplicationContext(),entry);
//                unit.buyGlass();
//                unit.saleGlass();
//            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonNewDay:
                newDay();
                //timerDay.onStart();
                break;
            case R.id.buttonMonth:
                timerDay.onStart();
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
            int sippingCash = goods.getCash();

            //mainCash -= goods.getCash();
            //mainTable.updateCash(mainId, mainCash);
            return goods.getCash();
        }
    };

    /**
     * Таймер для эмитации дней
     */
    public class TimerDay extends CountDownTimer {
        private boolean start = false;

        /**
         * Экземпляр таймера.
         *
         * @param millisInFuture    Время между интервалами в милисекундах.
         * @param countDownInterval Количество интервалов.
         */
        public TimerDay(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * Запускаем таимер
         */
        public void onStart() {
            start = true;
            start();
        }

        /**
         * Остановка таймера
         */
        @Override
        public void onFinish() {
            start = false;
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        /**
         * Тик через каждые millisInFuture
         *
         * @param millisUntilFinished Сколько осталось времени в милисекундах.
         */
        public void onTick(long millisUntilFinished) {
            /** Вызываем новый день */
            newDay();

        }

        boolean isStart() {
            return start;
        }

    }
}

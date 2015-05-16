package com.konst.glass;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import com.konst.glass.provider.CityTable;
import com.konst.glass.provider.UnitTable;

import java.util.Map;

public class Unit {
    Context mContext;
    HandlerMain mHandler;
    UnitTable unitTable;
    Map.Entry<String, ContentValues> entry;
    int initId;
    ContentValues valuesUnit;
    ContentValues valuesCity;


    Unit(Context context, Map.Entry<String, ContentValues> entry, ContentValues valuesCity, HandlerMain handler){
        this.entry = entry;
        mContext = context;
        mHandler = handler;
        unitTable = new UnitTable(mContext);
        initId = Integer.valueOf(entry.getKey());
        valuesUnit = entry.getValue();
        this.valuesCity = valuesCity;
        int day = valuesUnit.getAsInteger(UnitTable.KEY_DAYS) + 1;
        valuesUnit.put(UnitTable.KEY_DAYS, day);
    }



    private void glassPlus(float glass){ valuesUnit.put(UnitTable.KEY_GLASS, getGlass() + glass); }
    private void glassMinus(float glass){ valuesUnit.put(UnitTable.KEY_GLASS, getGlass() - glass); }
    private float getGlass(){ return valuesUnit.getAsFloat(UnitTable.KEY_GLASS); }
    private void setGlass(float glass){ valuesUnit.put(UnitTable.KEY_GLASS, glass); }

    private int getRate(){ return valuesUnit.getAsInteger(UnitTable.KEY_RATE); }
    private int getRatePrice(){return valuesUnit.getAsInteger(UnitTable.KEY_RATE_PRICE);}

    private void cashPlus(int cash){ valuesUnit.put(UnitTable.KEY_CASH, getCash() + cash); }
    private void cashMinus(int cash){ valuesUnit.put(UnitTable.KEY_CASH, getCash() - cash); }
    private int getCash(){ return valuesUnit.getAsInteger(UnitTable.KEY_CASH); }

    private int getDays(){ return valuesUnit.getAsInteger(UnitTable.KEY_DAYS); }

    /*-------------------------------------------------------------------------------------*/

    private float machGlassDay(){
        final int NOMINAL_GLASS = 300000;
        final int R_DAYS = 60;

        float r = getDays();
        if (r < R_DAYS){
            r /= R_DAYS;
        }else
            r = 1;

        float glassDay = valuesCity.getAsInteger(CityTable.KEY_G_REAL)/valuesCity.getAsInteger(CityTable.KEY_UNIT_QTY);
        if (glassDay > NOMINAL_GLASS)
            glassDay = NOMINAL_GLASS;
        glassDay /= 30;
        return  (glassDay/1000) * r;//переводим в тоны
    }


    public void buyGlass() {

        float glassDay = machGlassDay();
        int cashMinus = (int)(glassDay * valuesCity.getAsInteger(CityTable.KEY_PRICE_DOWN));
        //int cashUnit = valuesUnit.getAsInteger(UnitTable.KEY_CASH);

        if (getCash() >= cashMinus){
            cashMinus(cashMinus);
            glassPlus(glassDay);
        }else {
            cashPlus(mHandler.messageCash(cashMinus));
        }
        update();
    }

    public void shippingGlass(){
        if (getGlass() >= getRate()){
            if (getCash() >= getRatePrice()){
                cashMinus(getRatePrice());
                int sum = getRate() * valuesCity.getAsInteger(CityTable.KEY_PRICE_UP);
                glassMinus(getRate());
                cashPlus(mHandler.messageSippingGlass(getRate(), sum));
            }else{
                cashPlus(mHandler.messageCash(getRatePrice()));
            }
        }
        update();
    }

    private void update(){ unitTable.updateEntry(Integer.valueOf(entry.getKey()), valuesUnit); }

}

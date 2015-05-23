package com.konst.glass;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import com.konst.glass.provider.CityTable;
import com.konst.glass.provider.UnitTable;

import java.util.Map;

/**
 * Площадка по приему стеклобоя.
 */
public class Unit {
    Context mContext;
    HandlerMain mHandler;
    UnitTable unitTable;
    Map.Entry<String, ContentValues> entry;
    int initId;
    ContentValues valuesUnit;
    ContentValues valuesCity;


    /** Конструктор площадки.
     * @param context Контекст.
     * @param entry Значения данных площадки.
     * @param valuesCity Значения данных города.
     * @param handler Обработчик сообщений.  */
    Unit(Context context, Map.Entry<String, ContentValues> entry, ContentValues valuesCity, HandlerMain handler){
        this.entry = entry;
        mContext = context;
        mHandler = handler;
        unitTable = new UnitTable(mContext);
        initId = Integer.valueOf(entry.getKey());
        valuesUnit = entry.getValue();
        this.valuesCity = valuesCity;
        //int day = valuesUnit.getAsInteger(UnitTable.KEY_DAYS) + 1;
        //valuesUnit.put(UnitTable.KEY_DAYS, day);
    }

    /** Получаем колличество стекла в килограммах.
     * @return Количество стекла в килограмах.  */
    private float getGlass(){ return valuesUnit.getAsFloat(UnitTable.KEY_GLASS); }
    /** Получаем сумму денег купленого стекла.
     * @return Сумму денег.  */
    private int getGlassCash(){ return valuesUnit.getAsInteger(UnitTable.KEY_GLASS_CASH); }

    /** Получаем норму отгрузки стекла для площадки.
     * @return Количество в килограмах.  */
    private int getRate(){ return valuesUnit.getAsInteger(UnitTable.KEY_RATE); }

    /** Стоимость отгрузки.
     * @return Стоимость в гривнах.  */
    private int getRatePrice(){return valuesUnit.getAsInteger(UnitTable.KEY_RATE_PRICE);}

    /** Получаем сумму денег на площадке.
     * @return Сумма денег в гривнах.   */
    private int getCash(){ return valuesUnit.getAsInteger(UnitTable.KEY_CASH); }

    /** Получить возраст площадки в днях
     * @return Возраст в днях.   */
    private int getDays(){ return valuesUnit.getAsInteger(UnitTable.KEY_DAYS); }

    /** Получить возраст площадки в месяцах
     * @return Возраст в месяцах.   */
    private int getMonths(){ return valuesUnit.getAsInteger(UnitTable.KEY_MONTHS); }

    /** Получить возраст площадки в годах
     * @return Возраст в годах.   */
    private int getYears(){ return valuesUnit.getAsInteger(UnitTable.KEY_YEARS); }

    /** Получаем индекс площадки.
     * @return Индекс площадки.  */
    private int getId(){ return valuesUnit.getAsInteger(UnitTable.KEY_ID); }

    /** Получить расход по площадке в месяц.
     * @return Расход. */
    private int getExes(){ return valuesUnit.getAsInteger(UnitTable.KEY_EXES); }

    private int getDepositMain(){ return valuesUnit.getAsInteger(UnitTable.KEY_DEPOSIT_MAIN); }

    private int getDepositUnit(){ return valuesUnit.getAsInteger(UnitTable.KEY_DEPOSIT_UNIT); }

    //=================================================================================================
    /** Устанавливаем стекло на площадке.
     * @param glass Колчество стекла в килограмах.  */
    private void setGlass(float glass){ valuesUnit.put(UnitTable.KEY_GLASS, glass); }

    /** Устанавливаем дни.
     * @param days Колчество дней.  */
    private void setDays(int days){ valuesUnit.put(UnitTable.KEY_DAYS, days); }

    /** Устанавливаем месяци.
     * @param months Колчество месяцев.  */
    private void setMonths(int months){ valuesUnit.put(UnitTable.KEY_MONTHS, months); }

    /** Устанавливаем года.
     * @param years Колчество лет.  */
    private void setYears(int years){ valuesUnit.put(UnitTable.KEY_YEARS, years); }

    /** Установить вклад от Main.
     * @param depositMain Вклад */
    private void setDepositMain(int depositMain){ valuesUnit.put(UnitTable.KEY_DEPOSIT_MAIN, depositMain); }

    /** Установить вклад от Unit
     * @param depositUnit Вклад */
    private void setDepositUnit(int depositUnit){ valuesUnit.put(UnitTable.KEY_DEPOSIT_UNIT, depositUnit); }

    //==================================================================================================
    /** Добавляем сумму денег на площадку.
     * @param cash Сумма денег в гривнах.   */
    private void cashPlus(int cash){ valuesUnit.put(UnitTable.KEY_CASH, getCash() + cash); }
    /** Добавляем вклад от Main на площадку.
     * @param cash Сумма денег в гривнах.   */
    private void depositMainPlus(int cash){ valuesUnit.put(UnitTable.KEY_DEPOSIT_MAIN, getDepositMain() + cash); }
    /** Добавляем вклад от Unit на площадку.
     * @param cash Сумма денег в гривнах.   */
    private void depositUnitPlus(int cash){ valuesUnit.put(UnitTable.KEY_DEPOSIT_UNIT, getDepositUnit() + cash); }
    /** Отнимаем сумму денег с площадки.
     * @param cash сумма денег в гривнах.   */
    private void cashMinus(int cash){ valuesUnit.put(UnitTable.KEY_CASH, getCash() - cash); }
    /** Добавить стекло на площадку.
     * @param glass Стекло в килограммах.  */
    private void glassPlus(float glass){ valuesUnit.put(UnitTable.KEY_GLASS, getGlass() + glass); }
    /** Добавить стоимость стекла на площадку.
     * @param cash Сумма денег.  */
    private void glassCashPlus(int cash){ valuesUnit.put(UnitTable.KEY_GLASS_CASH, getGlassCash() + cash); }
    /** Отнимаем стекло с площадки.
     * @param glass Стекло в килограммах. */
    private void glassMinus(float glass){ valuesUnit.put(UnitTable.KEY_GLASS, getGlass() - glass); }
    /** Добавляем день к возрасту площадки */
    public void addDay(){
        int days = valuesUnit.getAsInteger(UnitTable.KEY_DAYS) + 1;
        /** Если дней больше месяца*/
        if (days > 30){
            /** Добавляем месяц*/
            int months = valuesUnit.getAsInteger(UnitTable.KEY_MONTHS) + 1;
            /** Если месяцев больше года*/
            if (months > 12){
                /** Добавляем год*/
                int years = valuesUnit.getAsInteger(UnitTable.KEY_YEARS) + 1;
                setYears(years);
                setMonths(1);
            }else {
                setMonths(months);
            }
            setDays(1);
        }else {
            setDays(days);
        }
        update();
    }



    /*-------------------------------------------------------------------------------------*/

    /** Расчет нормы стекла в день
     * @return Количество стекла в колограммах.  */
    private float machGlassDay(){
        /** Номинальное значение оборота стекла на площадке в день в колограммах*/
        final int NOMINAL_GLASS = 10000;
        /** Количество дней для раскрутки площадки*/
        final int R_DAYS = 60;
        /** Получить возраст площадки*/
        float r = getDays() + (getMonths()*30) + (getYears() * 12 * 30);
        /** Если возраст меньше раскрутки*/
        if (r < R_DAYS){
            /** Вычисляем коэфициет раскрутуи*/
            r /= R_DAYS;
        }else
            /** Коэфициент равен максимальному*/
            r = 1;
        /** Определяем кол-во прихода стекла в день (Реальный приход в городе на кол-во площадок)*/
        float glassDay = (valuesCity.getAsInteger(CityTable.KEY_G_REAL)/valuesCity.getAsInteger(CityTable.KEY_UNIT_QTY)) / 30;
        /** Если больше нолинала для площадки*/
        if (glassDay > NOMINAL_GLASS)
            /** Устанавливаем дневную норму равную номиналу прихода*/
            glassDay = NOMINAL_GLASS;
        /** Переводим в тонны делаем поправку на коэфициент раскрутки и возвращяем*/
        return  (glassDay/1000) * r;
    }

    /** Покупаем товар.  */
    public void buyGlass() {
        /** Расчитываем кол-во стекла в день*/
        float glassDay = machGlassDay();
        /** Расчитываем кол-во денег для покупки стекла */
        int cashMinus = (int)(glassDay * valuesCity.getAsInteger(CityTable.KEY_PRICE_DOWN));
        /** Если денег на площадке хватает для покупки*/
        if (getCash() >= cashMinus){
            /** Списываем деньги с площадки*/
            cashMinus(cashMinus);
            /** Добавляем стекло на площадку*/
            glassPlus(glassDay);
            /** Добавляем сумму денег купленого стекла*/
            glassCashPlus(cashMinus);
        }else {
            /** Отправляем запрос недостаточно денег на площадке*/
            cashPlus(mHandler.messageCash(cashMinus));
        }
        /** Обновить значения в базе данных*/
        update();
    }

    /** Продажа товара   */
    public void shippingGlass(){
        /** Если кол-во стекла на площадке равно для отгрузки */
        if (getGlass() >= getRate()){
            /** Если сумма денег на площадке равно расходам для отправки */
            if (getCash() >= getRatePrice()){
                /** Списываем деньги на отправку */
                cashMinus(getRatePrice());
                /** Списываем с площадки вес для отгрузки */
                glassMinus(getRate());
                /** Определяем сумму продажи товара */
                int sum = (int) ((getGlassCash() / getGlass()) * getRate());
                /** создаем экземпляр товара */
                Goods goods = new Goods();
                /** Устанавливаем вес товара */
                goods.setGlass(getRate());
                /** Устанавливаем сумму товара */
                goods.setCash(sum);
                /** Устанавливаем покупателя товара */
                goods.setMainIndex(1);
                /** Устанавливаем индекс площадки */
                goods.setUnitIndex(getId());
                /** Создаем и отправляем стекло покупателю, получаем деньги и добавляем на площадку */
                cashPlus(mHandler.messageSippingGlass(goods));
                //cashPlus(mHandler.messageSippingGlass(getRate(), sum));
            }else{
                cashPlus(mHandler.messageCash(getRatePrice()));
            }
            /** Обновить значения в базе данных*/
            update();
        }
    }

    /** Расходы */
    public void spending(){
        /** Добавляем расходы на рекламу*/
        int spending = valuesCity.getAsInteger(CityTable.KEY_AD_USED) ;
        /** Добавляем расходы на сервис*/
        spending += valuesCity.getAsInteger(CityTable.KEY_P_SERVICE);
        /** Разделить на количество площадок*/
        spending /= valuesCity.getAsInteger(CityTable.KEY_UNIT_QTY);
        /** Добавляем общии расходы */
        spending += getExes();
        /** Все расходы переводим в день*/
        spending /= 30;
        /** Списываем расходы*/
        cashMinus(spending);
        /** Обновить значения в базе данных*/
        update();
    }

    /** Обновляем данные площадки в базе данных. */
    private void update(){ unitTable.updateEntry(Integer.valueOf(entry.getKey()), valuesUnit); }

    /** Класс товара. */
    class Goods{
        int mainIndex;
        int cash;
        int glass;
        int dividends;


        int unitIndex;

        Goods(){}

        /** Конструктор товара.
         * @param mainIndex Индех фирмы которой отправляется товар.
         * @param cash Сумма стоимости товара.
         * @param glass Вес стекла.  */
        Goods(int mainIndex, int cash, int glass){
            this.mainIndex = mainIndex;
            this.cash = cash;
            this.glass = glass;
        }

        /** Получить индекс Main
         * @return Индекс */
        public int getMainIndex() { return mainIndex; }

        /** Установить индекс
         * @param mainIndex Индекс.  */
        public void setMainIndex(int mainIndex) { this.mainIndex = mainIndex; }

        /** Получить Сумму денег.
         * @return Сумму денег.  */
        public int getCash() { return cash; }

        /** Установить Сумму денег.
         * @param cash Сумма денег.  */
        public void setCash(int cash) { this.cash = cash; }

        /** Получить вес стекла.
         * @return Стекло  */
        public int getGlass() { return glass; }

        /** Установить вес стекла.
         * @param glass Вес стекла.  */
        public void setGlass(int glass) { this.glass = glass; }

        /** Получить индекс площадки отправителя.
         * @return Индекс площадки.  */
        public int getUnitIndex() { return unitIndex; }

        /** Установить индекс площадки отправителя.
         * @param unitIndex Индекс площадки. */
        public void setUnitIndex(int unitIndex) { this.unitIndex = unitIndex; }
    }

}

package com.konst.glass;

import android.content.ContentValues;
import android.content.Context;
import com.konst.glass.provider.MainTable;
import com.konst.glass.provider.UnitTable;

/**
 * Класс Main.
 *
 * @author Kostya
 */
public class Main {
    /**
     * The Context.
     */
    Context context;
    /**
     * Значения данных записи main.
     */
    ContentValues valuesMain;
    /**
     * The Main table.
     */
    MainTable mainTable;

    /**
     * Новый экземпляр Main.
     * @param context   the context
     * @param _rowIndex Индекс записи.
     */
    Main(Context context, int _rowIndex) {
        this.context = context;
        mainTable = new MainTable(context);
        valuesMain = mainTable.getEntry(_rowIndex);
    }

    /**
     * Получить Имя.
     * @return Имя в текстовом виде.
     */
    public String getName() {
        return valuesMain.getAsString(MainTable.KEY_NAME);
    }

    /**
     * Получить сумму денег.     *
     * @return Сумма денег.
     */
    public int getCash() {
        return valuesMain.getAsInteger(MainTable.KEY_CASH);
    }

    /**
     * Получить стекло.
     * @return Стекло float.
     */
    public float getGlass() {
        return valuesMain.getAsFloat(MainTable.KEY_GLASS);
    }

    /**
     * Додавить стекло.
     * @param glass Стекло в тоннах.
     */
    private void glassPlus(float glass) {
        valuesMain.put(UnitTable.KEY_GLASS, getGlass() + glass);
    }

    /**
     * Добавление суммы денег.
     * @param cash Сумма денег.
     */
    private void cashPlus(int cash) {
        valuesMain.put(UnitTable.KEY_CASH, getCash() + cash);
    }
}

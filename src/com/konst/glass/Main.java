package com.konst.glass;

import android.content.ContentValues;
import android.content.Context;
import com.konst.glass.provider.MainTable;
import com.konst.glass.provider.UnitTable;

/**
 * Created by Kostya on 22.05.2015.
 */
public class Main {
    Context context;
    ContentValues valuesMain;
    MainTable mainTable;

    Main(Context context, int _rowIndex){
        this.context = context;
        mainTable = new MainTable(context);
        valuesMain = mainTable.getEntry(_rowIndex);
    }

    public String getName(){
        return valuesMain.getAsString(MainTable.KEY_NAME);
    }

    public int getCash(){
        return valuesMain.getAsInteger(MainTable.KEY_CASH);
    }

    public float getGlass(){
        return valuesMain.getAsFloat(MainTable.KEY_GLASS);
    }

    private void glassPlus(float glass){ valuesMain.put(UnitTable.KEY_GLASS, getGlass() + glass); }

    private void cashPlus(int cash){ valuesMain.put(UnitTable.KEY_CASH, getCash() + cash); }
}

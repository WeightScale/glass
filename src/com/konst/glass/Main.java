package com.konst.glass;

import android.content.ContentValues;
import android.content.Context;
import com.konst.glass.provider.MainTable;
import com.konst.glass.provider.UnitTable;

/** ����� Main.
 * @author Kostya */
public class Main {
    /** The Context. */
    Context context;
    /** �������� ������ ������ main. */
    ContentValues valuesMain;
    /** The Main table. */
    MainTable mainTable;

    /** ����� ��������� Main.
     * @param context the context
     * @param _rowIndex ������ ������. */
    Main(Context context, int _rowIndex){
        this.context = context;
        mainTable = new MainTable(context);
        valuesMain = mainTable.getEntry(_rowIndex);
    }

    /** �������� ���
     * @return ��� � ��������� ����. */
    public String getName(){
        return valuesMain.getAsString(MainTable.KEY_NAME);
    }

    /** �������� ����� �����.
     *  @return ����� �����.  */
    public int getCash(){
        return valuesMain.getAsInteger(MainTable.KEY_CASH);
    }

    /** �������� ������.
     * @return ������ float. */
    public float getGlass(){
        return valuesMain.getAsFloat(MainTable.KEY_GLASS);
    }

    /** �������� ������.
     * @param glass ������ � ������. */
    private void glassPlus(float glass){ valuesMain.put(UnitTable.KEY_GLASS, getGlass() + glass); }

    /** ���������� ����� �����
     * @param cash ����� �����. */
    private void cashPlus(int cash){ valuesMain.put(UnitTable.KEY_CASH, getCash() + cash); }
}

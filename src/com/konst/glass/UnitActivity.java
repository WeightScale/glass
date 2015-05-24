package com.konst.glass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentQueryMap;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.konst.glass.provider.CityTable;
import com.konst.glass.provider.LinkUnitTable;
import com.konst.glass.provider.UnitTable;
import com.konst.glass.provider.VendorTable;

import java.util.Map;

/**
 * Created by Kostya on 08.05.2015.
 */
public class UnitActivity extends Activity implements View.OnClickListener {
    UnitTable unitTable;
    VendorTable vendorTable;
    CityTable cityTable;
    LinkUnitTable linkUnitTable;
    Button buttonAddUnit, buttonAddCash;
    EditText editTextCash, editTextRate, editTextRatePrice,editTextExes, editTextMainDeposit;
    TextView textViewVendor;
    Spinner spinnerCity;
    ContentValues valuesUnit  = new ContentValues();
    ContentValues valuesCity  = new ContentValues();
    ContentValues valuesLink  = new ContentValues();
    ContentValues valuesVendor  = new ContentValues();
    int unitId;

    int REQUEST_SELECT_VENDOR = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit);

        unitTable = new UnitTable(this);
        vendorTable = new VendorTable(this);
        cityTable = new CityTable(this);
        linkUnitTable = new LinkUnitTable(this);


        //Uri uri = unitTable.insertNewEntry();               //создаем новую запись в базе данных
        //Cursor cursor = new UnitTable(this).getItem(uri);
        //ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        //Map<String, ContentValues> map = mQueryMap.getRows();
        //valuesUnit = map.get(uri.getLastPathSegment());
        //if (valuesUnit != null)
            //unitId = Integer.valueOf(uri.getLastPathSegment());
        //else
            //finish();

        spinnerCity = (Spinner)findViewById(R.id.spinnerCity);
        textViewVendor = (TextView)findViewById(R.id.textViewVendor);
        textViewVendor.setOnLongClickListener(vendorListener);

        editTextRate = (EditText)findViewById(R.id.editTextRate);
        editTextRate.addTextChangedListener(new GenericTextWatcher(editTextRate));

        editTextRatePrice = (EditText)findViewById(R.id.editTextRatePrice);
        editTextRatePrice.addTextChangedListener(new GenericTextWatcher(editTextRatePrice));

        editTextExes = (EditText)findViewById(R.id.editTextExes);
        editTextExes.addTextChangedListener(new GenericTextWatcher(editTextExes));

        buttonAddCash = (Button)findViewById(R.id.buttonAddCash);
        buttonAddCash.setOnClickListener(this);

        buttonAddUnit = (Button)findViewById(R.id.buttonAddUnit);
        buttonAddUnit.setOnClickListener(this);

        setupSpinner();
        //textViewVendor.setText(vendorTable.getKeyName(values.getAsInteger(UnitTable.KEY_VENDOR_ID)));
        /*Cursor cursor = new CityDBAdapter(getApplicationContext()).getValidCity(50000);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        Map<String, ContentValues> map = mQueryMap.getRows();
        int i = map.size();*/
    }

    void setupSpinner(){
        Cursor cursor = new CityTable(getApplicationContext()).getValidCity(50000);
        if (cursor.getCount() > 0) {
            String[] columns = {CityTable.KEY_CITY, CityTable.KEY_AREA, CityTable.KEY_POPULATION};
            int[] to = {R.id.text1, R.id.text2, R.id.text3};
            SimpleCursorAdapter cityAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.city_spinner, cursor, columns, to);
            cityAdapter.setDropDownViewResource(R.layout.city_spinner_dropdown_item);
            spinnerCity.setAdapter(cityAdapter);
            spinnerCity.setOnItemSelectedListener(spinnerItemListener);
        }
    }

    AdapterView.OnItemSelectedListener spinnerItemListener =  new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            valuesUnit.put(UnitTable.KEY_CITY_ID, (int)l);
            valuesCity = cityTable.getEntry((int)l);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

   /* View.OnClickListener listenerAddUnit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Uri uri = unitTable.insertNewEntry();               //создаем новую запись в базе данных

            //valuesUnit.put(UnitTable.KEY_LINK_UNIT_ID,Integer.valueOf(uri.getLastPathSegment()));
            valuesUnit.put(UnitTable.KEY_GLASS, 0);
            int cash = valuesLink.getAsInteger(LinkUnitTable.KEY_CASH) + valuesVendor.getAsInteger(VendorTable.KEY_CASH);
            valuesUnit.put(UnitTable.KEY_CASH, cash);
            valuesUnit.put(UnitTable.KEY_DAYS, 0);
            valuesUnit.put(UnitTable.KEY_MONTHS, 0);
            valuesUnit.put(UnitTable.KEY_YEARS, 0);
            valuesUnit.put(UnitTable.KEY_DATA1, 0);
            valuesUnit.put(UnitTable.KEY_DATA2, "0");
            Uri uri = unitTable.insertNewEntry(valuesUnit);               //создаем новую запись в базе данных
            new LinkUnitTable(getBaseContext()).insertNewEntry(Integer.valueOf(uri.getLastPathSegment()), valuesLink);
            //unitTable.updateEntry(unitId, valuesUnit);
            int unitQty = valuesCity.getAsInteger(CityTable.KEY_UNIT_QTY)+1;
            valuesCity.put(CityTable.KEY_UNIT_QTY, unitQty);
            cityTable.updateEntry(valuesUnit.getAsInteger(UnitTable.KEY_CITY_ID), valuesCity);
        }
    };*/

    View.OnLongClickListener vendorListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Intent intent = new Intent(UnitActivity.this, VendorListActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_VENDOR);
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if(requestCode == REQUEST_SELECT_VENDOR){
                    int vendorId = data.getIntExtra(VendorTable.KEY_ID,0);
                    valuesVendor = vendorTable.getEntry(vendorId);
                    if (vendorId !=0){
                        valuesUnit.put(UnitTable.KEY_VENDOR_ID, vendorId);
                        textViewVendor.setText(vendorTable.getKeyName(valuesUnit.getAsInteger(UnitTable.KEY_VENDOR_ID)));
                    }
                }
            break;
            case RESULT_CANCELED:

            break;
            default:

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddCash:
                showDialog(1);
                break;
            case R.id.buttonAddUnit:
                valuesUnit.put(UnitTable.KEY_GLASS, 0);
                valuesUnit.put(UnitTable.KEY_GLASS_CASH, 0);
                int cash = valuesUnit.getAsInteger(unitTable.KEY_DEPOSIT_MAIN) + valuesUnit.getAsInteger(unitTable.KEY_DEPOSIT_UNIT);
                valuesUnit.put(UnitTable.KEY_CASH, cash);
                valuesUnit.put(UnitTable.KEY_DAYS, 0);
                valuesUnit.put(UnitTable.KEY_MONTHS, 0);
                valuesUnit.put(UnitTable.KEY_YEARS, 0);
                valuesUnit.put(UnitTable.KEY_DATA1, 0);
                valuesUnit.put(UnitTable.KEY_DATA2, "0");
                Uri uri = unitTable.insertNewEntry(valuesUnit);               //создаем новую запись в базе данных
                new LinkUnitTable(getBaseContext()).insertNewEntry(Integer.valueOf(uri.getLastPathSegment()), valuesLink);
                //unitTable.updateEntry(unitId, valuesUnit);
                int unitQty = valuesCity.getAsInteger(CityTable.KEY_UNIT_QTY)+1;
                valuesCity.put(CityTable.KEY_UNIT_QTY, unitQty);
                cityTable.updateEntry(valuesUnit.getAsInteger(UnitTable.KEY_CITY_ID), valuesCity);
                break;
        }
    }

    private class GenericTextWatcher implements TextWatcher{

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch(view.getId()){
                case R.id.editTextRate:
                    valuesUnit.put(UnitTable.KEY_RATE, setValueFloat(text, 1.0f));
                break;
                case R.id.editTextRatePrice:
                    valuesUnit.put(UnitTable.KEY_RATE_PRICE, setValueInt(text, 0));
                break;
                case R.id.editTextExes:
                    valuesUnit.put(UnitTable.KEY_EXES, setValueInt(text, 0));
                break;
                default:
            }
        }

        int setValueInt(String text, int _default){
            try {
                return Integer.valueOf(text);
            }catch (NumberFormatException e){
                return _default;
            }
        }

        float setValueFloat(String text, float _default){
            try {
                return Float.valueOf(text);
            }catch (NumberFormatException e){
                return _default;
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Custom dialog");
        // создаем view из dialog.xml
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.add_cash_unit, null);
        // устанавливаем ее, как содержимое тела диалога
        adb.setView(view);
        // находим TexView для отображения кол-ва
        //tvCount = (TextView) view.findViewById(R.id.tvCount);
        return adb.create();
    }

    @Override
    protected void onPrepareDialog(int id, final Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        if (id == 1) {
            // Находим TextView для отображения времени и показываем текущее
            // время
            ImageView buttonBack = (ImageView) dialog.getWindow().findViewById(R.id.buttonBack);
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            ImageView buttonAdd = (ImageView) dialog.getWindow().findViewById(R.id.buttonAdd);
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText main = (EditText)dialog.findViewById(R.id.editDepositMain);
                    EditText unit = (EditText)dialog.findViewById(R.id.editDepositUnit);
                    try {
                        valuesUnit.put(UnitTable.KEY_DEPOSIT_MAIN, Integer.valueOf(main.getText().toString()));
                    }catch (Exception e){
                        valuesUnit.put(UnitTable.KEY_DEPOSIT_MAIN,  0);
                    }

                    try {
                        valuesUnit.put(UnitTable.KEY_DEPOSIT_UNIT,  Integer.valueOf(unit.getText().toString()));
                    }catch (Exception e){
                        valuesUnit.put(UnitTable.KEY_DEPOSIT_UNIT,  0);
                    }
                    dialog.dismiss();
                }
            });
        }
    }
}

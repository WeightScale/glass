package com.konst.glass;

import android.app.Activity;
import android.content.ContentQueryMap;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.konst.glass.provider.CityTable;
import com.konst.glass.provider.UnitTable;
import com.konst.glass.provider.VendorTable;

import java.text.DecimalFormat;
import java.util.Map;


/**
 * Активность для города.
 *
 * @author Kostya
 */
public class CityActivity extends Activity {
    CityTable cityTable;
    EditText editKPopulation, editGlassReal, editAdUsed, editAdMax, editPriceDown, editPriceUp, editKService, editPriceService;
    Button buttonSaved;
    ContentValues valuesCity = new ContentValues();
    int cityId;
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city);

        cityId = getIntent().getIntExtra(CityTable.KEY_ID, 0);

        cityTable = new CityTable(this);
        Cursor cursor = cityTable.getItem(cityId);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        valuesCity = mQueryMap.getRows().get(String.valueOf(cityId));


        editKPopulation = (EditText) findViewById(R.id.edit_k_population);
        editKPopulation.setText(valuesCity.getAsString(CityTable.KEY_K_POPULATION));
        editKPopulation.addTextChangedListener(new GenericTextWatcher(editKPopulation));

        editGlassReal = (EditText) findViewById(R.id.edit_glass_real);
        editGlassReal.setText(valuesCity.getAsString(CityTable.KEY_G_REAL));
        editGlassReal.addTextChangedListener(new GenericTextWatcher(editGlassReal));

        editAdUsed = (EditText) findViewById(R.id.edit_ad_used);
        editAdUsed.setText(valuesCity.getAsString(CityTable.KEY_AD_USED));
        editAdUsed.addTextChangedListener(new GenericTextWatcher(editAdUsed));

        editAdMax = (EditText) findViewById(R.id.edit_ad_max);
        editAdMax.setText(valuesCity.getAsString(CityTable.KEY_AD_MAX));
        editAdMax.addTextChangedListener(new GenericTextWatcher(editAdMax));

        editPriceDown = (EditText) findViewById(R.id.edit_price_down);
        editPriceDown.setText(valuesCity.getAsString(CityTable.KEY_PRICE_DOWN));
        editPriceDown.addTextChangedListener(new GenericTextWatcher(editPriceDown));

        editPriceUp = (EditText) findViewById(R.id.edit_price_up);
        editPriceUp.setText(valuesCity.getAsString(CityTable.KEY_PRICE_UP));
        editPriceUp.addTextChangedListener(new GenericTextWatcher(editPriceUp));

        editKService = (EditText) findViewById(R.id.edit_k_service);
        editKService.setText(valuesCity.getAsString(CityTable.KEY_K_SERVICE));
        editKService.addTextChangedListener(new GenericTextWatcher(editKService));

        editPriceService = (EditText) findViewById(R.id.edit_p_service);
        editPriceService.setText(valuesCity.getAsString(CityTable.KEY_P_SERVICE));
        editPriceService.addTextChangedListener(new GenericTextWatcher(editPriceService));

        buttonSaved = (Button) findViewById(R.id.buttonSave);
        buttonSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityTable.updateEntry(cityId, valuesCity);
            }
        });

        /*uri = getIntent().getData();

        Cursor cursor = new VendorTable(getApplicationContext()).getItem(uri);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        Map<String, ContentValues> map = mQueryMap.getRows();
        values = map.get(uri.getLastPathSegment());
        if (values==null)
            finish();*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (uri != null)
            cityTable.updateEntry(cityId, valuesCity);
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edit_k_population:
                    valuesCity.put(CityTable.KEY_K_POPULATION, setValueFloat(text, 1.0f));
                    break;
                case R.id.edit_glass_real:
                    valuesCity.put(CityTable.KEY_G_REAL, setValueInt(text, 0));
                    break;
                case R.id.edit_ad_used:
                    valuesCity.put(CityTable.KEY_AD_USED, setValueInt(text, 0));
                    break;
                case R.id.edit_ad_max:
                    valuesCity.put(CityTable.KEY_AD_MAX, setValueInt(text, 0));
                    break;
                case R.id.edit_price_down:
                    valuesCity.put(CityTable.KEY_PRICE_DOWN, setValueInt(text, 0));
                    break;
                case R.id.edit_price_up:
                    valuesCity.put(CityTable.KEY_PRICE_UP, setValueInt(text, 0));
                    break;
                case R.id.edit_k_service:
                    valuesCity.put(CityTable.KEY_K_SERVICE, setValueFloat(text, 0.0f));
                    break;
                case R.id.edit_p_service:
                    valuesCity.put(CityTable.KEY_P_SERVICE, setValueInt(text, 0));
                    break;
                default:
            }
        }

        int setValueInt(String text, int _default) {
            try {
                return Integer.valueOf(text);
            } catch (NumberFormatException e) {
                return _default;
            }
        }

        float setValueFloat(String text, float _default) {
            try {
                return Float.valueOf(text);
            } catch (NumberFormatException e) {
                return _default;
            }
        }
    }

}

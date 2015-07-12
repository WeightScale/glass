package com.konst.glass;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.konst.glass.R;
import com.konst.glass.VendorActivity;
import com.konst.glass.provider.CityTable;
import com.konst.glass.provider.VendorTable;

/**
 * Created by Kostya on 09.05.2015.
 */
public class CityListActivity extends ListActivity implements View.OnLongClickListener {

    ListView listCity;
    //ImageView buttonNewVendor;
    ContentValues values = new ContentValues();
    CityTable cityTable;

    int REQUEST_CREATE_CITY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list);

        cityTable = new CityTable(this);

        setupListCity();
    }

    void setupListCity() {
        listCity = getListView();
        Cursor cursor = cityTable.getValidCity(50000);
        if (cursor == null) {
            return;
        }
        String[] columns = {CityTable.KEY_CITY, CityTable.KEY_AREA, CityTable.KEY_POPULATION};
        int[] to = {R.id.text1, R.id.text2, R.id.text3};
        SimpleCursorAdapter cityAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.city_spinner, cursor, columns, to);
        setListAdapter(cityAdapter);
        listCity.setOnItemClickListener(listItemListener);

    }

    /*Cursor getContact() {
        return getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }*/

    AdapterView.OnItemClickListener listItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(CityListActivity.this, CityActivity.class);
            intent.putExtra(CityTable.KEY_ID, (int) l);
            startActivity(intent);
        }
    };

    @Override
    public boolean onLongClick(View view) {
        //Uri uri = new VendorTable(getApplicationContext()).insertNewEntry();
        Intent intent = new Intent(this, VendorActivity.class);
        //intent.setData(uri);
        startActivity(intent);
        //startActivityForResult(intent, REQUEST_CREATE_VENDOR);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:

                break;
            case RESULT_CANCELED:

                break;
            default:

        }
    }
}

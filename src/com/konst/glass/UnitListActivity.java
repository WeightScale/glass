package com.konst.glass;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.konst.glass.provider.CityTable;
import com.konst.glass.provider.UnitTable;

/**
 * Created by Kostya on 09.05.2015.
 */
public class UnitListActivity extends ListActivity implements View.OnLongClickListener {

    ListView listUnit;
    //ImageView buttonNewVendor;
    ContentValues values = new ContentValues();
    UnitTable unitTable;

    int REQUEST_CREATE_CITY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list);

        unitTable = new UnitTable(this);

        setupListCity();
    }

    void setupListCity() {
        listUnit = getListView();
        Cursor cursor = unitTable.getAllEntries();
        if (cursor == null) {
            return;
        }
        String[] columns = {UnitTable.KEY_CITY_ID, CityTable.KEY_AREA, CityTable.KEY_POPULATION};
        int[] to = {R.id.text1, R.id.text2, R.id.text3};
        SimpleCursorAdapter cityAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.city_spinner, cursor, columns, to);
        setListAdapter(cityAdapter);
        listUnit.setOnItemClickListener(listItemListener);

    }

    /*Cursor getContact() {
        return getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }*/

    AdapterView.OnItemClickListener listItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(UnitListActivity.this, CityActivity.class);
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

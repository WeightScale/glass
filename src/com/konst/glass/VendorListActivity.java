package com.konst.glass;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.konst.glass.provider.VendorTable;

/**
 * Created by Kostya on 09.05.2015.
 */
public class VendorListActivity extends ListActivity implements View.OnLongClickListener {

    ListView listVendor;
    ImageView buttonNewVendor;
    ContentValues values = new ContentValues();

    int REQUEST_CREATE_VENDOR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_list);

        buttonNewVendor = (ImageView) findViewById(R.id.buttonNewVendor);
        buttonNewVendor.setOnLongClickListener(this);

        setupListVendor();
    }

    void setupListVendor() {
        listVendor = getListView();
        Cursor cursor = new VendorTable(this).getAllEntries();
        if (cursor == null) {
            return;
        }
        String[] columns = {VendorTable.KEY_NAME};
        int[] to = {R.id.text1};
        SimpleCursorAdapter vendorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.city_spinner, cursor, columns, to);
        setListAdapter(vendorAdapter);
        listVendor.setOnItemClickListener(listItemListener);

    }

    /*Cursor getContact() {
        return getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }*/

    AdapterView.OnItemClickListener listItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            intent.putExtra(VendorTable.KEY_ID, (int) l);
            setResult(RESULT_OK, intent);
            finish();
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

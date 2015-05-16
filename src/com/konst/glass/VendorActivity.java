package com.konst.glass;

import android.app.Activity;
import android.content.ContentQueryMap;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.*;
import com.konst.glass.provider.UnitTable;
import com.konst.glass.provider.VendorTable;

import java.util.Map;

/*
 * Created by Kostya on 09.05.2015.
 */
public class VendorActivity extends Activity {
    VendorTable vendorTable;
    Spinner spinnerContact;
    EditText editTextCash;
    Button buttonSaved;
    ContentValues values = new ContentValues();
    int vendorId;
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor);

        vendorTable = new VendorTable(this);
        editTextCash = (EditText)findViewById(R.id.editTextCash);
        buttonSaved = (Button)findViewById(R.id.buttonSave);
        buttonSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    values.put(VendorTable.KEY_CASH, Integer.valueOf(editTextCash.getText().toString()));
                }catch (NumberFormatException e){
                    values.put(VendorTable.KEY_CASH, 0);
                }
                if (uri == null)
                    uri = vendorTable.insertNewEntry(values);
                else
                    vendorTable.updateEntry(uri, values);
            }
        });

        /*uri = getIntent().getData();

        Cursor cursor = new VendorTable(getApplicationContext()).getItem(uri);
        ContentQueryMap mQueryMap = new ContentQueryMap(cursor, BaseColumns._ID, true, null);
        Map<String, ContentValues> map = mQueryMap.getRows();
        values = map.get(uri.getLastPathSegment());
        if (values==null)
            finish();*/

        spinnerContact = (Spinner)findViewById(R.id.spinnerContact);
        setupSpinnerContact();

    }

    void setupSpinnerContact() {
        String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
        int[] to = {R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.city_spinner, getContact(), from, to);
        adapter.setDropDownViewResource(R.layout.city_spinner_dropdown_item);
        spinnerContact.setAdapter(adapter);
        spinnerContact.setOnItemSelectedListener(spinnerListener);
    }

    Cursor getContact() {
        return getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            values.put(VendorTable.KEY_CONTACT_ID, (int)l);
            TextView textView = (TextView) view.findViewById(R.id.text1);
            values.put(VendorTable.KEY_NAME, textView.getText().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }

    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (uri != null)
            vendorTable.updateEntry(uri, values);
    }
}

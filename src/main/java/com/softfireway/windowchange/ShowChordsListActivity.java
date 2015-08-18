package com.softfireway.windowchange;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 14.06.2015.
 */
public class ShowChordsListActivity extends ListActivity {

    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getChords());
        setListAdapter(adapter);
    }

    public List<String> getChords() {
        List<String> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(mDatabaseHelper.TABLE_NAME_ACCORD, new String[]{mDatabaseHelper.ACCORD_NAME}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.ACCORD_NAME)));
        }
        cursor.close();
        return list;
    }
}

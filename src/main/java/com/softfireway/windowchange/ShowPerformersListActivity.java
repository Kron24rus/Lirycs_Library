package com.softfireway.windowchange;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


/**
 * Created by Александр on 16.03.2015.
 */
public class ShowPerformersListActivity extends ListActivity {

    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getPerformers());
        setListAdapter(adapter);

        ListView lv = getListView();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String removable = parent.getItemAtPosition(position).toString();
                String deleteRow = "delete from " + mDatabaseHelper.TABLE_NAME_PERFORMER  + " where " + mDatabaseHelper.PERFORMER_NAME +" = " + "'" + removable + "'";
                mDatabase.execSQL(deleteRow);
                adapter.remove(removable);
                adapter.notifyDataSetChanged();
                Toast.makeText(ShowPerformersListActivity.this, removable + " has been deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public List<String> getPerformers() {
        List<String> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(mDatabaseHelper.TABLE_NAME_PERFORMER, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.PERFORMER_NAME)));
        }
        cursor.close();
        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String getListSongsID = " SELECT * FROM " + mDatabaseHelper.TABLE_NAME_PERFORMER + " WHERE " + mDatabaseHelper.PERFORMER_NAME +" = " + "'" + l.getItemAtPosition(position).toString() + "'";
        String getLyrics = "SELECT * FROM " + mDatabaseHelper.TABLE_NAME_SONGS  + " WHERE " + mDatabaseHelper.SONGNAME +" = " + "'" + l.getItemAtPosition(position).toString() + "'";
        Cursor cursor = mDatabase.rawQuery(getLyrics, null);
        Cursor cursor1 = mDatabase.rawQuery(getListSongsID, null);
        cursor.moveToFirst();
        cursor1.moveToFirst();
        int tmp = cursor1.getInt(cursor1.getColumnIndex(mDatabaseHelper._ID));
        String getSongs = "select songname from songs where _id in (select _idsong from song_performer where _idperformer = " + tmp + ")";
        Cursor cursor2 = mDatabase.rawQuery(getSongs,null);
        int temp2 = cursor2.getCount();
        List<String> list = new ArrayList<>();
        while (cursor2.moveToNext()) {
            list.add(cursor2.getString(cursor2.getColumnIndex(mDatabaseHelper.SONGNAME)));
        }
        cursor2.close();
        Intent intentSongDetails = new Intent(ShowPerformersListActivity.this, ShowSongsOfPerformer.class);
        intentSongDetails.putStringArrayListExtra("song_list", (ArrayList<String>) list);
        Toast.makeText(this, temp2+"" , LENGTH_SHORT).show();
        cursor.close();
        startActivity(intentSongDetails);
    }

}

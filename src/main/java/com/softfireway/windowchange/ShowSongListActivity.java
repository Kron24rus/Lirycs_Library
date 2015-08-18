package com.softfireway.windowchange;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 16.03.2015.
 */
public class ShowSongListActivity extends ListActivity {

   // ListView mSongList; ****Если нужны навороченные списки то ListView****
    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();
        /*
          setContentView(R.layout.list_song);
          mSongList = (ListView)findViewById(R.id.lstvSong);  ****Если нужны навороченные списки то ListView****
          mSongList.setAdapter(adapter);
          */
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getSongs());
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String removable = parent.getItemAtPosition(position).toString();
                String deleteRow = "delete from " + mDatabaseHelper.TABLE_NAME_SONGS  + " where " + mDatabaseHelper.SONGNAME +" = " + "'" + removable + "'";
                mDatabase.execSQL(deleteRow);
                adapter.remove(removable);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public List<String> getSongs() {
        List<String> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(mDatabaseHelper.TABLE_NAME_SONGS, new String[]{mDatabaseHelper.SONGNAME}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.SONGNAME)));
        }
        cursor.close();
        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String getLyrics = "SELECT * FROM " + mDatabaseHelper.TABLE_NAME_SONGS  + " WHERE " + mDatabaseHelper.SONGNAME +" = " + "'" + l.getItemAtPosition(position).toString() + "'";
        Cursor cursor = mDatabase.rawQuery(getLyrics, null);
        cursor.moveToFirst();
        Intent intentSongDetails = new Intent(ShowSongListActivity.this, ShowSongDetailsActivity.class);
        intentSongDetails.putExtra("song_id", cursor.getInt(cursor.getColumnIndex(mDatabaseHelper._ID)));
        cursor.close();
        startActivity(intentSongDetails);
    }
}

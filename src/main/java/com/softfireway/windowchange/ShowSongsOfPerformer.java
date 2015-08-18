package com.softfireway.windowchange;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Александр on 27.05.2015.
 */
public class ShowSongsOfPerformer extends ListActivity {

    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getSongList());
        setListAdapter(adapter);

    }

    public List<String> getSongList() {
        List<String> list = new ArrayList<>();
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("song_list");
        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String getLyrics = "SELECT * FROM " + mDatabaseHelper.TABLE_NAME_SONGS  + " WHERE " + mDatabaseHelper.SONGNAME +" = " + "'" + l.getItemAtPosition(position).toString() + "'";
        Cursor cursor = mDatabase.rawQuery(getLyrics, null);
        cursor.moveToFirst();
        Intent intentSongDetails = new Intent(ShowSongsOfPerformer.this, ShowSongDetailsActivity.class);
        intentSongDetails.putExtra("song_id", cursor.getInt(cursor.getColumnIndex(mDatabaseHelper._ID)));
        cursor.close();
        startActivity(intentSongDetails);
    }

}

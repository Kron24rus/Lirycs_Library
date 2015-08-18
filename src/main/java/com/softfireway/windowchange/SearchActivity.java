package com.softfireway.windowchange;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Александр on 16.03.2015.
 */
public class SearchActivity extends ListActivity {

    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getSearchResult());
        setListAdapter(adapter);
    }

    public List<String> getSearchResult() {
        List<String> list = new ArrayList<>();
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("list");
        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intents = getIntent();
        String type = intents.getStringExtra("type");
        switch (type) {
            case "song":
                String getSongsID = " SELECT _ID FROM " + mDatabaseHelper.TABLE_NAME_SONGS + " WHERE "
                        + mDatabaseHelper.SONGNAME + " = " + "'" + l.getItemAtPosition(position).toString() + "'";
                Cursor cursor = mDatabase.rawQuery(getSongsID, null);
                cursor.moveToFirst();
                Intent intentSongDetails = new Intent(SearchActivity.this, ShowSongDetailsActivity.class);
                intentSongDetails.putExtra("song_id", cursor.getInt(cursor.getColumnIndex(mDatabaseHelper._ID)));
                cursor.close();
                startActivity(intentSongDetails);
                break;
            case "performer":
                String getSongList = "select " + mDatabaseHelper.SONGNAME + " from " + mDatabaseHelper.TABLE_NAME_SONGS + " where " + mDatabaseHelper._ID
                        + " in (select " + mDatabaseHelper.FK_SONG_ID + " from " + mDatabaseHelper.TABLE_SONG_PERFORMER + " where " + mDatabaseHelper.FK_PERFORMER_ID
                        + " = (select " + mDatabaseHelper._ID + " from " + mDatabaseHelper.TABLE_NAME_PERFORMER + " where " + mDatabaseHelper.PERFORMER_NAME
                        + " = '" + l.getItemAtPosition(position).toString() + "'))";
                Cursor cursor1 = mDatabase.rawQuery(getSongList, null);
                List<String> list = new ArrayList<>();
                while (cursor1.moveToNext()) {
                    list.add(cursor1.getString(cursor1.getColumnIndex(mDatabaseHelper.SONGNAME)));
                }
                Intent intentPerformersSongs = new Intent(SearchActivity.this, ShowSongsOfPerformer.class);
                intentPerformersSongs.putStringArrayListExtra("song_list", (ArrayList<String>) list);
                cursor1.close();
                startActivity(intentPerformersSongs);
                break;
            case "genre":
                break;
            default:
                break;
        }
    }
}

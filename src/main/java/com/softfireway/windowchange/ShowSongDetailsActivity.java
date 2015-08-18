package com.softfireway.windowchange;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Александр on 17.03.2015.
 */
public class ShowSongDetailsActivity extends Activity {

    TextView mTextSong;

    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_details);

        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();
        mTextSong = (TextView)findViewById(R.id.tvSong);
        setmTextSong();
    }

    public void setmTextSong () {
        int songID = getIntent().getExtras().getInt("song_id");
        String getLyrics = "SELECT * FROM " + mDatabaseHelper.TABLE_NAME_SONGS  + " WHERE " + mDatabaseHelper._ID +" = " + "'" + songID + "'";
        Cursor cursor = mDatabase.rawQuery(getLyrics,null);
        cursor.moveToFirst();
        mTextSong.setText(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.LYRICS)));
        cursor.close();
    }

    public void onClick(View view) {
        int SongID = getIntent().getExtras().getInt("song_id");
        Intent intentEditSong = new Intent(ShowSongDetailsActivity.this, EditSongActivity.class);
        intentEditSong.putExtra("song_id", SongID);
        startActivity(intentEditSong);
        finish();
    }
}

package com.softfireway.windowchange;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Александр on 31.05.2015.
 */
public class EditSongActivity extends FragmentActivity implements EditSongDialog.EditSongDetailsDialogListener {

    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private DialogFragment mEditSongDialog;
    private TextView mTextSongLyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_song_activity);


        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();

        mTextSongLyrics = (TextView)findViewById(R.id.tv_edit_song_lyrics);

        int SongId = getIntent().getExtras().getInt("song_id");
        String getSongDetails = "SELECT * FROM " + mDatabaseHelper.TABLE_NAME_SONGS  + " WHERE " + mDatabaseHelper._ID +" = " + "'" + SongId + "'";
        Cursor cursor = mDatabase.rawQuery(getSongDetails, null);
        cursor.moveToFirst();
        mTextSongLyrics.setText(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.LYRICS)));
/*
        String getPerformer = "SELECT " + mDatabaseHelper.PERFORMER_NAME + " FROM " + mDatabaseHelper.TABLE_NAME_PERFORMER + " WHERE "
                + mDatabaseHelper._ID + " = (SELECT " + mDatabaseHelper.FK_PERFORMER_ID + " FROM " + mDatabaseHelper.TABLE_SONG_PERFORMER
                + " WHERE " + mDatabaseHelper.FK_SONG_ID + " = " + SongId +")";
        cursor = mDatabase.rawQuery(getPerformer, null);
        cursor.moveToFirst();
        mTextPerformerName.setText(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.PERFORMER_NAME)));
*/
        mEditSongDialog = new EditSongDialog();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_song:
                String updateLyrics = "update " + mDatabaseHelper.TABLE_NAME_SONGS + " set " + mDatabaseHelper.LYRICS
                        + "= '" + mTextSongLyrics.getText().toString() + "' where " + mDatabaseHelper._ID
                        + " = " + getIntent().getExtras().getInt("song_id");
                mDatabase.execSQL(updateLyrics);
                Toast.makeText(this, "Song updated successfully", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_edit_lyrics:
                Bundle bundle = new Bundle();
                bundle.putString("lyrics_base", mTextSongLyrics.getText().toString());
                mEditSongDialog.setArguments(bundle);
                mEditSongDialog.show(getFragmentManager(), "EditSongLyricsDialog");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFinishEditLyricsDialog(String updatedSongLyrics) {
        mTextSongLyrics.setText(updatedSongLyrics);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Lyrics", mTextSongLyrics.getText().toString());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTextSongLyrics.setText(savedInstanceState.getString("Lyrics"));
    }
}

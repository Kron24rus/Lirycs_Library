package com.softfireway.windowchange;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    RadioGroup rbut;
    EditText mSearchCriteriaEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbut = (RadioGroup)findViewById(R.id.radGroup);

        mSearchCriteriaEt = (EditText)findViewById(R.id.searchField);

        mDatabaseHelper = new LyricsDataBaseHelper(this);

        mDatabase = mDatabaseHelper.getWritableDatabase();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                switch (rbut.getCheckedRadioButtonId()) {
                    case R.id.radioSong:
                        Intent intentSearch = new Intent(MainActivity.this, SearchActivity.class);
                        intentSearch.putStringArrayListExtra("list", (ArrayList<String>)getSearchList(1));
                        intentSearch.putExtra("type", "song");
                        startActivity(intentSearch);
                        break;
                    case R.id.radioPerformer:
                        Intent intentSearchPerformer = new Intent(MainActivity.this, SearchActivity.class);
                        intentSearchPerformer.putStringArrayListExtra("list", (ArrayList<String>)getSearchList(2));
                        intentSearchPerformer.putExtra("type", "performer");
                        startActivity(intentSearchPerformer);
                        break;
                    case R.id.radioGenre:
                        Intent intentSearchGenre = new Intent(MainActivity.this, SearchActivity.class);
                        intentSearchGenre.putStringArrayListExtra("list", (ArrayList<String>)getSearchList(3));
                        intentSearchGenre.putExtra("type", "genre");
                        startActivity(intentSearchGenre);
                        break;
                }
                break;
            case R.id.btnAuthor:
                Intent intentAuthorList = new Intent(MainActivity.this, ShowPerformersListActivity.class);
                startActivity(intentAuthorList);
                break;
            case R.id.btnSongs:
                Intent intentSongsList = new Intent(MainActivity.this, ShowSongListActivity.class);
                startActivity(intentSongsList);
                break;
            case R.id.btnAdd:
                Intent intentAddSong = new Intent(MainActivity.this, AddSongActivity.class);
                startActivity(intentAddSong);
                break;
            case R.id.btnChords:
                Intent intentShowChords = new Intent(MainActivity.this, ShowChordsListActivity.class);
                startActivity(intentShowChords);
                break;
            case R.id.btnDebug:
                Cursor cursor = mDatabase.query(mDatabaseHelper.TABLE_NAME_SONGS, new String[]{mDatabaseHelper._ID, mDatabaseHelper.SONGNAME, mDatabaseHelper.LYRICS}, null,
                        null,
                        null,
                        null,
                        null
                );
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(mDatabaseHelper._ID));
                    String name = cursor.getString(cursor.getColumnIndex(mDatabaseHelper.SONGNAME));
                    String lyrics = cursor.getString(cursor.getColumnIndex(mDatabaseHelper.LYRICS));
                    Log.i("LOG_TAG", "ROW " + id + " HAS NAME " + name + " Lyrics " + lyrics);
                }
                cursor.close();
                break;
            default:
                break;
        }
    }

    public List<String> getSearchList(int type) {
        List<String> list = new ArrayList<>();
        String searchCriteria;
        String search;
        switch (type) {
            case 1:
                searchCriteria = mSearchCriteriaEt.getText().toString();
                search = "select " + mDatabaseHelper.SONGNAME + " from " + mDatabaseHelper.TABLE_NAME_SONGS + " where " + mDatabaseHelper.SONGNAME + " like '%" + searchCriteria + "%'";
                Cursor cursorS = mDatabase.rawQuery(search,null);
                while (cursorS.moveToNext()) {
                    list.add(cursorS.getString(cursorS.getColumnIndex(mDatabaseHelper.SONGNAME)));
                }
                cursorS.close();
                break;
            case 2:
                searchCriteria = mSearchCriteriaEt.getText().toString();
                search = "select " + mDatabaseHelper.PERFORMER_NAME + " from " + mDatabaseHelper.TABLE_NAME_PERFORMER + " where " + mDatabaseHelper.PERFORMER_NAME + " like '%" + searchCriteria + "%'";
                Cursor cursorP = mDatabase.rawQuery(search,null);
                while (cursorP.moveToNext()) {
                    list.add(cursorP.getString(cursorP.getColumnIndex(mDatabaseHelper.PERFORMER_NAME)));
                }
                cursorP.close();
                break;
            case 3:
                searchCriteria = mSearchCriteriaEt.getText().toString();
                search = "select " + mDatabaseHelper.GENRE_NAME + " from " + mDatabaseHelper.TABLE_NAME_GENRE + " where " + mDatabaseHelper.GENRE_NAME + " like '%" + searchCriteria + "%'";
                Cursor cursorG = mDatabase.rawQuery(search,null);
                while (cursorG.moveToNext()) {
                    list.add(cursorG.getString(cursorG.getColumnIndex(mDatabaseHelper.GENRE_NAME)));
                }
                cursorG.close();
                break;
            default:
                break;
        }
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.close();
        mDatabaseHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }
}

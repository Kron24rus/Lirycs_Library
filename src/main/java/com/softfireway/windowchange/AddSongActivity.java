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
 * Created by Александр on 16.03.2015.
 */
public class AddSongActivity extends FragmentActivity implements AddSongDialog.EditSongDetailsDialogListener {

    private int song_performer_type = 1;
    private LyricsDataBaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private DialogFragment mAddSongDialog;
    private TextView mTextSongName;
    private TextView mTextSongLyrics;
    private TextView mTextGroupName;
    private ArrayList<String> performers_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_song_activity);

        mDatabaseHelper = new LyricsDataBaseHelper(this);
        mDatabase = mDatabaseHelper.getWritableDatabase();

        mTextSongName = (TextView)findViewById(R.id.tv_song_name);
        mTextSongLyrics = (TextView)findViewById(R.id.tv_song_lyrics);
        mTextGroupName = (TextView)findViewById(R.id.tv_performer);
        mAddSongDialog = new AddSongDialog();
    }

    public void onClickDB(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                int tmp1;
                ContentValues cv = new ContentValues();
                cv.put(mDatabaseHelper.SONGNAME, mTextSongName.getText().toString());
                cv.put(mDatabaseHelper.LYRICS, mTextSongLyrics.getText().toString());
                mDatabase.insert(mDatabaseHelper.TABLE_NAME_SONGS, null, cv);
                ContentValues cvP = new ContentValues();
                cvP.put(mDatabaseHelper.PERFORMER_TYPE, song_performer_type);
                cvP.put(mDatabaseHelper.PERFORMER_NAME, mTextGroupName.getText().toString());

                ContentValues cvLink = new ContentValues();

                String test = "select _id from performer where perfname = (select perfname from performer where perfname = '" + mTextGroupName.getText().toString() + "')";
                Cursor cursorTs = mDatabase.rawQuery(test,null);
                if (cursorTs.getCount() != 0) {
                    cursorTs.moveToFirst();
                    tmp1 = cursorTs.getInt(cursorTs.getColumnIndex(mDatabaseHelper._ID));
                    Toast.makeText(this, tmp1+"", Toast.LENGTH_LONG).show();
                }
                else {
                    mDatabase.insert(mDatabaseHelper.TABLE_NAME_PERFORMER, mDatabaseHelper.PERFORMER_NAME, cvP);


                    Cursor cursor = mDatabase.query(mDatabaseHelper.TABLE_NAME_PERFORMER, null, null, null, null, null, null);
                    cursor.moveToLast();
                    tmp1 = (cursor.getInt(cursor.getColumnIndex(mDatabaseHelper._ID)));
                    cursor.close();
                }
                int tmp2;
                Cursor cursor1 = mDatabase.query(mDatabaseHelper.TABLE_NAME_SONGS, null, null, null, null, null, null);
                cursor1.moveToLast();
                tmp2 = (cursor1.getInt(cursor1.getColumnIndex(mDatabaseHelper._ID)));
                cursor1.close();

           //     Toast.makeText(this, tmp1 + " " + tmp2, Toast.LENGTH_SHORT).show();

                cvLink.put(mDatabaseHelper.FK_PERFORMER_ID, tmp1);
                cvLink.put(mDatabaseHelper.FK_SONG_ID, tmp2);
                mDatabase.insert(mDatabaseHelper.TABLE_SONG_PERFORMER, null, cvLink);



                mTextSongName.setText("");
                mTextSongLyrics.setText("");
                mTextGroupName.setText("");
                Toast.makeText(this, "Song successfully added", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_add_song_name:
                mAddSongDialog.show(getFragmentManager(), "AddSongNameDialog");
                break;
            case R.id.btn_add_song_lyrics:
                mAddSongDialog.show(getFragmentManager(), "AddSongLyricsDialog");
                break;
            case R.id.btn_add_performer:
                Bundle bundle = new Bundle();
                bundle.putInt("perf_type", song_performer_type);
                bundle.putStringArrayList("perf_list", performers_list);
                mAddSongDialog.setArguments(bundle);
                mAddSongDialog.show(getFragmentManager(), "AddGroupNameDialog");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFinishEditGroupNameDialog(String inputText, ArrayList<String> perf_list, int performer_type) {
        mTextGroupName.setText(inputText);
        performers_list = perf_list;
        song_performer_type = performer_type;
    }

    @Override
    public void onFinishEditSongNameDialog(String inputSongName) {
        mTextSongName.setText(inputSongName);
    }

    @Override
    public void onFinishEditSongLyricsDialog(String inputSongLyrics) {
        mTextSongLyrics.setText(inputSongLyrics);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SongName", mTextSongName.getText().toString());
        outState.putString("Lyrics", mTextSongLyrics.getText().toString());
        outState.putString("Group", mTextGroupName.getText().toString());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTextSongName.setText(savedInstanceState.getString("SongName"));
        mTextSongLyrics.setText(savedInstanceState.getString("Lyrics"));
        mTextGroupName.setText(savedInstanceState.getString("Group"));
    }

}

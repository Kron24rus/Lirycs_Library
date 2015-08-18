package com.softfireway.windowchange;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Александр on 12.03.2015.
 */
public class LyricsDataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private final Context fContext;

    private static final String DATABASE_NAME = "lyrics_database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_SONGS = "songs";
    public static final String SONGNAME = "songname";
    public static final String LYRICS = "lyrics";
    public static final String TABS = "tabs";
    public static final String STRUM = "strum";
    public static final String FINGERING = "fingering";
    public static final String TABLE_NAME_ACCORD = "accords";
    public static final String ACCORD_NAME = "accordname";
    public static final String ACCORD_APPLICATURA = "applicatura";
    public static final String TABLE_NAME_PERFORMER = "performer";
    public static final String PERFORMER_NAME = "perfname";
    public static final String TABLE_NAME_GROUP = "groups";
    public static final String TABLE_SONG_GROUP = "song_group";
    public static final String FK_SONG_ID = "_idsong";
    public static final String FK_ACCORD_ID = "_idaccord";
    public static final String FK_PERFORMER_ID = "_idperformer";
    public static final String TABLE_SONG_ACCORD = "song_accord";
    public static final String TABLE_NAME_VIDEO = "video";
    public static final String TABLE_NAME_AUDIO = "audio";
    public static final String TABLE_NAME_GENRE = "genre";
    public static final String TABLE_SONG_GENRE = "song_genre";
    public static final String FK_GENRE_ID = "_idgenre";
    public static final String DESCRIPTION = "description";
    public static final String WEB_URL = "url";
    public static final String GENRE_NAME = "genrename";
    public static final String PERFORMER_TYPE = "performertype";
    public static final String TABLE_NAME_MEMBERS = "members";
    public static final String LIST_OF_MEMBERS = "memberslist";
    public static final String TABLE_SONG_PERFORMER = "song_performer";


    private static final String SQL_CREATE_TABLE_SONG = "CREATE TABLE " + TABLE_NAME_SONGS +
            " (" + LyricsDataBaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + SONGNAME + " VARCHAR(30), "
                    + LYRICS + " TEXT, "
                    + STRUM + " TEXT, "
                    + TABS + " TEXT, "
                    + FINGERING + " TEXT);";

    private static final String SQL_CREATE_TABLE_CHORDS = "CREATE TABLE " + TABLE_NAME_ACCORD +
            " (" + LyricsDataBaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ACCORD_NAME + " VARCHAR(15), "
                    + ACCORD_APPLICATURA + " TEXT);";

    public static final String SQL_CREATE_TABLE_PERFORMER = "CREATE TABLE " + TABLE_NAME_PERFORMER +
            " (" + LyricsDataBaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + PERFORMER_NAME + " VARCHAR(50),"
                    + DESCRIPTION + " TEXT,"
                    + PERFORMER_TYPE + " INTEGER);";

    public static final String SQL_CREATE_TABLE_MEMBERS = "CREATE TABLE " + TABLE_NAME_MEMBERS +
            " (" + FK_PERFORMER_ID + " INTEGER,"
                    + LIST_OF_MEMBERS + " TEXT,"
                    + "FOREIGN KEY(" + FK_PERFORMER_ID + ") REFERENCES " + TABLE_NAME_PERFORMER + "(" + _ID + ") ON DELETE CASCADE);";

    public static final String SQL_CREATE_TABLE_SONG_PERFORMER = "CREATE TABLE " + TABLE_SONG_PERFORMER +
            " (" + FK_PERFORMER_ID + " INTEGER,"
                    + FK_SONG_ID + " INTEGER,"
                    + "PRIMARY KEY(" + FK_SONG_ID + ", " + FK_PERFORMER_ID + "), "
                    + "FOREIGN KEY(" + FK_PERFORMER_ID + ") REFERENCES " + TABLE_NAME_PERFORMER + "(" + _ID + ") ON DELETE CASCADE,"
                    + "FOREIGN KEY(" + FK_SONG_ID + ") REFERENCES " + TABLE_NAME_SONGS + "(" + _ID + ") ON DELETE CASCADE);";

    public static final String SQL_CREATE_TABLE_SONG_ACCORD = "CREATE TABLE " + TABLE_SONG_ACCORD +
            " (" + FK_SONG_ID + " INTEGER,"
                    + FK_ACCORD_ID + " INTEGER,"
                    + "PRIMARY KEY(" + FK_ACCORD_ID + ", " + FK_SONG_ID + "), "
                    + "FOREIGN KEY(" + FK_ACCORD_ID + ") REFERENCES " + TABLE_NAME_ACCORD + "(" + _ID + ") ON DELETE CASCADE,"
                    + "FOREIGN KEY(" + FK_SONG_ID + ") REFERENCES " + TABLE_NAME_SONGS + "(" + _ID + ") ON DELETE CASCADE);";

    public static final String SQL_CREATE_TABLE_AUDIO = "CREATE TABLE " + TABLE_NAME_AUDIO +
            " (" + LyricsDataBaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DESCRIPTION + " TEXT,"
                    + WEB_URL + " TEXT,"
                    + FK_SONG_ID + " INTEGER REFERENCES " + TABLE_NAME_SONGS + "(" + _ID + ") ON DELETE CASCADE);";

    public static final String SQL_CREATE_TABLE_VIDEO = "CREATE TABLE " + TABLE_NAME_VIDEO +
            " (" + LyricsDataBaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DESCRIPTION + " TEXT,"
                    + WEB_URL + " TEXT,"
                    + FK_SONG_ID + " INTEGER REFERENCES " + TABLE_NAME_SONGS + "(" + _ID + ") ON DELETE CASCADE);";

    public static final String SQL_CREATE_TABLE_GENRE = "CREATE TABLE " + TABLE_NAME_GENRE +
            " (" + LyricsDataBaseHelper._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + GENRE_NAME + " VARCHAR(50));";

    public static final String SQL_CREATE_TABLE_SONG_GENRE = "CREATE TABLE " + TABLE_SONG_GENRE +
            " (" + FK_SONG_ID + " INTEGER,"
                    + FK_GENRE_ID + " INTEGER,"
                    + "PRIMARY KEY(" + FK_SONG_ID + ", " + FK_GENRE_ID + "), "
                    + "FOREIGN KEY(" + FK_SONG_ID + ") REFERENCES " + TABLE_NAME_SONGS + "(" + _ID + ") ON DELETE CASCADE,"
                    + "FOREIGN KEY(" + FK_GENRE_ID + ") REFERENCES " + TABLE_NAME_GENRE + "(" + _ID + ") ON DELETE CASCADE);";

    private static final String SQL_DELETE_TABLE_SONG = "DROP TABLE IF EXISTS " + TABLE_NAME_SONGS;
    private static final String SQL_DELETE_TABLE_CHORDS = "DROP TABLE IF EXISTS " + TABLE_NAME_ACCORD;
    private static final String SQL_DELETE_TABLE_PERFORMER = "DROP TABLE IF EXISTS " + TABLE_NAME_PERFORMER;
    private static final String SQL_DELETE_TABLE_GROUP = "DROP TABLE IF EXISTS " + TABLE_NAME_GROUP;
    private static final String SQL_DELETE_TABLE_SONG_GROUP = "DROP TABLE IF EXISTS " + TABLE_SONG_GROUP;
    private static final String SQL_DELETE_TABLE_SONG_ACCORD = "DROP TABLE IF EXISTS " + TABLE_SONG_ACCORD;
    private static final String SQL_DELETE_TABLE_GENRE = "DROP TABLE IF EXISTS " + TABLE_NAME_GENRE;
    private static final String SQL_DELETE_TABLE_AUDIO = "DROP TABLE IF EXISTS " + TABLE_NAME_AUDIO;
    private static final String SQL_DELETE_TABLE_VIDEO = "DROP TABLE IF EXISTS " + TABLE_NAME_VIDEO;
    private static final String SQL_DELETE_TABLE_SONG_GENRE = "DROP TABLE IF EXISTS " + TABLE_SONG_GENRE;
    private static final String SQL_DELETE_TABLE_MEMBERS = "DROP TABLE IF EXISTS " + TABLE_NAME_MEMBERS;
    private static final String SQL_DELETE_TABLE_SONG_PERFORMER = "DROP TABLE IF EXISTS " + TABLE_SONG_PERFORMER;
    private static final String SQL_DELETE_TRIGGER_CHECKNAME = "DROP TRIGGER IF EXISTS " + "checkNames";
    private static final String SQL_DELETE_TRIGGER_REMOVE_SP_ASSOCIATION = "DROP TRIGGER IF EXISTS " + "remspassocation";

    private static final String SQL_CREATE_TRIGGER_REMOVE_SP_ASSOCATION = "create trigger remspassocation before delete on "
            + TABLE_NAME_SONGS + " for each row begin delete from " + TABLE_SONG_PERFORMER + " where " + FK_SONG_ID + " = old._ID;"
            + "end;";

    private static final String SQL_CREATE_TRIGGER_CHECKNAME = "create trigger checkname after insert on songs" +
       " when (select count (songname) from songs where songname = new.songname) > 1" +
       " begin"
       + " update songs set songname = (songname || '(' || new._ID || ')') where _ID = new._ID; " +
        "end;";

    public LyricsDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(SQL_CREATE_TABLE_SONG);
        db.execSQL(SQL_CREATE_TABLE_CHORDS);
        db.execSQL(SQL_CREATE_TABLE_PERFORMER);
        db.execSQL(SQL_CREATE_TABLE_MEMBERS);
        db.execSQL(SQL_CREATE_TABLE_SONG_ACCORD);
        db.execSQL(SQL_CREATE_TABLE_AUDIO);
        db.execSQL(SQL_CREATE_TABLE_VIDEO);
        db.execSQL(SQL_CREATE_TABLE_GENRE);
        db.execSQL(SQL_CREATE_TABLE_SONG_GENRE);
        db.execSQL(SQL_CREATE_TABLE_SONG_PERFORMER);
        db.execSQL(SQL_CREATE_TRIGGER_CHECKNAME);
        db.execSQL(SQL_CREATE_TRIGGER_REMOVE_SP_ASSOCATION);

        ContentValues values = new ContentValues();

        Resources res = fContext.getResources();

        XmlResourceParser _xml = res.getXml(R.xml.chords_records);
        try {
            int eventType = _xml.getEventType();
            while (eventType != _xml.END_DOCUMENT) {
                if ((eventType == XmlPullParser.START_TAG) && (_xml.getName().equals("record"))) {
                    String title = _xml.getAttributeValue(0);
                    values.put("accordname", title);
                    db.insert(TABLE_NAME_ACCORD, null, values);
                }
                eventType = _xml.next();
            }
        }
        catch (XmlPullParserException e) {
            Log.e("Test", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Test", e.getMessage(), e);
        } finally {
            _xml.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("LOG_TAG", "Обновление базы данных с версии " + oldVersion + " до версии " + newVersion + ", которое удалит все старые данные");
        db.execSQL(SQL_DELETE_TABLE_SONG);
        db.execSQL(SQL_DELETE_TABLE_CHORDS);
        db.execSQL(SQL_DELETE_TABLE_PERFORMER);
        db.execSQL(SQL_DELETE_TABLE_SONG_GROUP);
        db.execSQL(SQL_DELETE_TABLE_SONG_ACCORD);
        db.execSQL(SQL_DELETE_TABLE_GENRE);
        db.execSQL(SQL_DELETE_TABLE_AUDIO);
        db.execSQL(SQL_DELETE_TABLE_VIDEO);
        db.execSQL(SQL_DELETE_TABLE_SONG_GENRE);
        db.execSQL(SQL_DELETE_TABLE_MEMBERS);
        db.execSQL(SQL_DELETE_TABLE_SONG_PERFORMER);
        db.execSQL(SQL_DELETE_TABLE_GROUP);
        db.execSQL(SQL_DELETE_TRIGGER_CHECKNAME);
        db.execSQL(SQL_DELETE_TRIGGER_REMOVE_SP_ASSOCIATION);
        onCreate(db);
    }
}

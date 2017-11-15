package com.example.android.chatapphodlife.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.chatapphodlife.data.ChatContract.ChatEntry;

/**
 * Created by Hrishikesh Kadam on 15/11/2017
 */

public class ChatDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chatDb.db";
    private static final int VERSION = 1;

    public ChatDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + ChatEntry.TABLE_NAME + " (" +
                ChatEntry._ID + " INTEGER PRIMARY KEY, " +
                ChatEntry.COLUMN_RECIPIENT + " TEXT, " +
                ChatEntry.COLUMN_MESSAGE + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

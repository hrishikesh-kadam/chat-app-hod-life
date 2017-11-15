package com.example.android.chatapphodlife.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.chatapphodlife.data.ChatContract.ChatEntry;

/**
 * Created by Hrishikesh Kadam on 15/11/2017
 */

public class ChatContentProvider extends ContentProvider {

    private static final int CHAT_ALL = 1;
    private static final int CHAT_WITH_ID = 2;
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private ChatDbHelper chatDbHelper;

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(ChatContract.AUTHORITY, ChatContract.PATH_CHAT, CHAT_ALL);
        uriMatcher.addURI(ChatContract.AUTHORITY, ChatContract.PATH_CHAT + "/#", CHAT_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        chatDbHelper = new ChatDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        final SQLiteDatabase db = chatDbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {

            case CHAT_ALL:
                cursor = db.query(ChatEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri + " in query method");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("getType not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Uri returnUri;
        final SQLiteDatabase db = chatDbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {

            case CHAT_WITH_ID:
                long id = db.insert(ChatEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(ChatEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri + " in insert method");

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int noOfRowsDeleted;
        final SQLiteDatabase db = chatDbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {

            case CHAT_ALL:
                noOfRowsDeleted = db.delete(ChatEntry.TABLE_NAME,
                        null,
                        null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri + " in delete method");
        }

        if (noOfRowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return noOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update method not yet implemented");
    }
}

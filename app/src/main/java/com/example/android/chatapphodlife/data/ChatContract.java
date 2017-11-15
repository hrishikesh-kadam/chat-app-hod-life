package com.example.android.chatapphodlife.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hrishikesh Kadam on 15/11/2017
 */

public class ChatContract {

    public static final String AUTHORITY = "com.example.android.chatapphodlife";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_CHAT = "chat";

    public static final class ChatEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHAT).build();

        public static final String TABLE_NAME = "chat";

        public static final String COLUMN_RECIPIENT = "recipient";
        public static final String COLUMN_MESSAGE = "message";

        public static final String RECIPIENT_SENDER = "sender";
        public static final String RECIPIENT_RECEIVER = "receiver";
    }
}

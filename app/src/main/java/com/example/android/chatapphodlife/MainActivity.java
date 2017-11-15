package com.example.android.chatapphodlife;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.chatapphodlife.data.ChatContract.ChatEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.editTextMessage)
    EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-> onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonSend)
    public void clickButtonSend(Button buttonSend) {
        Log.v(LOG_TAG, "-> clickButtonSend");

        ContentValues contentValues = new ContentValues();
        contentValues.put(ChatEntry.COLUMN_RECEIPIENT, ChatEntry.RECEIPIENT_SENDER);
        contentValues.put(ChatEntry.COLUMN_MESSAGE, editTextMessage.getText().toString());

        Uri uri = getContentResolver().insert(ChatEntry.CONTENT_URI, contentValues);
        Log.v(LOG_TAG, "-> clickButtonSend -> row inserted at " + uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(LOG_TAG, "-> onOptionsItemSelected -> " + item.getTitle());

        switch (item.getItemId()) {

            case R.id.deleteAll:

                int noOfRowsDeleted = getContentResolver().delete(
                        ChatEntry.CONTENT_URI,
                        null, null);

                Log.v(LOG_TAG, "-> onOptionsItemSelected -> " + item.getTitle() + " -> noOfRowsDeleted = " + noOfRowsDeleted);
                Toast.makeText(this, R.string.all_messages_deleted, Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

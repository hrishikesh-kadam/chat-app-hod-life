package com.example.android.chatapphodlife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.chatapphodlife.data.ChatContract.ChatEntry;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.editTextMessage)
    EditText editTextMessage;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ChatAdapter chatAdapter;
    private Cursor cursor;
    private String[] quotes;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-> onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        quotes = getResources().getStringArray(R.array.quotes);

        initRecyclerView();
    }

    public void initRecyclerView() {
        Log.v(LOG_TAG, "-> initRecyclerView");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        cursor = getContentResolver().query(
                ChatEntry.CONTENT_URI, null, null, null, null);

        chatAdapter = new ChatAdapter(this, cursor);

        recyclerView.setAdapter(chatAdapter);
    }

    @OnClick(R.id.buttonSend)
    public void clickButtonSend(Button buttonSend) {
        Log.v(LOG_TAG, "-> clickButtonSend");

        String message = editTextMessage.getText().toString().trim();
        editTextMessage.setText("");

//        if (TextUtils.isEmpty(message))
//            return;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ChatEntry.COLUMN_RECIPIENT, ChatEntry.RECIPIENT_SENDER);
        contentValues.put(ChatEntry.COLUMN_MESSAGE, message);

        Uri uri = getContentResolver().insert(ChatEntry.CONTENT_URI, contentValues);
        Log.v(LOG_TAG, "-> clickButtonSend -> row inserted at " + uri);

        int randomNumber = random.nextInt(quotes.length);

        contentValues = new ContentValues();
        contentValues.put(ChatEntry.COLUMN_RECIPIENT, ChatEntry.RECIPIENT_RECEIVER);
        contentValues.put(ChatEntry.COLUMN_MESSAGE, quotes[randomNumber]);

        uri = getContentResolver().insert(ChatEntry.CONTENT_URI, contentValues);
        Log.v(LOG_TAG, "-> clickButtonSend -> row inserted at " + uri);

        cursor = getContentResolver().query(
                ChatEntry.CONTENT_URI, null, null, null, null);
        chatAdapter.swapCursor(cursor);
        chatAdapter.notifyDataSetChanged();
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

                cursor = null;
                chatAdapter.swapCursor(cursor);
                chatAdapter.notifyDataSetChanged();

                Toast.makeText(this, R.string.all_messages_deleted, Toast.LENGTH_SHORT).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean clearEditTextMessageFocus() {
        Log.v(LOG_TAG, "-> clearEditTextMessageFocus");

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        editTextMessage.clearFocus();

        return true;
    }

    //@OnClick(R.id.editTextMessage)
    public void clickEditTextMessage() {
        Log.v(LOG_TAG, "-> clickEditTextMessage");

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        }, 100);
    }
}

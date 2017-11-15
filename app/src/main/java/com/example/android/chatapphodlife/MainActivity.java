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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.chatapphodlife.data.ChatContract.ChatEntry;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity implements CustomEditText.onKeyImeChangeListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.editTextMessage)
    CustomEditText editTextMessage;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ChatAdapter chatAdapter;
    private Cursor cursor;
    private String[] quotes;
    private Random random = new Random();
    private boolean isSoftKeyboardVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-> onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        editTextMessage.setKeyImeChangeListener(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v(LOG_TAG, "-> onBackPressed");

        isSoftKeyboardVisible = false;
    }

    public void onKeyPreIme(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.v(LOG_TAG, "-> onKeyPreIme");
            isSoftKeyboardVisible = false;
        }
    }

    @OnTouch(R.id.editTextMessage)
    public boolean touchEditTextMessage() {
        Log.v(LOG_TAG, "-> touchEditTextMessage");

        isSoftKeyboardVisible = true;

        return false;
    }

    public void hideSoftKeyboard() {
        Log.v(LOG_TAG, "-> hideSoftKeyboard");

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            iMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(LOG_TAG, "-> onSaveInstanceState");

        outState.putBoolean("isSoftKeyboardVisible", isSoftKeyboardVisible);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(LOG_TAG, "-> onRestoreInstanceState");

        isSoftKeyboardVisible = savedInstanceState.getBoolean("isSoftKeyboardVisible");

        if (!isSoftKeyboardVisible) {
            editTextMessage.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSoftKeyboard();
                }
            }, 200);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "-> onResume");

        if (!isSoftKeyboardVisible) {
            editTextMessage.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideSoftKeyboard();
                }
            }, 100);
        }
    }
}

package com.example.android.chatapphodlife;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by Hrishikesh Kadam on 16/11/2017
 */

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    private onKeyImeChangeListener onKeyImeChangeListenerListener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setKeyImeChangeListener(onKeyImeChangeListener listener) {
        onKeyImeChangeListenerListener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if (onKeyImeChangeListenerListener != null) {
            onKeyImeChangeListenerListener.onKeyPreIme(keyCode, event);
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface onKeyImeChangeListener {
        public void onKeyPreIme(int keyCode, KeyEvent event);
    }
}

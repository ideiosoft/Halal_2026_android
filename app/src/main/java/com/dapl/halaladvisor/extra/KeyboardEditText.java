package com.dapl.halaladvisor.extra;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.annotation.NonNull;

/**
 * Created by DAT-Asset-128 on 26-09-2016.
 */
public class KeyboardEditText extends EditText {

    public KeyboardEditText(Context context) {
        super(context);
    }

    public KeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (listener != null)
            listener.onStateChanged(this, true);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //Here it catch all back keys
            //Now you can do what you want.

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            // Eat the event
            return true;
        }
        return false;
    }

    /**
     * Keyboard Listener
     */
    KeyboardListener listener;

    public void setOnKeyboardListener(KeyboardListener listener) {
        this.listener = listener;
    }

    public interface KeyboardListener {
        void onStateChanged(KeyboardEditText keyboardEditText, boolean showing);
    }
}

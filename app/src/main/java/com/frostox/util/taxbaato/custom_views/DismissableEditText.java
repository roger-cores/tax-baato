package com.frostox.util.taxbaato.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.frostox.util.taxbaato.events.KeyboardDismissedListener;

/**
 * Created by roger on 1/4/2017.
 */
public class DismissableEditText extends EditText {

    KeyboardDismissedListener listener;

    public DismissableEditText(Context context) {
        super(context);
    }

    public DismissableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DismissableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DismissableEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //Here it catch all back keys
            //Now you can do what you want.

            Log.d("Dismissed", "ho gaya");
            if(listener!=null)
                listener.onKeyboardDismissed();

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            // Eat the event
            return true;
        }
        return false;
    }

    public void setOnKeyboardDismissedListener(KeyboardDismissedListener listener){
        this.listener = listener;
    }
}

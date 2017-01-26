package com.frostox.util.taxbaato.viewholders;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.frostox.util.taxbaato.R;
import com.frostox.util.taxbaato.activities.TaxSplitterActivity;
import com.frostox.util.taxbaato.custom_views.DismissableEditText;
import com.frostox.util.taxbaato.entities.Person;
import com.frostox.util.taxbaato.events.KeyboardDismissedListener;

import org.w3c.dom.Text;

/**
 * Created by roger on 1/4/2017.
 */
public class PersonViewHolder extends RecyclerView.ViewHolder{

    public TextView nameTV, amountTV, taxTV, totalTV;
    public DismissableEditText nameET, amountET;
    private Context context;
    private Person person;

    public PersonViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;

        nameTV = (TextView) itemView.findViewById(R.id.person_item_name_textview);
        amountTV = (TextView) itemView.findViewById(R.id.person_item_amount_textview);
        taxTV = (TextView) itemView.findViewById(R.id.person_item_tax);
        totalTV = (TextView) itemView.findViewById(R.id.person_item_total_amount);

        nameET = (DismissableEditText) itemView.findViewById(R.id.person_item_name_edittext);
        amountET = (DismissableEditText) itemView.findViewById(R.id.person_item_amount_edittext);

        nameET.setOnKeyboardDismissedListener(new KeyboardDismissedListener() {
            @Override
            public void onKeyboardDismissed() {
                nameETKeyboardDismissedEvent();
            }
        });

        nameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                nameETKeyboardDismissedEvent();
                return true;
            }
        });

        nameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    nameETKeyboardDismissedEvent();
                } else {

                }
            }
        });

        nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameTV.setVisibility(View.INVISIBLE);
                nameET.setVisibility(View.VISIBLE);
                nameET.requestFocus();
                InputMethodManager imm = (InputMethodManager) PersonViewHolder.this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(nameET, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        amountET.setOnKeyboardDismissedListener(new KeyboardDismissedListener() {
            @Override
            public void onKeyboardDismissed() {
                amountETKeyboardDismissedEvent();
            }
        });

        amountET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                amountETKeyboardDismissedEvent();
                return true;
            }
        });

        amountET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    amountETKeyboardDismissedEvent();
                }
            }
        });

        amountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountTV.setVisibility(View.INVISIBLE);
                amountET.setVisibility(View.VISIBLE);
                amountET.requestFocus();
                InputMethodManager imm = (InputMethodManager) PersonViewHolder.this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(amountET, InputMethodManager.SHOW_IMPLICIT);


                amountET.setSelection(amountET.getText().length());
            }
        });
    }

    public void nameETKeyboardDismissedEvent(){
        String value = nameET.getText().toString();

        if(value.equals(""))
            nameTV.setText(context.getString(R.string.tap_to_edit_name));
        else{
            nameTV.setText(value);
            getPerson().setName(value);
        }



        nameTV.setVisibility(View.VISIBLE);
        nameET.setVisibility(View.INVISIBLE);
        nameET.clearFocus();
        nameTV.requestFocus();

        InputMethodManager inputManager =
                (InputMethodManager) context.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        if(((Activity) context).getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(
                    ((Activity) context).getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void amountETKeyboardDismissedEvent(){
        String value = amountET.getText().toString();
        Double doubleValue = 0d;
        try {
            doubleValue = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(doubleValue == 0)
            amountTV.setText(context.getString(R.string.tap_to_edit_name));
        else{
            amountTV.setText(TaxSplitterActivity.symbol + " " + String.format("%.2f", doubleValue) + "");
            getPerson().setAmount(Double.parseDouble(String.format("%.2f", doubleValue)));
        }
        amountET.setText("" + String.format("%.2f", doubleValue));


        amountTV.setVisibility(View.VISIBLE);
        amountET.setVisibility(View.INVISIBLE);
        amountET.clearFocus();
        amountTV.requestFocus();

        InputMethodManager inputManager =
                (InputMethodManager) context.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        if(((Activity) context).getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(
                    ((Activity) context).getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        ((TaxSplitterActivity) context).updateValues();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

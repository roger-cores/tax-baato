package com.frostox.util.taxbaato.viewholders;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.frostox.util.taxbaato.R;
import com.frostox.util.taxbaato.activities.TaxSplitterActivity;
import com.frostox.util.taxbaato.custom_views.DismissableEditText;
import com.frostox.util.taxbaato.entities.Person;
import com.frostox.util.taxbaato.events.CurrencySelectedListener;
import com.frostox.util.taxbaato.events.KeyboardDismissedListener;

/**
 * Created by roger on 1/4/2017.
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder{

    public TextView code, name;
    public View itemView;



    public CurrencyViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;
        code = (TextView) itemView.findViewById(R.id.currency_item_cur_code);
        name = (TextView) itemView.findViewById(R.id.currency_item_cur_name);


    }



}

package com.frostox.util.taxbaato.adapters;

import java.util.Currency;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frostox.util.taxbaato.R;
import com.frostox.util.taxbaato.events.CurrencySelectedListener;
import com.frostox.util.taxbaato.viewholders.CurrencyViewHolder;

import java.util.List;

/**
 * Created by roger on 1/5/2017.
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {

    List<Currency> currencies;

    CurrencySelectedListener currencySelectedListener;

    public CurrencyAdapter(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, null);
        return new CurrencyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        final Currency currency = currencies.get(position);
        holder.code.setText(currency.getCurrencyCode());
        holder.name.setText(currency.getDisplayName() + " (" + currency.getSymbol() + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currencySelectedListener != null)
                    currencySelectedListener.onCurrencySelected(currency);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(currencies == null) return 0;
        else {
            return currencies.size();
        }
    }

    @Override
    public long getItemId(int position) {
        if(currencies == null || currencies.size() == 0) return super.getItemId(position);
        else {
            return currencies.get(position).getCurrencyCode().hashCode();
        }
    }

    public void setCurrencySelectedListener(CurrencySelectedListener currencySelectedListener) {
        this.currencySelectedListener = currencySelectedListener;
    }
}

package com.frostox.util.taxbaato.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import java.util.Currency;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.frostox.util.taxbaato.R;
import com.frostox.util.taxbaato.adapters.CurrencyAdapter;
import com.frostox.util.taxbaato.adapters.PersonAdapter;
import com.frostox.util.taxbaato.custom_views.DismissableEditText;
import com.frostox.util.taxbaato.entities.Person;
import com.frostox.util.taxbaato.events.CurrencySelectedListener;
import com.frostox.util.taxbaato.events.KeyboardDismissedListener;
import com.frostox.util.taxbaato.viewholders.PersonViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TaxSplitterActivity extends AppCompatActivity {

    TextView totalAmountTV;
    DismissableEditText totalAmountET;
    PersonAdapter personAdapter;
    RecyclerView recyclerView;
    Snackbar errorSnackbar;
    public static String symbol;
    Set<Currency> currencies;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_splitter);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        symbol = sharedPreferences.getString(getString(R.string.current_symbol), getString(R.string.dollar_symbol));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_tax_splitter_container);
        errorSnackbar = Snackbar.make(coordinatorLayout, "Error", Snackbar.LENGTH_LONG);
        currencies = Currency.getAvailableCurrencies();
        totalAmountTV = (TextView) findViewById(R.id.content_tax_splitter_total_amount_textview);
        totalAmountET = (DismissableEditText) findViewById(R.id.content_tax_splitter_total_amount_edittext);
        totalAmountET.setOnKeyboardDismissedListener(new KeyboardDismissedListener() {
            @Override
            public void onKeyboardDismissed() {
                keyboardDismissedEvent();
            }
        });

        totalAmountET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                keyboardDismissedEvent();
                return true;
            }
        });

        totalAmountET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    keyboardDismissedEvent();
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.content_tax_splitter_recyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        personAdapter = new PersonAdapter(new ArrayList<Person>(), this);
        personAdapter.setHasStableIds(true);
        recyclerView.setAdapter(personAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                List people = personAdapter.getPeople();
                Person person = ((PersonViewHolder) viewHolder).getPerson();
                personAdapter.removePerson(person);
                updateValues();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void keyboardDismissedEvent(){
        String value = totalAmountET.getText().toString();
        if(value.endsWith(".")) value = value.replace(".", "");

        if(value.equals(""))
            totalAmountTV.setText(getString(R.string.tap_to_enter_total_amount));
        else {
            totalAmountTV.setText(symbol + " " + String.format("%.2f", Double.parseDouble(value)) + "/-");
            totalAmountET.setText(String.format("%.2f", Double.parseDouble(value)) + "");
        }

        totalAmountTV.setVisibility(View.VISIBLE);
        totalAmountET.setVisibility(View.INVISIBLE);
        totalAmountET.clearFocus();
        totalAmountTV.requestFocus();

        InputMethodManager inputManager =
                (InputMethodManager) TaxSplitterActivity.this.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        if(TaxSplitterActivity.this.getCurrentFocus()!=null)
            inputManager.hideSoftInputFromWindow(
                    TaxSplitterActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        updateValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tax_splitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            personAdapter.addPerson(new Person("", 0d, 0d));
            updateValues();
            recyclerView.scrollToPosition(personAdapter.getItemCount()-1);
            return true;
        } else if(id == R.id.action_share){
            String shareBody = getValuesInTextFormat();
            if(shareBody!=null) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "TaxBaato Tax Information");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }

        } else if(id == R.id.action_currency){
            View view = getLayoutInflater().inflate(R.layout.select_currency, null);
            RecyclerView recyclerView = (RecyclerView) view;
            CurrencyAdapter currencyAdapter = new CurrencyAdapter(new ArrayList<>(currencies));
            currencyAdapter.setHasStableIds(true);
            recyclerView.setAdapter(currencyAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());


            final AlertDialog dialog = new AlertDialog.Builder(this).setView(recyclerView)
                    .setCancelable(true)
                    .setTitle(R.string.select_a_currency)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            dialog.show();

            currencyAdapter.setCurrencySelectedListener(new CurrencySelectedListener() {
                @Override
                public void onCurrencySelected(Currency currency) {
                    symbol = currency.getSymbol();
                    sharedPreferences.edit().putString(getString(R.string.current_symbol), symbol).commit();
                    updateValues();
                    dialog.dismiss();
                    keyboardDismissedEvent();
                }
            });

        } else if(id == R.id.action_info){
            View view = getLayoutInflater().inflate(R.layout.about, null);
            final AlertDialog dialog = new AlertDialog.Builder(this).setView(view)
                    .setCancelable(true)
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void editTotalAmount(View view){
        totalAmountTV.setVisibility(View.INVISIBLE);
        totalAmountET.setVisibility(View.VISIBLE);
        totalAmountET.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(totalAmountET, InputMethodManager.SHOW_IMPLICIT);

        totalAmountET.setSelection(totalAmountET.getText().length());
    }

    public String getValuesInTextFormat(){
        List<Person> people = personAdapter.getPeople();
        Double totalAmount = 0d;
        Double taxedAmount = 0d;
        Double totalTax = 0d;

        try {
            taxedAmount = Double.parseDouble(totalAmountET.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            totalAmountET.setError("Please input the amount");
            errorSnackbar.setText("Please input the amount");
            errorSnackbar.show();
            return null;
        }
        for(Person person : people){
            totalAmount += person.getAmount();
        }

        if(taxedAmount<totalAmount){
            totalAmountET.setError("Taxed amount should be more than total of individual amounts!");
            errorSnackbar.setText("Taxed amount should be more than total of individual amounts!");
            errorSnackbar.show();
            return null;
        }

        errorSnackbar.dismiss();

        totalTax = taxedAmount - totalAmount;

        String shareBody = "";
        shareBody += "Total: " + symbol + " " + String.format("%.2f",totalAmount) + "\n";
        shareBody += "Tax: " + symbol + " " + String.format("%.2f",totalTax) + "\n";
        shareBody += "Taxed Amount: " + symbol + " " + String.format("%.2f",taxedAmount) + "\n\n";
        shareBody += "-----\n\n";
        for(Person person : people){
            shareBody += "Name: " + person.getName() + "\n";
            shareBody += symbol + " " + String.format("%.2f",person.getAmount()) + " + " + symbol + " " + String.format("%.2f",person.getTax()) + "(tax) = " + symbol + " " + String.format("%.2f",(person.getAmount() + person.getTax()));
            shareBody += "\n\n";
        }

        return shareBody;
    }

    public void updateValues(){
        totalAmountET.setError(null);
        List<Person> people = personAdapter.getPeople();
        Double totalAmount = 0d;
        Double taxedAmount = 0d;
        Double totalTax = 0d;
        try {
            taxedAmount = Double.parseDouble(totalAmountET.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            totalAmountET.setError("Please input the amount");
            errorSnackbar.setText("Please input the amount");
            errorSnackbar.show();
            return;
        }
        for(Person person : people){
            totalAmount += person.getAmount();
        }

        if(taxedAmount<totalAmount){
            totalAmountET.setError("Taxed amount should be more than total of individual amounts!");
            errorSnackbar.setText("Taxed amount should be more than total of individual amounts!");
            errorSnackbar.show();
            return;
        }

        errorSnackbar.dismiss();

        totalTax = taxedAmount - totalAmount;

        for(Person person : people){
            Double percentAmount = (person.getAmount()/totalAmount) * 100;
            person.setTax(Double.parseDouble(String.format("%.2f", (percentAmount/100) * totalTax)));


        }

        postAndNotifyAdapter(new Handler());


    }



    protected void postAndNotifyAdapter(final Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!recyclerView.isComputingLayout()) {
                    personAdapter.notifyDataSetChanged();
                } else {
                    postAndNotifyAdapter(handler);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }


}

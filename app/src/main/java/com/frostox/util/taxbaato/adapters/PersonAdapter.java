package com.frostox.util.taxbaato.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frostox.util.taxbaato.R;
import com.frostox.util.taxbaato.activities.TaxSplitterActivity;
import com.frostox.util.taxbaato.entities.Person;
import com.frostox.util.taxbaato.viewholders.PersonViewHolder;

import java.util.ArrayList;

/**
 * Created by roger on 1/4/2017.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    ArrayList<Person> people;
    Context context;

    public PersonAdapter(ArrayList<Person> people, Context context) {
        this.people = people;
        this.context = context;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, null);
        return new PersonViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person person = people.get(position);
        if(person.getName().equals("")){
            holder.nameTV.setText(context.getString(R.string.tap_to_edit_name));
            holder.nameET.setText("");
        }
        else{
            holder.nameTV.setText(person.getName());
            holder.nameET.setText(person.getName());
        }

        holder.taxTV.setText(TaxSplitterActivity.symbol + " " + person.getTax());
        holder.amountET.setText(person.getAmount() + "");

        if(person.getAmount()==null || person.getAmount() == 0){
            holder.amountTV.setText(R.string.tap_to_enter_amount);
        } else holder.amountTV.setText(TaxSplitterActivity.symbol + " " + person.getAmount());
        holder.totalTV.setText(TaxSplitterActivity.symbol + " " +  (Double.parseDouble(String.format("%.2f", (person.getTax() + person.getAmount())))));



        holder.setPerson(person);

    }

    @Override
    public int getItemCount() {
        if(people == null) return 0;
        else return people.size();
    }

    public void addPerson(Person person){
        people.add(person);
        this.notifyItemInserted(people.size()-1);
    }

    public void removePerson(int position){
        people.remove(position);
        this.notifyItemRemoved(position);
    }

    public void removePerson(Person person){
        int position = people.indexOf(person);
        people.remove(position);
        this.notifyItemRemoved(position);
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    @Override
    public long getItemId(int position) {
        if(people == null) return 0;
        else return people.get(position).getUUID().hashCode();
    }
}

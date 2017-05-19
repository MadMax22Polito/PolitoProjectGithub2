package com.example.jaumenartbaron.politoproject;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.jaumenartbaron.politoproject.R.id.textViewCost;

/**
 * Created by Jaume Nart Baron on 29/04/2017.
 */

public class ExpenseListViewAdapter extends ArrayAdapter<ExpenseInformation> {

    private Activity context;
    private List<ExpenseInformation> expenseList;
    //private List<String> costList;

    public ExpenseListViewAdapter(Activity context, List<ExpenseInformation> expenseList) {
        super(context, R.layout.single_row_expenses, expenseList);
        this.context = context;
        this.expenseList = expenseList;
        //this.costList = costList;
    }

    @NonNull
    @Override
    //gets called for every row (putting content)
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        //row object contains single_row_groups layout
        View row1 = inflater.inflate(R.layout.single_row_expenses, null, true);

        TextView textViewName = (TextView) row1.findViewById(R.id.textView3);
        TextView textViewCost = (TextView) row1.findViewById(R.id.textView4);

        ExpenseInformation expenseInformation = expenseList.get(position);
        //String costInformation = costList.get(position);
        //String expenseInformation = expenseList.get(position);

        String cost1 = String.valueOf(expenseInformation.cost);

        //textViewName.setText(expenseInformation);
        //textViewCost.setText(costInformation);
        textViewName.setText(expenseInformation.name);
        textViewCost.setText(cost1);

        return row1;


    }
}
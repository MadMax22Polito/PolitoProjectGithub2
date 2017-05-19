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

/**
 * Created by Jaume Nart Baron on 07/05/2017.
 */

public class SpinnerAdapter extends ArrayAdapter <String> {

    private Activity context;
    private List<String> MembersList;

    //override constructor
    public SpinnerAdapter(Activity context, List<String> MembersList)
    {
        super(context, R.layout.single_row_spinner,R.id.textViewMembers, MembersList);
        this.context = context;
        this.MembersList = MembersList;
    }

    //override getView method
    @NonNull
    @Override
    //gets called for every row (putting content)
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        //row object contains single_row_groups layout
        View row = inflater.inflate(R.layout.single_row_spinner, null, true);

        TextView textViewName = (TextView) row.findViewById(R.id.textViewMembers);

        String Membername = MembersList.get(position);
        textViewName.setText(Membername);

        return row;

    }
}

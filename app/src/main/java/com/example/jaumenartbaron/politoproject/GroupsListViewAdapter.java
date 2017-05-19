package com.example.jaumenartbaron.politoproject;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Jaume Nart Baron on 29/04/2017.
 */

public class GroupsListViewAdapter extends ArrayAdapter <String>
{
    private Activity context;
    private List<String> groupsList;
    int addimages;

    //override constructor
    public GroupsListViewAdapter(Activity context, List<String> groupsList, int addimgs)
    {
        super(context, R.layout.single_row_groups, groupsList);
        this.context = context;
        this.groupsList = groupsList;
        this.addimages = addimgs;
    }

    //override getView method
    @NonNull
    @Override
    //gets called for every row (putting content)
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        //row object contains single_row_groups layout
        View row = inflater.inflate(R.layout.single_row_groups, null, true);

        TextView textViewName = (TextView) row.findViewById(R.id.textView);
        ImageView addButton = (ImageView) row.findViewById(R.id.imageView2);

        String Groupname = groupsList.get(position);

        textViewName.setText(Groupname);
        addButton.setImageResource(addimages);

        return row;

    }
}

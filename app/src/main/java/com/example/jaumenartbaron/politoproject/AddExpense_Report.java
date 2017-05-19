package com.example.jaumenartbaron.politoproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;
import static android.os.Build.VERSION_CODES.M;


public class AddExpense_Report extends AppCompatActivity implements View.OnClickListener {

    private Button ButtonAddExpense;
    private EditText editName;
    private EditText editCost;
    private TextView GroupSelected;
    private TextView ShowMembers;
    private String ActualGroupName;
    private String ActualGroupKey;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference GroupRef = database.getReference("groups");
    DatabaseReference ActualRefGroup;
    DatabaseReference ActualRefExpenses;
    DatabaseReference ActualRefMembers;
    DatabaseReference RefMember;
    DatabaseReference ActualRefMember;


    ListView listViewExpenses;//Declare listview, responsible to display data
    ListView listViewMembers;//Declare listview, responsible to display data
    List<String> expensesList; //list containing group objects (name and description)
    List<String> costList; //list containing group objects (name and description)
    List<ExpenseInformation> ExpenseInformationList; //list containing group objects (name and description)
    List<String> MembersList; //list containing group objects (name and description)

    private List<String> members;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexpense_report);

        ButtonAddExpense = (Button) findViewById(R.id.buttonAddExpense);
        editName = (EditText) findViewById(R.id.editTextNameExpense);
        editCost = (EditText) findViewById(R.id.editTextCost);
        GroupSelected = (TextView) findViewById(R.id.textViewGroupSelected);

        ButtonAddExpense.setOnClickListener(this);

        listViewExpenses = (ListView) findViewById(R.id.listViewExpense);
        listViewMembers = (ListView) findViewById(R.id.listViewMembers);
        expensesList = new ArrayList<>();
        costList = new ArrayList<>();
        ExpenseInformationList = new ArrayList<>();
        MembersList = new ArrayList<>();
        members = new ArrayList<>();

        //recuperar datos que se han pasado por parametro a esta activity de la previa
        Bundle b2 = this.getIntent().getExtras(); //enlazarte con algo que tienes en tu mismo contexto es this, get extras para coger variables

        GroupSelected.setText(b2.getString("NOMBRE")); //cojer valor variable nombre i meterlo en objeto creado texto

        ActualGroupName = b2.getString("NOMBRE");

        listViewMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String groupInformationtemp = MembersList.get(position);
                members.add(groupInformationtemp);
                Toast.makeText(getBaseContext(),members.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

//--------------------------------------------------------------------------------------------------

    private void saveExpenseInformation() {

        final String name = editName.getText().toString().trim();
        final String cost = editCost.getText().toString().trim();
        final double value= Double.parseDouble(cost);

                GroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            //every time change in database
            //used to read values of database

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {

                    String GroupKey = groupSnapshot.getKey();
                    String GroupName = groupSnapshot.child("name").getValue().toString();

                    if (GroupName.equals(ActualGroupName))
                    {

                        ActualGroupKey = GroupKey;
                        ActualRefGroup = GroupRef.child(ActualGroupKey).getRef();

                        ActualRefGroup.child("expenses"); //Writing static

                        ActualRefExpenses = ActualRefGroup.child("expenses").getRef(); //Generating Expenses reference

                        String NewExpensesKey = ActualRefExpenses.push().getKey();//Writing dynamic

                        ExpenseInformation expenseInformation = new ExpenseInformation(name, value, members);
                        ActualRefExpenses.child(NewExpensesKey).setValue(expenseInformation);//Writing dynamic



                        //write balance

                         ActualRefMembers = ActualRefGroup.child("members").getRef(); //Generating members reference

                        //Iterate through members (different keys)
                        for (final DataSnapshot membersSnapshot : dataSnapshot.child(ActualGroupKey).child("members").getChildren())
                        {

                            String Memberkey = membersSnapshot.getKey();
                            String Membername = membersSnapshot.child("name").getValue().toString();


                            for(int i = 0; i < members.size(); i++)
                            {
                               if(Membername.equals(members.get(i)))
                                {

                                    RefMember = GroupRef.child(ActualGroupKey).child("members").child(Memberkey);

                                    RefMember.child("expenses");
                                    ActualRefMember = RefMember.child("expenses").getRef(); //Generating Expenses reference

                                    String NewExpensesMemberkey = ActualRefMember.push().getKey();//Writing dynamic
                                    ActualRefMember.child(NewExpensesMemberkey).setValue(value/members.size());//Writing dynamic
                                }
                            }

                        }
                        members.clear();

                    }
                }


            }



            //every time error in database
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, "Information saved", Toast.LENGTH_SHORT).show();
        editCost.setText("");
        editName.setText("");



    }

//--------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {

        if (v == ButtonAddExpense)
        {
            saveExpenseInformation();
        }

    }

//-----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();

        GroupRef.addValueEventListener(new ValueEventListener() {
            //every time change in database
            //used to read values of database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //expensesList.clear();
                //costList.clear();
                ExpenseInformationList.clear();
                MembersList.clear();

                //all data stored in DataSnapshot
                //getChildren gives access to all of the immediate children of this snapshot
                //we need to pass all data to groupsList(argument of adapter)
                //we iterate through snapshot fetching data to groupsList
                //The listener’s onDataChange() method is triggered whenever data at the monitored location changes. The method receives a snapshot of the data at that location which we can then access
                //getChildren() returns the children of this snapshot as an Iterable object. This enables us to get an Iterator so that we can treat the snapshot as a collection

                //Iterate through groups (different keys) and find actual group
                for (final DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {

                    String GroupKey = groupSnapshot.getKey();
                    String GroupName = groupSnapshot.child("name").getValue().toString();

                    if (GroupName.equals(ActualGroupName)) {

                        ActualGroupKey = GroupKey;
                        ActualRefGroup = GroupRef.child(ActualGroupKey).getRef();
                        ActualRefExpenses = ActualRefGroup.child("expenses").getRef(); //Generating Expenses reference


                            //Iterate through expenses (different keys)
                            for (final DataSnapshot expensesSnapshot : dataSnapshot.child(ActualGroupKey).child("expenses").getChildren())
                            {
                                ExpenseInformation expenseInformation = expensesSnapshot.getValue(ExpenseInformation.class);
                                ExpenseInformationList.add(expenseInformation);
                            }

                            ActualRefMembers = ActualRefGroup.child("members").getRef(); //Generating members reference

                            //Iterate through members (different keys)
                            for (final DataSnapshot membersSnapshot : dataSnapshot.child(ActualGroupKey).child("members").getChildren())
                            {
                                //String Expensecost = expensesSnapshot.child("cost").getValue().toString();
                                //costList.add(Expensecost);
                                //String Expensename = expensesSnapshot.child("name").getValue().toString();
                                //expensesList.add(Expensename);

                                String Membername = membersSnapshot.child("name").getValue().toString();
                                MembersList.add(Membername);
                            }

                    }
                }

                ExpenseListViewAdapter adapter1 = new ExpenseListViewAdapter(AddExpense_Report.this,ExpenseInformationList);
                listViewExpenses.setAdapter(adapter1);

                SpinnerAdapter adapter2 = new SpinnerAdapter(AddExpense_Report.this, MembersList);
                listViewMembers.setAdapter(adapter2);



            }
            //every time error in database
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
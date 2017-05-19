package com.example.jaumenartbaron.politoproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
{


    private TextView textViewUserName;
    private Button buttonLogout;

    private Button AddGroup;

    String UserName1;

    ListView listViewGroups;//Declare listview, responsible to display data
    List<String> groupsListNames; //list containing group objects (name and description)
    List<String> groupsListUsers; //list containing group objects (name and description)
    int addimages = R.drawable.add_icon; //image

    private FirebaseAuth firebaseAuth;

    private String ActualUserKey;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Ref = database.getReference();
    DatabaseReference RefGroups = database.getReference("groups");
    DatabaseReference RefUsers = database.getReference("users");
    DatabaseReference ActualRefUser;

    private Toolbar toolbar;
    private String[] category=null;

    List<String> SpinnerList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);





        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        textViewUserName = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogOut);
        AddGroup=(Button) findViewById(R.id.button2);

        buttonLogout.setOnClickListener(this);
        AddGroup.setOnClickListener(this);


        listViewGroups = (ListView) findViewById(R.id.listView);
        groupsListNames = new ArrayList<>();

        SpinnerList = new ArrayList<>();
        SpinnerList.add("$");
        SpinnerList.add("-$35");
        SpinnerList.add("-$55");


        category = getResources().getStringArray(R.array.category);

        SpinnerAdapter adapter1 = new SpinnerAdapter(ProfileActivity.this,SpinnerList);
        Spinner navigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        navigationSpinner.setAdapter(adapter1);
        toolbar.addView(navigationSpinner, 0);

        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProfileActivity.this,
                        "you selected: " + category[position],
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        listViewGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(getBaseContext(), " " + position, Toast.LENGTH_LONG).show();

                //lanzar segunda actividad con intent (clase que intenta lanzar actividad)
                //Creamos intent
                Intent intent = new Intent(ProfileActivity.this, AddExpense_Report.class); //intentar ir de Main ==> Main3

                //Creamos la informacion a pasar entre actividades
                Bundle b = new Bundle();

                String groupInformationtemp = groupsListNames.get(position);

                // coger contenido (string) objeto texto en layout y lo asignamos a la variable "NOMBRE" dentro de objeto b
                b.putString("NOMBRE", groupInformationtemp);

                //añadimos informacion al intent
                intent.putExtras(b);

                //iniciamos la nueva actividad
                startActivity(intent);
            }
        });




    }

//-----------------------------------------------------------------------------------------------------------------------------------------

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
        if(id==R.id.action_settings)
        {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {

        if(v == buttonLogout)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if(v == AddGroup)
        {
            startActivity(new Intent(this,AddGroup.class));
        }


    }

//-----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();


        Ref.addValueEventListener(new ValueEventListener() {



            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                for(DataSnapshot groupsSnapshot : dataSnapshot.child("users").getChildren())
                {

                    String UserKey = groupsSnapshot.getKey();
                    String EmailName = groupsSnapshot.child("email").getValue().toString();


                    if (EmailName.equals(user.getEmail())) {

                        String ActualUserNameKey = UserKey;
                        ActualRefUser = RefUsers.child(ActualUserNameKey).getRef();
                        UserName1 = groupsSnapshot.child("name").getValue().toString();
                    }

                }
                textViewUserName.setText("Welcome " + UserName1);

                groupsListNames.clear();

                //all data stored in DataSnapshot
                //getChildren gives access to all of the immediate children of this snapshot
                //we need to pass all data to groupsList(argument of adapter)
                //we iterate through snapshot fetching data to groupsList
                //The listener’s onDataChange() method is triggered whenever data at the monitored location changes. The method receives a snapshot of the data at that location which we can then access
                //getChildren() returns the children of this snapshot as an Iterable object. This enables us to get an Iterator so that we can treat the snapshot as a collection
                for(DataSnapshot groupsSnapshot : dataSnapshot.child("groups").getChildren())
                {
                    String GroupKey = groupsSnapshot.getKey();
                    String Groupname = groupsSnapshot.child("name").getValue().toString();

                    //Iterate through members (different keys)
                    for (final DataSnapshot membersSnapshot : dataSnapshot.child("groups").child(GroupKey).child("members").getChildren())
                    {
                        String Username = membersSnapshot.child("name").getValue().toString();

                        if (Username.equals(UserName1))
                        {
                            groupsListNames.add(Groupname);
                        }
                    }
                }
                
                GroupsListViewAdapter adapter = new GroupsListViewAdapter(ProfileActivity.this,groupsListNames,addimages);
                listViewGroups.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

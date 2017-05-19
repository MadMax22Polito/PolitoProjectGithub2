package com.example.jaumenartbaron.politoproject;


import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddGroup extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0 ;



    private FirebaseAuth firebaseAuth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference RefGroups = database.getReference("groups");
    DatabaseReference RefMembers;
    DatabaseReference RefUsers = database.getReference("users");

    private Button ButtonAddGroup;
    private Button ButtonAddMember;
    private Button ButtonAddNewUser;
    private EditText editName;
    private EditText editMembers;

    private ArrayList<String> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        firebaseAuth = FirebaseAuth.getInstance();

        ButtonAddGroup = (Button) findViewById(R.id.buttonAddGroup);
        ButtonAddMember = (Button) findViewById(R.id.buttonAddMember);
        ButtonAddNewUser = (Button) findViewById(R.id.buttonAddUser);
        editName = (EditText) findViewById(R.id.editTextName12);
        editMembers = (EditText) findViewById(R.id.editTextMembers);


        ButtonAddGroup.setOnClickListener(this);
        ButtonAddMember.setOnClickListener(this);
        ButtonAddNewUser.setOnClickListener(this);



    }

    private void saveGroupInformation()
    {
        String name = editName.getText().toString().trim();

        //push generates a child random key with no value, get the reference of that key
        String SpecificGroupkey = RefGroups.push().getKey();//Writing dynamic
        //go to the child of which we have the reference (key) and generate a child (key) called name, set the value of the name
        RefGroups.child(SpecificGroupkey).child("name").setValue(name);//Writing dynamic


        RefGroups.child(SpecificGroupkey).child("members"); //Writing static

        RefMembers = RefGroups.child(SpecificGroupkey).child("members").getRef();
        String NewMembersKey;


        for(int i=0;i<members.size();i++)
        {
            //Log.i("member",members.toString());

            //Method 1
            //memberskey = RefGroups.child(SpecificGroupkey).child("members").push().getKey();//Writing dynamic
            //RefGroups.child(SpecificGroupkey).child("members").child(memberskey).child("name").setValue(members.get(i));//Writing dynamic

            //Method 2 with RefMembers


            NewMembersKey = RefMembers.push().getKey();//Writing dynamic

            RefMembers.child(NewMembersKey).child("name").setValue(members.get(i));//Writing dynamic

        }

        Toast.makeText(this, "Information saved", Toast.LENGTH_SHORT).show();

    }

    private void saveMember()
    {

        final String newMember = editMembers.getText().toString().trim();

        RefUsers.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean userFound = false;

                for(DataSnapshot groupsSnapshot : dataSnapshot.getChildren())
                {

                    String UserKey = groupsSnapshot.getKey();
                    String UserName = groupsSnapshot.child("name").getValue().toString();


                    if (UserName.equals(newMember))
                    {
                        userFound = true;
                        members.add(newMember);
                        editMembers.setText("");
                        Toast.makeText(getBaseContext(), "Member added", Toast.LENGTH_SHORT).show();
                    }
                }

                if(!userFound)
                {
                    Toast.makeText(getBaseContext(), "Member is not registered", Toast.LENGTH_SHORT).show();
                    editMembers.setText("");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }






    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
               // .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }






    @Override
    public void onClick(View v)
    {
        if(v == ButtonAddGroup)
        {
            saveGroupInformation();
            finish();
            startActivity(new Intent(this,ProfileActivity.class));//go back to profile activity
        }
        if(v == ButtonAddMember)
        {
            saveMember();
        }
        if(v == ButtonAddNewUser)
        {
            onInviteClicked();
        }
    }
}

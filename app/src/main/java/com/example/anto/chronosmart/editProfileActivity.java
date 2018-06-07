package com.example.anto.chronosmart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class editProfileActivity  extends AppCompatActivity {

    private EditText inputName;
    private EditText inputAge;
    private EditText inputSurname;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private Button btnSubmit;
    private Button bBack;
    private String userID,email,age,name,surname,privilegi;

    private ListView mListView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        inputName = (EditText) findViewById(R.id.edName);
        inputSurname = (EditText) findViewById(R.id.edSurname);
        inputAge = (EditText) findViewById(R.id.edAge);

        btnSubmit = (Button) findViewById(R.id.updateData);
        bBack = (Button) findViewById(R.id.Bbutton);



        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        email = user.getEmail();
        userID = user.getUid();





        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {



                    // User is signed in
                    Log.d("signed_in", "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d("signedout", "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
                Log.d("add", "onDataChange: Added information to database: \n" +
                        dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("fail read value", "Failed to read value.", error.toException());
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Log.d("msg", "onClick: Attempting to submit to database: \n" +
                         "name: " + name + "\n" +
                         "surname: " + surname + "\n"
                 );

                 Intent setting = new Intent(editProfileActivity.this, UserAreaActivity.class);
                 //setting.putExtra("name", name);
                 //setting.putExtra("age", age);
                 editProfileActivity.this.startActivity(setting);

                 }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String surname = inputSurname.getText().toString();
                String age = inputAge.getText().toString();




                Log.d("msg", "onClick: Attempting to submit to database: \n" +
                        "name: " + name + "\n" +
                        "surname: " + surname + "\n" +
                        "age: " + age + "\n"
                );

                //handle the exception if the EditText fields are null
                if(!name.equals("") && !email.equals("")){
                    UserInformation userInformation = new UserInformation(name,surname,email,age,privilegi);
                    myRef.child("users").child(userID).setValue(userInformation);
                    toastMessage("New Information has been saved.");
                    inputName.setText(name);
                    inputSurname.setText(surname);
                    inputAge.setText(age);
                }else{
                    toastMessage("Fill out all the fields");
                }
            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();
            try {
                uInfo.setName(ds.child(userID).getValue(UserInformation.class).getName()); //set the name
                uInfo.setSurname(ds.child(userID).getValue(UserInformation.class).getSurname()); //set the name
                uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email
                uInfo.setAge(ds.child(userID).getValue(UserInformation.class).getAge()); //set the phone_num
                uInfo.setPrivilegi(ds.child(userID).getValue(UserInformation.class).getPrivilegi()); //set the phone_num


                //display all the information
                Log.d("read", "showData: name: " + uInfo.getName());
                Log.d("read", "showData: email: " + uInfo.getSurname());
                Log.d("read", "showData: age: " + uInfo.getAge());

                ArrayList<String> array = new ArrayList<>();
                array.add(uInfo.getName());
                array.add(uInfo.getSurname());
                array.add(uInfo.getEmail());
                array.add(uInfo.getAge());
                array.add(uInfo.getPrivilegi());
                // ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
                //mListView.setAdapter(adapter);

                name=uInfo.getName();
                surname=uInfo.getSurname();
                age=uInfo.getAge();
                privilegi=uInfo.getPrivilegi();

                inputName.setText(name);
                inputAge.setText(age);
                inputSurname.setText(surname);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



}


package com.example.anto.chronosmart;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class UserAreaActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID,email,name,surname,age,privilegi;
    private TextView tResult,tAge;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();


    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address ="00:14:03:06:62:F9";
    @Override
    public void onBackPressed() {

        //super.onBackPressed();
    }

    /* public void onDestroy() {
         super.onDestroy();
         disconnectFromFacebook();


     }*/ CallbackManager callbackManager;

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        Button logoutB = (Button) findViewById(R.id.logoutB);
        ImageButton logout = (ImageButton) findViewById(R.id.logut);
        ImageButton info = (ImageButton) findViewById(R.id.tInfo);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);

        TextView tMail = (TextView) findViewById(R.id.tMail);
        tAge = (TextView) findViewById(R.id.tAge);
        tResult = (TextView) findViewById(R.id.tResult);
        ImageView imageView = (ImageView) findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        email = user.getEmail();
        userID = user.getUid();



       // Bundle b = getIntent().getExtras();
        //name = b.getString("name");
        //age = b.getString("age");

        Log.d("maill", name+ " "+age );


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

        // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);

                    // User Exists
                    Log.d("addtx", "onDataChange: Added information to database: \n" + dataSnapshot.child("users/"+userID).exists());
                Log.d("why", userID +"  :"+ email+" "+age+" "+privilegi);
                if (!dataSnapshot.child("users/"+userID).exists()){
                    UserInformation userInformation = new UserInformation(name,surname, email, age ,"0");
                    myRef.child("users").child(userID).setValue(userInformation);

                    Log.d("ciao", userID +"  :"+ email+" "+age+" "+privilegi);
                    toastMessage("New Information has been saved.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("fail read value", "Failed to read value.", error.toException());
            }
        });

        Log.d("database",""+ name);


        if (user != null) {
            //per utente facebook
            Uri photoUrl = user.getPhotoUrl();
            String namef = user.getDisplayName();

            if(!namef.equals("")) {
                name=namef;
                Log.d("vediamodentro", namef );

            }

            tMail.setText(email); //email user
            tAge.setText(age);

            if (photoUrl != null) {

                    Picasso.get().load(photoUrl).resize(580, 580).into(imageView);
                }

            }



        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent info = new Intent(UserAreaActivity.this, infoActivity.class);
                UserAreaActivity.this.startActivity(info);

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                progressDialog = new ProgressDialog(UserAreaActivity.this);
                progressDialog.setMessage("wait..."); // Setting Message
                progressDialog.setTitle("Logout"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            disconnectFromFacebook();
                            Thread.sleep(3600);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        Intent registerIntent = new Intent(UserAreaActivity.this, loginActivity.class);
                        UserAreaActivity.this.startActivity(registerIntent);
                    }
                }).start();


            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:

                        break;
                    case R.id.action_cron:
                        if(name==null && surname==null){
                            progressDialog = new ProgressDialog(UserAreaActivity.this);
                            progressDialog.setMessage("Required name and surname"); // Setting Message
                            progressDialog.setTitle("Missing data"); // Setting Title
                            //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                            progressDialog.show(); // Display Progress Dialog
                            progressDialog.setCancelable(false);
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(1500);
                                        Intent chrono = new Intent(UserAreaActivity.this, editProfileActivity.class);
                                        UserAreaActivity.this.startActivity(chrono);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    }
                            }).start();
                        }
                        else {
                            Intent chrono = new Intent(UserAreaActivity.this, chronoActivity.class);
                            chrono.putExtra("name", name);
                            chrono.putExtra("surname", surname);
                            chrono.putExtra("id", userID);
                            chrono.putExtra("email", email);
                            chrono.putExtra("age", age);
                            chrono.putExtra("privilegi", privilegi);
                            UserAreaActivity.this.startActivity(chrono);
                        }



                        break;
                    case R.id.action_statistic:
                        Intent mainStatistiche = new Intent(UserAreaActivity.this, mainStatistiche.class);

                        mainStatistiche.putExtra("name", name);
                        mainStatistiche.putExtra("surname", surname);
                        mainStatistiche.putExtra("id", userID);
                        mainStatistiche.putExtra("email", email);
                        mainStatistiche.putExtra("age", age);
                        mainStatistiche.putExtra("privilegi", privilegi);

                        Log.d("privilegiU",""+privilegi);


                        UserAreaActivity.this.startActivity(mainStatistiche);
                        break;

                    case R.id.action_face:
                        Intent setting = new Intent(UserAreaActivity.this, editProfileActivity.class);
                        UserAreaActivity.this.startActivity(setting);
                        break;


                    case R.id.action_top:
                        Intent top = new Intent(UserAreaActivity.this, topFive.class);
                        top.putExtra("name", name);
                        top.putExtra("surname", surname);
                        top.putExtra("id", userID);
                        top.putExtra("email", email);
                        top.putExtra("age", age);
                        top.putExtra("privilegi", privilegi);
                        UserAreaActivity.this.startActivity(top);
                        break;




                }
                return false;
            }
        });
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();
            try {

                uInfo.setName(ds.child(userID).getValue(UserInformation.class).getName()); //set the name
                uInfo.setSurname(ds.child(userID).getValue(UserInformation.class).getSurname());
                uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email
                uInfo.setAge(ds.child(userID).getValue(UserInformation.class).getAge()); //set the phone_num
                uInfo.setPrivilegi(ds.child(userID).getValue(UserInformation.class).getPrivilegi()); //set the phone_num


                //display all the information
                Log.d("read", "showData: name: " + uInfo.getName());
                Log.d("read", "showData: email: " + uInfo.getEmail());
                Log.d("read", "showData: age: " + uInfo.getAge());

                /*ArrayList<String> array = new ArrayList<>();
                array.add(uInfo.getName());
                array.add(uInfo.getEmail());
                array.add(uInfo.getAge());
                array.add(uInfo.getPrivilegi());
                ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
                mListView.setAdapter(adapter);*/

                Log.d("UINFO",""+uInfo.getName());

                name=uInfo.getName();
                surname=uInfo.getSurname();
                age=uInfo.getAge();
                privilegi=uInfo.getPrivilegi();

               /* if (!userID.equals("")) {

                    UserInformation userInformation = new UserInformation(name, email, age, privilegi);
                    myRef.child("users").child(userID).setValue(userInformation);

                    Log.d("ciao", userID +"  :"+ email+" "+age+" "+privilegi);
                    toastMessage("New Information has been saved.");

                }*/

                tResult.setText(name);
                tAge.setText(age);


            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       // mAuth.addAuthStateListener(mAuthListener);
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



    public class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            // progress = ProgressDialog.show(SecondActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null )
                {
                    btAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = btAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(MY_UUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                //msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();


            }
            else
            {
                // msg("Connected.");

            }

        }

    }
}
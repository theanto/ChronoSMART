package com.example.anto.chronosmart;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class chronoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "bluetooth2";

    Button btnOn;
    TextView txtArduino,tMeteo,tInsert;
    Handler h;

    final int RECIEVE_MESSAGE = 1;        // Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    private String userID,email,name,surname,age,privilegi;

    private ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address ="00:14:03:06:62:F9";

    private Button bBack,bGo;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String time,currentTime;
    private ProgressDialog progress;
    private String spinner;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chrono);



       // new ConnectBT().execute();

        btnOn = (Button) findViewById(R.id.btnOn);// button starting
        bGo = (Button) findViewById(R.id.bGo);
        bBack = (Button) findViewById(R.id.bBack);
        txtArduino = (TextView) findViewById(R.id.txtArduino);
        tMeteo = (TextView) findViewById(R.id.tMeteo);
        tInsert = (TextView) findViewById(R.id.tInsert);



        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        email = user.getEmail();
        userID = user.getUid();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);

        Bundle b = getIntent().getExtras();

        name = b.getString("name");
        surname = b.getString("surname");
        //dati aggiuntivi
        age = b.getString("age");
        privilegi = b.getString("privilegi"); // 1 allenatore 0 atleta


        Long tsLong = System.currentTimeMillis()/1000;
        final String ts = tsLong.toString();




        //roba sul spinner
        final Spinner weatherSpinner = (Spinner) findViewById(R.id.w_sp);
        Custom_adapter myAdapter = null;
        ArrayList<Weather_condition> weather_conditions = new ArrayList<>();
        weather_conditions = populate_weather_condition_data(weather_conditions);
        myAdapter = new Custom_adapter(this, weather_conditions);
        weatherSpinner.setAdapter(myAdapter);
        weatherSpinner.setOnItemSelectedListener(this);

        Log.d("weather_conditions",""+weather_conditions);
        Log.d("myAdapter",""+myAdapter.toString());

      mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    Log.d("signed_in", "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d("signedout", "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

      /*  bB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String name = inputName.getText().toString();
                //String age = inputAge.getText().toString();
                String email = user.getEmail();


                Log.d("msg", "onClick: Attempting to submit to database: \n" +
                                //"name: " + name + "\n" +
                                "userID: " + userID + "\n"
                        // "age: " + age + "\n"
                );

                //handle the exception if the EditText fields are null
                //insert data in db
                if(!userID.equals("")){
                    InsertTime tempo = new InsertTime(userID,ts,"1");
                    myRef.child("times").child(currentTime.toString()).child(userID).setValue(tempo);

                    //toastMessage("New Information has been saved.");
                    //inputName.setText("");
                    //inputEmail.setText("");
                    //inputAge.setText("");
                }else{
                    //toastMessage("Fill out all the fields");


                }
            }


        } );*/



        //menu //
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent home = new Intent(chronoActivity.this, UserAreaActivity.class);
                        chronoActivity.this.startActivity(home);

                        break;
                    case R.id.action_cron:
                        break;
                    case R.id.action_statistic:
                        Intent mainStatistiche = new Intent(chronoActivity.this, mainStatistiche.class);
                        mainStatistiche.putExtra("name", name);
                        mainStatistiche.putExtra("surname", surname);
                        mainStatistiche.putExtra("id", userID);
                        mainStatistiche.putExtra("email", email);
                        mainStatistiche.putExtra("age", age);
                        mainStatistiche.putExtra("privilegi", privilegi);
                        chronoActivity.this.startActivity(mainStatistiche);
                        break;

                    case R.id.action_face:
                        Intent sett = new Intent(chronoActivity.this, editProfileActivity.class);
                        chronoActivity.this.startActivity(sett);
                        break;

                    case R.id.action_top:
                        Intent top = new Intent(chronoActivity.this, topFive.class);
                        top.putExtra("name", name);
                        top.putExtra("surname", surname);
                        top.putExtra("id", userID);
                        top.putExtra("email", email);
                        top.putExtra("age", age);
                        top.putExtra("privilegi", privilegi);
                        chronoActivity.this.startActivity(top);
                        break;

                }
                return false;
            }
        });

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                        if (endOfLineIndex > 0) {                                            // if end-of-line,
                            String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                            sb.delete(0, sb.length());                                      // and clear
                            txtArduino.setText("Your time (on 100m) is : " + sbprint );            // update TextView
                            btnOn.setEnabled(true);

                            time=sbprint;

                            btnOn.setVisibility(View.INVISIBLE);
                            bGo.setVisibility(View.VISIBLE);// fa uscire bottone per salvare la sessione
                            bBack.setVisibility(View.VISIBLE); // fa uscire bottone back
                            weatherSpinner.setVisibility(View.VISIBLE);
                            tMeteo.setVisibility(View.VISIBLE); // fa uscire meteo
                            tInsert.setVisibility(View.VISIBLE);

                        }
                        //Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            };
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();


       /* bGo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // btnOn.setEnabled(false);

             /*   btnOn.setVisibility(View.VISIBLE);
                bGo.setVisibility(View.INVISIBLE);// fa uscire bottone per salvare la sessione
                bBack.setVisibility(View.INVISIBLE); // fa uscire bottone back
                weatherSpinner.setVisibility(View.INVISIBLE);
                tMeteo.setVisibility(View.INVISIBLE); // fa uscire meteo
                tInsert.setVisibility(View.INVISIBLE);


            }
        }); */



            btnOn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    btnOn.setEnabled(false);

                    mConnectedThread.write("1");    // Send "1" via Bluetooth
                    Toast.makeText(getBaseContext(), "Connected, ready to start", Toast.LENGTH_SHORT).show();

                  /*  btnOn.setVisibility(View.INVISIBLE);
                    bGo.setVisibility(View.VISIBLE);// fa uscire bottone per salvare la sessione
                    bBack.setVisibility(View.VISIBLE); // fa uscire bottone back
                    weatherSpinner.setVisibility(View.VISIBLE);
                    tMeteo.setVisibility(View.VISIBLE); // fa uscire meteo
                    tInsert.setVisibility(View.VISIBLE);*/


                }
            });

        bBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getBaseContext(), "Time dropped", Toast.LENGTH_SHORT).show();
                btnOn.setVisibility(View.VISIBLE);
                bGo.setVisibility(View.INVISIBLE);// fa uscire bottone per salvare la sessione
                bBack.setVisibility(View.INVISIBLE); // fa uscire bottone back
                weatherSpinner.setVisibility(View.INVISIBLE);
                tMeteo.setVisibility(View.INVISIBLE); // fa uscire meteo
                tInsert.setVisibility(View.INVISIBLE);
                txtArduino.setText(null);

            }
        });

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        }
        catch (IOException e) {

            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
        }
        catch (IOException e) {

            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {

        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");

        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }


    }

   /* public class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
           progress = ProgressDialog.show(chronoActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
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
                toastMessage("Deviced not connected");


            }
            else
            {
               // msg("Connected.");

            }

        }

    }*/


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    //roba su meteo
    public void serverInteraction(View view) {

        /*
        // Catch the input
        Spinner sp_ex = (Spinner)findViewById(R.id.sp_ex);
        final String exercise = sp_ex.getSelectedItem().toString();
        Spinner sp_se = (Spinner)findViewById(R.id.sp_se);
        final String series = sp_se.getSelectedItem().toString();
        Spinner sp_re = (Spinner)findViewById(R.id.sp_re);
        final String repetitions = sp_re.getSelectedItem().toString();
        */

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        //data dell allenamento
        final Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String cu =dateFormat.format(currentTime);

        Log.d("DATIUT",""+name+""+surname);

        String url = "https://anto-mc.000webhostapp.com/populating_db.php";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Toast.makeText(getBaseContext(), "Time recorded", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
                Log.d("Perche", "VolleyError: ", error); // debugging
                Log.d("DATIPOP", " "+time+" "+" "+ userID+" "+currentTime);
            }
        }
        ){
            // Pass parametres through HTTP POST
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name",name);
                params.put("surname",surname);
                params.put("time", time);
                params.put("userID", userID);
                params.put("meteo", spinner);
                params.put("data", cu);
                Log.d("Popolazione", time+" "+" "+ userID+ " "+ currentTime);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private ArrayList<Weather_condition> populate_weather_condition_data(ArrayList<Weather_condition> weather_conditions) {
        weather_conditions.add(new Weather_condition("Sunny", R.drawable.sunny));
        weather_conditions.add(new Weather_condition("Rain", R.drawable.rain));

        return weather_conditions;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        spinner=parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
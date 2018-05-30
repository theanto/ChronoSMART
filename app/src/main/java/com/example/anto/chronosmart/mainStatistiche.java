package com.example.anto.chronosmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mainStatistiche extends AppCompatActivity {

    private BarChart barChart;
    private ListView list;
    public RequestQueue queue;
    private Float ris;
    private TextView tWarning;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String time,currentTime;
    private ProgressDialog progress;

    private String userID,email,name,surname,age,privilegi;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_statistiche);


        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);

        Bundle b = getIntent().getExtras();

        name = b.getString("name");
        surname = b.getString("surname");
        //dati aggiuntivi
        age = b.getString("age");
        privilegi = b.getString("privilegi"); // 1 allenatore 0 atleta
        ris= Float.valueOf(0);
        tWarning = (TextView) findViewById(R.id.tWarning);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        email = user.getEmail();
        userID = user.getUid();
        /*TextView PassName = (TextView) findViewById(R.id.idutente);
        PassName.setText(ID);*/

        //menu //
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent home = new Intent(mainStatistiche.this, UserAreaActivity.class);
                        mainStatistiche.this.startActivity(home);
                        break;
                    case R.id.action_cron:
                        // open fragment 2
                        break;
                    case R.id.action_statistic:

                        break;

                    case R.id.action_face:
                       /* Intent addData = new Intent(UserAreaActivity.this, addData.class);

                        addData.putExtra("name", name);
                        addData.putExtra("id", user);

                        UserAreaActivity.this.startActivity(addData);*/
                        break;

                }
                return false;
            }
        });

        queue = Volley.newRequestQueue(this);

        //final TextView lTextView = (TextView) findViewById(R.id.List);
        final ArrayList<String> List = new ArrayList<String>();

        Log.d("privilegi", ""+privilegi);

                //allenatore
                if (privilegi.equals("1")) {
                    //final TextView gTextView = (TextView) findViewById(R.id.graph);
                    //gTextView.setVisibility(View.VISIBLE);

                    //Log.d("DEBUG", "Sono dentro l'if");
                    //prova
                    String url1 = "https://anto-mc.000webhostapp.com/prova.php";

                    StringRequest postRequest = new StringRequest(Request.Method.GET, url1,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    //LISTA FATTA ora cliccando sul nome
                                    // chiama la funzione graficaDatiAtleta(userID)

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);


                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject out = jsonArray.getJSONObject(i);
                                            try {
                                                //String ID = out.getString("UserID");
                                                String name = out.getString("nome");
                                                String cognome = out.getString("cognome");
                                                List.add(name+" "+cognome);
                                                //List.add(name +"         " +ID);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //MODIFICARE E METTERE LA POSSIBILITA' DI CLICCARE SUL NOME
                                        // PER VEDERE IL GRAFICO DEL SINGOLO ATLETA

                                        list = (ListView) findViewById(R.id.listView);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainStatistiche.this,
                                                android.R.layout.simple_list_item_1, android.R.id.text1, List);
                                        list.setAdapter(adapter);

                                        // Set an item click listener for ListView
                                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                // Get the selected item text from ListView
                                                String selectedItem = (String) parent.getItemAtPosition(position);
                                                tovaID(selectedItem);

                                            }
                                        });

                                        //
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    Log.d("Response", response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                }
                            }
                    );
                    queue.add(postRequest);

                    //prova


                    //fine prova
                }
                //atleta
                else {
                    graficaDati(userID);
                }


    }

    private void tovaID (final String selected ){

        //final TextView gTextView = (TextView) findViewById(R.id.graph);
        //richiesta database POST
        final String[] items = selected.split(" ");
       // gTextView.append(items[0]);
       // gTextView.append(items[1]);


        String url = "https://anto-mc.000webhostapp.com/trovaID.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            //for (int i = 0 ; i < jsonArray.length(); i++) {
                            JSONObject out = jsonArray.getJSONObject(0);

                            //int ID = out.getInt("ID");
                            String userID =  out.getString("UserID");

                           // String h = String.valueOf(userID);
                            //gTextView.append( "userID:"+h);
                            graficaDati(userID);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nome", items[0]);
                params.put("cognome", items[1]);
                return params;
            }
        };

        queue.add(postRequest);

        //fine richiesta POST


    }





    private void graficaDati(final String ID) {
        //final TextView gTextView = (TextView) findViewById(R.id.graph);
       // gTextView.append("  entrato in Data");

        barChart = (BarChart) findViewById(R.id.bargraph);

        String url = "https://anto-mc.000webhostapp.com/atleti_statistiche.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final ArrayList<BarEntry> barEntries = new ArrayList<>();
                            ArrayList<String> theDates = new ArrayList<>();


                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0 ; i < jsonArray.length(); i++) {
                                JSONObject out = jsonArray.getJSONObject(i);

                                //int ID = out.getInt("ID");
                                //String UserID =  out.getString("UserID");
                                String nome = out.getString("nome");
                                String cognome = out.getString("cognome");
                                String meteo = out.getString("meteo");
                                String dataa = out.getString("data");
                                String risultato = out.getString("risultato");
                                Float ris = Float.parseFloat(risultato);
                                barEntries.add(new BarEntry(ris,i));
                                theDates.add(dataa+"-" +meteo);

                            }


                            barChart.setVisibility(View.VISIBLE);
                            BarDataSet barDataSet = new BarDataSet(barEntries,"results");
                            barDataSet.setColors(new int[]{Color.parseColor("#5ef812")}); //colorare le barre
                            barDataSet.setBarSpacePercent(50f); //spessore barre, vedere bene
                            BarData theData = new BarData(theDates,barDataSet);
                            barChart.setData(theData);
                            //barChart.setVisibleXRange(1,3);
                            //barChart.getAxisLeft().setAxisMinValue(0f);
                            //barChart.getAxisLeft().setSpaceTop(200f);
                           // barChart.getAxisLeft().setAxisMaxValue(200f);
                            barChart.getAxisRight().setEnabled(false);
                            barChart.setDescription("");
                            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                            barChart.invalidate(); // refresh



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }

                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                tWarning.setVisibility(View.VISIBLE);


            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userID", ID);

                return params;
            }
        };

        queue.add(postRequest);
    }



}

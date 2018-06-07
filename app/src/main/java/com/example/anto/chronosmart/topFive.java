package com.example.anto.chronosmart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class topFive extends AppCompatActivity {
    private String userID,email,name,surname,age,privilegi;
    private String risultato,cu,tot;
    public RequestQueue queue;



    private JSONArray result;
    private TextView tOne;
    private TextView tTwo;
    private TextView tThree;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_five);


        tOne = (TextView) findViewById(R.id.tOne);
        tTwo = (TextView) findViewById(R.id.tTwo);
        tThree = (TextView) findViewById(R.id.tThree);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);

        final Date currentTime = Calendar.getInstance().getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        cu =dateFormat.format(newDate);

        Log.d("newDate", ""+cu);

        Bundle b = getIntent().getExtras();

        name = b.getString("name");
        surname = b.getString("surname");
        //dati aggiuntivi
        age = b.getString("age");
        privilegi = b.getString("privilegi"); // 1 allenatore 0 atleta



        Response.Listener<String> responseL = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    final ArrayList<String> theDates = new ArrayList<>();



                    for (int i = 0; i < jsonArray.length(); i++) {

                        Log.d("dentro", ""+jsonArray);


                        JSONObject out = jsonArray.getJSONObject(i);

                        //int ID = out.getInt("ID");
                        //String UserID =  out.getString("UserID");
                        String risultato = out.getString("MIN(risultato)");
                        String nome = out.getString("nome");
                        String cognome = out.getString("cognome");
                        theDates.add(nome+"\n"+cognome+"\n"+risultato+"\n");





                    }

                    String top1 = theDates.get(0);
                    String top2 = theDates.get(1);
                    String top3 = theDates.get(2);

                    tOne.setText(top1);
                    tTwo.setText(top2);
                    tThree.setText(top3);

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        };

        top3Request loginRequest = new top3Request(cu , responseL);
        RequestQueue queue = Volley.newRequestQueue(topFive.this);
        queue.add(loginRequest);




        //menu //
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent home = new Intent(topFive.this, UserAreaActivity.class);
                        topFive.this.startActivity(home);
                        break;
                    case R.id.action_cron:
                         Intent chrono = new Intent(topFive.this, chronoActivity.class);
                        chrono.putExtra("name", name);
                        chrono.putExtra("surname", surname);
                        chrono.putExtra("id", userID);
                        chrono.putExtra("email", email);
                        chrono.putExtra("age", age);
                        chrono.putExtra("privilegi", privilegi);
                         topFive.this.startActivity(chrono);
                        break;
                    case R.id.action_statistic:
                        Intent stat = new Intent(topFive.this, mainStatistiche.class);
                        stat.putExtra("name", name);
                        stat.putExtra("surname", surname);
                        stat.putExtra("id", userID);
                        stat.putExtra("email", email);
                        stat.putExtra("age", age);
                        stat.putExtra("privilegi", privilegi);
                        topFive.this.startActivity(stat);
                        break;

                    case R.id.action_face:
                        Intent sett = new Intent(topFive.this, editProfileActivity.class);
                        topFive.this.startActivity(sett);
                        break;

                }
                return false;
            }
        });





        /*  String url = "https://anto-mc.000webhostapp.com/top.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //ArrayList<String> theDates = new ArrayList<>();


                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0 ; i < jsonArray.length(); i++) {
                                JSONObject out = jsonArray.getJSONObject(i);


                                String nome = out.getString("nome");
                                String cognome = out.getString("cognome");
                                String risultato = out.getString("risultato");
                                theDates.add(nome+"-" +cognome+""+risultato);

                            }



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
                params.put("data", cu.toString());

                return params;
            }
        };

        queue.add(postRequest);*/
    }


}



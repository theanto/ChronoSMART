package com.example.anto.chronosmart;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anto- on 18/03/2018.
 */

public class top3Request extends StringRequest {

    private static final String REGISTER_REQUEST_URL="http://anto-mc.000webhostapp.com/top.php";
    private Map<String, String> params;

    public top3Request(String data, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener , null);
        params=new HashMap< >();
        params.put("data",data);
    }

    @Override
    public Map<String ,String> getParams(){
        return params;
    }
}


package com.example.anto.chronosmart;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anto- on 18/03/2018.
 */

public class registerRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL="http://anto-mc.000webhostapp.com/Register.php";
    private Map<String, String> params;

    public registerRequest(String name, String user, String pass, String mail, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener , null);
        params=new HashMap< >();
        params.put("name",name);
        params.put("user",user);
        params.put("pass",pass);
        params.put("mail",mail);
    }

    @Override
    public Map<String ,String> getParams(){
        return params;
    }
}

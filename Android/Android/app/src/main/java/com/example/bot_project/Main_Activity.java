package com.example.bot_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main_Activity extends AppCompatActivity {

    public ImageButton Imb_user;
    public ImageButton Area01;
    public ImageButton Area02;
    public ImageButton Area03;
    public ImageButton Area04;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        intent  = new Intent(getApplicationContext(), Area_Main.class);
        Area01 = (ImageButton)findViewById(R.id.imb_Area01);
        Area02 = (ImageButton)findViewById(R.id.imb_Area02);
        Area03 = (ImageButton)findViewById(R.id.imb_Area03);
        Area04 = (ImageButton)findViewById(R.id.imb_Area04);
        Imb_user = (ImageButton)findViewById(R.id.imb_user);




        if(AppHelper.requestQueue != null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        } //RequestQueue 생성

        //버튼에 따라 다른 인자값을 전달하여 몇번째 Area인지 확인
        Area01.setOnClickListener(new View.OnClickListener() {//Area01
            @Override
            public void onClick(View view) {
                intent.putExtra("number", 1);
                startActivity(intent);
            }
        });

        Area02.setOnClickListener(new View.OnClickListener() {//Area02
            @Override
            public void onClick(View view) {
                intent.putExtra("number", 2);
                startActivity(intent);

            }
        });

        Area03.setOnClickListener(new View.OnClickListener() {//Area03
            @Override
            public void onClick(View view) {
                intent.putExtra("number", 3);
                startActivity(intent);
            }
        });

        Area04.setOnClickListener(new View.OnClickListener() {//Area04
            @Override
            public void onClick(View view) {
                intent.putExtra("number", 4);
                startActivity(intent);
            }
        });

        Imb_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }


    private void start() {
        Log.d("volleytest", "request");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf("http://14.51.48.218:3001/iot/queryAllIoTs"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("volleytest", response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("volleytest", "error : "+error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("devicekey","IoT0");
//                params.put("lamp","true");

//
//                params.put("device","1");
//                params.put("id","2");
//                params.put("area","3");
//                params.put("lamp","");
//                params.put("gas","");
//                params.put("tmp","");
//                params.put("hum","");
//                params.put("feedback","");



                Log.d("volleytest", "ok");
                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);

    }


}
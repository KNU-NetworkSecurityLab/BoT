package com.example.bot_project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddIot_Fragment extends Fragment {


    public EditText et_addiot_deivce;
    public EditText et_addiot_area;
    public Button btn_addiot_add;
    public String device;
    public String area;
    private Check Network;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View layout =inflater.inflate(R.layout.addiot, container, false);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        et_addiot_deivce = getView().findViewById(R.id.et_addiot_device);
        et_addiot_area =getView().findViewById(R.id.et_addiot_area);
        btn_addiot_add = getView().findViewById(R.id.btn_addiot_add);
        Network = new Check();


        btn_addiot_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                device = et_addiot_deivce.getText().toString();
                area = et_addiot_area.getText().toString();

                Network.AddIoT(device, area, getContext());
            }
        });
    }


}

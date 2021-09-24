package com.example.bot_project;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Area04_Fragment extends Fragment {

    public TextView Area04_working;
    public TextView Area04_update;
    public TextView Area04_errormessage;
    public ImageView check;
    public Switch onoff;
    private RecyclerView listview;
    private Adapter adapter;
    private ArrayList<String> iotlist;
    private ArrayList<String> devicekey;
    private String lampkey;
    private String lamp;
    private String tmp;
    private String hum;
    private String date;
    private String feedback;
    private String gas;
    private Check Network;
    public Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View layout =inflater.inflate(R.layout.area04, container, false);
        return layout;
    }


    public void onStart() {

        super.onStart();
        Network = new Check();
        handler =null;
        Area04_working = getView().findViewById(R.id.tv_Area04_working);
        Area04_update = getView().findViewById(R.id.tv_Area04_updatetime);
        Area04_errormessage = getView().findViewById(R.id.tv_Area04_errormessage);
        check = getView().findViewById(R.id.imv_Area04_check);
        onoff = getView().findViewById(R.id.switch_Area04);
        init();


        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Network.lampStateUpdate("true", lampkey, getContext());
                }else {
                    Network.lampStateUpdate("false", lampkey, getContext());
                }
            }
        });


    }


    private void init() {

        listview = getView().findViewById(R.id.Area04_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);
        handler = new Handler();

        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Network.FindArea("area04", getContext());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        iotlist = Network.getIotlist();
                        devicekey = Network.getDevicekey();
                        Adapter adapter = new Adapter(getContext(), iotlist, devicekey);
                        listview.setAdapter(adapter);
                        ListDecoration listdeco = new ListDecoration();
                        listview.addItemDecoration(listdeco);

                        adapter.setOnClickListener(new Adapter.itemClick() {
                            @Override
                            public void onItemClickListener(View v, int position, String iotname, String devicekey) {
                                if (iotname.equals("lamp")) {
                                    lampkey = devicekey;
                                    check.setImageResource(R.drawable.iotact);
                                    try {
                                        Network.IoTStatus("area04", devicekey, getContext());
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    lamp = Network.getLamp();
                                    date = Network.getDate();
                                    feedback = Network.getFeedback();
                                    Area04_update.setText(date);
                                    Area04_errormessage.setText(feedback);
                                    Area04_working.setText(lamp);

                                } else if (iotname.equals("gas")) {
                                    check.setImageResource(R.drawable.gas);
                                    try {
                                        Network.IoTStatus("area04", devicekey, getContext());
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    gas = Network.getGas();
                                    date = Network.getDate();
                                    feedback = Network.getFeedback();
                                    Area04_update.setText(date);
                                    Area04_errormessage.setText(feedback);
                                    Area04_working.setText(gas);

                                } else if (iotname.equals("tmp")) {
                                    check.setImageResource(R.drawable.thermometer);
                                    try {
                                        Network.IoTStatus("area04", devicekey, getContext());
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    tmp = Network.getTmp();
                                    hum = Network.getHum();
                                    date = Network.getDate();
                                    feedback = Network.getFeedback();
                                    Area04_update.setText(date);
                                    Area04_errormessage.setText(feedback);
                                    Area04_working.setText(tmp+ " "+hum);

                                }
                            }
                        });
                    }
                });


            }
        });
        t.start();

    }




}

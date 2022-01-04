package com.example.bot_project;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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


public interface NetWork_Interface {

    void AllIot(Context context);
    void AddIoT(String device, String area, Context context);
    void UpdateIoT(Context context);
    void AddUser(Context context);
    void lampStateUpdate(String onoff, String devicekey, Context context);
    void IoTFind(Context context);
    void FindArea(String Area, Context context);
    void IoTStatus(String Area, String devicekey, Context context);
    void Alluser(Context context);
    void Login(Context context);
    void Userinfo(Context context);

}


//iot장비 리스트 출력
 class Check extends Fragment implements NetWork_Interface {


    ArrayList<String> iotlist = new ArrayList<String>();
    ArrayList<String> devicekey = new ArrayList<String>();
    String date =null;
    String feedback=null;
    String gas=null;
    String lamp=null;
    String tmp=null;
    String hum=null;

    public synchronized ArrayList<String> getIotlist(){
          return iotlist;
     }
    public synchronized ArrayList<String> getDevicekey(){
         return devicekey;
     }
    public synchronized String getDate(){
         return date;
    }
    public synchronized String getFeedback(){
         return feedback;
    }
    public synchronized String getGas(){
         return gas;
    }
    public synchronized String getLamp(){
         return lamp;
    }
    public synchronized String getTmp(){
         return tmp;
    }
    public synchronized String getHum(){
         return hum;
    }

    @Override
    public void AddIoT(String device, String area, Context context) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.AddIoT),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "addIot_Failed_incorrectArgumentsExpecting9"://인자수가 잘못되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "addIot_Failed_walletError"://블록체인 wallet 오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "addIot_Failed"://문법오류 또는 서버코드오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "addIot_Success"://성공
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                break;
                        }
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
                params.put("device", device);
                params.put("id", "user");
                params.put("area", area);
                params.put("lamp", "");
                params.put("gas", "");
                params.put("tmp", "");
                params.put("hum", "");
                params.put("feedback", "");
                params.put("ip", "");
                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);



    }



    @Override
    public void UpdateIoT(Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.UpdateIoT),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "updateIot_Failed_incorrectArgumentsExpecting10"://인자수가 잘못되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "updateIot_Failed_notExsitIoTNo"://서버에 등록되지 않은 IoT 번호일 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "updateIot_Failed_walletError"://블록체인 wallet 오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "updateIot_Failed"://문법오류 또는 서버코드오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "updateIot_Success"://성공
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                break;

                        }
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
                params.put("devicekey", "");
                params.put("device", "");
                params.put("id", "");
                params.put("area", "");
                params.put("lamp", "");
                params.put("gas", "");
                params.put("tmp", "");
                params.put("hum", "");
                params.put("feedback", "");
                params.put("ip", "");

                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }


    @Override
    public void lampStateUpdate(String onoff, String devicekey, Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.lampStateUpdate),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "lampStateUpdate_Failed_incorrectArgumentsExpecting2"://인자수가 잘못되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "lampStateUpdate_Failed_couldNotLocateIoT"://서버에 등록되지 않은 IoT 번호일 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "lampStateUpdate_Failed_walletError"://블록체인 wallet 오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "lampStateUpdate_Failed"://문법오류 또는 서버코드오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "lampStateUpdate_Success"://성공
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                break;
                        }
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
                params.put("devicekey", devicekey);
                params.put("lamp", onoff);
                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }



    @Override
    public void IoTFind(Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.IoTFind),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "iotFind_Failed_incorrectArgumentsExpecting1"://인자수가 잘못되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "iotFind_Failed_notExistUserid"://존재하지 않는 사용자일 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "iotFind_Failed_walletError"://블록체인 wallet 오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "iotFind_Failed"://문법오류 또는 서버코드오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                //JSON
                        }
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
                params.put("userId","");
                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }




    public void FindArea(String Area, Context context) {//IOT장비 확인

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.FindArea),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                     switch (response) {

                         case "iotFindByArea_Failed_incorrectArgumentsExpecting1"://인자수가 잘못됨
                             Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                             break;
                         case "iotFindByArea_Failed_notExistUseridOrArea"://존재하지 않는 사용자 또는 지역
                             Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                             break;
                         case "iotFindByArea_Failed_walletError"://블록체인 wallet오류
                             Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                             break;
                         case "iotFindByArea_Failed"://문법 또는 서버오류
                             Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                             break;
                         default:
                             try {
                                //iotlist = new ArrayList<>();
                                JSONArray ja = new JSONArray(response);
                                int size = ja.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject row = ja.getJSONObject(i);
                                    JSONObject record =  new JSONObject(row.getString("Record"));
                                    iotlist.add(record.getString("device"));
                                    devicekey.add(record.getString("devicekey"));

                                 }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                        break;
                     }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", "user");
                params.put("area", Area);
                return params;
            }
        };


        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }


    @Override
    public void IoTStatus(String Area, String devicekey, Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.FindArea),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {

                            case "iotFindByArea_Failed_incorrectArgumentsExpecting1"://인자수가 잘못됨
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "iotFindByArea_Failed_notExistUseridOrArea"://존재하지 않는 사용자 또는 지역
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "iotFindByArea_Failed_walletError"://블록체인 wallet오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "iotFindByArea_Failed"://문법 또는 서버오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                try {
                                    //iotlist = new ArrayList<>();
                                    JSONArray ja = new JSONArray(response);
                                    int size = ja.length();
                                    for (int i = 0; i < size; i++) {
                                        JSONObject row = ja.getJSONObject(i);
                                        JSONObject record =  new JSONObject(row.getString("Record"));
                                        if(devicekey.equals(record.getString("devicekey"))){
                                            feedback = record.getString("feedback");
                                            date = record.getString("date");
                                            if(record.getString("device").equals("gas")){
                                                gas = record.getString("gas");
                                                break;
                                            }else if (record.getString("device").equals("lamp")){
                                                lamp = record.getString("lamp");
                                                break;
                                            }else if(record.getString("device").equals("tmp")){
                                                tmp =record.getString("tmp");
                                                hum = record.getString("hum");
                                                break;
                                            }
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", "user");
                params.put("area", Area);
                return params;
            }
        };


        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);



    }



    @Override
    public void AllIot(Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.AllIot),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "queryAllIoTs_Failed_walletError"://블록체인 wallet 오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "queryAllIoTs_Failed"://문법오류 또는 서버코드오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
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
                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }



    @Override
    public void AddUser(Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.Adduser),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "addUser_Failed_incorrectArgumentsExpecting6"://인자수가 잘못되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "addUser_Failed_alreadyExistingId"://이미 존재하는 ID일 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "addUser_Failed_walletError"://블록체인 wallet 오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "addUser_Failed"://문법오류 또는 서버코드오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "addUser_Success"://성공
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                break;
                        }
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
                params.put("id", "");
                params.put("pw", "");
                params.put("name", "");
                params.put("mail", "");
                params.put("phone", "");
                params.put("team", "");

                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }



    @Override
    public void Login(Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.Login),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "login_Failed_incorrectArgumentsExpecting2"://인자수가 잘못되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "login_Failed_incorrectId"://ID가 잘못 되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "login_Failed_incorrectPw"://PW가 잘못 되었을 떄
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "login_Failed_walletError"://블록체인 wallet 오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "login_Failed"://문법오류 또는 서버코드오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "login_Success"://성공
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                break;

                        }
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
                params.put("id", "");
                params.put("pw", "");
                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }


    @Override
    public void Alluser(Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.AllUsers),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "queryAllUsers_Failed_walletError"://인자수가 잘못되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "queryAllUsers_Failed"://이미 존재하는 ID일 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                //JSON
                                break;

                        }
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
                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }


    @Override
    public void Userinfo(Context context) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, String.valueOf(NetworkURL.Userinfo),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response){
                            case "getUser_Failed_incorrectArgumentsExpecting1"://인자수가 잘못되었을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "getUser_Failed_infoNotExist"://ID값에 해당하는 유저가 없을 때
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "getUser_Failed_walletError"://블록체인 wallet 오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            case "getUser_Failed"://문법오류 또는 서버코드오류
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                //JSON
                                break;
                        }
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
                return params;
            }
        };



        stringRequest.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(stringRequest);


    }



}







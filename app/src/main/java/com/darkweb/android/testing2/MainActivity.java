package com.darkweb.android.testing2;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listView;

    static String fname1 = "first_name";
    static String lname1 = "last_name";
    static String avatar1 = "avatar";

    FloatingActionButton fab;

    Dialog dialog;
    ArrayList<HashMap<String, String>> arraylist;

    EditText email , pass;
    Button submit;
    Context context;


    String postUrl,postBody;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_adduser);
                dialog.show();

                email = dialog.findViewById(R.id.email);
                pass = dialog.findViewById(R.id.pass);

                submit = dialog.findViewById(R.id.submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AddUser();
                        dialog.dismiss();
                    }

                });



            }



        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder ( ).permitAll ( ).build ( );
        StrictMode.setThreadPolicy ( policy );

        isStoragePermissionGranted();


        new DownloadJSON().execute();
    }



    private void AddUser() {

         postUrl= "https://reqres.in/api/register/";
         postBody="{\n" +
                "\"email\": \"morpheus\",\n" +
                "\"password\": \"leader\"\n" +
                "}";


        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void postRequest(String postUrl,String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                call.cancel();
                ShowError();
//                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG",response.body().string());

                ShowSuccess();


            }


        });
    }

    private void ShowError() {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowSuccess() {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, "Request Success", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("granted","Permission is granted");
                return true;
            } else {

                Log.v("permissiion revoked","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("granted permission","Permission is granted");
            return true;
        }
    }

    public class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions.getJSONfromURL("https://reqres.in/api/users");

            try {
                jsonarray = jsonobject.getJSONArray("data");
                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);

                    map.put("first_name", String.valueOf(jsonobject.get("first_name")));
                    map.put("last_name", String.valueOf(jsonobject.get("last_name")));
                    map.put("avatar",String.valueOf( jsonobject.get("avatar")));


                    arraylist.add(map);
                    Log.e("jaon",arraylist.toString());

//                    Toast.makeText(VideoActivity.this, arraylist.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            super.onPostExecute(args);
            listView = (ListView) findViewById(R.id.videos_recycler_view);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(MainActivity.this, arraylist);
            // Set the adapter to the ListView
            listView.setAdapter(adapter);

        }
    }
}



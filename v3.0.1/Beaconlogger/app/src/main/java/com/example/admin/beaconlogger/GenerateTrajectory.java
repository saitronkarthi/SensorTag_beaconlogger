package com.example.admin.beaconlogger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BrijD on 16-04-26.
 */
public class GenerateTrajectory extends AppCompatActivity {

    public static final String url_get_data = "http://wireless.site88.net/pull.php";
    JSONParser2 jsonParser = new JSONParser2();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trajectory);

        Intent myIntent = getIntent(); // gets the previously created intent
        String StartDateTime = myIntent.getStringExtra("startdatetime"); // will return "FirstKeyValue"
        String EndDateTime= myIntent.getStringExtra("enddatetime");
        new GetData().execute(StartDateTime, EndDateTime);
    }

    class GetData extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        //Integer success_value;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(GenerateTrajectory.this, "", "Sending");
        }

        @Override
        protected String doInBackground(String... auth) {

            try {

                Log.i("string", auth[0]);
                String sdt = auth[0];
                String edt = auth[1];
                // Building Parameters
                //Log.d("value", email);
                //Log.d("value", password);
                ArrayList<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("StartDateTime", sdt));
                params.add(new BasicNameValuePair("EndDateTime", edt));
                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_get_data, "GET", params);
                Log.d("Create Response", json.toString());

                JSONArray jsonarray = json.getJSONArray("data");
                Log.d("jsonarray",String.valueOf(jsonarray.length()));

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String deviceAddr = jsonobject.getString("DeviseAddr");
                    String rssi = jsonobject.getString("DeviseRSSI");
                    String timestamp = jsonobject.getString("TStamp");
                    Log.d("DeviseAddr", deviceAddr);
                    Log.d("DeviseRSSI", rssi);
                    Log.d("TStamp", timestamp);

                }
                TwoDimentionalArrayList<String> twoDimentionalArrayList = new TwoDimentionalArrayList<String>();
                for (int i = 0;i < jsonarray.length(); i++)
                {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String deviceAddr = jsonobject.getString("DeviseAddr");
                    String rssi = jsonobject.getString("DeviseRSSI");
                    String timestamp = jsonobject.getString("TStamp");
                    for (int j = 0; j<3; j++)
                    {
                        if(j == 0)
                            twoDimentionalArrayList.addToInnerArray(i, j, deviceAddr);
                        else if (j == 1)
                            twoDimentionalArrayList.addToInnerArray(i, j, rssi);
                        else if (j == 2)
                            twoDimentionalArrayList.addToInnerArray(i, j, timestamp);

                    }
                }
                Log.i("error", "Success");
                //success_value = Integer.parseInt(json.getString("success"));
                //Log.d("Create Response", success_value.toString());

            } catch (Exception e) {
                Log.i("error", e.toString());
            }
            return "call";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
            //Toast.makeText(MainActivity.this, "Not valid", Toast.LENGTH_SHORT).show();



        }
    }
}

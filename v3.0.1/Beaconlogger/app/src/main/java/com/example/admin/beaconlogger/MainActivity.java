package com.example.admin.beaconlogger;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static String TAG = "MainActivity";
    private String devname;
    private String devaddress;
    private int devrssi;
    private String url;
    private String timestamp;
    ArrayList<String> DeviceList=new ArrayList<>();
    private Handler mHandler = new Handler();
    private TextView tv1 ;
    private TextView tv2 ;
    private TextView tv3 ;
    private TextView tv4 ;
    private LinkedList<String> addressesFound = new LinkedList<String>();
    private String srssi;
    // Button gt;
    private int rssi;
    public static final String url_insert_location = "http://wireless.site88.net/push.php";
    JSONParser jsonParser3 = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Button gj = (Button) findViewById(R.id.gt);


        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        boolean bad=mBluetoothAdapter.isEnabled();
        Log.d(TAG, "Bluetooth adapter ena " + Boolean.toString((bad)));
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivity(enableBtIntent);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }
//        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
//        startActivity(i);
        if (mBluetoothAdapter != null) {

        }
        }
    public BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            devname=device.getName();
                            devaddress=device.getAddress();
                            devrssi=rssi;
                            UriBeacon record=UriBeacon.parseFromBytes(scanRecord);
                            if (record!=null) url=record.getUriString();
                            timestamp=getCurrentTimeStamp();
                            if(!checkFoundList(devaddress)) {
                                //add the device to list
                                DeviceList.add(devname + "," + devaddress + "," + devrssi + "," + url+","+timestamp);
                                //add the device to the array of addresses
                                addressesFound.add(devaddress);
                            }
                            Log.d(TAG, "the final uri from beakon scanner in main activity is:" +url);
                            //byte[] serviceData = scanRecord.getServiceData(EDDYSTONE_SERVICE_UUID);
                            Log.d(TAG,"url"+url);
                            Log.d(TAG,"address"+device.getAddress().toString());
                            Log.d(TAG,"DeviceList: "+DeviceList.toString());

                        }
                    });

                }

            };

    private boolean checkFoundList(String address)
    {
        boolean res = false;
        if(addressesFound.contains(address))
        {res = true;}
        return res;
    }
    public void GenTra(View view) {
        Intent intent = new Intent(MainActivity.this, GetTime.class);
        startActivity(intent);
    }

    public void scanurl(View view) {
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        TextView tv3 = (TextView) findViewById(R.id.textView3);
        TextView tv4 = (TextView) findViewById(R.id.textView4);
        TextView tv5 = (TextView) findViewById(R.id.textView5);
        startScan();
//        tv1.setTextColor(Color.BLUE);
//        tv2.setTextColor(Color.BLUE);
//        tv3.setTextColor(Color.RED);
//        tv1.setText("DeviceName:" + devname);
//
//        tv2.setText("Device Address:"+devaddress);
//        tv3.setText("Device RSSI"+devrssi);
//        tv4.setText("url:"+url);
//        tv5.setText("Date and Time"+timestamp);
    }
    //from here
    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            TextView tv1 = (TextView) findViewById(R.id.textView1);
            TextView tv2 = (TextView) findViewById(R.id.textView2);
            TextView tv3 = (TextView) findViewById(R.id.textView3);
            TextView tv4 = (TextView) findViewById(R.id.textView4);
            TextView tv5 = (TextView) findViewById(R.id.textView5);
            tv1.setTextColor(Color.BLUE);
            tv2.setTextColor(Color.BLUE);
            tv3.setTextColor(Color.RED);
            int count=0;
            int Frssival=0;
            int Nextrssival=0;
            int highestrssi=0;
            for(String device : DeviceList)
            {
                count=count+1;

                srssi=device.split(",")[2];
                if( count==1 ){
                    Frssival=Integer.parseInt(srssi);
                }
                if (count>1)
                {
                    Nextrssival=Integer.parseInt(srssi);
                }
                if (Frssival > Nextrssival) {
                    highestrssi=Frssival;
                }
                if (Frssival < Nextrssival) {
                    highestrssi=Nextrssival;
                }
                Log.d(TAG, "DeviceRSSI: " +srssi);
                }
            Log.d(TAG, "Highest DeviceRSSI: " + highestrssi);
            for(String device : DeviceList)
            {
                srssi=device.split(",")[2];
                if (highestrssi==Integer.parseInt(srssi)) {
                    devname = device.split(",")[0];
                    devaddress = device.split(",")[1];
                    devrssi = Integer.parseInt(device.split(",")[2]);
                    url = device.split(",")[3];
                    timestamp = device.split(",")[4];
                }
            }

            tv1.setText("DeviceName:" + devname);
            tv2.setText("Device Address:"+devaddress);
            tv3.setText("Device RSSI"+devrssi);
            tv4.setText("url:"+url);
            tv5.setText("Date and Time"+timestamp);

            new SendToServer3().execute(devname, devaddress, String.valueOf(devrssi), timestamp, url);

            stopScan();
        }
    };
    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            devname=null;
            devaddress=null;
            devrssi=0;
            url=null;
            timestamp=null;
            for(int i=0;i<addressesFound.size();i++){
                addressesFound.removeFirst();
            }
            for(int i=0;i<DeviceList.size();i++){
                DeviceList.remove(i);
            }
            startScan();
        }
    };

    class SendToServer3 extends AsyncTask<String, String, String> {
        //ProgressDialog dialog;
        //Integer success_value;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //dialog = ProgressDialog.show(MainActivity.this, "", "Sending");
        }

        @Override
        protected String doInBackground(String... auth) {

            try {

                Log.i("string", auth[0]);
                String DevisName = auth[0];
                String DeviseAddr = auth[1];
                String DeviseRSSI = auth[2];
                String TStamp = auth[3];
                String url = auth[4];
                // Building Parameters
                //Log.d("value", email);
                //Log.d("value", password);
                ArrayList<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("Device_Name", DevisName));
                params.add(new BasicNameValuePair("Device_Address", DeviseAddr));
                params.add(new BasicNameValuePair("Device_RSSI", DeviseRSSI));
                params.add(new BasicNameValuePair("TimeStamp", TStamp));
                params.add(new BasicNameValuePair("URL", url));
                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser3.makeHttpRequest(url_insert_location, "GET", params);
                Log.d("Create Response", json.toString());
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

            //dialog.dismiss();
            //Toast.makeText(MainActivity.this, "Not valid", Toast.LENGTH_SHORT).show();



        }
    }

    private void startScan() {
         mBluetoothAdapter.startLeScan(mLeScanCallback);
         mHandler.postDelayed(mStopRunnable, 2500);//scanning time
    }

    private void stopScan() {
          mBluetoothAdapter.stopLeScan(mLeScanCallback);
          mHandler.postDelayed(mStartRunnable, 1000);
           }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}

package com.example.admin.beaconlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by BrijD on 16-04-26.
 */
public class GetTime extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_time);
        Button submit = (Button) findViewById(R.id.submitTime);
        /*
        EditText strDate = (EditText) findViewById(R.id.strdate);
        EditText strTime = (EditText) findViewById(R.id.strTime);
        EditText endTime = (EditText) findViewById(R.id.endTime);
        EditText endDate = (EditText) findViewById(R.id.endDate);
        final String startDateTime = strDate+" " +strTime;
        final String endDateTime = endDate+" "+ endTime;
        */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gen = new Intent(GetTime.this, GenerateTrajectory.class);
                gen.putExtra("startdatetime","2016-04-15 12:43:24");
                gen.putExtra("enddatetime","2016-04-15 12:43:24");
                startActivity(gen);
            }
        });

    }
}

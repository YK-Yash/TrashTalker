package com.example.android.trashtalker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference reference;
    int sensor_reading;
    CustomGauge methane_gauge;
    TextView methane_status_textview, methane_value_textview;
    private TextView mTextMessage;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        methane_gauge = findViewById(R.id.methane_gauge);
        methane_gauge.setStrokeColor(Color.WHITE);
        methane_status_textview = findViewById(R.id.methane_status_textview);
        methane_value_textview = findViewById(R.id.methane_value_textview);
        mTextMessage = findViewById(R.id.message);

        db = FirebaseDatabase.getInstance();
        reference = db.getReferenceFromUrl("https://myapplication-d680d.firebaseio.com/Gas_Readings/MQ135");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    sensor_reading = dataSnapshot.getValue(Integer.class);
                    if (sensor_reading > 50) {
                        methane_gauge.setPointStartColor(Color.YELLOW);
                        methane_status_textview.setBackgroundColor(Color.YELLOW);
                        if (sensor_reading > 75)
                            methane_gauge.setPointStartColor(Color.RED);
                        methane_status_textview.setBackgroundColor(Color.RED);
                    } else {
                        methane_gauge.setPointStartColor(Color.GREEN);
                        methane_status_textview.setBackgroundColor(Color.GREEN);
                    }
                    //int reading = Integer.valueOf(sensor_reading);
                    methane_gauge.setValue(sensor_reading);
                    methane_value_textview.setText("" + sensor_reading);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}

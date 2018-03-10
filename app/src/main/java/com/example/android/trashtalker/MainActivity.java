package com.example.android.trashtalker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.gkravas.meterview.MeterView;

import java.util.Random;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gkravas.meterview.MeterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private MeterView meterView;
    FirebaseDatabase db;
    DatabaseReference reference;
    int sensor_reading;

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

        mTextMessage = (TextView) findViewById(R.id.message);
        meterView = findViewById(R.id.meter_view);

        db = FirebaseDatabase.getInstance();
        reference = db.getReferenceFromUrl("https://myapplication-d680d.firebaseio.com/Gas_Readings/MQ135");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    sensor_reading = dataSnapshot.getValue(Integer.class);
                    //int reading = Integer.valueOf(sensor_reading);
                    meterView.setValue(sensor_reading);
                }catch (Exception e){
                    meterView.setValue(20);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        final float min = 0;
//        final float max = 54;
//        final Random random = new Random();
//
//        Observable.interval(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(new Func1<Long, Float>() {
//                    @Override
//                    public Float call(Long aLong) {
//                        return min + (random.nextFloat() * (max - min));
//                    }
//                })
//                .subscribe(new Action1<Float>() {
//                    @Override
//                    public void call(Float value) {
//                        meterView.setValue(value);
//                    }
//                });

    }
}

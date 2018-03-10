package com.example.android.trashtalker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.trashtalker.Fragments.Home_page_fragment;
import com.example.android.trashtalker.Fragments.SensorReadingsFragment;
import com.gkravas.meterview.MeterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private MeterView meterView;
    FirebaseDatabase db;
    DatabaseReference reference;
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setViewPager(0);
                    return true;
                case R.id.navigation_dashboard:
                    setViewPager(1);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.vp_pages);
        //setup the pager
        setupViewPager(mViewPager);

        mTextMessage = (TextView) findViewById(R.id.message);
//        meterView = findViewById(R.id.meter_view);

        db = FirebaseDatabase.getInstance();
        reference = db.getReferenceFromUrl("https://myapplication-d680d.firebaseio.com/Gas_Readings/MQ135");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
////                    sensor_reading = dataSnapshot.getValue(Integer.class);
//                    //int reading = Integer.valueOf(sensor_reading);
////                    meterView.setValue(sensor_reading);
//                }catch (Exception e){
//                    meterView.setValue(20);
//                }
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

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home_page_fragment(), "Home Page");
        adapter.addFragment(new SensorReadingsFragment(), "SensorReadings");
//        adapter.addFragment(new Fragment3(), "Fragment3");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }

}

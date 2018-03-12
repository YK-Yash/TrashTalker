package com.example.android.trashtalker.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.trashtalker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.pawelkleczkowski.customgauge.CustomGauge;


public class DashBoardFragment extends Fragment {

    FirebaseDatabase db;
    DatabaseReference reference_dustbinFull;
    DatabaseReference reference_mq135, reference_mq7;
    int sensor_reading_US, sensor_reading_MQ135, sensor_reading_MQ7;
    CustomGauge methane_gauge, mq135_gauge, mq7_gauge;
    TextView trash_value_textview, co2_lvl_textview, methane_level_textview;


    public static DashBoardFragment newInstance() {
        DashBoardFragment fragment = new DashBoardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        methane_gauge = view.findViewById(R.id.methane_gauge);
        methane_gauge.setStrokeColor(Color.WHITE);
        co2_lvl_textview = view.findViewById(R.id.co2_level);
        methane_level_textview = view.findViewById(R.id.methane_level);
        trash_value_textview = view.findViewById(R.id.trash_level_value_textview);
        mq135_gauge = view.findViewById(R.id.co2_gauge);
        mq135_gauge.setStrokeColor(Color.WHITE);
        mq7_gauge = view.findViewById(R.id.nitrogen_gauge);
        mq7_gauge.setStrokeColor(Color.WHITE);

        db = FirebaseDatabase.getInstance();
        reference_dustbinFull = db.getReferenceFromUrl("https://myapplication-d680d.firebaseio.com/Dustbins/D1");

        reference_dustbinFull.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    sensor_reading_US = dataSnapshot.getValue(Integer.class);
                    if (sensor_reading_US > 75) {
                        methane_gauge.setPointStartColor(Color.YELLOW);
                        if (sensor_reading_US > 90) {
                            methane_gauge.setPointStartColor(Color.RED);
                        }

                    } else {
                        methane_gauge.setPointStartColor(Color.GREEN);
                    }
                    //int reading = Integer.valueOf(sensor_reading);
                    methane_gauge.setValue(sensor_reading_US);
                    trash_value_textview.setText("" + sensor_reading_US);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference_mq135 = db.getReferenceFromUrl("https://myapplication-d680d.firebaseio.com/Gas_Readings/MQ135");

        reference_mq135.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    sensor_reading_MQ135 = dataSnapshot.getValue(Integer.class);
                    if (sensor_reading_MQ135 > 100) {
                        mq135_gauge.setPointStartColor(Color.YELLOW);
                        if (sensor_reading_MQ135 > 120) {
                            mq135_gauge.setPointStartColor(Color.RED);
                        }

                    } else {
                        mq135_gauge.setPointStartColor(Color.GREEN);
                    }
                    //int reading = Integer.valueOf(sensor_reading);
                    mq135_gauge.setValue(sensor_reading_MQ135);
                    co2_lvl_textview.setText("" + sensor_reading_MQ135);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference_mq7 = db.getReferenceFromUrl("https://myapplication-d680d.firebaseio.com/Gas_Readings/MQ7");

        reference_mq7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    Log.d("MQ7", "Updating values");
                    sensor_reading_MQ7 = dataSnapshot.getValue(Integer.class);
                    if (sensor_reading_MQ7 > 100) {
                        mq7_gauge.setPointStartColor(Color.YELLOW);
                        if (sensor_reading_MQ7 > 120) {
                            mq7_gauge.setPointStartColor(Color.RED);
                        }

                    } else {
                        mq7_gauge.setPointStartColor(Color.GREEN);
                    }
                    //int reading = Integer.valueOf(sensor_reading);
                    mq7_gauge.setValue(sensor_reading_MQ7);
                    methane_level_textview.setText("" + sensor_reading_MQ7);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

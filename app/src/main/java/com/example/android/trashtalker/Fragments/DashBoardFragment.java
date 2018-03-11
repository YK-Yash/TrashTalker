package com.example.android.trashtalker.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    DatabaseReference reference;
    int sensor_reading;
    CustomGauge methane_gauge;
    TextView methane_status_textview, methane_value_textview;


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
        methane_status_textview = view.findViewById(R.id.methane_status_textview);
        methane_value_textview = view.findViewById(R.id.methane_value_textview);

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
                        if (sensor_reading > 75) {
                            methane_gauge.setPointStartColor(Color.RED);
                            methane_status_textview.setBackgroundColor(Color.RED);
                        }

                    } else {
                        methane_gauge.setPointStartColor(Color.GREEN);
                        methane_status_textview.setBackgroundColor(Color.GREEN);
                    }
                    //int reading = Integer.valueOf(sensor_reading);
                    methane_gauge.setValue(sensor_reading);
                    methane_value_textview.setText("" + sensor_reading);
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

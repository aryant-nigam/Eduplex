package com.example.Eduplex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Friday extends Fragment {
    private ArrayList<TimetableClass> timetable;
    private View view;
    String _class,_section;

    public Friday(String _class,String _section){
        this._class=_class;
        this._section=_section;
    }
    public Friday(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friday, container, false);
        //prevents screenshots
        intialise();

        return view;
    }

    private void intialise() {
        timetable=new ArrayList<>();
        ListView fridayTimetable = view.findViewById(R.id.fridayTimetable);
        if(CheckNetworkAvailibility.isConnectionAvailable(view.getContext()))
        {
            DatabaseReference FRIDAYNODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord").child(_class).child(_section).child("Friday");
            FRIDAYNODE.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        for (DataSnapshot daySnap : snapshot.getChildren()) {
                            TimetableClass tc = (TimetableClass) daySnap.getValue(TimetableClass.class);
                            timetable.add(tc);
                            //Log.d("Aryant", String.valueOf(snapshot.getKey()));
                        }
                        //Log.d("Ary", String.valueOf(snapshot.getChildrenCount()));
                        fridayTimetable.setAdapter(new TimetableAdapter(timetable, view.getContext()));
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else
        {
            Toast.makeText(view.getContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}
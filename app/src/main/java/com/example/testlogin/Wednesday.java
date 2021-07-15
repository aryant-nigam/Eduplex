package com.example.testlogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Wednesday extends Fragment {
    private ArrayList<TimetableClass> timetable;
    private View view;

 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_wednesday, container, false);
        intialise();
        return view;
    }
    private void intialise() {

        timetable=new ArrayList<>();
        timetable.add(new TimetableClass("Chemistry","8:00:00-8:40:00","Aman"));
        timetable.add(new TimetableClass("Chemistry","8:00:00-8:40:00","Aman"));
        timetable.add(new TimetableClass("Chemistry","8:00:00-8:40:00","Aman"));
        timetable.add(new TimetableClass("Chemistry","8:00:00-8:40:00","Aman"));
        timetable.add(new TimetableClass("Chemistry","8:00:00-8:40:00","Aman"));

        ListView wednesdayTimetable = view.findViewById(R.id.wednesdayTimetable);
        TimetableAdapter timetableAdapter=new TimetableAdapter(timetable, view.getContext());
        wednesdayTimetable.setAdapter(timetableAdapter);


    }
}
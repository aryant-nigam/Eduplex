package com.example.testlogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Saturday extends Fragment {
    private ArrayList<TimetableClass> timetable;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view= inflater.inflate(R.layout.fragment_saturday, container, false);
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

        ListView saturdayTimetable = view.findViewById(R.id.saturdayTimetable);
        TimetableAdapter timetableAdapter=new TimetableAdapter(timetable, view.getContext());
        saturdayTimetable.setAdapter(timetableAdapter);

    }
}
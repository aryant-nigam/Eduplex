package com.example.testlogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Monday extends Fragment {
    private ArrayList<TimetableClass> timetable;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_monday, container, false);
        intialise();
        return view;

    }

    private void intialise() {
        timetable=new ArrayList<>();
        timetable.add(new TimetableClass("Chemistry","8:00 Am-8:40 AM","Aman Singh Chauhan"));
        timetable.add(new TimetableClass("Chemistry","8:00 AM-8:40 AM","Aman"));
        timetable.add(new TimetableClass("Chemistry","8:00 AM-8:40 AM","Aman"));
        timetable.add(new TimetableClass("Chemistry","8:00 AM-8:40 AM","Aman"));
        timetable.add(new TimetableClass("Chemistry","8:00 AM-8:40 AM","Aman"));

        ListView mondayTimetable = view.findViewById(R.id.mondayTimetable);
        TimetableAdapter timetableAdapter=new TimetableAdapter(timetable, view.getContext());
        mondayTimetable.setAdapter(timetableAdapter);



    }

}
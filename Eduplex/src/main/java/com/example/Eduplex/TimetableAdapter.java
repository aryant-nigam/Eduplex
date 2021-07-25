package com.example.Eduplex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TimetableAdapter extends BaseAdapter {
    ArrayList<TimetableClass>timetable;
    Context context;

    public TimetableAdapter(ArrayList<TimetableClass> timetable, Context context) {
        this.timetable = timetable;
        this.context = context;
    }

    @Override
    public int getCount() {
        return timetable.size();
    }

    @Override
    public Object getItem(int position) {
        return timetable.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater TimetableInflater = LayoutInflater.from(context);
        convertView=TimetableInflater.inflate(R.layout.activity_timetable_adapter,parent,false);

        TextView subjectName=convertView.findViewById(R.id.subjectName);
        TextView subjectTiming=convertView.findViewById(R.id.subjectTiming);
        TextView subjectTeacher=convertView.findViewById(R.id.subjectTeacher);
        ImageView timetableIcon=convertView.findViewById(R.id.timetableIcon);

        TimetableClass temp_timetable = timetable.get(position);

        subjectName.setText(temp_timetable.getSubjectName());
        subjectTiming.setText(temp_timetable.getSubjectTiming());
        subjectTeacher.setText(temp_timetable.getSubjectTeacher());
        timetableIcon.setImageResource(R.drawable.timetable_icon);

        return convertView;
    }
}
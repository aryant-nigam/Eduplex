package com.example.Eduplex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MeetingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MeetingClass>meetings;

    public MeetingAdapter(Context context, ArrayList<MeetingClass> meetings) {
        this.context = context;
        this.meetings = meetings;
    }

    @Override
    public int getCount() {
        return meetings.size();
    }

    @Override
    public Object getItem(int position) {
        return meetings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        convertView=inflater.inflate(R.layout.activity_meeting_adapter,parent,false);
        TextView date=convertView.findViewById(R.id.dateOfMeet);
        TextView time=convertView.findViewById(R.id.timeOfMeet);
        TextView id=convertView.findViewById(R.id.meetId);
        TextView classes=convertView.findViewById(R.id.forClasses);
        ImageView ptm_icon=convertView.findViewById(R.id.ptm_icon);

        MeetingClass tempMeeting= meetings.get(position);

        date.setText(tempMeeting.getMeetingDate());
        time.setText(tempMeeting.getMeetingTime());
        id.setText(tempMeeting.getMeetingId());
        classes.setText(tempMeeting.getFromClass()+"th to "+tempMeeting.getToClass()+"th");
        ptm_icon.setImageResource(R.drawable.parent_teacher_meeting_icon);

        return convertView;
    }
}
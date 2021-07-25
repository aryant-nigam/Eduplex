package com.example.Eduplex;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TeacherOnLeaveAdapter extends BaseAdapter {
    Context context;
    ArrayList<TeacherOnLeaveClass>teachersOnLeave;

    public TeacherOnLeaveAdapter(Context context, ArrayList<TeacherOnLeaveClass> teachersOnLeave) {
        this.context = context;
        this.teachersOnLeave = teachersOnLeave;
    }

    @Override
    public int getCount() {
        return teachersOnLeave.size();
    }

    @Override
    public Object getItem(int position) {
        return teachersOnLeave.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        convertView=layoutInflater.inflate(R.layout.activity_teacher_on_leave_adapter,parent,false);
        TextView teacherOnLeaveTid=convertView.findViewById(R.id.teacherOnLeaveTid);
        TextView teacherOnLeaveDate=convertView.findViewById(R.id.teacherOnLeaveDate);
        TextView teacherOnLeaveMessage=convertView.findViewById(R.id.teacherOnLeaveMessage);
        ImageView teacherOnLeaveIcon=convertView.findViewById(R.id.teacherOnLeaveIcon);
        TeacherOnLeaveClass tempTeacherOnleave=teachersOnLeave.get(position);

        teacherOnLeaveTid.setText(tempTeacherOnleave.getTeacherOnLeaveTid());
        teacherOnLeaveDate.setText(tempTeacherOnleave.getLeaveDate());
        teacherOnLeaveMessage.setText(tempTeacherOnleave.getTeacherOnLeaveMessage());
        teacherOnLeaveIcon.setImageResource(R.drawable.teacher_onleave_icon);

        return convertView;
    }
}
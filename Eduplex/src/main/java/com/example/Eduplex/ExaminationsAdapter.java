package com.example.Eduplex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExaminationsAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<ExamClass> Exams;
    public ExaminationsAdapter(Context context, ArrayList<ExamClass> studentExams) {
        this.context = context;
        this.Exams = studentExams;
    }

    @Override
    public int getCount() {
        return Exams.size();
    }

    @Override
    public ExamClass getItem(int position) {
        return Exams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layout_Inflater= LayoutInflater.from(context);
        View view=layout_Inflater.inflate(R.layout.activity_exams_adapter,parent,false);

        TextView subjectExam=view.findViewById(R.id.subjectExam);
        TextView date=view.findViewById(R.id.date);
        TextView time=view.findViewById(R.id.time);
        TextView duration=view.findViewById(R.id.duration);
        ImageView examinationLogo=view.findViewById(R.id.examinationLogo);

          ExamClass tempExam=Exams.get(position);
        examinationLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.exam_icon));
        subjectExam.setText(tempExam.getSubjectExam());
        date.setText(tempExam.getDate());
        time.setText(tempExam.getTime());
        duration.setText(tempExam.getDuration());

        return view;
    }
}
package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class teacherOnLeave extends AppCompatActivity {

    private TextView teacherOnLeaveTitle;
    private Animation translateNegX;
    private ListView teacherOnLeaveList;
    private ArrayList<TeacherOnLeaveClass>teachersOnLeave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_on_leave);
        initialise();
    }

    private void initialise() {
        teacherOnLeaveTitle=findViewById(R.id.teacherOnLeaveTitle);
        translateNegX= AnimationUtils.loadAnimation(this,R.anim.translatenegx);
        teacherOnLeaveTitle.startAnimation(translateNegX);
        teacherOnLeaveList=findViewById(R.id.teacherOnLeaveList);
        teachersOnLeave=new ArrayList<>();
        teachersOnLeave.add(new TeacherOnLeaveClass("T12356","Shobhita Aggarwal","Practice chapter 5 difficult word and write central idea for poem The Road Not Taken"));
        teachersOnLeave.add(new TeacherOnLeaveClass("T98675","Divya Verma","WAP to create a password tester on the basis o user input and use a global variable for stored password"));
        teachersOnLeave.add(new TeacherOnLeaveClass("T32455","PC Govil","Write short notes on 1) Diffraction. 2) poloroids. 3) interference. 4) Biot Savart Law.  5) Double rainbow formation."));
        teachersOnLeave.add(new TeacherOnLeaveClass("T88455","Ravikant Srivastava","Perform a practical to find the molarity and molality of given solution if initial weight of solvent(MgSO4) was 12.3gm and KOH solution is 1.2 W/W"));

        TeacherOnLeaveAdapter teacherOnLeaveAdapter=new TeacherOnLeaveAdapter(getApplicationContext(),teachersOnLeave);
        teacherOnLeaveList.setAdapter(teacherOnLeaveAdapter);

    }
}
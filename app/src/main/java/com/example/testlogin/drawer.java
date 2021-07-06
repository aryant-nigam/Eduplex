package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class drawer extends AppCompatActivity {
    Animation openNavBarAnim,closeNavBarAnim;
    ImageButton navBarOpen, closeNavBar;
    LinearLayout navBar;
    TextView nav_announcements,nav_attendence,nav_examinations,nav_teacherOnLeave,nav_feePayment,nav_logLeave,nav_results,nav_timetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        initialise();

        navBarOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  navBarOpen.setVisibility(View.INVISIBLE);
                navBar.setVisibility(View.VISIBLE);
                navBar.startAnimation(openNavBarAnim);
                navBarOpen.setVisibility(View.GONE);

            }
        });
        closeNavBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navBar.startAnimation(closeNavBarAnim);
                navBar.setVisibility(View.GONE);
                navBarOpen.setVisibility(View.VISIBLE);
            }
        });

        nav_announcements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent announcementsIntent=new Intent(getApplicationContext(),Announcements.class) ;
                startActivity(announcementsIntent);
            }
        });

        nav_examinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendenceIntent=new Intent(getApplicationContext(), Examinations.class);
                startActivity(attendenceIntent);
            }
        });

        nav_feePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feePaymentIntent=new Intent(getApplicationContext(),FeePayment.class);
                startActivity(feePaymentIntent);
            }
        });

        nav_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultsIntent=new Intent(getApplicationContext(),results.class);
                startActivity(resultsIntent);
            }
        });

        nav_teacherOnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teacherOnLeaveIntent=new Intent(getApplicationContext(),teacherOnLeave.class);
                startActivity(teacherOnLeaveIntent);
            }
        });

        nav_logLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logLeaveIntent=new Intent(getApplicationContext(), LogLeave.class);
                startActivity(logLeaveIntent);
            }
        });

        nav_attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendenceIntent=new Intent(getApplicationContext(), Attendence.class);
                startActivity(attendenceIntent);
            }
        });
        
    }

    private void initialise() {
        navBarOpen=findViewById(R.id.navBarOpen);
        navBar=findViewById(R.id.navbar);
        closeNavBar=findViewById(R.id.closeNavBar);
        nav_announcements=findViewById(R.id.nav_announcements);
        nav_attendence=findViewById(R.id.nav_attendence);
        nav_examinations=findViewById(R.id.nav_examinations);
        nav_teacherOnLeave=findViewById(R.id.nav_teacherOnLeave);
        nav_feePayment=findViewById(R.id.nav_feePayment);
        nav_logLeave=findViewById(R.id.nav_logLeave);
        nav_results=findViewById(R.id.nav_results);
        nav_timetable=findViewById(R.id.nav_timetable);
        openNavBarAnim= AnimationUtils.loadAnimation(this,R.anim.open_nav_anim);
        closeNavBarAnim= AnimationUtils.loadAnimation(this,R.anim.close_nav_anim);
    }
}
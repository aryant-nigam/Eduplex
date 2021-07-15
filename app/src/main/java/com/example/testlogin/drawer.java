package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class drawer extends AppCompatActivity {
    Animation openNavBarAnim,closeNavBarAnim;
    ImageButton navBarOpen, closeNavBar;
    LinearLayout navBar;
    TextView nav_announcements,nav_attendence,nav_examinations,nav_teacherOnLeave,nav_feePayment,nav_logLeave,nav_results,nav_timetable,nav_adminPanel;
    final int schoolImages[]={R.drawable.school1,R.drawable.school2,R.drawable.school3,R.drawable.school4};


private ViewFlipper imagesSlideshow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        initialise();
        if(getIntent().getStringExtra("user").equals("admin"))
        {
            nav_announcements.setVisibility(View.GONE);
            nav_attendence.setVisibility(View.GONE);
            nav_examinations.setVisibility(View.GONE);
            nav_teacherOnLeave.setVisibility(View.GONE);
            nav_feePayment.setVisibility(View.GONE);
            nav_logLeave.setVisibility(View.GONE);
            nav_results.setVisibility(View.GONE);
            nav_timetable.setVisibility(View.GONE);
            nav_adminPanel.setVisibility(View.VISIBLE);
        }
        else
        {
            nav_announcements.setVisibility(View.VISIBLE);
            nav_attendence.setVisibility(View.VISIBLE);
            nav_examinations.setVisibility(View.VISIBLE);
            nav_teacherOnLeave.setVisibility(View.VISIBLE);
            nav_feePayment.setVisibility(View.VISIBLE);
            nav_logLeave.setVisibility(View.VISIBLE);
            nav_results.setVisibility(View.VISIBLE);
            nav_timetable.setVisibility(View.VISIBLE);
            nav_adminPanel.setVisibility(View.GONE);
        }
        for(int image:schoolImages)
        {
        makeSlideShow(image);
        }
        navBarOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  navBarOpen.setVisibility(View.INVISIBLE);
                navBar.setVisibility(View.VISIBLE);
                navBar.startAnimation(openNavBarAnim);
                navBarOpen.setEnabled(false);
                navBarOpen.setVisibility(View.INVISIBLE);

            }
        });
        closeNavBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navBar.startAnimation(closeNavBarAnim);
                navBar.setVisibility(View.GONE);
                navBarOpen.setVisibility(View.VISIBLE);
                navBarOpen.setEnabled(true);
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
                Intent attendenceIntent=new Intent(getApplicationContext(),Attendence.class);
                startActivity(attendenceIntent);
            }
        });
        nav_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timetableIntent=new Intent(getApplicationContext(), Timetable.class);
                startActivity(timetableIntent);
            }
        });
        nav_adminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminPanelIntent=new Intent(getApplicationContext(), AdminPanel.class);
                startActivity(adminPanelIntent);
            }
        });
        
    }

    private void makeSlideShow(int image) {
        ImageView imageView=new ImageView(getApplicationContext());
        imageView.setBackgroundResource(image);
        imagesSlideshow.addView(imageView);
        //viewFlipper.setFlipInterval(4000);
        //viewFlipper.setAutoStart(true);
        //viewFlipper.setInAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        //viewFlipper.setOutAnimation(getApplicationContext(), android.R.anim.slide_out_right);
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
        nav_adminPanel=findViewById(R.id.nav_adminPanel);
        imagesSlideshow=findViewById(R.id.schoolImagesFlipper);
        openNavBarAnim= AnimationUtils.loadAnimation(this,R.anim.open_nav_anim);
        closeNavBarAnim= AnimationUtils.loadAnimation(this,R.anim.close_nav_anim);
    }
}
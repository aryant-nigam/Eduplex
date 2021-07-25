package com.example.Eduplex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class drawer extends AppCompatActivity {
    Animation openNavBarAnim,closeNavBarAnim;
    ImageButton navBarOpen, closeNavBar,logout;
    LinearLayout navBar;
    StudentsManager s;
    TextView registrationNumberOfSignee,nameOfSignee,classSectionOfSignee,userImage;
    TextView nav_announcements,nav_attendence,nav_examinations,nav_teacherOnLeave,nav_feePayment,nav_logLeave,nav_results,nav_timetable,nav_adminPanel;
    final int schoolImages[]={R.drawable.school1,R.drawable.school2,R.drawable.school3,R.drawable.school4};
    final int schoolNews[]={R.drawable.news1,R.drawable.news2,R.drawable.news3,R.drawable.news4};
    //AlertDialog.Builder builder;
    AlertDialog alertDialog;
    private ViewFlipper imagesSlideshow,newsSlideshow;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        initialise();

        if(getIntent().getStringExtra("user").equals("admin"))
        {
            userImage.setText("A");
            nameOfSignee.setText("ADMIN");
            registrationNumberOfSignee.setText("INSTITUTE NAME");
            classSectionOfSignee.setVisibility(View.INVISIBLE);

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
            s=(StudentsManager)(getIntent().getSerializableExtra("StudentData"));
            String UI=""+s.get_name().toUpperCase().charAt(0);
            userImage.setText(UI);
            nameOfSignee.setText(s.get_name());
            registrationNumberOfSignee.setText(s.get_registrationNumber());
            classSectionOfSignee.setText(s.get_class()+" - "+s.get_section());

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
        for(int image:schoolNews)
        {
            makeNewsShow(image);
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
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                    Intent announcementsIntent = new Intent(getApplicationContext(), Announcements.class);
                    announcementsIntent.putExtra("StudentData", s);
                    startActivity(announcementsIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nav_examinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                    Intent attendenceIntent = new Intent(getApplicationContext(), Examinations.class);
                    attendenceIntent.putExtra("StudentData", s);
                    startActivity(attendenceIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nav_feePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                    Intent feePaymentIntent = new Intent(getApplicationContext(), FeePayment.class);
                    feePaymentIntent.putExtra("StudentData", s);
                    startActivity(feePaymentIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nav_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                    Intent resultsIntent = new Intent(getApplicationContext(), results.class);
                    resultsIntent.putExtra("StudentData", s);
                    startActivity(resultsIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nav_teacherOnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                    Intent teacherOnLeaveIntent = new Intent(getApplicationContext(), teacherOnLeave.class);
                    teacherOnLeaveIntent.putExtra("StudentData", s);
                    startActivity(teacherOnLeaveIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nav_logLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                    Intent logLeaveIntent = new Intent(getApplicationContext(), LogLeave.class);
                    logLeaveIntent.putExtra("StudentData", s);
                    startActivity(logLeaveIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nav_attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                    Intent attendenceIntent = new Intent(getApplicationContext(), Attendence.class);
                    attendenceIntent.putExtra("StudentData", s);
                    startActivity(attendenceIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nav_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                    Intent timetableIntent = new Intent(getApplicationContext(), Timetable.class);
                    timetableIntent.putExtra("StudentData", s);
                    startActivity(timetableIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nav_adminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
                {
                    Intent adminPanelIntent = new Intent(getApplicationContext(), AdminPanel.class);
                    startActivity(adminPanelIntent);
                }
                else
                {
                    Toast.makeText(drawer.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(drawer.this);
                alertDialogBuilder.setTitle("CONFIRMATION");
                alertDialogBuilder.setMessage("Are you sure, You You want to logout");
                alertDialogBuilder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(intent);
                     }
                });

                alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void makeSlideShow(int image) {
        ImageView imageView=new ImageView(getApplicationContext());
        imageView.setBackgroundResource(image);
        imagesSlideshow.addView(imageView);
        //viewFlipper.setFlipInterval(4000);
        //viewFlipper.setAutoStart(true);
        imagesSlideshow.setInAnimation(getApplicationContext(), android.R.anim.fade_in);
        imagesSlideshow.setOutAnimation(getApplicationContext(), android.R.anim.fade_out);
    }
    private void makeNewsShow(int image) {
        ImageView imageView=new ImageView(getApplicationContext());
        imageView.setBackgroundResource(image);
        newsSlideshow.addView(imageView);
        //viewFlipper.setFlipInterval(4000);
        //viewFlipper.setAutoStart(true);
        newsSlideshow.setInAnimation(getApplicationContext(), android.R.anim.fade_in);
        newsSlideshow.setOutAnimation(getApplicationContext(), android.R.anim.fade_out);
    }

    private void initialise() {
        navBarOpen = findViewById(R.id.navBarOpen);
        navBar = findViewById(R.id.navbar);
        closeNavBar = findViewById(R.id.closeNavBar);
        nav_announcements = findViewById(R.id.nav_announcements);
        nav_attendence = findViewById(R.id.nav_attendence);
        nav_examinations = findViewById(R.id.nav_examinations);
        nav_teacherOnLeave = findViewById(R.id.nav_teacherOnLeave);
        nav_feePayment = findViewById(R.id.nav_feePayment);
        nav_logLeave = findViewById(R.id.nav_logLeave);
        nav_results = findViewById(R.id.nav_results);
        nav_timetable = findViewById(R.id.nav_timetable);
        nav_adminPanel = findViewById(R.id.nav_adminPanel);
        imagesSlideshow = findViewById(R.id.schoolImagesFlipper);
        newsSlideshow = findViewById(R.id.schoolNewsFlipper);
        userImage = findViewById(R.id.userimage);
        logout = findViewById(R.id.logOut);

        openNavBarAnim = AnimationUtils.loadAnimation(this, R.anim.open_nav_anim);
        closeNavBarAnim = AnimationUtils.loadAnimation(this, R.anim.close_nav_anim);


        nameOfSignee = findViewById(R.id.nameOfSignee);
        registrationNumberOfSignee = findViewById(R.id.registrationNumberOfSignee);
        classSectionOfSignee = findViewById(R.id.classSectionOfSignee);
    }

}
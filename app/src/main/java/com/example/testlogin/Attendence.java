package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Attendence extends AppCompatActivity {
    private Animation translateX,translateNegX;
    TextView daysAttended,daysRan,attendencePercentage,leaves,attendenceTitle;
    ProgressBar attendencePercentageProgress;
    LinearLayout attendenceCard,requestCorrectionCard;
    Button requestCorrection;
    private Spinner classSpinner , sectionSpinner;
    private AlertDialog alertDialog;
    private CheckBox requestCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        initialise();

        requestCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentsClass = classSpinner.getSelectedItem().toString();
                String studentsSection = sectionSpinner.getSelectedItem().toString();
                sendLogLeaveMail requestAttendenceCorrection=new sendLogLeaveMail("shobhanigam9453@gmail.com","11901811",studentsClass,studentsSection,"unofficialaryant@gmail.com","11901811aryant");
                if(requestAttendenceCorrection.logMail(1))
                {
                    alertDialog.show();
                }
                else
                {
                    if(isConnectionAvailable(getApplicationContext()))
                    Toast.makeText(Attendence.this, "Try again later", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Attendence.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        requestCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                requestCorrectionCard.setVisibility(View.VISIBLE);
                else
                requestCorrectionCard.setVisibility(View.GONE);
            }
        });
    }

    private void initialise() {
        translateX= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translatex);
        translateNegX=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translatenegx);

        daysAttended=findViewById(R.id.daysAttended);
        daysRan=findViewById(R.id.daysRan);
        attendencePercentage=findViewById(R.id.attendencePercentage);
        leaves=findViewById(R.id.leaves);
        attendenceTitle=findViewById(R.id.attendenceTitle);
        attendencePercentageProgress=findViewById(R.id.attendencePercentageProgress);
        requestCorrection=findViewById(R.id.requestCorrection);
        attendenceCard=findViewById(R.id.attendenceCard);
        requestCorrectionCard=findViewById(R.id.requestCorrectionCard);
        classSpinner=findViewById(R.id.attendenceClass);
        sectionSpinner=findViewById(R.id.attendenceSection);
        requestCheck=findViewById(R.id.requestCheck);

        attendenceCard.startAnimation(translateX);
        attendenceTitle.startAnimation(translateNegX);

        ArrayList<String> classes=new ArrayList<String>();
        classes.add("Select Class");
        classes.add("1st");
        classes.add("2nd");
        classes.add("3rd");
        classes.add("4th");
        classes.add("5th");
        classes.add("6th");
        classes.add("7th");
        classes.add("8th");
        classes.add("9th");
        classes.add("10th");
        classes.add("11th");
        classes.add("12th");

        ArrayAdapter<String> arrayAdapterClass=new ArrayAdapter<String >(this,android.R.layout.simple_spinner_item,classes);
        arrayAdapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(arrayAdapterClass);

        ArrayList<String> sections = new ArrayList<String>();
        sections.add("Select Section");
        sections.add("A");
        sections.add("B");
        sections.add("C");

        ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sections);
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(arrayAdapterSection);
        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("SUCCESSFUL");
        alertDialog.setMessage("Your correction request has been successfully logged . You will recieve a response on leave over registered mail ");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
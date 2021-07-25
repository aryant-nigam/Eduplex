package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Attendence extends AppCompatActivity {
    private Animation translateX,translateNegX;
    TextView daysAttended,daysRan,attendencePercentage,attendenceTitle,status;
    ProgressBar attendencePercentageProgress;
    LinearLayout attendenceCard,requestCorrectionCard;
    Button requestCorrection;
    private Spinner correctionDaySpinner ;
    private AlertDialog alertDialog;
    private CheckBox requestCheck;
    private StudentsManager extra;
    private  ProgressBar progressBar;
    EditText emailPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        initialise();

        requestCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String studentsClass = extra.get_class();
                String studentsSection = extra.get_section();
                
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
                {
                    DatabaseReference TEACHERNODE = FirebaseDatabase.getInstance().getReference().child("School").child("TeachersRecord").child(studentsClass).child(studentsSection);
                    TEACHERNODE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //teachersMail[0] =snapshot.child("_mail").getValue().toString();
                                sendLogLeaveMail requestAttendenceCorrection = new sendLogLeaveMail(
                                        snapshot.child("_mail").getValue().toString(),
                                        extra.get_registrationNumber(),
                                        studentsClass,
                                        studentsSection,
                                        extra.get_email(),
                                        emailPassword.getText().toString(),
                                        correctionDaySpinner.getSelectedItem().toString()
                                );
                                if (requestAttendenceCorrection.logMail(1)) {
                                    progressBar.setVisibility(View.GONE);
                                    alertDialog.show();
                                } else {
                                    if (isConnectionAvailable(getApplicationContext())) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Attendence.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Attendence.this, "No Internet", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else
                                progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
                }
                //sendLogLeaveMail requestAttendenceCorrection=new sendLogLeaveMail(teachersMail[0],extra.get_registrationNumber(),studentsClass,studentsSection,extra.get_email(),emailPassword.getText().toString());
                /*if(requestAttendenceCorrection.logMail(1))
                {
                    alertDialog.show();
                }
                else
                {
                    if(isConnectionAvailable(getApplicationContext()))
                    Toast.makeText(Attendence.this, "Try again later", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Attendence.this, "No Internet", Toast.LENGTH_SHORT).show();
                }*/
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

        attendenceTitle=findViewById(R.id.attendenceTitle);
        attendencePercentageProgress=findViewById(R.id.attendencePercentageProgress);
        requestCorrection=findViewById(R.id.requestCorrection);
        attendenceCard=findViewById(R.id.attendenceCard);
        requestCorrectionCard=findViewById(R.id.requestCorrectionCard);
        correctionDaySpinner=findViewById(R.id.correctionDay);

        requestCheck=findViewById(R.id.requestCheck);

        attendenceCard.startAnimation(translateX);
        attendenceTitle.startAnimation(translateNegX);
        status=findViewById(R.id.status);
        status.startAnimation(translateX);

        progressBar=findViewById(R.id.load3);
        extra=(StudentsManager)getIntent().getSerializableExtra("StudentData");

         emailPassword=findViewById(R.id.emailPassword2);
         DatabaseReference ATTENDENCENODE= FirebaseDatabase.getInstance().getReference().child("School").child("StudentsAttendenceRecord").child(extra.get_class()).child(extra.get_section()).child(extra.get_registrationNumber());
         ATTENDENCENODE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 StudentsManager.AttendenceManager attendenceManager=extra.new AttendenceManager(
                        Integer.parseInt(snapshot.child("_daysAttended").getValue().toString()),
                        Integer.parseInt(snapshot.child("_daysRan").getValue().toString())
                );

              daysAttended.setText(String.valueOf(attendenceManager.get_daysAttended()));
              daysRan.setText(String.valueOf(attendenceManager.get_daysRan()));
              if(attendenceManager.get_daysRan()==0)
              {
                  attendencePercentageProgress.setProgress(0);
                  attendencePercentage.setText("0.0 % ");
                  status.setText("STATUS  : Status of Evaluation Eligibility will be reflected soon");
              }
              else {
                  Double progress=((double)attendenceManager.get_daysAttended()/(double)attendenceManager.get_daysRan())*100;
                  Double eligibilityCriterion=75.00d;

                  DecimalFormat df = new DecimalFormat("##.##");
                  DecimalFormat df2=new DecimalFormat("##");

                  attendencePercentageProgress.setProgress(Integer.parseInt(df2.format(progress)));
                  attendencePercentage.setText(df.format(progress)+" % ");
                  if(progress.compareTo(eligibilityCriterion)==0&&progress-eligibilityCriterion<1)
                      status.setText("STATUS  : You just met the Evaluations criterion specified by the School.");
                  else if(progress.compareTo(eligibilityCriterion)>0)
                      status.setText("STATUS  : You satisfy the Evaluations criterion specified by the School.");
                  else
                      status.setText("STATUS  : You are not eligible for Evaluations conducted by School.");
              }

            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(Attendence.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<String> correctionDay=new ArrayList<String>();
        correctionDay.add("Select Day with incorrect Attendence ");
        correctionDay.add("Last Monday");
        correctionDay.add("Last Tuesday");
        correctionDay.add("Last Wednesday");
        correctionDay.add("Last Thursday");
        correctionDay.add("Last Friday");
        correctionDay.add("Last Saturday");

        ArrayAdapter<String> arrayAdapterCorrectionDay=new ArrayAdapter<String >(this,android.R.layout.simple_spinner_item,correctionDay);
        arrayAdapterCorrectionDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        correctionDaySpinner.setAdapter(arrayAdapterCorrectionDay);


        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("SUCCESSFUL");
        alertDialog.setMessage("Your correction request has been successfully logged . You will recieve a response on correction status over registered mail ");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                emailPassword.setText("");
                correctionDaySpinner.setSelection(0);
                requestCheck.setChecked(false);
                requestCorrectionCard.setVisibility(View.GONE);

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
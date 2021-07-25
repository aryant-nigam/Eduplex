package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogLeave extends AppCompatActivity {
    private EditText leaveSubject, leaveDescription,emailPassword;
    private Button logleavebutton;
    private CheckBox confirmation;
    private boolean checkedConfirmation=false;
    private AlertDialog alertDialog;
    private StudentsManager extra;
    TextView logLeaveTitle;
    Animation translateNegX;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_leave);


        initialise();

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmation.isChecked()) {
                    checkedConfirmation = !checkedConfirmation;
                    emailPassword.setEnabled(true);
                    emailPassword.setVisibility(View.VISIBLE);
                }
                else {
                    emailPassword.setText("");
                    emailPassword.setEnabled(false);
                    emailPassword.setVisibility(View.GONE);
                }
            }
        });

        logleavebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                /*Toast.makeText(LogLeave.this, extra.get_name(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LogLeave.this, extra.get_section(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LogLeave.this, extra.get_class(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LogLeave.this, extra.get_email(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LogLeave.this, extra._password, Toast.LENGTH_SHORT).show();
                //Toast.makeText(LogLeave.this, extra.get_feeStatus(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LogLeave.this, extra.get_dueFee(), Toast.LENGTH_SHORT).show();*/
                if(checkedConfirmation) {
                    String Subject = leaveSubject.getText().toString();
                    String Description = leaveDescription.getText().toString();
                    String password = emailPassword.getText().toString();

                    DatabaseReference NODE= FirebaseDatabase.getInstance().getReference().child("School").child("TeachersRecord").child(extra.get_class()).child(extra.get_section());
                    NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            if(snapshot.exists());
                            {
                                //ClassTeacherMail, studentRegistrationNumber, Subject, studentsClass,
                                        //studentsSection, Description, studentEmailId, password
                                sendLogLeaveMail logLeaveMail = new sendLogLeaveMail(
                                        snapshot.child("_mail").getValue().toString(), extra.get_registrationNumber(), Subject, extra.get_class(),
                                        extra.get_section(), Description, extra.get_email().trim(), password
                                );

                                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                                    if (logLeaveMail.logMail(0)) {
                                        progressBar.setVisibility(View.GONE);
                                        alertDialog.show();

                                    }
                                    else {
                                        if (CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(LogLeave.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(LogLeave.this, "No Internet", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LogLeave.this, "leave request failed ! Kindly check notes section and try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LogLeave.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    }
                else
                    Toast.makeText(LogLeave.this, "Kindly check the confirmation before logging leave", Toast.LENGTH_LONG).show();
            }
        });
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
        private void initialise()
        {

            leaveSubject =findViewById(R.id.LeaveSubject);
            leaveDescription =findViewById(R.id.LeaveDescription);
            logleavebutton=findViewById(R.id.logLeaveButton);
            confirmation=findViewById(R.id.confirmation);
            emailPassword=findViewById(R.id.emailPassword);
            logLeaveTitle=findViewById(R.id.logLeaveTitle);
            translateNegX= AnimationUtils.loadAnimation(this,R.anim.translatenegx);
            logLeaveTitle.startAnimation(translateNegX);
            progressBar=findViewById(R.id.load2);
            extra=(StudentsManager)getIntent().getSerializableExtra("StudentData");


            alertDialog=new AlertDialog.Builder(this).create();
            alertDialog.setTitle("SUCCESSFUL");
            alertDialog.setMessage("Your leave has been successfully logged .You will recieve a response on leave over registered mail ");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
}

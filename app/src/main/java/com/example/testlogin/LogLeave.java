package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class LogLeave extends AppCompatActivity {
    private Spinner classSpinner , sectionSpinner;
    private EditText leaveSubject, leaveDescription,emailPassword;
    private Button logleavebutton;
    private CheckBox confirmation;
    private boolean checkedConfirmation=false;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_leave);
        initialise();

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classSpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sectionSpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

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
                if(checkedConfirmation) {
                    String Subject = leaveSubject.getText().toString();
                    String studentsClass = classSpinner.getSelectedItem().toString();
                    String studentsSection = sectionSpinner.getSelectedItem().toString();
                    String Description = leaveDescription.getText().toString();
                    String ClassTeacherMail = "shobhanigam9453@gmail.com";
                    final String studentEmailId = "unofficialaryant@gmail.com";
                    final String studentRegistrationNumber = "11901811";
                    final String password = "11901811aryant";
                    sendLogLeaveMail logLeaveMail = new sendLogLeaveMail(ClassTeacherMail, studentRegistrationNumber, Subject, studentsClass,
                            studentsSection, Description, studentEmailId, password);
                    if(logLeaveMail.logMail(0))
                    {
                                   alertDialog.show();
                    } else Toast.makeText(LogLeave.this, "leave request failed ! Kindly check notes section and try again.", Toast.LENGTH_SHORT).show();
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
            classSpinner=findViewById(R.id.LeaveClass);
            sectionSpinner=findViewById(R.id.LeaveSection);
            leaveSubject =findViewById(R.id.LeaveSubject);
            leaveDescription =findViewById(R.id.LeaveDescription);
            logleavebutton=findViewById(R.id.logLeaveButton);
            confirmation=findViewById(R.id.confirmation);
            emailPassword=findViewById(R.id.emailPassword);

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
            alertDialog.setMessage("Your leave has been successfully logged .You will recieve a response on leave over registered mail ");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
}

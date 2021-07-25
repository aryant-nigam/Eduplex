package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAttendence extends AppCompatActivity {
    private Spinner Aclass,Asection,P_A;
    EditText registrationNumber;
    Button proceed,punchAttendence, pushAttendenceCorrection;
    RadioGroup radioGroup;
    RadioButton radioButton;
    StudentsManager studentsManager;
    LinearLayout attendencePanel;
    private AlertDialog alertDialog;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendence);
        initialise();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checked=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(radioGroup.getCheckedRadioButtonId());
                ;
                if(radioButton.getText().toString().equals("Punch Attendence"))
                {
                    pushAttendenceCorrection.setVisibility(View.GONE);
                    punchAttendence.setVisibility(View.VISIBLE);
                    attendencePanel.setVisibility(View.VISIBLE);
                    Aclass.setSelection(0);
                    Asection.setSelection(0);
                    P_A.setSelection(0);
                }
                else if(radioButton.getText().toString().equals("Push Attendence Correction"))
                {
                    punchAttendence.setVisibility(View.GONE);
                    pushAttendenceCorrection.setVisibility(View.VISIBLE);
                    attendencePanel.setVisibility(View.VISIBLE);
                    Aclass.setSelection(0);
                    Asection.setSelection(0);
                    P_A.setSelection(0);
                }
            }
        });

        punchAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsAttendenceRecord").child(Aclass.getSelectedItem().toString()).child(Asection.getSelectedItem().toString()).child(registrationNumber.getText().toString().trim());
                    NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                StudentsManager.AttendenceManager attendenceManager = studentsManager.new AttendenceManager(
                                        Integer.parseInt(snapshot.child("_daysAttended").getValue().toString()),
                                        Integer.parseInt(snapshot.child("_daysRan").getValue().toString()));
                                if (P_A.getSelectedItem().toString().equals("Present"))
                                    attendenceManager.punchAttendence('P');
                                else
                                    attendenceManager.punchAttendence('A');
                                NODE.setValue(attendenceManager).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AdminAttendence.this, "Successfully Punched The Attendence", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AdminAttendence.this, "Failed To Punch The Attendence : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                Aclass.setSelection(0);
                                Asection.setSelection(0);
                                P_A.setSelection(0);
                            }
                            else
                                Toast.makeText(AdminAttendence.this, "Student Unfound !!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        pushAttendenceCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(validate())
                {
                    DatabaseReference TEACHERNODE= FirebaseDatabase.getInstance().getReference().child("School").child("TeachersRecord").child(Aclass.getSelectedItem().toString()).child(Asection.getSelectedItem().toString());
                    DatabaseReference STUDENTNODE= FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(Aclass.getSelectedItem().toString()).child(Asection.getSelectedItem().toString()).child(registrationNumber.getText().toString().trim());
                    DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsAttendenceRecord").child(Aclass.getSelectedItem().toString()).child(Asection.getSelectedItem().toString()).child(registrationNumber.getText().toString().trim());
                    NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                StudentsManager.AttendenceManager attendenceManager = studentsManager.new AttendenceManager(
                                        Integer.parseInt(snapshot.child("_daysAttended").getValue().toString()),
                                        Integer.parseInt(snapshot.child("_daysRan").getValue().toString()));

                                boolean changed = false;
                                if (P_A.getSelectedItem().toString().equals("Present")) {
                                    if (attendenceManager.changeAttendence('P'))
                                        changed = true;
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(AdminAttendence.this, "Number of Days Attended can not exceed Number of Days Ran", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (attendenceManager.changeAttendence('A'))
                                        changed = true;
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(AdminAttendence.this, "Number of Days Attended can not be in negative ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(changed)
                                {
                                    NODE.setValue(attendenceManager).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        TEACHERNODE.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    STUDENTNODE.addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot studentSnapshot, @Nullable String previousChildName) {

                                                            sendLogLeaveMail responseOfRequest = new sendLogLeaveMail(
                                                                    studentSnapshot.child("_email").getValue().toString(),
                                                                    snapshot.child("_name").getValue().toString(),
                                                                    Aclass.getSelectedItem().toString(),
                                                                    Asection.getSelectedItem().toString(),
                                                                    snapshot.child("_mail").getValue().toString(),
                                                                    "goofy@2000",
                                                                    "subject"
                                                            );

                                                            if (responseOfRequest.logMail(2)) {
                                                                progressBar.setVisibility(View.GONE);
                                                                alertDialog.show();
                                                                Toast.makeText(AdminAttendence.this, "Successfully Pushed The Attendence Change", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                progressBar.setVisibility(View.GONE);
                                                                Toast.makeText(AdminAttendence.this, "wrong password", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override
                                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                                                        @Override
                                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                                                        @Override
                                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) { }
                                                    });

                                                } else if (!snapshot.exists()) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(AdminAttendence.this, "Class teacher yes not appointed", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(AdminAttendence.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(AdminAttendence.this, "Failed To Push The Attendence Change : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AdminAttendence.this, "Student Unfound !!", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AdminAttendence.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private boolean validate() {
        if(Aclass.getSelectedItem().toString().trim().equals("Select Class"))
        {
            Toast.makeText(this, "Class Field Can't Be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Asection.getSelectedItem().toString().trim().equals("Select Section"))
        {
            Toast.makeText(this, "Section Field Can't Be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(P_A.getSelectedItem().toString().trim().equals("Present / Absent"))
        {
            Toast.makeText(this, "Please Punch The Attendence Status", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isNumber((registrationNumber.getText().toString().trim()))||registrationNumber.getText().toString().trim().length()!=8)
        {
            Toast.makeText(this, "Invalid Registration Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isNumber(String regnum){
        for(int i=0;i<regnum.length();i++)
        {
            if(!(Character.isDigit(regnum.charAt(i))))
                return false;
        }
        return true;
    }

    private void initialise() {
        Aclass=findViewById(R.id.classAtt);
        Asection=findViewById(R.id.sectionAtt);
        P_A=findViewById(R.id.presentAbsent);
        registrationNumber=findViewById(R.id.regNoAtt);
        proceed=findViewById(R.id.proceedATP);
        punchAttendence=findViewById(R.id.punchAttendence);
        pushAttendenceCorrection =findViewById(R.id.punchAttendenceCorrection);
        radioGroup=findViewById(R.id.attendencePanelUtility);
        attendencePanel=findViewById(R.id.attendencePanel);
        studentsManager=new StudentsManager(null,null,null,null,null,null,null,null);

        progressBar=findViewById(R.id.load4);

        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("SUCCESSFUL");
        alertDialog.setMessage("Response on correction status of student's attendence has been sent  over his/her registered mail ");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Aclass.setSelection(0);
                Asection.setSelection(0);
                P_A.setSelection(0);
                registrationNumber.setText("");
            }
        });

        ArrayList<String> classes=new ArrayList<String>();
        classes.add("Select Class");
        classes.add("1");
        classes.add("2");
        classes.add("3");
        classes.add("4");
        classes.add("5");
        classes.add("6");
        classes.add("7");
        classes.add("8");
        classes.add("9");
        classes.add("10");
        classes.add("11");
        classes.add("12");
        ArrayAdapter<String> arrayAdapterClass=new ArrayAdapter<String >(this,android.R.layout.simple_spinner_item,classes);
        arrayAdapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Aclass.setAdapter(arrayAdapterClass);

        ArrayList<String> sections = new ArrayList<String>();
        sections.add("Select Section");
        sections.add("A");
        sections.add("B");
        sections.add("C");
        sections.add("D");
        ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sections);
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Asection.setAdapter(arrayAdapterSection);

        ArrayList<String> p_a = new ArrayList<String>();
        p_a.add("Present / Absent");
        p_a.add("Present");
        p_a.add("Absent");
        ArrayAdapter<String> arrayAdapterP_A = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, p_a);
        arrayAdapterP_A.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        P_A.setAdapter(arrayAdapterP_A);
    }
}
/*for(DataSnapshot ss:studentSnapshot.getChildren())
                                                            {
                                                                StudentsManager sendMailToStudentsManage=studentSnapshot.getValue(StudentsManager.class);
                                                                if(sendMailToStudentsManage.get_registrationNumber().toString().equals(registrationNumber.getText().toString()))
                                                                {
                                                                    Log.d("Ary", sendMailToStudentsManage.get_name());
                                                                    /*sendLogLeaveMail responseOfRequest=new sendLogLeaveMail(
                                                                            sendMailToStudentsManage.get_email().trim(),
                                                                            snapshot.child("_name").getValue().toString(),
                                                                            Aclass.getSelectedItem().toString(),
                                                                            Asection.getSelectedItem().toString(),
                                                                            "studybuddy162@gmail.com",
                                                                            "Aryant@123",
                                                                            " "
                                                                    );
                                                                }
                                                            }*/
/*
* private boolean sendResponse(String adminMail, String adminPassword, String toString, String toString1, String s1, String s2) {

    }*/
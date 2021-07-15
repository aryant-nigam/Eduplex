package com.example.testlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
                if(validate())
                {
                    DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsAttendenceRecord").child(Aclass.getSelectedItem().toString()).child(Asection.getSelectedItem().toString()).child(registrationNumber.getText().toString().trim());
                    NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                StudentsManager.AttendenceManager attendenceManager = studentsManager.new AttendenceManager(
                                        Integer.parseInt(snapshot.child("_daysAttended").getValue().toString()),
                                        Integer.parseInt(snapshot.child("_daysRan").getValue().toString()));
                                if (P_A.getSelectedItem().toString().equals("Present"))
                                    attendenceManager.changeAttendence('P');
                                else
                                    attendenceManager.changeAttendence('A');
                                NODE.setValue(attendenceManager).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AdminAttendence.this, "Successfully Pushed The Attendence Change", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AdminAttendence.this, "Failed To Push The Attendence Change : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
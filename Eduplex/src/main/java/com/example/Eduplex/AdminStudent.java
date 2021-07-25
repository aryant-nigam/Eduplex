package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminStudent extends AppCompatActivity {
    EditText newStudentResgistrationNumber,newStudentFullName,
            newStudentEmail,newStudentPassword,newStudentDueFee;
    Spinner newStudentClass,newStudentSection,newStudentFeeStatus;
    Button addStudentButton,removeStudentButton,proceed,generatePassword;
    StudentsManager studentsManager;
    RadioGroup radioGroup;
    RadioButton radioButton;
    LinearLayout addStudentPanel,removeStudentPanel;
    boolean getPass=true;
    final String emailregex="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student);
        initialiseSpinners();
        proceed=findViewById(R.id.proceedSP);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioButton.getText().toString().equals("Add Student")) {

                    addStudentPanel.setVisibility(View.VISIBLE);
                    removeStudentPanel.setVisibility(View.GONE);
                    newStudentFullName.setVisibility(View.VISIBLE);
                    newStudentEmail.setVisibility(View.VISIBLE);
                    newStudentPassword.setVisibility(View.VISIBLE);
                    newStudentFeeStatus.setVisibility(View.VISIBLE);
                    newStudentDueFee.setVisibility(View.VISIBLE);
                    addStudentButton.setVisibility(View.VISIBLE);
                    generatePassword.setVisibility(View.VISIBLE);
                }
                else if (radioButton.getText().toString().equals("Remove Student")) {

                    addStudentPanel.setVisibility(View.VISIBLE);
                    newStudentFullName.setVisibility(View.GONE);
                    newStudentEmail.setVisibility(View.GONE);
                    newStudentPassword.setVisibility(View.GONE);
                    newStudentFeeStatus.setVisibility(View.GONE);
                    newStudentDueFee.setVisibility(View.GONE);
                    addStudentButton.setVisibility(View.GONE);
                    removeStudentPanel.setVisibility(View.VISIBLE);
                    generatePassword.setVisibility(View.GONE);

                    newStudentPassword.setText("");
                    newStudentResgistrationNumber.setEnabled(true);
                    newStudentClass.setEnabled(true);
                    getPass=true;
                    generatePassword.setText("GET");

                }
            }
        });
        generatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPass) {
                    if (
                            isNumber(newStudentResgistrationNumber.getText().toString().trim())
                                    && !newStudentResgistrationNumber.getText().toString().trim().equals("")
                                    && newStudentResgistrationNumber.getText().toString().length() == 8
                                    && !newStudentClass.getSelectedItem().toString().equals("Select Class")
                    ) {
                        newStudentPassword.setText(newStudentResgistrationNumber.getText().toString() + "@" + newStudentClass.getSelectedItem().toString());
                        newStudentResgistrationNumber.setEnabled(false);
                        newStudentClass.setEnabled(false);
                        getPass=false;
                        generatePassword.setText("Unlock");
                    }
                    else
                        Toast.makeText(AdminStudent.this, "Make Sure Registration Number and Class is valid", Toast.LENGTH_SHORT).show();
                }
                else{
                    newStudentPassword.setText("");
                    newStudentResgistrationNumber.setEnabled(true);
                    newStudentClass.setEnabled(true);
                    getPass=true;
                    generatePassword.setText("GET");
                }
            }
        });

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initialise();
                if (validateStudentRecord(studentsManager)){
                    DatabaseReference DUPLICACYNODE=FirebaseDatabase.getInstance().getReference().child("School").child("LoginRecord");
                    DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber());
                    DatabaseReference ATTENDENCENODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsAttendenceRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber());
                    DatabaseReference LOGINRECORD=FirebaseDatabase.getInstance().getReference().child("School").child("LoginRecord").child(studentsManager.get_registrationNumber());
                    DUPLICACYNODE.child(newStudentResgistrationNumber.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            NODE.push().setValue(studentsManager).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    StudentsManager.AttendenceManager attendenceManager = studentsManager.new AttendenceManager(0, 0);
                                    ATTENDENCENODE.setValue(attendenceManager).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(AdminStudent.this, "Initiating Records", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AdminStudent.this, "Unable To Initialise Student's Attendence", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    LOGINRECORD.push().setValue(new usernamePassword(studentsManager.get_registrationNumber(),studentsManager.get_password(),studentsManager.get_class()+"-"+studentsManager.get_section())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(AdminStudent.this, "Initialising Credentials", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(AdminStudent.this, "Student Added Successfully", Toast.LENGTH_SHORT).show();
                                            newStudentClass.setSelection(0);
                                            newStudentSection.setSelection(0);
                                            newStudentFeeStatus.setSelection(0);
                                            newStudentFullName.setText("");
                                            newStudentEmail.setText("");
                                            newStudentPassword.setText("");
                                            newStudentDueFee.setText("");
                                            newStudentResgistrationNumber.setText("");
                                            newStudentPassword.setText("");
                                            newStudentResgistrationNumber.setEnabled(true);
                                            newStudentClass.setEnabled(true);
                                            getPass=true;
                                            generatePassword.setText("GET");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Toast.makeText(AdminStudent.this, "Failed To initiate Login Record Of The Student : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminStudent.this, "Failed To Add The Student : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(AdminStudent.this, "Duplicate Registration Number", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            }
        });

        removeStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initialiseRemove()){
                    DatabaseReference NODE= FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber());
                    DatabaseReference ATTENDENCENODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsAttendenceRecord").child(studentsManager.get_class()).child(studentsManager.get_section()).child(studentsManager.get_registrationNumber());
                    DatabaseReference LOGINRECORD=FirebaseDatabase.getInstance().getReference().child("School").child("LoginRecord").child(studentsManager.get_registrationNumber());
                    NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        ATTENDENCENODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AdminStudent.this, "Cleaning the attendence records", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull  Exception e) {
                                                Toast.makeText(AdminStudent.this, "Failed in the cleaning attendence records", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        LOGINRECORD.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AdminStudent.this, "Cleaning the credentials", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(AdminStudent.this, "Successfully Deleted Student Record", Toast.LENGTH_SHORT).show();
                                                newStudentClass.setSelection(0);
                                                newStudentSection.setSelection(0);
                                                newStudentResgistrationNumber.setText("");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull  Exception e) {
                                                Toast.makeText(AdminStudent.this, "Failed in cleaning the credentials", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(AdminStudent.this, "Failed To Delete Student's  Record", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                                Toast.makeText(AdminStudent.this, "Student Unfound !!", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {
                            Toast.makeText(AdminStudent.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private boolean initialiseRemove() {
        studentsManager=new StudentsManager(
                newStudentResgistrationNumber.getText().toString().trim(),
                newStudentClass.getSelectedItem().toString().trim(),
                newStudentSection.getSelectedItem().toString().trim(),
                null,
                null,
                null,
                null,
                null
        );
        if(!isNumber(studentsManager.get_registrationNumber())) {
            Toast.makeText(this, "Invalid Registration Number", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(studentsManager.get_class().equals("Select Class"))
        {
            Toast.makeText(this, "Class field can't be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(studentsManager.get_section().equals("Select Section")){
            Toast.makeText(this, "Section field can't be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void initialiseSpinners(){
        radioGroup=findViewById(R.id.studentPanelUtility);

        generatePassword=findViewById(R.id.generatePassword);
        addStudentButton=findViewById(R.id.addStudentButton);
        newStudentClass = findViewById(R.id.newStudentClass);
        newStudentSection = findViewById(R.id.newStudentSection);
        newStudentFeeStatus = findViewById(R.id.newStudentFeeStatus);
        addStudentPanel=findViewById(R.id.addStudentPanel);
        removeStudentPanel=findViewById(R.id.removeStudentPanel);
        newStudentFullName = findViewById(R.id.newStudentFullName);
        newStudentEmail = findViewById(R.id.newStudentEmail);
        newStudentPassword = findViewById(R.id.newStudentPassword);
        newStudentDueFee = findViewById(R.id.newStudentDueFee);
        newStudentResgistrationNumber = findViewById(R.id.newStudentResgistrationNumber);
        removeStudentButton=findViewById(R.id.removeStudentButton);

        newStudentPassword.setEnabled(false);

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
        newStudentClass.setAdapter(arrayAdapterClass);

        ArrayList<String> sections = new ArrayList<String>();
        sections.add("Select Section");
        sections.add("A");
        sections.add("B");
        sections.add("C");
        sections.add("D");

        ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sections);
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newStudentSection.setAdapter(arrayAdapterSection);

        ArrayList<String> fee_status = new ArrayList<String>();
        fee_status.add("Select Fee Status");
        fee_status.add("Paid While Admission");
        fee_status.add("Still Due");

        ArrayAdapter<String> arrayAdapterFeeStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fee_status);
        arrayAdapterFeeStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newStudentFeeStatus.setAdapter(arrayAdapterFeeStatus);


    }

    private void initialise() {


        boolean status=false;
        if(newStudentFeeStatus.getSelectedItem().toString().equals("Select Fee Status"))
                Toast.makeText(this, "Fee status field can't be empty", Toast.LENGTH_LONG).show();
        else if (newStudentFeeStatus.getSelectedItem().toString().equals("Paid While Admission"))
            status=true;
        else status=false;
        studentsManager=new StudentsManager(
                newStudentResgistrationNumber.getText().toString().trim(),
                newStudentClass.getSelectedItem().toString().trim(),
                newStudentSection.getSelectedItem().toString().trim(),
                newStudentFullName.getText().toString().trim(),
                newStudentEmail.getText().toString().trim(),
                newStudentPassword.getText().toString().trim(),
                newStudentDueFee.getText().toString().trim(),
                status
        );
        StudentsManager.AttendenceManager attendenceManager=studentsManager.new AttendenceManager(0,0);
        ResultManager resultManager=new ResultManager(null,null,null,null,null);
    }

    private boolean validateStudentRecord(StudentsManager studentsManager) {
        if(!isNumber(studentsManager.get_registrationNumber())&&studentsManager.get_registrationNumber().length()!=8) {
            Toast.makeText(this, "Invalid Registration Number", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(studentsManager.get_class().equals("Select Class"))
        {
            Toast.makeText(this, "Class field can't be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(studentsManager.get_section().equals("Select Section")){
            Toast.makeText(this, "Section field can't be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(hasSpecialCharecters(studentsManager.get_name())){
            Toast.makeText(this, "Name Can't have special charecters", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!studentsManager.get_email().matches(emailregex)) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!studentsManager.get_password().equals(studentsManager.get_registrationNumber()+"@"+studentsManager.get_class()))//studentsManager.get_name().split(" ")[0])
        {
            Toast.makeText(this, "Regenerate Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Integer.parseInt(studentsManager.get_dueFee())>15000&&Integer.parseInt(studentsManager.get_dueFee())<0){
            Toast.makeText(this, "Kindly recheck the fee", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean hasSpecialCharecters(String name) {
        for(int i=0;i<name.length();i++)
        {
            if(!(name.charAt(i)>=65&&name.charAt(i)<=90
                    ||(name.charAt(i)>=97&&name.charAt(i)<=122)
                    ||(name.charAt(i)==32)))
                return true;
        }
        return false;
    }

    private boolean isNumber(String regnum){
        for(int i=0;i<regnum.length();i++)
        {
            if(!(Character.isDigit(regnum.charAt(i))))
                return false;
        }
        return true;
    }
}
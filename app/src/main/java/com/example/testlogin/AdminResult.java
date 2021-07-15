package com.example.testlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class AdminResult extends AppCompatActivity {
    Spinner rClass,rSection,rSubject,testType,maxMarks;
    EditText obtainedMarks,regno;
    Button addResultButton,removeResultButton,proceed;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ResultClass resultClass;
    String rclass,rsection,rtestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_result);
        initialise();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioButton.getText().toString().equals("Add Result")) {
                    Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                    maxMarks.setVisibility(View.VISIBLE);
                    obtainedMarks.setVisibility(View.VISIBLE);
                    addResultButton.setVisibility(View.VISIBLE);
                    removeResultButton.setVisibility(View.GONE);
                } else if (radioButton.getText().toString().equals("Remove Result")) {
                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    maxMarks.setVisibility(View.GONE);
                    obtainedMarks.setVisibility(View.GONE);
                    removeResultButton.setVisibility(View.VISIBLE);
                    addResultButton.setVisibility(View.GONE);
                }
            }
        });

        addResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getterAdd()){
                DatabaseReference NODE= FirebaseDatabase.getInstance().getReference().child("School").child("ResultRecord").child(regno.getText().toString()).child(rtestType).child(resultClass.getSubjectVal());
                NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists())
                        {
                            NODE.setValue(resultClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminResult.this,"Successfully Punched The Result",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    Toast.makeText(AdminResult.this,"Failed To Punch The Result : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(AdminResult.this,"Result Has Been Punched Already",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });}
            }
        });

        removeResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getterRem())
                {
                    DatabaseReference NODE= FirebaseDatabase.getInstance().getReference().child("School").child("ResultRecord").child(regno.getText().toString()).child(rtestType).child(rSubject.getSelectedItem().toString().trim());
                    NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AdminResult.this,"Successfully Deleted The Result",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(AdminResult.this,"Failed To Delete The Result : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(AdminResult.this,"Result Unfound !!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    boolean getterAdd()
    {
        rclass=rClass.getSelectedItem().toString().trim();
        rsection=rSection.getSelectedItem().toString().trim();
        rtestType=testType.getSelectedItem().toString().trim();

        if(regno.getText().toString().trim().length()!=8){
            Toast.makeText(this, "Invalid Registration Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Double.isNaN(Double.parseDouble(obtainedMarks.getText().toString().trim()))&&Double.parseDouble(obtainedMarks.getText().toString().trim())<Double.parseDouble(maxMarks.getSelectedItem().toString().trim())) {
            Toast.makeText(this, "Invalid Marks", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(rclass.equals("Select Class"))
        {
            Toast.makeText(this, "Class Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(rsection.equals("Select Section"))
        {
            Toast.makeText(this, "Section Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(rSubject.getSelectedItem().toString().trim().equals("Select Subject"))
        {
                Toast.makeText(this, "Subject Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
                return(false);
        }
        if(maxMarks.getSelectedItem().toString().trim().equals("Maximum Marks"))
        {
            Toast.makeText(this, "Maximum Marks Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(rtestType.equals("Test Type"))
        {
            Toast.makeText(this, "Test Type Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            return(false);
        }
        resultClass=new ResultClass(rSubject.getSelectedItem().toString().trim(),
                                    Double.parseDouble(obtainedMarks.getText().toString().trim()),
                                    Double.parseDouble(maxMarks.getSelectedItem().toString().trim()));
        return true;
    }

    boolean getterRem()
    {
        rclass=rClass.getSelectedItem().toString().trim();
        rsection=rSection.getSelectedItem().toString().trim();
        rtestType=testType.getSelectedItem().toString().trim();
        if(regno.getText().toString().trim().length()!=8){
            Toast.makeText(this, "Invalid Registration Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(rclass.equals("Select Class"))
        {
            Toast.makeText(this, "Class Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(rsection.equals("Select Section"))
        {
            Toast.makeText(this, "Section Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(rtestType.equals("Test Type"))
        {
            Toast.makeText(this, "Test Type Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            return(false);
        }
        if(rSubject.getSelectedItem().toString().trim().equals("Select Subject"))
        {
            Toast.makeText(this, "Subject Field Couldn't Be Left Empty", Toast.LENGTH_SHORT).show();
            return(false);
        }
        else
            return true;
    }
    private void initialise() {
        rClass=findViewById(R.id.rClass);
        rSection=findViewById(R.id.rSection);
        rSubject=findViewById(R.id.rSubject);
        testType=findViewById(R.id.testType);
        maxMarks=findViewById(R.id.maxMarks);
        obtainedMarks=findViewById(R.id.marksScored);
        addResultButton=findViewById(R.id.addResultButton);
        removeResultButton=findViewById(R.id.removeResultButton);
        proceed=findViewById(R.id.proceedRP);
        radioGroup=findViewById(R.id.resultPanelUtility);
        regno=findViewById(R.id.RregNO);

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
        rClass.setAdapter(arrayAdapterClass);

        ArrayList<String> sections = new ArrayList<String>();
        sections.add("Select Section");
        sections.add("A");
        sections.add("B");
        sections.add("C");
        sections.add("D");
        ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sections);
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rSection.setAdapter(arrayAdapterSection);

        ArrayList<String>subjectsOffered=new ArrayList<>();
        subjectsOffered.add("Select Subject");
        subjectsOffered.add("Math");
        subjectsOffered.add("Chemistry");
        subjectsOffered.add("Biology");
        subjectsOffered.add("Physics");
        subjectsOffered.add("English Literature");
        subjectsOffered.add("English Language");
        subjectsOffered.add("Hindi Literature");
        subjectsOffered.add("Hindi Language");
        subjectsOffered.add("Social Studies");
        subjectsOffered.add("Psychology");
        subjectsOffered.add("French");
        subjectsOffered.add("Music");
        subjectsOffered.add("German");
        subjectsOffered.add("Computer Science");
        subjectsOffered.add("Physical Education");
        subjectsOffered.add("Information Technology");
        subjectsOffered.add("Chemistry");
        subjectsOffered.add("Math");
        subjectsOffered.add("General Knowledge");
        subjectsOffered.add("Moral Science");
        subjectsOffered.add("Commerce");
        subjectsOffered.add("Business Studies");
        subjectsOffered.add("Art and Craft");
        ArrayAdapter<String> arrayAdapterSubjectsOffered = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjectsOffered);
        arrayAdapterSubjectsOffered.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rSubject.setAdapter(arrayAdapterSubjectsOffered);

        ArrayList<String> test = new ArrayList<String>();
        test.add("Test Type");
        test.add("Test 1");
        test.add("Test 2");
        test.add("Exam 1");
        test.add("Test 3");
        test.add("Exam 2");

        ArrayAdapter<String> arrayAdapterTest = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, test);
        arrayAdapterTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testType.setAdapter(arrayAdapterTest);

        ArrayList<String> max = new ArrayList<String>();
        max.add("Maximum Marks");
        max.add("10");max.add("20");max.add("30");max.add("40");max.add("50");max.add("60");max.add("70");max.add("80");max.add("90");max.add("100");;


        ArrayAdapter<String> arrayAdapterMax = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, max);
        arrayAdapterMax.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxMarks.setAdapter(arrayAdapterMax);
    }
}
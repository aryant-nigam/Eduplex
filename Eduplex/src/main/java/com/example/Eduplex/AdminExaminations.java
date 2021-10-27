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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;

public class AdminExaminations extends AppCompatActivity {
    private Spinner examClass,examSubject,startHour,startMin,examDuration;
    private EditText examDate;
    private Button addExamSchedule,removeExamSchedule,proceed;
    private RadioGroup examsPanelUtility;
    private RadioButton radioButton;
    LinearLayout examPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_examinations);
        initialise();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = findViewById(examsPanelUtility.getCheckedRadioButtonId());
                if (radioButton.getText().toString().equals("Add Examination Schedule")) {
                    examPanel.setVisibility(View.VISIBLE);
                    addExamSchedule.setVisibility(View.VISIBLE);
                    removeExamSchedule.setVisibility(View.GONE);

                    startHour.setVisibility(View.VISIBLE);
                    startMin.setVisibility(View.VISIBLE);
                    examDuration.setVisibility(View.VISIBLE);
                    examDate.setVisibility(View.VISIBLE);

                }
                else if (radioButton.getText().toString().equals("Remove Examination Schedule")) {
                    examPanel.setVisibility(View.VISIBLE);

                    addExamSchedule.setVisibility(View.GONE);
                    removeExamSchedule.setVisibility(View.VISIBLE);

                    startHour.setVisibility(View.GONE);
                    startMin.setVisibility(View.GONE);
                    examDuration.setVisibility(View.GONE);
                    examDate.setVisibility(View.GONE);

                }
            }
        });

        addExamSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference EXAMNODE= FirebaseDatabase.getInstance().getReference().child("School").child("ExaminationRecord").child(examClass.getSelectedItem().toString()).child("Exams");
                if(validate(new ExamClass(
                        examSubject.getSelectedItem().toString(),
                        examDate.getText().toString(),
                        startHour.getSelectedItem().toString()+" : "+startMin.getSelectedItem().toString(),
                        examDuration.getSelectedItem().toString()
                ))) {
                    EXAMNODE.push().setValue(new ExamClass(
                            examSubject.getSelectedItem().toString(),
                            examDate.getText().toString(),
                            startHour.getSelectedItem().toString()+" : "+startMin.getSelectedItem().toString()+"AM",
                            examDuration.getSelectedItem().toString()
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminExaminations.this, "Successfully added Examination Schedule", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminExaminations.this, "Successfully added Examination Schedule", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        removeExamSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!examClass.getSelectedItem().toString().equals("Select Class") && !examSubject.getSelectedItem().toString().equals("Select Subject")) {
                    DatabaseReference EXAMNODE = FirebaseDatabase.getInstance().getReference().child("School").child("ExaminationRecord").child(examClass.getSelectedItem().toString()).child("Exams");
                    EXAMNODE.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            boolean []flag= {true};
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ExamClass ec = (ExamClass) dataSnapshot.getValue(ExamClass.class);
                                    if (ec.getSubjectExam().equals(examSubject.getSelectedItem().toString())) {
                                        EXAMNODE.child("Exams").child(dataSnapshot.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AdminExaminations.this, "Successfully deleted Examination Schedule", Toast.LENGTH_SHORT).show();
                                                flag[0]=false;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AdminExaminations.this, "Failed to delete Examination Schedule", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                        }
                                    }
                            }
                            if(!snapshot.exists()) {
                                Log.d("Ary", "does not exists");
                                Toast.makeText(AdminExaminations.this, "Examination Schedule Unfound", Toast.LENGTH_SHORT).show();
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
                }
            }
        });
    }

    private boolean validate(ExamClass examSchedule) {
        if(examClass.getSelectedItem().toString().equals("Select Class")){
            Toast.makeText(this, "Class field can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(examSubject.getSelectedItem().toString().equals("Select Subject")){
            Toast.makeText(this, "Subject field can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(examSchedule.getTime().split(" : ")[0].equals("Select Exam Start Hour")||examSchedule.getTime().split(" : ")[1].equals("Select Exam Start Minute")){
            Toast.makeText(this, "Either Start Hour or Start Minute is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(examSchedule.getDate().equals("")){
            Toast.makeText(this, "Exam Date should be specified", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(examSchedule.getDuration().equals("Select Exam Duration")) {
            Toast.makeText(this, "Exam Duration  field can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return ValidateDate(examSchedule.getDate());
    }

    private boolean ValidateDate(String Date) {
        if(Date.length()<10||Date.contains(".")||Date.contains("/")||Date.charAt(4)!='-'||Date.charAt(7)!='-'){
            Toast.makeText(this, "Invalid date!! Specify as Directed", Toast.LENGTH_SHORT).show();
            return false;
        }

        ZoneId zid=ZoneId.of("Asia/Kolkata");
        LocalDate today =LocalDate.now(zid);

        ArrayList<Integer>monthWithThirtyDays=new ArrayList<>();
        monthWithThirtyDays.add(2);
        monthWithThirtyDays.add(4);
        monthWithThirtyDays.add(6);
        monthWithThirtyDays.add(9);
        monthWithThirtyDays.add(11);

        String[] date=new String[3];

        date = Date.split("-");

        //Log.d("Ary", Integer.parseInt(date[0]) + "\n" + Integer.parseInt(date[1]) + "\n" + Integer.parseInt(date[2]));

        if(Integer.parseInt(date[1])<0||Integer.parseInt(date[1])>12||Integer.parseInt(date[2])<0||Integer.parseInt(date[2])>31){
            Toast.makeText(this, "Unidentified Date !!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Integer.parseInt(date[0])!=today.getYear()) {
            Toast.makeText(this, "Kindly punch the exams of current year", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Integer.parseInt(date[1])<today.getMonthValue()) {
            Toast.makeText(this, "Kindly punch the exams of current or upcoming Month", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Integer.parseInt(date[2])<today.getDayOfYear() && Integer.parseInt(date[1])<=today.getMonthValue()) {
                Toast.makeText(this, "Kindly punch the exams of current or upcoming Day", Toast.LENGTH_SHORT).show();
                return false;
        }
        if((Integer.parseInt(date[2])==30 || Integer.parseInt(date[2])==31) && Integer.parseInt(date[1])==2) {
            Toast.makeText(this, "Invalid Day", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Integer.parseInt(date[2])==31&&monthWithThirtyDays.contains(Integer.parseInt(date[1]))){
            Toast.makeText(this, "Invalid Day", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initialise() {
        examClass=findViewById(R.id.examClass);
        examSubject=findViewById(R.id.examSubject);

        startHour=findViewById(R.id.startHour);
        startMin=findViewById(R.id.startMin);

        examDuration=findViewById(R.id.examDuration);

        examDate=findViewById(R.id.examDate);

        addExamSchedule=findViewById(R.id.addExam);
        removeExamSchedule=findViewById(R.id.removeExam);

        examsPanelUtility=findViewById(R.id.examsPanelUtility);
        proceed=findViewById(R.id.proceedAEP);

        examPanel=findViewById(R.id.examinationPanel);
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
        examClass.setAdapter(arrayAdapterClass);

        ArrayList<String>subjectsOffered=new ArrayList<>();
        subjectsOffered.add("Maths");
        subjectsOffered.add("Chemistry");
        subjectsOffered.add("Biology");
        subjectsOffered.add("Physics");
        subjectsOffered.add("English Lit");
        subjectsOffered.add("English Lang");
        subjectsOffered.add("Hindi Lit");
        subjectsOffered.add("Hindi Lang");
        subjectsOffered.add("Social Studies");
        subjectsOffered.add("Psychology");
        subjectsOffered.add("French");
        subjectsOffered.add("Music");
        subjectsOffered.add("German");
        subjectsOffered.add("Computer Science");
        subjectsOffered.add("Physical Ed.");
        subjectsOffered.add("IT");
        subjectsOffered.add("GK");
        subjectsOffered.add("Moral Science");
        subjectsOffered.add("Science");
        subjectsOffered.add("Commerce");
        subjectsOffered.add("Business Studies");
        subjectsOffered.add("Art & Craft");
        Collections.sort(subjectsOffered);
        subjectsOffered.add(0,"Select Subject");

        ArrayAdapter<String> arrayAdapterSubjectsOffered = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjectsOffered);
        arrayAdapterSubjectsOffered.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examSubject.setAdapter(arrayAdapterSubjectsOffered);

        ArrayList<String> hour=new ArrayList<String>();
        hour.add("Select Exam Start Hour");
        hour.add("1");
        hour.add("2");
        hour.add("3");
        hour.add("4");
        hour.add("5");
        hour.add("6");
        hour.add("7");
        hour.add("8");
        hour.add("9");
        hour.add("10");
        hour.add("11");
        hour.add("12");
        ArrayAdapter<String> arrayAdapterHour=new ArrayAdapter<String >(this,android.R.layout.simple_spinner_item,hour);
        arrayAdapterHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startHour.setAdapter(arrayAdapterHour);

        ArrayList<String> minutes=new ArrayList<String>();
        minutes.add("Select Exam Start Minute");
        minutes.add("00");
        minutes.add("5");
        minutes.add("10");
        minutes.add("15");
        minutes.add("20");
        minutes.add("25");
        minutes.add("30");
        minutes.add("35");
        minutes.add("40");
        minutes.add("45");
        minutes.add("50");
        minutes.add("55");
        ArrayAdapter<String> arrayAdapterMinutes=new ArrayAdapter<String >(this,android.R.layout.simple_spinner_item,minutes);
        arrayAdapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMin.setAdapter(arrayAdapterMinutes);

        ArrayList<String> duration=new ArrayList<String>();
        duration.add("Select Exam Duration");
        duration.add("30 min");
        duration.add("45 min");
        duration.add("1 hr");
        duration.add("1 hr 30 min");
        duration.add("2 hr");
        duration.add("2 hr 30 min");
        duration.add("3 hr");
        ArrayAdapter<String> arrayAdapterDuration=new ArrayAdapter<String >(this,android.R.layout.simple_spinner_item,duration);
        arrayAdapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examDuration.setAdapter(arrayAdapterDuration);

    }
}
/*EXAMNODE.push().setValue(new ExamClass(
                            examSubject.getSelectedItem().toString(),
                            examDate.getText().toString(),
                            startHour.getSelectedItem().toString()+" : "+startMin.getSelectedItem().toString()+"AM",
                            examDuration.getSelectedItem().toString()
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminExaminations.this, "Successfully added Examination Schedule", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminExaminations.this, "Successfully added Examination Schedule", Toast.LENGTH_SHORT).show();
                        }
                    });*/
package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminTimetable extends AppCompatActivity {

    private Spinner TTclass,TTsection,TTday,TTsubjectName;
    private EditText subjectTiming,subjectTeacher;
    private Button addMoreEntries,deleteTimetable,addTimetable,proceed;
    private ArrayList<TimetableClass>_daySchedule;
    LinearLayout timetablePanel;
    //private TimetableClass _subjectSchedule;
    private String _subjectName,_subjectTiming ,_subjectTeacher,_class,_section,_day;

    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private Switch enableMultipleEntries;
    int flag;
    boolean multiple;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_timetable);
        initialiseSpinnersSwitchButtons();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioButton.getText().toString().equals("Add Timetable")) {
                    Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                    //addTimetable.setVisibility(View.VISIBLE);
                    //addMoreEntries.setVisibility(View.VISIBLE);
                    TTclass.setVisibility(View.VISIBLE);
                    TTsection.setVisibility(View.VISIBLE);
                    TTday.setVisibility(View.VISIBLE);
                    TTsubjectName.setVisibility(View.VISIBLE);
                    subjectTiming.setVisibility(View.VISIBLE);
                    subjectTeacher.setVisibility(View.VISIBLE);
                    enableMultipleEntries.setVisibility(View.VISIBLE);
                    deleteTimetable.setVisibility(View.GONE);
                    addTimetable.setVisibility(View.VISIBLE);;
                    timetablePanel.setVisibility(View.VISIBLE);

                } else if (radioButton.getText().toString().equals("Remove Timetable")) {
                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    //addTimetable.setVisibility(View.GONE);
                    //addMoreEntries.setVisibility(View.GONE);
                    TTclass.setVisibility(View.VISIBLE);
                    TTsection.setVisibility(View.VISIBLE);
                    TTday.setVisibility(View.VISIBLE);
                    TTsubjectName.setVisibility(View.VISIBLE);
                    subjectTiming.setVisibility(View.GONE);
                    subjectTeacher.setVisibility(View.GONE);
                    enableMultipleEntries.setVisibility(View.GONE);
                    addTimetable.setVisibility(View.GONE);
                    addMoreEntries.setVisibility(View.GONE);
                    deleteTimetable.setVisibility(View.VISIBLE);
                    timetablePanel.setVisibility(View.VISIBLE);
                }
            }
        });

        enableMultipleEntries.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    if (TTclass.getSelectedItem().toString().equals("Select Class") || TTsection.getSelectedItem().toString().equals("Select Section") || TTday.getSelectedItem().toString().equals("Select Day")) {
                        enableMultipleEntries.setChecked(false);
                        Toast.makeText(getApplicationContext(), "Make sure you have selected Class Section and day before enabling multiple entries ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Class , Section and Day will be locked !", Toast.LENGTH_SHORT).show();
                        addMoreEntries.setVisibility(View.VISIBLE);
                        TTclass.setEnabled(false);
                        TTsection.setEnabled(false);
                        TTday.setEnabled(false);
                        multiple = true;
                        addMoreEntries.setVisibility(View.VISIBLE);
                        addTimetable.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Class , Section and Day are unlocked !", Toast.LENGTH_SHORT).show();
                    addMoreEntries.setVisibility(View.GONE);
                    TTclass.setEnabled(true);
                    TTsection.setEnabled(true);
                    TTday.setEnabled(true);
                    addMoreEntries.setVisibility(View.GONE);
                    addTimetable.setVisibility(View.VISIBLE);
                    multiple = false;
                }
            }
        });

        addMoreEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _subjectName = TTsubjectName.getSelectedItem().toString();
                _subjectTiming = subjectTiming.getText().toString().trim();
                _subjectTeacher = subjectTeacher.getText().toString().trim();
                Toast.makeText(AdminTimetable.this, _subjectName+" "+_subjectTiming+" "+_subjectTeacher, Toast.LENGTH_SHORT).show();
                _daySchedule=new ArrayList<>();
                if (validate()) {
                   _daySchedule.add(new TimetableClass(_subjectName, _subjectTiming, _subjectTeacher));
                    manageMultipleAddition();
                    TTsubjectName.setSelection(0);
                    subjectTiming.setText("");
                    subjectTeacher.setText((""));
                    _daySchedule.clear();
                }

            }
        });

        addTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _subjectName = TTsubjectName.getSelectedItem().toString();
                _subjectTiming = subjectTiming.getText().toString().trim();
                _subjectTeacher = subjectTeacher.getText().toString().trim();
                if (validate()) {
                    _daySchedule=new ArrayList<>();
                    _daySchedule.add(new TimetableClass(_subjectName, _subjectTiming, _subjectTeacher));
                    DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord")
                            .child(TTclass.getSelectedItem().toString())
                            .child(TTsection.getSelectedItem().toString())
                            .child(TTday.getSelectedItem().toString())
                            .child("Schedule");
                    NODE.push().setValue(_daySchedule.get(0)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminTimetable.this, "Successfully Added The Timetable", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(AdminTimetable.this, "Failed To Add The Timetable : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        deleteTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord")
                        .child(TTclass.getSelectedItem().toString())
                        .child(TTsection.getSelectedItem().toString())
                        .child(TTday.getSelectedItem().toString())
                        .child("Schedule");
                NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminTimetable.this, "Successfully Deleted The Subject's Schedule", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    Toast.makeText(AdminTimetable.this, "Failed To Delete The Subject's Schedule", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(AdminTimetable.this, "The Subject's Schedule Doesn't Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
            }
        });
    }

    private void manageMultipleAddition() {
        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TimetableRecord")
                .child(TTclass.getSelectedItem().toString())
                .child(TTsection.getSelectedItem().toString())
                .child(TTday.getSelectedItem().toString())
                .child("Schedule");
        NODE.push().setValue(_daySchedule.get(0)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AdminTimetable.this, "Successfully Added The Timetable", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(AdminTimetable.this, "Failed To Add The Timetable : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validate() {
        if(_subjectName.equals("Select Subject"))
        {
            Toast.makeText(this, "Subject field can't be empty", Toast.LENGTH_LONG);
            return false;
        }
        if(hasSpecialCharecters(_subjectTiming)||_subjectTiming.equals(""))
            {
                Toast.makeText(this, "Invalid Time format", Toast.LENGTH_LONG);
                return  false;
            }
        if(nameHasSpecialCharectersNumbers(_subjectTeacher)||_subjectTeacher.equals(""))
        {
            Toast.makeText(this, "Invalid Teacher's Name", Toast.LENGTH_LONG);
            return  false;
        }

        Toast.makeText(this, "validated", Toast.LENGTH_SHORT).show();
        return validateTiming();
    }

    private boolean validateTiming()
    {
        boolean status=true;
        if(subjectTiming.getText().toString().length()!=15)
        {
            Toast.makeText(this, "Invalid Format : check hint", Toast.LENGTH_SHORT).show();
            status=false;
        }

        String []firstSplits=subjectTiming.getText().toString().split("-");
        Log.d("Ary",firstSplits[0]+"    "+firstSplits[1]);

        for(String splitOne:firstSplits)
        {
            String[]secondSplits=splitOne.split(":");
            Log.d("Ary",secondSplits[0]+"\n"+ secondSplits[1].substring(0,2)+"\n"+secondSplits[1].substring(2,4));
            Log.d("Ary", String.valueOf((secondSplits[1].substring(2,4).equals("AM"))));
            if(Integer.parseInt(secondSplits[0])<0||Integer.parseInt(secondSplits[0])>12)
            {
                Toast.makeText(this, "Specify in 12 hrs format", Toast.LENGTH_SHORT).show();
                status=false;
                break;
            }
            if(Integer.parseInt(secondSplits[1].substring(0,2))<0||Integer.parseInt(secondSplits[1].substring(0,2))>59)
            {
                Toast.makeText(this, "Invalid start minutes", Toast.LENGTH_SHORT).show();
                status=false;
                break;
            }
            if((secondSplits[1].substring(2,4).equals("AM"))|| (secondSplits[1].substring(2,4).equals("PM")))
            {

            }
            else
            {
                Toast.makeText(this, "Only AM or PM is valid", Toast.LENGTH_SHORT).show();
                status=false;
                break;
            }
        }
        return status;
    }
    private boolean hasSpecialCharecters(String name) {
        for(int i=0;i<name.length();i++)
        {
            if((name.charAt(i)>=33 && name.charAt(i)<=47 || name.charAt(i)>=59 && name.charAt(i)<=64 && name.charAt(i)>=91 && name.charAt(i)<=96 &&name.charAt(i)>=123 && name.charAt(i)<=126))
                return false;
        }
        return true;
    }

    private boolean nameHasSpecialCharectersNumbers(String name) {
        for(int i=0;i<name.length();i++)
        {
            if(!(name.charAt(i)>=65&&name.charAt(i)<=90
                    ||(name.charAt(i)>=97&&name.charAt(i)<=122)
                    ||(name.charAt(i)==32)))
                return true;
        }
        return false;
    }


    private void initialiseSpinnersSwitchButtons(){
        TTclass=findViewById(R.id.TTclass);
        TTsection=findViewById(R.id.TTsection);
        TTday=findViewById(R.id.TTday);
        TTsubjectName=findViewById(R.id.TTsubjectName);

        subjectTiming =findViewById(R.id.TTsubjectTiming);
        subjectTeacher=findViewById(R.id.TTsubjectTeacher);

        enableMultipleEntries =findViewById(R.id.enableMoreEntries);
        addMoreEntries=findViewById(R.id.addMoreEntries);
        deleteTimetable=findViewById(R.id.deleteTimetable);
        addTimetable=findViewById(R.id.addTimetableButton);
        radioGroup=findViewById(R.id.timetablePanelUtility);
        proceed=findViewById(R.id.proceedATP);
        timetablePanel=findViewById(R.id.timetablePanel);

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
        TTclass.setAdapter(arrayAdapterClass);

        ArrayList<String> sections = new ArrayList<String>();
        sections.add("Select Section");
        sections.add("A");
        sections.add("B");
        sections.add("C");
        sections.add("D");

        ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sections);
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TTsection.setAdapter(arrayAdapterSection);

        ArrayList<String> days = new ArrayList<String>();
        days.add("Select Day");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");

        ArrayAdapter<String> arrayAdapterDays= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        arrayAdapterDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TTday.setAdapter(arrayAdapterDays);

        ArrayList<String>subjectsOffered=new ArrayList<>();
        subjectsOffered.add("Select Subject");
        subjectsOffered.add("Maths");
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
        subjectsOffered.add("General Knowledge");
        subjectsOffered.add("Moral Science");
        subjectsOffered.add("Commerce");
        subjectsOffered.add("Business Studies");
        subjectsOffered.add("Art and Craft");
        ArrayAdapter<String> arrayAdapterSubjectsOffered = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjectsOffered);
        arrayAdapterSubjectsOffered.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TTsubjectName.setAdapter(arrayAdapterSubjectsOffered);

    }
}

//if(_subjectTiming.matches("/([0-9]{1,2})+[:]+([0-9]{1,2})+[ ]+([a-zA-Z]{2})-.*/g"))     //8:00 AM-8:40 AM
//

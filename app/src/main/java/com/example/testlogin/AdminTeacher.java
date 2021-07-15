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

public class AdminTeacher extends AppCompatActivity {
    private EditText tid,tname,tmail,tidDel;
    private Button addTeacher,removeTeacher,proceed;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private LinearLayout AddTeacherPanel,DeleteTeacherPanel;
    private int checked;
    private String radioString,stid,stname,stclass,stmail,stSection,stidDel;
    final String emailregex="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    Spinner classTeacherOf,sectionTeacherOf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher);
        initialise();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(radioGroup.getCheckedRadioButtonId());
                radioString=radioButton.getText().toString();
                if(radioString.equals("Add  New Teacher's Record"))
                {
                    Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                    DeleteTeacherPanel.setVisibility(View.GONE);
                    AddTeacherPanel.setVisibility(View.VISIBLE);
                }
                else if(radioString.equals("Delete Teachers Record"))
                {
                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    AddTeacherPanel.setVisibility(View.GONE);
                    DeleteTeacherPanel.setVisibility(View.VISIBLE);
                }
            }
        });

        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stid=tid.getText().toString().trim();
                stname=tname.getText().toString().trim();
                stclass=classTeacherOf.getSelectedItem().toString().trim();
                stSection=sectionTeacherOf.getSelectedItem().toString().trim();
                stmail=tmail.getText().toString().trim();
                TeachersManager teachersManager=new TeachersManager(stid,stname,stclass+"-"+stSection,stmail);
                if(validate(teachersManager))
                {
                    DatabaseReference NODE=FirebaseDatabase.getInstance().getReference().child("School").child("TeachersRecord").child(teachersManager.get_id());
                    NODE.setValue(teachersManager).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminTeacher.this, "Added Teacher Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminTeacher.this, "Failed To Add Teacher : "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        removeTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stidDel=tidDel.getText().toString();
                if(isNumeric(stidDel))
                {
                    DatabaseReference NODE=FirebaseDatabase.getInstance().getReference().child("School").child("TeachersRecord").child(stidDel);
                   NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull  DataSnapshot snapshot) {
                           if(snapshot.hasChildren())
                           {
                               NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(AdminTeacher.this, "Teacher's record Deleted Successfully", Toast.LENGTH_SHORT).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull  Exception e) {
                                       Toast.makeText(AdminTeacher.this, "Teacher's record Deletion Failed : " +e.getMessage(), Toast.LENGTH_LONG).show();
                                   }
                               });
                           }
                           else
                               Toast.makeText(AdminTeacher.this, "Mentioned Teacher's Id record Doesnt Exists", Toast.LENGTH_SHORT).show();

                       }

                       @Override
                       public void onCancelled(@NonNull  DatabaseError error) {

                       }
                   });
                }
            }
        });
    }

    private boolean validate(TeachersManager teachersManager) {
        if(!teachersManager.get_mail().matches(emailregex))
        {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(hasSpecialCharecters(teachersManager.get_name()))
        {
            Toast.makeText(this, "Invalid Teacher's Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isNumeric(teachersManager.get_id()))
        {
            Toast.makeText(this, "Invalid Teachers Id "+teachersManager.get_id(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isNumeric(String id) {
        for(int i=0;i<id.length();i++)
        {
            if(!(id.charAt(i)>=48&&id.charAt(i)<=57))
            {
                return false;
            }
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


    private void initialise() {
        tid=findViewById(R.id.tid);
        tname=findViewById(R.id.tname);
        tmail=findViewById(R.id.tmail);
        addTeacher=findViewById(R.id.addTeacher);
        removeTeacher=findViewById(R.id.removeTeacher);
        radioGroup=findViewById(R.id.teachersPanelUtility);
        proceed=findViewById(R.id.proceedATP);
        AddTeacherPanel=findViewById(R.id.AddTeacherPanel);
        DeleteTeacherPanel=findViewById(R.id.DeleteTeacherPanel);
        classTeacherOf=findViewById(R.id.tClass);
        sectionTeacherOf=findViewById(R.id.tSection);
        tidDel=findViewById(R.id.tidDel);
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
        classTeacherOf.setAdapter(arrayAdapterClass);

        ArrayList<String> sections = new ArrayList<String>();
        sections.add("Select Section");
        sections.add("A");
        sections.add("B");
        sections.add("C");
        sections.add("D");

        ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sections);
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionTeacherOf.setAdapter(arrayAdapterSection);

    }

}
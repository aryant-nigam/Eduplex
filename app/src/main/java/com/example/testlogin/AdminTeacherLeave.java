package com.example.testlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminTeacherLeave extends AppCompatActivity {

    private Button addLeaveButton,removeLeaveButton,proceed;
    RadioButton radioButton;
    RadioGroup radioGroup;
    EditText tid,message;
    LinearLayout TOLPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_leave);
        initialise();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton=findViewById(radioGroup.getCheckedRadioButtonId());
                if(radioButton.getText().toString().equals("Add Teacher's Leave"))
                {
                    Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                    TOLPanel.setVisibility(View.VISIBLE);
                    removeLeaveButton.setVisibility(View.GONE);
                    addLeaveButton.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                }
                else if(radioButton.getText().toString().equals("Remove Teacher's Leave"))
                {
                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    TOLPanel.setVisibility(View.VISIBLE);
                    addLeaveButton.setVisibility(View.GONE);
                    removeLeaveButton.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                }
            }
        });

        addLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TeachersRecord").child(tid.getText().toString().trim());
                    NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                DatabaseReference LEAVENODE = FirebaseDatabase.getInstance().getReference().child("School").child("TeachersOnLeaveRecord").child(tid.getText().toString().trim());
                                LEAVENODE.setValue(new TeacherOnLeaveClass(tid.getText().toString().trim(),
                                        snapshot.child("_name").getValue().toString().trim(),
                                        message.getText().toString().trim()
                                )).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AdminTeacherLeave.this, "Successully Added Teacher's Leave", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(AdminTeacherLeave.this, "Failed To Teacher's Leave", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                tid.setText("");
                                message.setText("");
                            }
                            else{
                                Toast.makeText(AdminTeacherLeave.this, "Teacher's Id Unfound", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        removeLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("TeachersOnLeaveRecord").child(tid.getText().toString().trim());
                NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminTeacherLeave.this, "Successfully Removed Teacher's Leave", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminTeacherLeave.this, "Failed To Remove Teacher's Leave : " + e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                            tid.setText("");
                            message.setText("");
                        }
                        else
                            Toast.makeText(AdminTeacherLeave.this, "Teacher's Leave Record Unfound !!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
            }
        });
    }

    private boolean validate() {
        if(tid.getText().toString().trim().length()!=6||!isNumeric(tid.getText().toString().trim()))
        {
            Toast.makeText(this, "Invalid Teacher's id", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private boolean isNumeric(String id) {
        for (int i = 0; i < id.length(); i++) {
            if (!(id.charAt(i) >= 48 && id.charAt(i) <= 57)) {
                return false;
            }
        }
        return true;
    }

    private void initialise() {
        addLeaveButton=findViewById(R.id.addLeaveButton);
        removeLeaveButton=findViewById(R.id.removeLeaveButton);
        proceed=findViewById(R.id.proceedTOL);

        radioGroup=findViewById(R.id.teacherOnLeaveUtility);

        tid=findViewById(R.id.TOLid);
        message=findViewById(R.id.TOLmessage);

        TOLPanel=findViewById(R.id.TOLPanel);
    }
}
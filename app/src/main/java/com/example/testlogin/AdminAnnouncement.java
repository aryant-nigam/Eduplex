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

public class AdminAnnouncement extends AppCompatActivity {

    private EditText announcementPSubject, announcementPBody, announcementSubjectDel;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addAnnouncement, deleteAnnouncement, proceed;
    LinearLayout addAnnPanel, removeAnnPanel;
    String subject, body, subjectDel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_announcement);
        initialise();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioButton.getText().toString().equals("Add Announcement")) {
                    Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                    removeAnnPanel.setVisibility(View.GONE);
                    addAnnPanel.setVisibility(View.VISIBLE);
                } else if (radioButton.getText().toString().equals("Remove Announcement")) {
                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    addAnnPanel.setVisibility(View.GONE);
                    removeAnnPanel.setVisibility(View.VISIBLE);
                }
            }
        });
        addAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnnouncement.setEnabled(false);
                subject = announcementPSubject.getText().toString().trim();
                body = announcementPBody.getText().toString().trim();
                AnnouncementClass announcementClass = new AnnouncementClass(subject, body);
                DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("AnnouncementsRecord").child(announcementClass.getAnnouncementSubject().toLowerCase());
                NODE.setValue(announcementClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdminAnnouncement.this, "Announcement Successfully Added", Toast.LENGTH_SHORT).show();
                        addAnnouncement.setEnabled(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminAnnouncement.this, "Failed To Add Announcement : " + e.getMessage(), Toast.LENGTH_LONG).show();
                        addAnnouncement.setEnabled(true);
                    }
                });
            }
        });


        deleteAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAnnouncement.setEnabled(false);
                subjectDel = announcementSubjectDel.getText().toString().toLowerCase();
                DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("AnnouncementsRecord").child(subjectDel);

                NODE.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminAnnouncement.this, "Successfully Deleted The Announcement", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminAnnouncement.this, "Failed To Delete Announcement : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(AdminAnnouncement.this, "Subject Unfound", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
                deleteAnnouncement.setEnabled(true);
            }

        });
    }



    private void initialise() {
        announcementPSubject=findViewById(R.id.announcementPSubject);
        announcementPBody=findViewById(R.id.announcementPBody);
        radioGroup=findViewById(R.id.announcementPanelUtility);
        announcementSubjectDel=findViewById(R.id.announcementSubjectDel);
        addAnnouncement=findViewById(R.id.addAnnouncement);
        deleteAnnouncement=findViewById(R.id.removeAnnouncement);
        proceed=findViewById(R.id.proceedAP);
        addAnnPanel=findViewById(R.id.addAnnPanel);
        removeAnnPanel=findViewById(R.id.removeAnnPanel);
    }
}
/*
* */
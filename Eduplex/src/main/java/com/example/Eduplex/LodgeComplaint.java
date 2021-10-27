package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LodgeComplaint extends AppCompatActivity {

    private Spinner complaintType;
    private EditText complaintDescription;
    private TextView charCounter;
    private Button lodgeComplaint;
    private ListView allComplaintList;
    private StudentsManager studentsManager;
    private ArrayList<ComplaintClass> complaints;
    private complaintStatusAdapter complaintAdapter;
    DatabaseReference COMPLAINTNODE;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lodge_complaint);
        initialise();

        complaintDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length=complaintDescription.length();
                charCounter.setText(String.valueOf(length)+" / 120");
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length=complaintDescription.length();
                charCounter.setText(String.valueOf(length)+" / 120");
            }
            @Override
            public void afterTextChanged(Editable s) {
                int length=complaintDescription.length();
                charCounter.setText(String.valueOf(length)+" / 120");
            }
        });

        allComplaintList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LodgeComplaint.this);
                alertDialogBuilder.setTitle("RESPONSE");
                alertDialogBuilder.setMessage(complaints.get(position).getResponse());
                alertDialogBuilder.setPositiveButton("CLOSE",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                if(!complaints.get(position).getResponse().equals("")) {
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else
                {
                    Snackbar snackbar=Snackbar.make(view,"Complaint hasn't been responded",Snackbar.LENGTH_LONG).setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //snackbar.dismiss();
                        }
                    });
                    snackbar.setBackgroundTint(Color.parseColor("#000000"));
                    snackbar.setTextColor(Color.parseColor("#f2a191"));
                    snackbar.setActionTextColor(Color.parseColor("#f07aa0"));
                    snackbar.show();
                }
            }
        });

        lodgeComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LodgeComplaint.this);
                alertDialogBuilder.setTitle("ANNONIMITY CONFIRMATION");
                alertDialogBuilder.setMessage("If you chose to lodge this complaint under anonimity click YES else click NO.\nAnonimity will not reveal your identity, But is traceable in case of inappropriate request description.");
                alertDialogBuilder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        lodgeComplaintUtility(studentsManager.get_name(),studentsManager.get_class(),studentsManager.get_section(),studentsManager.get_registrationNumber(),true);
                    }
                });

                alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lodgeComplaintUtility(studentsManager.get_name(),studentsManager.get_class(),studentsManager.get_section(),studentsManager.get_registrationNumber(),false);
                    }
                });

                if(validate())
                {
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }


    private boolean validate() {
        if(complaintType.getSelectedItem().toString().equals("Select Complaint Type")){
            Toast.makeText(this, "Kindly select the complaint type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(complaintDescription.getText().toString().trim().equals("")||complaintDescription.getText().toString().length()<=20)
        {
            Toast.makeText(this, "Kindly explain the issue properly for better response", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initialise() {
        complaintType=findViewById(R.id.complaintType);
        complaintDescription=findViewById(R.id.complaintDescription);
        charCounter=findViewById(R.id.charCounter);
        lodgeComplaint=findViewById(R.id.lodgeComplaint);
        allComplaintList=findViewById(R.id.allComplaintList);

        ArrayList<String> Type = new ArrayList<String>();
        Type.add("Select Complaint Type");
        Type.add("Laboratory Issue");
        Type.add("Gaming Equipment Issue");
        Type.add("Grievance about Faculty");
        Type.add("Grievance about Helper Staff");
        Type.add("Conveyance Issue");
        Type.add("Cleaning & Dusting Issue");
        Type.add("Other");

        ArrayAdapter<String> arrayAdapterCType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Type);
        arrayAdapterCType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        complaintType.setAdapter(arrayAdapterCType);

        studentsManager=(StudentsManager)getIntent().getSerializableExtra("StudentData");
        complaints =new ArrayList();

        if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
        {
            COMPLAINTNODE= FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintRecord").child(studentsManager.get_registrationNumber());

            COMPLAINTNODE= FirebaseDatabase.getInstance().getReference();
            childEventListener=COMPLAINTNODE.child("School").child("ComplaintRecord").child(studentsManager.get_registrationNumber())
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        for(DataSnapshot complaintSnap:snapshot.getChildren())
                        {
                           ComplaintClass cc= (ComplaintClass) complaintSnap.getValue(ComplaintClass.class);
                           complaints.add(cc);
                        }
                        complaintAdapter=new complaintStatusAdapter(getApplicationContext(),complaints);
                        allComplaintList.setAdapter(complaintAdapter);
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
        else
        {
            Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
        }
        COMPLAINTNODE.removeEventListener(childEventListener);
    }

    private void lodgeComplaintUtility(String _name,String _class,String _section,String _regno,boolean isAnonymous) {
        ComplaintClass complaintClass;
        if(!isAnonymous)
             complaintClass=new ComplaintClass(
                    complaintType.getSelectedItem().toString(),
                    complaintDescription.getText().toString(),
                    _name,
                    _class,
                    _regno,
                    "Lodged",
                    "",
                     _name+"-"+_class+_section+"-"+_regno,
                     null
            );
        else
            complaintClass=new ComplaintClass(
                    complaintType.getSelectedItem().toString(),
                    complaintDescription.getText().toString(),
                    "Anonymous",
                    "XX-X",
                    "XXXXXXXX",
                    "Lodged",
                    "",
                    _name+"-"+_class+_section+"-"+_regno,
                    null
                    );

            COMPLAINTNODE= FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintRecord").child(studentsManager.get_registrationNumber()).child("Complaints");
            DatabaseReference COMPLAINTLISTNODE= FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintList").child("Complaints");

        if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
            {
                COMPLAINTNODE.push().setValue(complaintClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(LodgeComplaint.this, "Complaint Lodging Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                COMPLAINTLISTNODE.push().setValue(new ComplaintToken(studentsManager.get_registrationNumber(),false)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LodgeComplaint.this);
                        alertDialogBuilder.setTitle("SUCESSFUL");
                        alertDialogBuilder.setMessage("Your complaint has been sucessfully lodged. You will recieve a response soon.");
                        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                              finish();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LodgeComplaint.this, "Complaint Lodging Token generation failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Network Uavailable !!", Toast.LENGTH_SHORT).show();
            }

    }

}
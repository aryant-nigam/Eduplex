package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PutComplaintResponse extends AppCompatActivity {

    TextView complaintTypeResponse,complaintDescriptionResponse;
    EditText complaintResponseResponse;
    Button putResponse,revealAnonimity;
    ComplaintClass complaint;

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_complaint_response);
        complaint= (ComplaintClass) getIntent().getSerializableExtra("ComplaintDetails");
        initialise();

        putResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!complaintResponseResponse.getText().toString().trim().equals("")) {

                    complaint.setResponse(createFormat());
                    Map<String, Object> data = new HashMap<>();
                    data.put("complaintType", complaint.getComplaintType());
                    data.put("complaintDescription", complaint.getComplaintDescription());
                    data.put("lodgedBy", complaint.getLodgedBy());
                    data.put("classOfStudent", complaint.getClassOfStudent());
                    data.put("registrationNoOfStudent", complaint.getRegistrationNoOfStudent());
                    data.put("status", "Responded");
                    data.put("response", complaint.getResponse());
                    data.put("revealAnonimity", complaint.getRevealAnonimity());
                    DatabaseReference COMPLAINTNODE = FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintRecord").child(complaint.getRevealAnonimity().substring(complaint.getRevealAnonimity().length()-8,complaint.getRevealAnonimity().length())).child("Complaints").child(complaint.getToken());
                    COMPLAINTNODE.updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(PutComplaintResponse.this, "You have successfully responded to the complaint", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),AdminComplaints.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PutComplaintResponse.this, "Failed to respond complaint : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        revealAnonimity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference CHECKANONIMITYCHANCES = FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintAnonimityRights");
                CHECKANONIMITYCHANCES.child("leftChances").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        if(Integer.parseInt(snapshot.getValue().toString().trim())>=1)
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PutComplaintResponse.this);
                            alertDialogBuilder.setTitle("Who Lodged The Complaint ?");
                            alertDialogBuilder.setCancelable(false);
                            String _name=complaint.getRevealAnonimity().split("-")[0];
                            String _class=complaint.getRevealAnonimity().split("-")[1];
                            String _regno=complaint.getRevealAnonimity().split("-")[2];
                            alertDialogBuilder.setMessage(
                                    "Registration Number : "+_regno+"\n"+
                                    "Name : "+_name+"\n"+
                                    "Class : "+_class
                            );
                            alertDialogBuilder.setPositiveButton("CLOSE",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    HashMap<String, Object> refreshedChances = new HashMap<>();
                                    refreshedChances.put("leftChances", String.valueOf(Integer.parseInt(snapshot.getValue().toString().trim())-1));
                                    DatabaseReference REFRESH = FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintAnonimityRights");

                                    REFRESH.updateChildren(refreshedChances).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(PutComplaintResponse.this, "Anonimity Reveal Chances Left from 1st reveal after refresh : "+refreshedChances.get("leftChances"), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PutComplaintResponse.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                        else
                        {
                            DatabaseReference CHECKNEXTREFRESH = FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintAnonimityRights");
                            CHECKNEXTREFRESH.child("firstRevealDate").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    LocalDate firstRevealDate = LocalDate.parse(snapshot.getValue().toString().trim());

                                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(PutComplaintResponse.this);
                                    alertDialogBuilder.setTitle("Action Denied");
                                    alertDialogBuilder.setMessage("You have utilised your monthly limit of 5 Anonimity reveals,wait till your limit refreshes \n\n"+
                                    "Date of Next Refresh : "+String.valueOf(firstRevealDate.plusDays(31l)));
                                    alertDialogBuilder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    AlertDialog alertDialog=alertDialogBuilder.create();
                                    alertDialog.show();
                                    }
                                @Override
                                public void onCancelled(@NonNull  DatabaseError error) {
                                    Toast.makeText(PutComplaintResponse.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });

            }
        });
    }



    private String createFormat() {
        return("Dear Student,\nYour Query/Complaint has been noted and relevant action for betterment has been initiated."+complaintResponseResponse.getText().toString().trim()+"\nThanks for contacting us.\n\nComplaints Department");
    }

    private void initialise() {
        complaintTypeResponse=findViewById(R.id.complaintTypeResponse);
        complaintDescriptionResponse=findViewById(R.id.complaintDescriptionResponse);
        complaintResponseResponse=findViewById(R.id.complaintResponseResponse);
        putResponse=findViewById(R.id.putResponse);
        revealAnonimity=findViewById(R.id.revealAnonimity);

        complaintTypeResponse.setText(complaint.getComplaintType());
        complaintDescriptionResponse.setText(complaint.getComplaintDescription());

    }
}
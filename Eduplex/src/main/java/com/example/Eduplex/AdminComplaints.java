package com.example.Eduplex;

import static org.webrtc.ContextUtils.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AdminComplaints extends AppCompatActivity {
    private ListView unresolvedComplaintList,resolvedComplaintList;
    private ArrayList<ComplaintClass> AllComplaints;
    private ArrayList<ComplaintClass> unresolvedList,resolvedList;
    private HashSet<String> Token;
    private AdminComplaintsAdapter adminUnresolvedComplaintsAdapter,adminResolvedComplaintsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complaints);
        initialise();


        unresolvedComplaintList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComplaintClass complaintClicked=unresolvedList.get(position);
                if(complaintClicked.getStatus().equals("Lodged")) {

                    Map<String, Object> data = new HashMap<>();
                    data.put("complaintType", complaintClicked.getComplaintType());
                    data.put("complaintDescription", complaintClicked.getComplaintDescription());
                    data.put("lodgedBy", complaintClicked.getLodgedBy());
                    data.put("classOfStudent", complaintClicked.getClassOfStudent());
                    data.put("registrationNoOfStudent", complaintClicked.getRegistrationNoOfStudent());
                    data.put("status", "Seen");
                    data.put("response", complaintClicked.getResponse());
                    data.put("revealAnonimity", complaintClicked.getRevealAnonimity());

                    DatabaseReference COMPLAINTNODE = FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintRecord").child(complaintClicked.getRevealAnonimity().substring(complaintClicked.getRevealAnonimity().length()-8,complaintClicked.getRevealAnonimity().length())).child("Complaints").child(complaintClicked.getToken());
                    Toast.makeText(AdminComplaints.this, complaintClicked.getToken(), Toast.LENGTH_SHORT).show();
                    COMPLAINTNODE.updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminComplaints.this, "Complaint has been marked as SEEN ", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(AdminComplaints.this, "Complaint failed to be marked as SEEN : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Intent putResponseIntent=new Intent(getApplicationContext(),PutComplaintResponse.class);
                putResponseIntent.putExtra("ComplaintDetails",complaintClicked);
                startActivity(putResponseIntent);
            }
        });

        resolvedComplaintList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminComplaints.this);
                alertDialogBuilder.setTitle("RESPONSE");
                alertDialogBuilder.setMessage(resolvedList.get(position).getResponse());
                alertDialogBuilder.setPositiveButton("CLOSE",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    private void initialise() {
        unresolvedComplaintList = findViewById(R.id.unresolvedComplaintsListAdmin);
        resolvedComplaintList = findViewById(R.id.resolvedComplaintsListAdmin);
        AllComplaints = new ArrayList<>();
        unresolvedList = new ArrayList<>();
        resolvedList=new ArrayList<>();

        Token = new HashSet<>();

        if (CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
            DatabaseReference COMPLAINTLISTNODE = FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintList");

            COMPLAINTLISTNODE.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        for (DataSnapshot TokenSnap : snapshot.getChildren()) {
                            ComplaintToken token = TokenSnap.getValue(ComplaintToken.class);
                            Token.add(token.getRegistrationNumber());
                        }
                         getComplaints();
                    }
                }
                @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }@Override public void onChildRemoved(@NonNull DataSnapshot snapshot) { }@Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }@Override public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    private void getComplaints() {
        adminUnresolvedComplaintsAdapter = new AdminComplaintsAdapter(getApplicationContext(), unresolvedList);
        adminResolvedComplaintsAdapter = new AdminComplaintsAdapter(getApplicationContext(), resolvedList);
        for (String rno : Token) {
            if (CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())) {
                DatabaseReference COMPLAINTNODE = FirebaseDatabase.getInstance().getReference().child("School").child("ComplaintRecord").child(rno);

                COMPLAINTNODE.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            for (DataSnapshot complaintSnap : snapshot.getChildren()) {
                                ComplaintClass cc= (ComplaintClass) complaintSnap.getValue(ComplaintClass.class);
                                AllComplaints.add(cc);

                                if(cc.getToken()==null)
                                    cc.setToken(complaintSnap.getKey());

                                if(cc.getResponse().equals("")) {
                                    unresolvedList.add(cc);
                                    Log.d("Aryant", "unresolved:"+String.valueOf(unresolvedList.size()));
                                    initialiseListViews(1);
                                }
                                else {
                                    resolvedList.add(cc);
                                    Log.d("Aryant", "resolved:"+String.valueOf(resolvedList.size()));
                                    initialiseListViews(2);
                                }
                            }
                        }
                    }
                    @Override public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }@Override public void onChildRemoved(@NonNull DataSnapshot snapshot) { }@Override public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }@Override public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
            }
        }

        }

    private void initialiseListViews(int flag) {
        Log.d("Aryant", "initialiseListViews");

        if(flag==1) {
            adminUnresolvedComplaintsAdapter = new AdminComplaintsAdapter(getApplicationContext(), unresolvedList);
            unresolvedComplaintList.setAdapter(adminUnresolvedComplaintsAdapter);
        }
        if(flag==2) {
            adminResolvedComplaintsAdapter = new AdminComplaintsAdapter(getApplicationContext(), resolvedList);
            resolvedComplaintList.setAdapter(adminResolvedComplaintsAdapter);
        }
    }



    /*@Override
    protected void onResume() {
        super.onResume();
        adminResolvedComplaintsAdapter.notifyDataSetChanged();
        adminUnresolvedComplaintsAdapter.notifyDataSetChanged();
    }*/
}
//Toast.makeText(this, resolvedList.get(unresolvedList.size()-1).getRevealAnonimity(), Toast.LENGTH_SHORT).show();;
//
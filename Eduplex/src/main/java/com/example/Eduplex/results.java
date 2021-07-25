package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class results extends AppCompatActivity {
    private Animation translateNegX;
    private TextView resultTitle;
    ListView resultList;
    ArrayList<ResultClass>studentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        initialise();
    }

    public void initialise()
    {
        translateNegX= AnimationUtils.loadAnimation(this,R.anim.translatenegx);
        resultTitle=findViewById(R.id.resultTitle);
        resultTitle.startAnimation(translateNegX);
        resultList=findViewById(R.id.resultList);
        studentResult=new ArrayList();
        StudentsManager studentsManager=(StudentsManager)getIntent().getSerializableExtra("StudentData");
        DatabaseReference RESULTNODE= FirebaseDatabase.getInstance().getReference().child("School").child("ResultRecord").child(studentsManager.get_registrationNumber());
       // for(int i=0;i<5;i++)
        //{
        if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
        {
            RESULTNODE.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        for (DataSnapshot resultSnap : snapshot.getChildren()) {
                            ResultClass rc = resultSnap.getValue(ResultClass.class);
                            //Log.d("Aryant",resultSnap.getKey());
                            studentResult.add(rc);
                        }
                        resultList.setAdapter(new ResultsAdapter(getApplicationContext(), studentResult));
                        //results.this.notify();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
        }
       //}
        /*studentResult.add(new ResultClass("Chemistry",95.0,95.0));
        studentResult.add(new ResultClass("Chemistry",95.0,95.0));
        studentResult.add(new ResultClass("Chemistry",55.0,55.0));
        studentResult.add(new ResultClass("Chemistry",35.756,35.0));
        studentResult.add(new ResultClass("Chemistry",15.0,15.0));*/

        //ResultsAdapter resultsAdapter=new ResultsAdapter(getApplicationContext(),studentResult);
        //resultList.setAdapter(resultsAdapter);
    }
}
/*
* addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {
                    if(snapshot.exists())
                    {
                        for(DataSnapshot resultSnap:snapshot.getChildren())
                        {
                            studentResult.add(resultSnap.getValue(ResultClass.class));
                        }
                    }
                }
                @Override
                public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {
                    resultList.setAdapter(new ResultsAdapter(getApplicationContext(),studentResult));
                }
                @Override
                public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
                @Override
                public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) { }
                @Override
                public void onCancelled(@NonNull  DatabaseError error) { }
            });*/
/*
* addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        for(DataSnapshot resultSnap:snapshot.getChildren())
                        {
                            ResultClass rc=resultSnap.getValue(ResultClass.class);
                            Log.d("hi", "onDataChange: ");
                            studentResult.add(rc);
                        }
                        resultList.setAdapter(new ResultsAdapter(getApplicationContext(),studentResult));
                    }
                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error) {

                }
            });*/
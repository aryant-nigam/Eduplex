package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class teacherOnLeave extends AppCompatActivity {

    private TextView teacherOnLeaveTitle;
    private Animation translateNegX;
    private ListView teacherOnLeaveList;
    private ArrayList<TeacherOnLeaveClass>teachersOnLeave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_on_leave);


        initialise();
    }

    private void initialise() {
        teacherOnLeaveTitle=findViewById(R.id.teacherOnLeaveTitle);
        translateNegX= AnimationUtils.loadAnimation(this,R.anim.translatenegx);
        teacherOnLeaveTitle.startAnimation(translateNegX);
        teacherOnLeaveList=findViewById(R.id.teacherOnLeaveList);
        teachersOnLeave=new ArrayList<>();
        if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
        {
            DatabaseReference TEACHERONLEAVE = FirebaseDatabase.getInstance().getReference().child("School").child("TeachersOnLeaveRecord");
            TEACHERONLEAVE.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        for (DataSnapshot TeacherOnLeaveSnap : snapshot.getChildren()) {
                            TeacherOnLeaveClass tol = TeacherOnLeaveSnap.getValue(TeacherOnLeaveClass.class);
                            ZoneId zid = ZoneId.of("Asia/Kolkata");
                            LocalDate today = LocalDate.now(zid);
                            if (today.compareTo(LocalDate.parse(tol.getLeaveDate())) == 0) {
                                teachersOnLeave.add(tol);
                            }

                            Log.d("Aryant", TeacherOnLeaveSnap.getKey());
                        }
                        teacherOnLeaveList.setAdapter(new TeacherOnLeaveAdapter(getApplicationContext(), teachersOnLeave));
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
        /*teachersOnLeave.add(new TeacherOnLeaveClass("T12356","Practice chapter 5 difficult word and write central idea for poem The Road Not Taken"));
        teachersOnLeave.add(new TeacherOnLeaveClass("T98675","Divya Verma","WAP to create a password tester on the basis o user input and use a global variable for stored password"));
        teachersOnLeave.add(new TeacherOnLeaveClass("T32455","PC Govil","Write short notes on 1) Diffraction. 2) poloroids. 3) interference. 4) Biot Savart Law.  5) Double rainbow formation."));
        teachersOnLeave.add(new TeacherOnLeaveClass("T88455","Ravikant Srivastava","Perform a practical to find the molarity and molality of given solution if initial weight of solvent(MgSO4) was 12.3gm and KOH solution is 1.2 W/W"));
*/

    }
}
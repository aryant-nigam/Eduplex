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

public class Announcements extends AppCompatActivity {
    private TextView announcementTitle;
    private ListView announcementList;
    private Animation translateNegX;
    private ArrayList<AnnouncementClass>announcements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        initialise();
    }

    private void initialise() {
        announcementTitle=findViewById(R.id.announcementTitle);
        announcementList=findViewById(R.id.announcementList);
        translateNegX= AnimationUtils.loadAnimation(this,R.anim.translatenegx);
        announcementTitle.startAnimation(translateNegX);
        announcements=new ArrayList();
        if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
        {
            DatabaseReference ANNOUNCEMENTNODE = FirebaseDatabase.getInstance().getReference().child("School").child("AnnouncementsRecord");
            ANNOUNCEMENTNODE.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        for (DataSnapshot announcementSnap : snapshot.getChildren()) {
                            AnnouncementClass ac = announcementSnap.getValue(AnnouncementClass.class);
                            announcements.add(ac);
                            //Log.d("Aryant", announcementSnap.getKey());
                        }
                        announcementList.setAdapter(new AnnouncementsAdapter(getApplicationContext(), announcements));
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
        /*announcements.add(new AnnouncementClass("chess competition","today is chess competition .You all have to register for it till evening." +
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"+
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"));
        announcements.add(new AnnouncementClass("chess competition","today is chess competition .You all have to register for it till evening." +
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"));
        announcements.add(new AnnouncementClass("chess competition","today is chess competition .You all have to register for it till evening." +
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"));
        announcements.add(new AnnouncementClass("chess competition","today is chess competition .You all have to register for it till evening." +
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"));
        AnnouncementsAdapter announcementsAdapter=new AnnouncementsAdapter(getApplicationContext(),announcements);
        announcementList.setAdapter(announcementsAdapter);*/
    }

}
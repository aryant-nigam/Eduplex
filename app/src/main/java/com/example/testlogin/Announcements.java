package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

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
        announcements.add(new AnnouncementClass("chess competition","today is chess competition .You all have to register for it till evening." +
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"+
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"));
        announcements.add(new AnnouncementClass("chess competition","today is chess competition .You all have to register for it till evening." +
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"));
        announcements.add(new AnnouncementClass("chess competition","today is chess competition .You all have to register for it till evening." +
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"));
        announcements.add(new AnnouncementClass("chess competition","today is chess competition .You all have to register for it till evening." +
                " There will be 3 rounds  and the winner would be getting trophee with certificate and chance to represent school in interschool chess competition"));
        AnnouncementsAdapter announcementsAdapter=new AnnouncementsAdapter(getApplicationContext(),announcements);
        announcementList.setAdapter(announcementsAdapter);
    }

}
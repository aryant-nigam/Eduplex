package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ParentTeacherMeeting extends AppCompatActivity {
    private TextView meetingDate,meetingTime,timeRemaining,meetingStatus;
    private Button enterMeeting;
    String meetingId;
    private StudentsManager studentsManager;
    LinearLayout meetingCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentt_teacher_meeting);
        initialise();

        enterMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL serverUrl;
                JitsiMeetUserInfo userInfo=new JitsiMeetUserInfo();
                userInfo.setDisplayName(studentsManager.get_name());
                userInfo.setEmail(studentsManager.get_email());
                try {
                    //https://meet.jit.si/myrandomroom#config.disableInviteFunctions=true
                    serverUrl=new URL("https://meet.jit.si/");
                    JitsiMeetConferenceOptions defaultOptions=
                            new JitsiMeetConferenceOptions.Builder()
                                    .setServerURL(serverUrl)
                                    .setWelcomePageEnabled(false)
                                    .setUserInfo(userInfo)
                                    .setFeatureFlag("chat.enabled",true)
                                    .setFeatureFlag("video-share.enabled",false)
                                    .setFeatureFlag("live-streaming.enabled",false)
                                    .setFeatureFlag("recording.enabled",false)
                                    .setFeatureFlag("meeting-password.enabled",false)
                                    .setFeatureFlag("invite.enabled",false)
                                    .setFeatureFlag("pip.enabled",false)
                                    .setFeatureFlag("close-captions.enabled",true)
                                    .setAudioMuted(true)
                                    .build();
                    JitsiMeet.setDefaultConferenceOptions(defaultOptions);
                }//.setFeatureFlag("invite.enabled",false)close-captions.enabled
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }

                JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder()
                        .setRoom(meetingId)
                        .setWelcomePageEnabled(false)
                        .build();
                JitsiMeetActivity.launch(ParentTeacherMeeting.this,options);
            }
        });

    }

    private void initialise() {
        enterMeeting = findViewById(R.id.enterMeeting);
        meetingDate=findViewById(R.id.meetingDate);
        meetingTime=findViewById(R.id.meetingTime);
        timeRemaining=findViewById(R.id.timeRemaining);
        meetingStatus=findViewById(R.id.meetingStatus);
        Animation translatex= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translatex);
        translatex.setStartTime(1500);
        meetingCard=findViewById(R.id.meetingCard);
        meetingCard.startAnimation(translatex);
        studentsManager =(StudentsManager)getIntent().getSerializableExtra("StudentData");

        DatabaseReference MEETING= FirebaseDatabase.getInstance().getReference().child("School").child("MeetingRecord").child("Meeting");
        MEETING.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot,String previousChildName) {
                if(snapshot.exists())
                {
                    for(DataSnapshot meets:snapshot.getChildren()) {
                        MeetingClass meeting = snapshot.getValue(MeetingClass.class);
                        Integer studentsClass=Integer.parseInt(studentsManager.get_class());
                        if(studentsClass>=Integer.parseInt(meeting.getFromClass())&&studentsClass<=Integer.parseInt(meeting.getToClass()))
                        {
                            if(meeting.getMeetingTime().length()==5)
                                meeting.setMeetingTime(meeting.getMeetingTime()+":00");
                            else
                                meeting.setMeetingTime(meeting.getMeetingTime().substring(0,8));

                            meetingId=meeting.getMeetingId();
                            ZoneId zid=ZoneId.of("Asia/Kolkata");
                            LocalDate todayDate=LocalDate.now(zid);
                            LocalTime todayTime=LocalTime.now(zid);

                            LocalDate meetDate=LocalDate.parse(meeting.getMeetingDate());
                            LocalTime meetTime=LocalTime.parse(meeting.getMeetingTime());

                            if((ChronoUnit.DAYS.between(todayDate,meetDate)>0))
                            {
                                Toast.makeText(ParentTeacherMeeting.this, "Hi", Toast.LENGTH_SHORT).show();
                                Toast.makeText(ParentTeacherMeeting.this, meeting.getMeetingDate(), Toast.LENGTH_SHORT).show();
                                meetingDate.setText(format(meeting.getMeetingDate(),1));
                                meetingTime.setText(format(meeting.getMeetingTime(),0));
                                timeRemaining.setText(String.valueOf(ChronoUnit.DAYS.between(todayDate,meetDate)+" Day (s)"));
                                break;
                            }
                            else if((ChronoUnit.DAYS.between(todayDate,meetDate)==0))
                            {
                                if(ChronoUnit.MINUTES.between(todayTime,meetTime)>=0)
                                {
                                    meetingStatus.setText("Starts In : ");
                                    meetingDate.setText(format(meeting.getMeetingDate(),1));
                                    meetingTime.setText(format(meeting.getMeetingTime(),0));
                                    timeRemaining.setText(String.valueOf(ChronoUnit.MINUTES.between(todayTime,meetTime))+" Minute (s)");
                                    Toast.makeText(ParentTeacherMeeting.this, String.valueOf(ChronoUnit.MINUTES.between(todayTime,meetTime)), Toast.LENGTH_SHORT).show();
                                    openEntries(ChronoUnit.MINUTES.between(todayTime,meetTime),Long.parseLong(meeting.getMeetingDuration()));

                                }
                                else if(ChronoUnit.MINUTES.between(todayTime,meetTime)<0)
                                {
                                    Toast.makeText(ParentTeacherMeeting.this, "meet started", Toast.LENGTH_SHORT).show();
                                    meetingStatus.setText("Ends In");
                                    meetingStatus.setTextColor(Color.parseColor("#d60404"));
                                    enterMeeting.setVisibility(View.VISIBLE);
                                    if(ChronoUnit.MINUTES.between(todayTime,meetTime)>-60*(Long.parseLong(meeting.getMeetingDuration())))
                                    {
                                        meetingDate.setText(format(meeting.getMeetingDate(),1));
                                        meetingTime.setText(format(meeting.getMeetingTime(),0));

                                        Long duration=Long.parseLong(meeting.getMeetingDuration());
                                        //Toast.makeText(ParentTeacherMeeting.this, String.valueOf(ChronoUnit.MINUTES.between(todayTime,meetTime.plusHours(duration.longValue()))), Toast.LENGTH_SHORT).show();
                                        timeRemaining.setText(String.valueOf(ChronoUnit.MINUTES.between(todayTime,meetTime.plusHours(duration.longValue()))));
                                        startTimer(ChronoUnit.MINUTES.between(todayTime,meetTime.plusHours(duration.longValue())));
                                        Toast.makeText(ParentTeacherMeeting.this,String.valueOf(ChronoUnit.MINUTES.between(todayTime,meetTime.plusHours(duration.longValue()))) , Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        meetingDate.setText(format(meeting.getMeetingDate(),1));
                                        meetingTime.setText(format(meeting.getMeetingTime(),0));
                                        meetingStatus.setTextColor(Color.parseColor("#f0abc1"));
                                        timeRemaining.setText("Meeting Ended");
                                        enterMeeting.setVisibility(View.GONE);
                                    }
                                }
                            }
                            break;
                        }
                        /*else
                        {
                            meetingDate.setText(defaultMeeting.getMeetingDate());
                            meetingTime.setText(defaultMeeting.getMeetingTime());
                            //timeRemaining.setText(defaultMeeting.getMeetingDate().toString());
                            enterMeeting.setVisibility(View.GONE);
                        }*/
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot,String previousChildName) { }
            @Override
            public void onChildRemoved(DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(DataSnapshot snapshot,String previousChildName) { }
            @Override
            public void onCancelled(DatabaseError error) { }
        });

    }

   private void openEntries(Long timeLeft,Long meetingDuration)
   {
       //Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
       meetingStatus.setText("Starts In : ");
       final Long[]duration ={timeLeft};
       Long lastFor=60000*duration[0];
       new CountDownTimer(lastFor,60000) {
           @Override
           public void onTick(long millisUntilFinished) {
               timeRemaining.setText(String.valueOf(duration[0]--)+" Minute (s)");
           }

           @Override
           public void onFinish() {
                enterMeeting.setVisibility(View.VISIBLE);
                startTimer(meetingDuration*60);
           }
       }.start();
   }
    private void startTimer(Long meetingDuration) {
        //Toast.makeText(this, "Hi i am timer", Toast.LENGTH_SHORT).show();
        meetingStatus.setText("Ends In: ");
        timeRemaining.setTextColor(Color.parseColor("#d60404"));

        final Long[] duration = {meetingDuration};
        //Toast.makeText(this, String.valueOf(duration[0]), Toast.LENGTH_SHORT).show();
        Long lastFor=60000*duration[0];
        enterMeeting.setVisibility(View.VISIBLE);
        new CountDownTimer(lastFor,60000){

           @Override
           public void onTick(long millisUntilFinished) {
                timeRemaining.setText(String.valueOf(duration[0]--)+" Minute (s)");

           }

           @Override
           public void onFinish() {

               meetingStatus.setTextColor(Color.parseColor("#f0abc1"));
               timeRemaining.setText("Meeting Ended");
               enterMeeting.setVisibility(View.GONE);
           }
       }.start();
    }

    private  String format(String value,int flag)
    {
        if(flag==0)
        {
            String AM_PM;
            AM_PM=Integer.parseInt(value.substring(0,2))<12?" AM":" PM";
            return value.substring(0,5)+AM_PM;
        }
        else
        {
            String arr[]=value.split("-");
            return  arr[2]+"-"+arr[1]+"-"+arr[0];

        }
    }
}
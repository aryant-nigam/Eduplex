package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class AdminParentTeacherMeeting extends AppCompatActivity {
    private TimePicker selectTimeMeeting;
    private CalendarView selectDateMeeting;
    private Button createMeeting;
    private EditText meetingId,confirmMeetingId,meetingDuration;
    private LocalDate selectedDate=null;
    private LocalTime selectedTime=null;
    private Spinner minClass,maxClass;
    private ListView meetingsList;
    private ArrayList<MeetingClass>meetings;
    private ArrayList<String> fromClass;
    private ArrayList<String> toClass;
    private ArrayAdapter<String> arrayAdapterToClass,arrayAdapterFromClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_parent_teacher_meeting);
        initialise();
        selectTimeMeeting.setIs24HourView(true);

        minClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                toClass.clear();
                for(int i=position;i<fromClass.size();i++)
                    toClass.add(fromClass.get(i));
                arrayAdapterToClass=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,toClass);
                maxClass.setAdapter(arrayAdapterToClass);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectDateMeeting.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate=LocalDate.of(year,month+1,dayOfMonth);
                Toast.makeText(AdminParentTeacherMeeting.this, selectedDate.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        selectTimeMeeting.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfDay) {
                selectedTime=LocalTime.of(hourOfDay,minuteOfDay,00);
                Toast.makeText(AdminParentTeacherMeeting.this, selectedTime.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    MeetingClass meetingClass=new MeetingClass(
                            meetingId.getText().toString().trim(),
                            selectedDate.toString(),
                            selectedTime.toString(),
                            minClass.getSelectedItem().toString(),
                            maxClass.getSelectedItem().toString(),
                            meetingDuration.getText().toString()
                    );
                    Log.d("Ary", selectedDate.toString());
                    Log.d("Ary", selectedTime.toString());

                    AlertDialog.Builder builder=new AlertDialog.Builder(AdminParentTeacherMeeting.this);
                    String AM_PM=LocalTime.parse(meetingClass.getMeetingTime()).getHour()>=12?"PM":"AM";
                    builder.setTitle("MEETING CONFIRMATION");
                    builder.setMessage(
                            "Date : "+meetingClass.getMeetingDate()+"\n"+
                            "Time : "+meetingClass.getMeetingTime()+" "+AM_PM+"\n"+
                            "Meeting Id : "+meetingClass.getMeetingId()
                    );
                    builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())){
                                DatabaseReference MEETING = FirebaseDatabase.getInstance().getReference().child("School").child("MeetingRecord").child("Meeting");
                                MEETING.push().setValue(meetingClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AdminParentTeacherMeeting.this, "Parent Teacher Meeting Scheduled Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(AdminParentTeacherMeeting.this, "Failed To Schedule Parent Teacher Meeting ", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(AdminParentTeacherMeeting.this, "Network Unavailable !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setCancelable(false);

                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
            }
        });

        meetingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                URL serverUrl;
                JitsiMeetUserInfo userInfo=new JitsiMeetUserInfo();
                userInfo.setDisplayName("Admin");
                userInfo.setEmail("admin@gmail.com");
                try {
                    //https://meet.jit.si/myrandomroom#config.disableInviteFunctions=true
                    serverUrl=new URL("https://meet.jit.si/");
                    JitsiMeetConferenceOptions defaultOptions=
                            new JitsiMeetConferenceOptions.Builder()
                                    .setServerURL(serverUrl)
                                    .setWelcomePageEnabled(false)
                                    .setUserInfo(userInfo)
                                    .setFeatureFlag("chat.enabled",true)
                                    .setFeatureFlag("ios.recording.enabled",true)
                                    .setFeatureFlag("ios.screensharing.enabled",true)
                                    .setFeatureFlag("meeting-password.enabled",false)
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
                        .setRoom(meetings.get(position).getMeetingId())
                        .setWelcomePageEnabled(false)
                        .build();
                JitsiMeetActivity.launch(AdminParentTeacherMeeting.this,options);
            }
        });
    }

    private boolean validate() {
        ZoneId zid=ZoneId.of("Asia/Kolkata");
        LocalDate today=LocalDate.now(zid);
        LocalDate MeetingDate;
        LocalTime MeetingTime;

        if(selectedDate==null) {
            MeetingDate=LocalDate.now(zid);
            selectedDate=MeetingDate;
        }
        else {
            MeetingDate=selectedDate;
        }
        if(selectedTime==null) {
            MeetingTime=LocalTime.now(zid);
            selectedTime=MeetingTime;
        }
        else {
            MeetingTime=selectedTime;
        }
        Toast.makeText(this, String.valueOf(ChronoUnit.DAYS.between(today,MeetingDate)), Toast.LENGTH_SHORT).show();
        if(!(ChronoUnit.DAYS.between(today,MeetingDate)>2)&&(ChronoUnit.DAYS.between(today,MeetingDate)<=7))
        {
            Toast.makeText(this, "Parent Teachers Meeting can be scheduled after 1 day or before 7 days only.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(MeetingTime.getHour()>19||MeetingTime.getHour()<7){
            Toast.makeText(this, "Parent Teachers Meeting can't be hosted in Odd hours", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(meetingId.getText().toString().trim().equals("")){
            Toast.makeText(this, "Meeting Id Required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!meetingId.getText().toString().equals(confirmMeetingId.getText().toString()))
        {
            Toast.makeText(this, "Kindly Renter Meeting Id : Match Failed", Toast.LENGTH_SHORT).show();
            confirmMeetingId.setText("");
            return false;
        }
        if(minClass.getSelectedItem().toString().equals("Select Class")||minClass.getSelectedItem().toString().equals("Select Class")){
            Toast.makeText(this, "Kindly select the classes ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(meetingDuration.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Kindly Fix The Meeting Duration ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void initialise() {
        selectDateMeeting=findViewById(R.id.selectDateMeeting);
        selectTimeMeeting=findViewById(R.id.selectTimeMeeting);
        createMeeting=findViewById(R.id.createMeeting);
        meetingId=findViewById(R.id.meetingId);
        confirmMeetingId=findViewById(R.id.confirmMeetingId);
        minClass=findViewById(R.id.minClass);
        maxClass=findViewById(R.id.maxClass);
        meetingsList=findViewById(R.id.meetingsList);
        meetingDuration=findViewById(R.id.meetingDuration);

        meetings=new ArrayList<>();

        DatabaseReference MEETING= FirebaseDatabase.getInstance().getReference().child("School").child("MeetingRecord");
        MEETING.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                if(snapshot.exists())
                {
                    for(DataSnapshot meets:snapshot.getChildren()) {

                        MeetingClass meeting = meets.getValue(MeetingClass.class);
                        //Toast.makeText(AdminParentTeacherMeeting.this, meeting.getMeetingId(), Toast.LENGTH_SHORT).show();

                        String date=meeting.getMeetingDate();
                        String time=meeting.getMeetingTime();

                        ZoneId zid=ZoneId.of("Asia/Kolkata");
                        LocalDate todayDate=LocalDate.now(zid);
                        LocalTime todayTime=LocalTime.now(zid);

                        LocalDate meetDate=LocalDate.parse(date);

                        if(time.length()==5)
                            time=time+":00";
                        else
                            time=time.substring(0,8);
                        LocalTime meetTime=LocalTime.parse(time);

                        if((ChronoUnit.DAYS.between(todayDate,meetDate)>0))
                        {
                            meetings.add(meeting);
                        }
                        else if((ChronoUnit.DAYS.between(todayDate,meetDate)==0))
                        {
                           if(ChronoUnit.MINUTES.between(todayTime,meetTime)>=0)
                            {
                                meetings.add(meeting);
                            }
                            else if(ChronoUnit.MINUTES.between(todayTime,meetTime)<0)
                            {
                                if(ChronoUnit.MINUTES.between(todayTime,meetTime)>-60*(Long.parseLong(meeting.getMeetingDuration())))
                                {
                                   meetings.add(meeting);
                                }
                            }
                        }

                    }
                    MeetingAdapter meetingAdapter=new MeetingAdapter(getApplicationContext(),meetings);
                    meetingsList.setAdapter(meetingAdapter);
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


        fromClass =new ArrayList<String>();
        fromClass.add("1");
        fromClass.add("2");
        fromClass.add("3");
        fromClass.add("4");
        fromClass.add("5");
        fromClass.add("6");
        fromClass.add("7");
        fromClass.add("8");
        fromClass.add("9");
        fromClass.add("10");
        fromClass.add("11");
        fromClass.add("12");

        arrayAdapterFromClass=new ArrayAdapter<String >(this,android.R.layout.simple_spinner_item, fromClass);
        minClass.setAdapter(arrayAdapterFromClass);

        toClass=new ArrayList<>();


    }
}
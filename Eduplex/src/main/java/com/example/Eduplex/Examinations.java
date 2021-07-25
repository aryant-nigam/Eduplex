package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Examinations extends AppCompatActivity {

    private TextView examinationTitle;
    private Animation translateNegX;
    private ListView examList;
    private ArrayList<ExamClass>studentExams;
    private ExaminationsAdapter examinationsAdapter;
    private Button setRemainder,clearRemainder;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinations);

        initialise();
        //createNotificationChannel();


        setRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int setAlarmFor=0;
                String date,time;
                while(setAlarmFor<examinationsAdapter.getCount())
                {
                    ExamClass exam=examinationsAdapter.getItem(setAlarmFor);

                    date=exam.getDate();
                    time=(String)exam.getTime();
                    ZoneId zoneId=ZoneId.of("Asia/Kolkata");

                    LocalDate startDate = LocalDate.parse(date);
                    LocalDate endDate = LocalDate.now(zoneId);
                    long intervalDays=ChronoUnit.DAYS.between(endDate,startDate);
                    intervalDays-=1;
                    Log.d("no of days left ", String.valueOf(intervalDays));
                    LocalTime startTime = LocalTime.parse(time);//"10:15:45"
                    LocalTime endTime = LocalTime.now(zoneId);
                    Log.d("time now and alarm", String.valueOf(startTime)+" "+endTime);
                    long intervalSeconds=ChronoUnit.SECONDS.between(endTime,startTime);

                    long totalSecondsBeforeRemainder=(intervalDays*86400)+intervalSeconds;
                    Log.d("total miliseconds", String.valueOf(totalSecondsBeforeRemainder)+String.valueOf(setAlarmFor));
                    setAlarm(totalSecondsBeforeRemainder,setAlarmFor);
                    setAlarmFor++;
                }
                Toast.makeText(Examinations.this, "REMAINDER FOR EXAMS HAS BEEN SET", Toast.LENGTH_LONG).show();
            }
        });

        clearRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cancelAlarmFor=0;
                while(cancelAlarmFor<examinationsAdapter.getCount())
                {
                    cancelAlarm(cancelAlarmFor);
                    cancelAlarmFor++;
                }
                Toast.makeText(Examinations.this, "REMAINDER FOR EXAMS HAS BEEN CANCELLED", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initialise(){
        translateNegX= AnimationUtils.loadAnimation(this,R.anim.translatenegx);
        examinationTitle=findViewById(R.id.examinationTitle);
        examinationTitle.startAnimation(translateNegX);
        examList=findViewById(R.id.examList);
        setRemainder=findViewById(R.id.setRemainder);
        clearRemainder=findViewById(R.id.clearRemainder);
        studentExams=new ArrayList();
        StudentsManager s= (StudentsManager) getIntent().getSerializableExtra("StudentData");
        ArrayList<ExamClass>exams=new ArrayList();
        if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
        {
            DatabaseReference EXAMNODE = FirebaseDatabase.getInstance().getReference().child("School").child("ExaminationRecord").child(s.get_class());
            EXAMNODE.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ExamSnap : snapshot.getChildren()) {
                            ExamClass ec = ExamSnap.getValue(ExamClass.class);
                            exams.add(ec);
                            //Log.d("Aryant", announcementSnap.getKey());
                        }
                        examList.setAdapter(new ExaminationsAdapter(getApplicationContext(), exams));
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
        /*studentExams.add(new ExamClass("Chemistry","2021-07-08","18:53:00","2 hours"));
        studentExams.add(new ExamClass("English","2021-07-09","20:56:00","2 hours"));
        studentExams.add(new ExamClass("History","2021-07-07","19:00:00","2 hours"));
        studentExams.add(new ExamClass("Computer","2021-07-23","10:00:00","2 hours"));
        studentExams.add(new ExamClass("Physics","2021-07-25","10:00:00","2 hours"));

        examinationsAdapter=new ExaminationsAdapter(getApplicationContext(),studentExams);
        examList.setAdapter(examinationsAdapter);*/

    }

    private  void  createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="ExaminationRemainderChannel";
            String description="Channel for exam remainder";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("ExaminationAlert",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarm(long totalSecondsBeforeRemainder,int setAlarmFor)
    {
        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,AlarmReciever.class);

        pendingIntent=PendingIntent.getBroadcast(this,setAlarmFor,intent,0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,totalSecondsBeforeRemainder*1000,pendingIntent);

    }

    private void cancelAlarm(int cancelAlarmFor) {
        Intent intent = new Intent(this, AlarmReciever.class);
        pendingIntent = PendingIntent.getBroadcast(this, cancelAlarmFor, intent, 0);
        if (alarmManager == null)
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
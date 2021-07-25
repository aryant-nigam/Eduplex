package com.example.Eduplex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {

    ListView listView;
    ArrayList<String>panels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        listView = findViewById(R.id.panelList);
        panels=new ArrayList<>();
        panels.add("Announcements Panel");
        panels.add("Attendence Panel");
        panels.add("Results Panel");
        panels.add("Students Panel");
        panels.add("Teachers Panel");
        panels.add("Teacher's Leave Panel");
        panels.add("Timetable Panel");
        panels.add("Examinations Panel");
        adminAdapter adminAdapter=new adminAdapter(panels,getApplicationContext());
        listView.setAdapter(adminAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent=new Intent(getApplicationContext(),AdminAnnouncement.class);
                    startActivity(intent);
                }
                else if (position == 1) {
                    Intent intent=new Intent(getApplicationContext(),AdminAttendence.class);
                    startActivity(intent);
                }
                else if (position == 2) {
                    Intent intent=new Intent(getApplicationContext(),AdminResult.class);
                    startActivity(intent);
                }
                else if (position == 3) {
                    Intent intent=new Intent(getApplicationContext(),AdminStudent.class);
                    startActivity(intent);
                }
                else if (position == 4) {
                    Intent intent=new Intent(getApplicationContext(),AdminTeacher.class);
                    startActivity(intent);
                }
                else if (position == 5) {
                    Intent intent=new Intent(getApplicationContext(),CalenderActivity.class);
                    startActivity(intent);
                }
                else if (position == 6) {
                    Intent intent=new Intent(getApplicationContext(),AdminTimetable.class);
                    startActivity(intent);
                }
                else if (position == 7) {
                    Intent intent=new Intent(getApplicationContext(),AdminExaminations.class);
                    startActivity(intent);
                }
            }
        });

    }
}
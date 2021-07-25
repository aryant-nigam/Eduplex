package com.example.Eduplex;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Timetable extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<TimetableClass> timetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_main);
        intialise();
    }

    private void intialise() {
        StudentsManager studentsManager=(StudentsManager)getIntent().getSerializableExtra("StudentData");
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),studentsManager.get_class(),studentsManager.get_section()));
        tabLayout.setupWithViewPager(viewPager);
    }
}
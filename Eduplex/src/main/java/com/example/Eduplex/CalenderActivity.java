package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;

public class CalenderActivity extends AppCompatActivity {
    private CalendarView calendarView;
    Button select,skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        //prevents screenshots
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        calendarView=findViewById(R.id.calendar);

        select=findViewById(R.id.select);
        skip=findViewById(R.id.skip);

        final String[] selectedDate = {null};

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if(month+1<10)
                    selectedDate[0] =""+year+"-0"+(month+1)+"-"+dayOfMonth;
                if(dayOfMonth<10)
                    selectedDate[0] =""+year+"-"+(month+1)+"-0"+dayOfMonth;
                if(dayOfMonth<10&&month+1<10)
                    selectedDate[0] =""+year+"-0"+(month+1)+"-0"+dayOfMonth;
                if(!(dayOfMonth<10)&&!(month+1<10))
                    selectedDate[0] =""+year+"-"+(month+1)+"-"+dayOfMonth;
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoneId zid=ZoneId.of("Asia/Kolkata");
                LocalDate today =LocalDate.now(zid);
                validate(today, selectedDate[0]);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnDateIntent = new Intent(getApplicationContext(), AdminTeacherLeave.class);
                returnDateIntent.putExtra("LeaveDate", "");
                startActivity(returnDateIntent);
            }
        });

    }

    private void validate( LocalDate todayParam,String selectedDateParam) {

        LocalDate leaveDate;
        if(selectedDateParam==null) {
            leaveDate = todayParam;
            selectedDateParam=leaveDate.toString();
        }
        else
            leaveDate=LocalDate.parse(selectedDateParam);

        int flag = 0;
        if(leaveDate.compareTo(todayParam)!=0)
        {
            // checks if leave date is not passed
            if (leaveDate.compareTo(todayParam) > 0)
                flag += 1;
            else
                Toast.makeText(CalenderActivity.this, "Kindly select the valid date", Toast.LENGTH_SHORT).show();

            //Checks if leave is for ater month or not
            if (leaveDate.compareTo(todayParam) == 1) {
                if (leaveDate.getMonthValue() - todayParam.getMonthValue() != 0) {
                    Log.d("Aryant", String.valueOf(leaveDate.compareTo(todayParam)));
                    Toast.makeText(CalenderActivity.this, "Leaves Punching for current is month is permitted", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Kindly try next Month", Toast.LENGTH_SHORT).show();
                }
                else
                    flag += 1;
            }
            else
                flag += 1;
        }
        else
        {
            flag+=2;
        }


        //if all validations are satisfied return date
        if(flag==2) {
            Intent returnDateIntent = new Intent(getApplicationContext(), AdminTeacherLeave.class);
            returnDateIntent.putExtra("LeaveDate", selectedDateParam);
            startActivity(returnDateIntent);
        }

    }
}

//checks if leave is being punched for the same year and if not is it 31 december
        /*if(leaveDate.getYear()!=todayParam.getYear())
        {
            if(todayParam.getMonthValue()!=12&&todayParam.getDayOfMonth()!=31)
            {
                Toast.makeText(CalenderActivity.this, "Leaves Punching for current year is permitted", Toast.LENGTH_SHORT).show();
            }
            else
                flag+=1;
        }
        else{
            flag+=1;
        }*/

//checks if leave is being punched for the same month and if not is it last dat of that month
        /*if(leaveDate.getMonthValue()!=todayParam.getMonthValue())
        {
            if(todayParam.getMonthValue()!=2) {
                if (todayParam.getDayOfMonth() == 30 || todayParam.getDayOfMonth() == 31) {
                    flag+=1;
                }
            }
            else
            if(todayParam.getDayOfMonth()==29||todayParam.getDayOfMonth()==28){
                flag+=1;
            }
            else
                Toast.makeText(CalenderActivity.this, "Leaves Punching for current month is permitted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            flag+=1;
        }*/
package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

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

        studentResult.add(new ResultClass("Chemistry",95.0,95.0));
        studentResult.add(new ResultClass("Chemistry",95.0,95.0));
        studentResult.add(new ResultClass("Chemistry",55.0,55.0));
        studentResult.add(new ResultClass("Chemistry",35.756,35.0));
        studentResult.add(new ResultClass("Chemistry",15.0,15.0));

        ResultsAdapter resultsAdapter=new ResultsAdapter(getApplicationContext(),studentResult);
        resultList.setAdapter(resultsAdapter);
    }
}

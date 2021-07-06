package com.example.testlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ResultsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ResultClass>results;

    public ResultsAdapter(Context context, ArrayList<ResultClass> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.activity_results_adapter,parent,false);

        ImageView image=view.findViewById(R.id.resultLogo);
        TextView subjectValue=view.findViewById(R.id.subjectValue);
        TextView scoreValue=view.findViewById(R.id.scoreValue);
        ProgressBar percentageValue=view.findViewById(R.id.percentageValue);
        TextView percentageIndicator=view.findViewById(R.id.percentageIndicator);

        ResultClass tempResult=results.get(position);

        image.setImageDrawable(context.getResources().getDrawable(R.drawable.result_icon));
        subjectValue.setText(tempResult.getSubjectVal());
        scoreValue.setText(((Double)tempResult.getScoreVal()).toString());
        percentageValue.setProgress((int) tempResult.getPercentageVal());

        DecimalFormat df = new DecimalFormat("##.#");
        Double subjectpercentage=(tempResult.getScoreVal()/100)*100;
        percentageIndicator.setText(df.format(subjectpercentage)+" % ");

        return view;
    }
}
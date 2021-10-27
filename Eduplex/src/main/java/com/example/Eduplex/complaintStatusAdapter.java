package com.example.Eduplex;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class complaintStatusAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ComplaintClass>complaintList;

    public complaintStatusAdapter(Context context, ArrayList<ComplaintClass> complaintList) {
        this.context = context;
        this.complaintList = complaintList;
    }

    @Override
    public int getCount() {
        return complaintList.size();
    }

    @Override
    public Object getItem(int position) {
        return complaintList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater complaintsInflator=LayoutInflater.from(context);
        convertView=complaintsInflator.inflate(R.layout.activity_complaint_status_adapter,parent,false);

        TextView complaint_Type=convertView.findViewById(R.id.complaint_Type);
        TextView complaint_Description=convertView.findViewById(R.id.complaint_Description);
        TextView complaint_Status=convertView.findViewById(R.id.complaint_Status);
        ImageView complaint_icon=convertView.findViewById(R.id.complaint_iconS);

        ComplaintClass tempComplaint=complaintList.get(position);

        complaint_Type.setText(tempComplaint.getComplaintType());
        complaint_Description.setText(tempComplaint.getComplaintDescription());

        if(tempComplaint.getStatus().equals("Lodged"))
            complaint_Status.setTextColor(Color.parseColor("#f07aa0"));
        else if(tempComplaint.getStatus().equals("Seen"))
            complaint_Status.setTextColor(Color.parseColor("#f2a191"));
        else if(tempComplaint.getStatus().equals("Responded"))
            complaint_Status.setTextColor(Color.parseColor("#94ddde"));

        complaint_Status.setText(tempComplaint.getStatus());

        complaint_icon.setImageResource(R.drawable.lodge_complaint_icon);

        return convertView;
    }
}
package com.example.Eduplex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminComplaintsAdapter extends BaseAdapter {
    Context context;
    private ArrayList<ComplaintClass>complaints;

    public AdminComplaintsAdapter(Context context, ArrayList<ComplaintClass> complaints) {
        this.context = context;
        this.complaints = complaints;
    }

    @Override
    public int getCount() {
        return complaints.size();
    }

    @Override
    public Object getItem(int position) {
        return complaints.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        convertView=layoutInflater.inflate(R.layout.activity_admin_complaints_adapter,parent,false);

        TextView complaint_Type_admin=convertView.findViewById(R.id.complaint_Type_admin);
        TextView complaint_Description_admin=convertView.findViewById(R.id.complaint_Description_admin);
        TextView complaint_By_admin=convertView.findViewById(R.id.complaint_By_admin);
        ImageView icon=convertView.findViewById(R.id.complaint_iconA);

        ComplaintClass tempComplaint=complaints.get(position);

        complaint_Type_admin.setText(tempComplaint.getComplaintType());
        complaint_Description_admin.setText(tempComplaint.getComplaintDescription());
        complaint_By_admin.setText(tempComplaint.getRegistrationNoOfStudent());
        icon.setImageResource(R.drawable.lodge_complaint_icon);

        return convertView;
    }
}
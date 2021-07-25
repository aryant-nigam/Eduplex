package com.example.Eduplex;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
public class adminAdapter extends BaseAdapter {
    ArrayList<String> panel;
    Context context;

    public adminAdapter(ArrayList<String>panel,Context context){
        this.panel=panel;
        this.context=context;
    }

    @Override
    public int getCount() {return panel.size();}

    @Override
    public Object getItem(int position) {return panel.get(position);}

    @Override
    public long getItemId(int position) { return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater PanelInflater = LayoutInflater.from(context);
        convertView=PanelInflater.inflate(R.layout.admin_adapter,parent,false);


        TextView panelName=convertView.findViewById(R.id.panelName);
        ImageView panelIcon=convertView.findViewById(R.id.panelIcon);


        panelName.setText(panel.get(position));
        panelIcon.setImageResource(R.drawable.panel_logo);

        return convertView;
    }
}

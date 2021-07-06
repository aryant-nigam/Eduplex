package com.example.testlogin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AnnouncementsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AnnouncementClass>announcements;

    public AnnouncementsAdapter(Context context, ArrayList<AnnouncementClass> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    @Override
    public int getCount() {
        return announcements.size();
    }

    @Override
    public Object getItem(int position) {
        return announcements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        convertView=layoutInflater.inflate(R.layout.activity_announcements_adapter,parent,false);

        TextView announcementSubject=convertView.findViewById(R.id.announcementSubject);
        TextView announcementbody=convertView.findViewById(R.id.announcementBody);
        ImageView announcementIcon=convertView.findViewById(R.id.announcementIcon);

        AnnouncementClass tempAnnouncement=announcements.get(position);

        announcementSubject.setText(tempAnnouncement.getAnnouncementSubject().toUpperCase());
        announcementbody.setText(tempAnnouncement.getAnnouncementBody());
        announcementIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.announcement_icon));

        return convertView;
    }
}
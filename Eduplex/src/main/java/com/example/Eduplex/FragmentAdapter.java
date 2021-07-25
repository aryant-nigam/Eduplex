package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {
    String _class,_section;
    public FragmentAdapter(@NonNull FragmentManager fm,String _class,String _section) {
        super(fm);
        this._class=_class;
        this._section=_section;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new Monday(_class,_section);
            case 1: return new Tuesday(_class,_section);
            case 2: return new Wednesday(_class,_section);
            case 3: return new Thursday(_class,_section);
            case 4: return new Friday(_class,_section);
            case 5: return new Saturday(_class,_section);

            default:return new Monday(_class,_section);

        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       String title = null;
       if(position==0){
           title="Monday";
       }
        else if(position==1){
            title="Tuesday";
        }
       else if(position==2){
           title="Wednesday";
       }
       else if(position==3){
           title="Thursday";
       }
       else if(position==4){
           title="Friday";
       }
       else if(position==5){
           title="Saturday";
       }
       return title;
    }
}

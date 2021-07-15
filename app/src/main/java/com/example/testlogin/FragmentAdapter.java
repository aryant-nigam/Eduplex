package com.example.testlogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new Monday();
            case 1: return new Tuesday();
            case 2: return new Wednesday();
            case 3: return new Thursday();
            case 4: return new Friday();
            case 5: return new Saturday();

            default:return new Monday();

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

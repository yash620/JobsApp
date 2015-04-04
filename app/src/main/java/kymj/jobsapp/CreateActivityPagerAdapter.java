package kymj.jobsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Yashwanth on 4/4/2015.
 */
public class CreateActivityPagerAdapter extends FragmentPagerAdapter{

    public CreateActivityPagerAdapter(FragmentManager fm){
        super(fm);

    }
    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                //create job fragment here
                return new CreateJob();
            case 1:
                //myJobs fragment here
                return new MyJobsCreate();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

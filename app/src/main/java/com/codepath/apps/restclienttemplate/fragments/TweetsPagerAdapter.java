package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by danahe97 on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabtitles[] = new String[] {"Home", "Mentions"};
    private Context context;
    HomeTimelineFragment homeFrag = new HomeTimelineFragment();
    MentionsTimelineFragment mentionsFrag = new MentionsTimelineFragment();

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }
    // return the total number of fragments

    @Override
    public int getCount() {
        return 2;
    }

    // return the fragment to use depending on the position

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return homeFrag;
        }
        else if (position == 1) {
            return mentionsFrag;
        }
        else {
            return null;
        }
    }

    // return title

    @Override
    public CharSequence getPageTitle(int position) {
        // generate title based on item position
        return tabtitles[position];
    }
}

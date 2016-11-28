package com.mdxx.qmmz.newfeature.adapter;

import android.support.v4.app.Fragment;

import com.mdxx.qmmz.newfeature.view.FragmentNavigatorAdapter;


/**
 * Created by aspsine on 16/3/31.
 */
public class TabFragmentAdapter implements FragmentNavigatorAdapter {
    public enum fragment {
        jilu, wangluo, dongtai,wode;
    }
    @Override
    public Fragment onCreateFragment(int position) {
        if (position == fragment.jilu.ordinal()) {
            return null;
        } else if (position == fragment.wangluo.ordinal()) {
            return null;
        } else if (position == fragment.dongtai.ordinal()) {
           return null;
        } else if(position == fragment.wode.ordinal()){
            return null;
        }
        return null;
    }

    @Override
    public String getTag(int position) {
        return fragment.values()[position].toString();
    }

    @Override
    public int getCount() {
        return fragment.values().length;
    }
}

package com.example.tt.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tt.fragment.NewsSectionFragment;
import com.example.tt.pojo.NewsType;

import java.util.List;

/**
 * Created by TT on 2016/9/18.
 */
public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<NewsType> newsTypeList;
    public NewsFragmentPagerAdapter(FragmentManager fm, List<NewsType> newsTypeList) {
        super(fm);
        this.newsTypeList = newsTypeList;
    }
    public void setNewsTypeList(List<NewsType> newsTypeList) {
        this.newsTypeList = newsTypeList;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment f = new NewsSectionFragment();
        Bundle b = new Bundle();
        b.putInt("typeId",position);
        f.setArguments(b);
        return f;
    }
    @Override
    public int getCount() {
        return newsTypeList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return newsTypeList.get(position).getName();
    }

}

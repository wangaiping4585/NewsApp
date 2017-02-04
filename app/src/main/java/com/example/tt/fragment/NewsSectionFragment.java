package com.example.tt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tt.adapter.NewsAdapter;
import com.example.tt.dao.NewsInfoDao;
import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.newsapp.NewsDetailActivity;
import com.example.tt.newsapp.R;
import com.example.tt.pojo.NewsInfo;
import com.example.tt.util.Utils;

import java.util.List;

/**
 * Created by TT on 2016/9/18.
 */
public class NewsSectionFragment extends Fragment {
    private ListView newsListView;
    private NewsInfoDao newsInfoDao;
    private DatabaseHelp dbHelper;
    private int typeId;
    private NewsAdapter adapter;
    private List<NewsInfo> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_news_section,null);
        newsListView = (ListView) v.findViewById(R.id.newListView);
        dbHelper = new DatabaseHelp(getContext());
        newsInfoDao = new NewsInfoDao(dbHelper);
        typeId = getArguments().getInt("typeId");
        if(typeId == 0){
            list = newsInfoDao.findAll();
        }else{
            list = newsInfoDao.findByNewsType(typeId);
        }
        dbHelper.close();
        adapter = new NewsAdapter(list,getContext());
        newsListView.setAdapter(adapter);
        //单击进入新闻详细页
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getContext(), NewsDetailActivity.class);
                it.putExtra("news",list.get(i));
                startActivity(it);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        dbHelper = new DatabaseHelp(getContext());
        newsInfoDao = new NewsInfoDao(dbHelper);
        if(typeId == 0){
            list = newsInfoDao.findAll();
        }else{
            list = newsInfoDao.findByNewsType(typeId);
        }
        dbHelper.close();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }
}

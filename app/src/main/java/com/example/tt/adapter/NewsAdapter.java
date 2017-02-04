package com.example.tt.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tt.newsapp.MainActivity;
import com.example.tt.newsapp.NewsDetailActivity;
import com.example.tt.newsapp.R;
import com.example.tt.pojo.NewsInfo;
import com.example.tt.util.Utils;

import java.util.List;


/**
 * Created by TT on 2016/9/18.
 */
public class NewsAdapter extends BaseAdapter {
    private List<NewsInfo> list;
    private Context context;
    public NewsAdapter(List<NewsInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setList(List<NewsInfo> list) {
        this.list = list;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder h = null;
        if(view == null){
            h = new ViewHolder();
            switch (getItemViewType(i)){
                case 0:view = View.inflate(context, R.layout.item_news,null);break;
                case 1:view = View.inflate(context, R.layout.item_news_type1,null);break;
                case 2:view = View.inflate(context, R.layout.item_news_type2,null);break;
            }
            h.newsImg = (ImageView) view.findViewById(R.id.newsImg);
            h.newsTitle = (TextView) view.findViewById(R.id.newsTitle);
            h.pubIns = (TextView) view.findViewById(R.id.pubIns);
            h.commentCount = (TextView) view.findViewById(R.id.commentCount);
            if(getItemViewType(i) == 1){
                h.newsImg2 = (ImageView) view.findViewById(R.id.newsImg2);
                h.newsImg3 = (ImageView) view.findViewById(R.id.newsImg3);
            }
            view.setTag(h);
        }else{
            h = (ViewHolder) view.getTag();
        }
        final NewsInfo news = list.get(i);
        h.newsImg.setImageResource(news.getNewsPic().getSrc());
        h.newsTitle.setText(news.getTitle());
        h.pubIns.setText(news.getPubIns()+"—"+news.getNewsType().getName());
        h.commentCount.setText(news.getCommentCount()==0?"":news.getCommentCount()+"跟帖");
        if(getItemViewType(i) == 1){
            h.newsImg2.setImageResource(news.getNewsPic2().getSrc());
            h.newsImg3.setImageResource(news.getNewsPic3().getSrc());
        }

        if(((ListView)viewGroup).isItemChecked(i)){
            view.setBackgroundColor(context.getResources().getColor(R.color.red_background));
        }else{
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return view;
    }
    class ViewHolder{
        ImageView newsImg, newsImg2, newsImg3;
        TextView newsTitle,pubIns,commentCount;
    }
}

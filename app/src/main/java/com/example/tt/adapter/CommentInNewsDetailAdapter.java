package com.example.tt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tt.newsapp.R;
import com.example.tt.pojo.Comment;

import java.util.List;


/**
 * Created by TT on 2016/9/19.
 */
public class CommentInNewsDetailAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> list;
    public CommentInNewsDetailAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
    }
    public void setList(List<Comment> list) {
        this.list = list;
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
        ViewHolder h;
        if(view == null){
            h = new ViewHolder();
            view = View.inflate(context, R.layout.item_comment_top3_in_newsdetail,null);
            h.icon = (ImageView) view.findViewById(R.id.icon);
            h.nickname = (TextView) view.findViewById(R.id.nickname);
            h.commentTime = (TextView) view.findViewById(R.id.commentTime);
            h.commentContent = (TextView) view.findViewById(R.id.commentContent);
            h.upCount = (TextView) view.findViewById(R.id.upCount);
            h.borderBottom = view.findViewById(R.id.borderBottom);
            view.setTag(h);
        }else{
            h = (ViewHolder) view.getTag();
        }
        Comment comment = list.get(i);
        h.icon.setImageResource(comment.getUserInfo().getSrc());
        h.nickname.setText(comment.getUserInfo().getNickname());
        h.commentContent.setText(comment.getContent());
        h.commentTime.setText(comment.getCommentTime());
        h.upCount.setText(comment.getUpCount()==0?"":comment.getUpCount()+"人赞");
        if(i == getCount()-1){
            h.borderBottom.setVisibility(View.GONE);
        }else{
            h.borderBottom.setVisibility(View.VISIBLE);
        }
        return view;
    }
    class ViewHolder{
        ImageView icon;
        TextView nickname,commentTime,commentContent,upCount;
        View borderBottom;
    }
}

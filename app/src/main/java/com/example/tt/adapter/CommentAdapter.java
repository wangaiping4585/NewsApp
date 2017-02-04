package com.example.tt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tt.dao.CommentDao;
import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.newsapp.CommentActivity;
import com.example.tt.newsapp.R;
import com.example.tt.pojo.Comment;
import com.example.tt.util.Utils;

import java.util.List;


/**
 * Created by TT on 2016/9/20.
 */
public class CommentAdapter extends BaseAdapter {
    private DatabaseHelp dbHelper;
    private CommentDao commentDao;
    private Context context;
    private List<Comment> list;
    private int actualTopNum;
    public CommentAdapter(Context context, List<Comment> list, int actualTopNum) {
        this.context = context;
        this.list = list;
        this.actualTopNum = actualTopNum;
        dbHelper = new DatabaseHelp(context);
        commentDao = new CommentDao(dbHelper);
    }
    public void setList(List<Comment> list) {
        this.list = list;
    }
    public void setActualTopNum(int actualTopNum){
        this.actualTopNum = actualTopNum;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? 0 : 1;
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
        final int index = i;
        final ViewHolder h;
        final Comment comment = list.get(i);
        if(view == null){
            h = new ViewHolder();
            if(comment == null){
                view = View.inflate(context, R.layout.item_comment_tag, null);
                h.tag = (TextView) view.findViewById(R.id.tag);
                h.noComment = (TextView) view.findViewById(R.id.noComment);
            }else {
                view = View.inflate(context, R.layout.item_comment, null);
                h.icon = (ImageView) view.findViewById(R.id.icon);
                h.nickname = (TextView) view.findViewById(R.id.nickname);
                h.commentTime = (TextView) view.findViewById(R.id.commentTime);
                h.commentContent = (TextView) view.findViewById(R.id.commentContent);
                h.upCount = (TextView) view.findViewById(R.id.upCount);
                h.borderBottom = view.findViewById(R.id.borderBottom);
                h.replyComment = (TextView) view.findViewById(R.id.replyComment);
                h.upIcon = (ImageView) view.findViewById(R.id.upIcon);
                h.upAnim = (TextView) view.findViewById(R.id.upAnim);
            }
            view.setTag(h);
        }else{
            h = (ViewHolder) view.getTag();
        }
        if(comment == null){
            if(i == 0){
                h.tag.setText(R.string.comment_hot);
            }else if(i == actualTopNum + 1){
                h.tag.setText(R.string.comment_latest);
            }
            if(actualTopNum == 0)
                h.noComment.setVisibility(View.VISIBLE);
            else{
                h.noComment.setVisibility(View.GONE);
            }
        }else{
            h.icon.setImageResource(comment.getUserInfo().getSrc());
            h.nickname.setText(comment.getUserInfo().getNickname());
            h.commentContent.setText(comment.getContent());
            h.commentTime.setText(comment.getCommentTime());
            h.upCount.setText(comment.getUpCount()==0?"":String.valueOf(comment.getUpCount()));
            h.upIcon.setImageResource(comment.getIsUp()==0?R.drawable.up_default:R.drawable.up_click);
            //点赞、取消点赞
            View.OnClickListener upCommentOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.loginUser == null){
                        Utils.toast(context,"登录后即可点赞哦！");
                        return;
                    }
                    if(commentDao.hasLiked(comment,Utils.loginUser.getId())==0){
                        commentDao.like(comment,Utils.loginUser.getId(),1);
                        commentDao.updateUpCount(comment.getId(),1);
                        comment.setIsUp(1);
                        comment.setUpCount(comment.getUpCount()+1);
                        synchronizeUpIcon(index,comment,1);
                        ((CommentActivity)context).setResult(Utils.hasNewChange);

                        AnimationSet as = (AnimationSet) AnimationUtils.loadAnimation(context,R.anim.set_up);
                       // h.upAnim.setVisibility(View.VISIBLE);
                        h.upAnim.startAnimation(as);
                    }else{
                        commentDao.like(comment,Utils.loginUser.getId(),0);
                        commentDao.updateUpCount(comment.getId(),-1);
                        comment.setIsUp(0);
                        comment.setUpCount(comment.getUpCount()-1);
                        synchronizeUpIcon(index,comment,0);
                        ((CommentActivity)context).setResult(Utils.hasNewChange);
                    }
                }
            };
            h.upIcon.setOnClickListener(upCommentOnClick);
            h.upCount.setOnClickListener(upCommentOnClick);
            if(i == actualTopNum || i == getCount() - 1){
                h.borderBottom.setVisibility(View.INVISIBLE);
            }else{
                h.borderBottom.setVisibility(View.VISIBLE);
            }
            Comment refComment = comment.getComment();
            if(refComment != null){
                h.replyComment.setVisibility(View.VISIBLE);
                String refNickname = refComment.getUserInfo().getNickname();
                String refContent = refComment.getContent();
                String refCommentTime = refComment.getCommentTime();
                StringBuilder sb = new StringBuilder("回复：");
                sb.append(refNickname);
                sb.append("\n");
                sb.append(refCommentTime);
                sb.append("\n");
                sb.append(refContent);

                //创建一个 SpannableString对象
                SpannableString sp = new SpannableString(sb);
                //设置高亮样式二
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#428bca")),sb.indexOf(refNickname),sb.indexOf(refNickname)+refNickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#aaaaaa")),sb.indexOf(refCommentTime),sb.indexOf(refCommentTime)+refCommentTime.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //字体大小
                sp.setSpan(new RelativeSizeSpan(0.9f),sb.indexOf(refCommentTime),sb.indexOf(refCommentTime)+refCommentTime.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置斜体
                sp.setSpan(new StyleSpan(Typeface.ITALIC),0,sb.indexOf(refContent), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                h.replyComment.setText(sp);
            }else{
                h.replyComment.setVisibility(View.GONE);
            }
        }
        return view;
    }
    class ViewHolder{
        ImageView icon, upIcon;
        TextView nickname,commentTime,commentContent,upCount,tag,replyComment,noComment,upAnim;
        View borderBottom;
    }
    private void synchronizeUpIcon(int clickPosition, Comment comment, Integer isUp){
        if(clickPosition < actualTopNum + 1){
            for(int i = actualTopNum +2; i < getCount(); i++){
                Comment c = list.get(i);
                if(c == null)
                    continue;
                if(c.getId() == comment.getId()){
                    c.setIsUp(isUp);
                    c.setUpCount(comment.getUpCount());
                    break;
                }
            }
        }else if(clickPosition > actualTopNum + 1){
            for(int i = 0; i < actualTopNum + 1; i++){
                Comment c = list.get(i);
                if(c == null)
                    continue;
                if(c.getId() ==  comment.getId()){
                    c.setIsUp(isUp);
                    c.setUpCount(comment.getUpCount());
                    break;
                }
            }
        }
        this.notifyDataSetChanged();
    }

}

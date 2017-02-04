package com.example.tt.newsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tt.adapter.CommentAdapter;
import com.example.tt.dao.CommentDao;
import com.example.tt.dao.NewsInfoDao;
import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.Comment;
import com.example.tt.pojo.NewsInfo;
import com.example.tt.util.Utils;
import com.example.tt.xlistview.XListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private XListView commentListView;
    private CommentDao commentDao;
    private NewsInfoDao newsInfoDao;
    private CommentAdapter adapter;
    private DatabaseHelp d;
    private NewsInfo currentNews;
    private List<Comment> commentList;
    private Comment replyToComment;
    int actualTopNum;
    int currentPid = 1;

    private View commentEditAvatar;
    private View commentEditor;
    private EditText commentEditContent;
    private Button commentBtn;
    private TextView commentCount,replyTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentEditAvatar = findViewById(R.id.commentEditAvatar);
        commentEditor = findViewById(R.id.commentEditor);
        commentEditContent = (EditText) commentEditor.findViewById(R.id.commentEditContent);
        commentBtn = (Button) commentEditor.findViewById(R.id.commentBtn);
        commentCount = (TextView) findViewById(R.id.commentCount);
        replyTo = (TextView) findViewById(R.id.replyTo);
        commentListView = (XListView) findViewById(R.id.commentListView);

        Intent data = getIntent();
        currentNews = (NewsInfo) data.getSerializableExtra("currentNews");
        commentCount.setText(currentNews.getCommentCount()+"");

        d = new DatabaseHelp(this);
        newsInfoDao = new NewsInfoDao(d);
        commentDao = new CommentDao(d);
        actualTopNum = initCommentList();

        adapter = new CommentAdapter(this,commentList,actualTopNum);
        commentListView.setAdapter(adapter);
        final TextView loadMoreFooterTextView = (TextView)commentListView.findViewById(R.id.xlistview_footer_hint_textview);
        Boolean hasComment = data.getBooleanExtra("hasComment",false);
        if(hasComment){
            commentListView.setSelection(actualTopNum + 2);
        }
        //点击评论
        commentEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.loginUser == null){
                    Utils.toast(CommentActivity.this,"登录后即可参与评论哦！");
                    return;
                }
                replyToComment = null;
                replyTo.setVisibility(View.GONE);
                commentBtn.setVisibility(View.VISIBLE);
                commentBtn.setText("评论");
                commentEditAvatar.setVisibility(View.GONE);
                commentEditor.setVisibility(View.VISIBLE);
                commentEditContent.requestFocus();
                commentEditContent.setHint("请输入评论内容");
                InputMethodManager imm = (InputMethodManager) CommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
        commentEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().isEmpty()){
                    commentBtn.setBackgroundResource(R.drawable.shape_btn_disabled);
                    commentBtn.setEnabled(false);
                }else{
                    commentBtn.setBackgroundResource(R.drawable.shape_btn_enabled);
                    commentBtn.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //发表 评论/回复
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.loginUser == null){
                    Utils.toast(CommentActivity.this,"登录后即可回复哦！");
                    return;
                }
                Comment comment = new Comment();
                String commentContent = commentEditContent.getText().toString();
                comment.setContent(commentContent);
                comment.setNewsInfo(currentNews);
                comment.setUserInfo(Utils.loginUser);
                comment.setComment(replyToComment==null?new Comment(0,null,null,null,null,null,null):replyToComment);
                comment.setCommentTime(Utils.formatTime(new Date()));
                commentDao.save(comment);
                Utils.toast(CommentActivity.this,replyToComment==null?"评论成功！":"回复成功！");
                actualTopNum = initCommentList();
                adapter.setList(commentList);
                adapter.setActualTopNum(actualTopNum);
                adapter.notifyDataSetChanged();
                newsInfoDao.updateCommentCount(currentNews);//新闻评论数+1
                commentCount.setText(currentNews.getCommentCount()+1+"");
                currentNews.setCommentCount(currentNews.getCommentCount()+1);
                setResult(Utils.hasNewChange);

                commentEditContent.setText("");
                commentBtn.setEnabled(false);
                commentBtn.setBackgroundResource(R.drawable.shape_btn_disabled);
                commentEditor.setVisibility(View.GONE);
                commentEditAvatar.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) CommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                commentListView.setSelection(actualTopNum + 2);
                loadMoreFooterTextView.setText(R.string.xlistview_footer_hint_normal);

//                Utils.isNewsNeedChange = true;
                Utils.isCollectionNeedChange = true;
            }
        });
        //长按回复
        commentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i <= 1 || i == actualTopNum + 2){
                    return true;
                }
                replyToComment = commentList.get(i - 1);
                replyTo.setVisibility(View.VISIBLE);
                commentBtn.setText("回复");
                commentEditAvatar.setVisibility(View.GONE);
                commentEditor.setVisibility(View.VISIBLE);
                commentEditContent.requestFocus();
                commentEditContent.setHint("想对他/她说些什么");
                InputMethodManager imm = (InputMethodManager) CommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                SpannableStringBuilder sp = new SpannableStringBuilder("回复 ");
                sp.append(replyToComment.getUserInfo().getNickname());
                sp.append(" ：");
                sp.setSpan(new StyleSpan(Typeface.ITALIC),0,sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#428bca")),3,sp.length()-2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                replyTo.setText(sp);
                commentListView.setSelection(i);
                return true;
            }
        });
        //下拉刷新、加载更多
        commentListView.setRefreshTime(Utils.formatTime(new Date()));
        commentListView.setPullLoadEnable(true);
        commentListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        actualTopNum = initCommentList();
                        adapter.setList(commentList);
                        adapter.setActualTopNum(actualTopNum);
                        adapter.notifyDataSetChanged();
                        commentListView.setRefreshTime(Utils.formatTime(new Date()));
                        commentListView.stopRefresh();
                        loadMoreFooterTextView.setText(R.string.xlistview_footer_hint_normal);
                    }
                },1000);
            }
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int moreDataSize = addCommentsWithPagination(++currentPid);
                        adapter.setList(commentList);
                        adapter.notifyDataSetChanged();
                        commentListView.stopLoadMore();
                        if(moreDataSize < Utils.commentPageSize){
                            loadMoreFooterTextView.setText(R.string.xlistview_footer_hint_no_more_data);
                        }else{
                            loadMoreFooterTextView.setText(R.string.xlistview_footer_hint_normal);
                        }
                    }
                },1000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(commentEditor.getVisibility() == View.VISIBLE){
            commentEditor.setVisibility(View.GONE);
            commentEditAvatar.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(d != null){
            d.close();
        }
    }

    public void toCommentArea(View v){
        commentListView.setSelection(0);
    }

    /**
     * 初始化评论列表
     * @return 热门评论数量
     */
    public int initCommentList(){
        currentPid = 1;
        commentList = new ArrayList<>();
        List<Comment> topList = commentDao.findByNewsIdAndTopNum(currentNews.getId(),Utils.commentTopNum);
        List<Comment> newList = commentDao.findByNewsIdWithPagination(currentNews.getId(),currentPid);
        commentList.add(null);
        commentList.addAll(topList);
        commentList.add(null);
        commentList.addAll(newList);
        return topList.size();
    }
    /**
     * 分页查看更多数据
     * @param pid
     * @return
     */
    public int addCommentsWithPagination(int pid){
        List<Comment> list = commentDao.findByNewsIdWithPagination(currentNews.getId(),pid);
        commentList.addAll(list);
        return list.size();
    }
}

package com.example.tt.newsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tt.adapter.CommentInNewsDetailAdapter;
import com.example.tt.dao.CommentDao;
import com.example.tt.dao.NewsInfoDao;
import com.example.tt.dao.UserInfoDao;
import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.Comment;
import com.example.tt.pojo.NewsInfo;
import com.example.tt.util.Utils;

import java.util.Date;
import java.util.List;

public class NewsDetailActivity extends AppCompatActivity {
    private ImageView newsImg , newsImg2, newsImg3;
    private TextView newsImgDesc ,newsImgDesc2 ,newsImgDesc3;
    private TextView newsTitle, pubIns, pubTime, newsContent, commentCount;
    private CommentDao commentDao;
    private DatabaseHelp d;
    private CommentInNewsDetailAdapter adapter;
    private ListView commentListView;
    private NewsInfo currentNews;

    private View commentEditAvatar;
    private View commentEditor;
    private EditText commentEditContent;
    private Button commentBtn;

    private TextView titleBar;
    private ImageView collectionIcon;

    private NewsInfoDao newsDao;
    private UserInfoDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        d = new DatabaseHelp(this);
        newsDao = new NewsInfoDao(d);
        userDao = new UserInfoDao(d);
        commentListView = (ListView) findViewById(R.id.commentListView);
        currentNews = (NewsInfo) getIntent().getSerializableExtra("news");
        //标题栏
        collectionIcon = (ImageView) findViewById(R.id.collectionIcon);
        collectionIcon.setImageResource(currentNews.getHasCollected()==0?R.drawable.star_default:R.drawable.star_collection);
        titleBar = (TextView) findViewById(R.id.titleBar);
        titleBar.setText(currentNews.getNewsType().getName());
        //收藏
        collectionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.loginUser == null){
                    Utils.toast(NewsDetailActivity.this,"登录之后才能收藏喜欢的新闻哦！");
                    return;
                }
                if(newsDao.hasCollected(currentNews.getId(),Utils.loginUser.getId())==0){
                    newsDao.collected(currentNews.getId(),Utils.loginUser.getId());
                    userDao.updateCollectionCount(Utils.loginUser.getId(),1);
                    collectionIcon.setImageResource(R.drawable.star_collection);
                    Utils.toast(NewsDetailActivity.this,"已加入收藏！");
                }else{
                    newsDao.disCollected(currentNews.getId(),Utils.loginUser.getId());
                    userDao.updateCollectionCount(Utils.loginUser.getId(),-1);
                    collectionIcon.setImageResource(R.drawable.star_default);
                    Utils.toast(NewsDetailActivity.this,"已取消收藏！");
                }
//                Utils.isNewsNeedChange = true;
                Utils.isCollectionNeedChange = true;
            }
        });
        //评论栏
        commentEditAvatar = findViewById(R.id.commentEditAvatar);
        commentEditor = findViewById(R.id.commentEditor);
        commentEditContent = (EditText) findViewById(R.id.commentEditContent);
        commentBtn = (Button) findViewById(R.id.commentBtn);
        commentCount = (TextView) findViewById(R.id.commentCount);
        //新闻主体
        View newsBody = View.inflate(this,R.layout.header_news_deital_body,null);
        newsImg = (ImageView) newsBody.findViewById(R.id.newsImg);
        newsTitle = (TextView) newsBody.findViewById(R.id.newsTitle);
        pubIns = (TextView) newsBody.findViewById(R.id.pubIns);
        pubTime = (TextView) newsBody.findViewById(R.id.pubTime);
        newsImgDesc = (TextView) newsBody.findViewById(R.id.newsImgDesc);
        newsContent = (TextView) newsBody.findViewById(R.id.newsContent);

        newsImg2 = (ImageView) newsBody.findViewById(R.id.newsImg2);
        newsImg3 = (ImageView) newsBody.findViewById(R.id.newsImg3);
        newsImgDesc2 = (TextView) newsBody.findViewById(R.id.newsImgDesc2);
        newsImgDesc3 = (TextView) newsBody.findViewById(R.id.newsImgDesc3);
        if(currentNews.getType() != 1){
            newsImg2.setVisibility(View.GONE);
            newsImg3.setVisibility(View.GONE);
            newsImgDesc2.setVisibility(View.GONE);
            newsImgDesc3.setVisibility(View.GONE);
        }
        //热门跟贴头
        View commentTop3Header = View.inflate(this,R.layout.header_news_detail_top3_comment_header,null);
        //热门跟贴尾
        final View commentTop3Footer = View.inflate(this,R.layout.header_news_detail_top3_comment_footer,null);
        //填充新闻页信息
        newsImg.setImageResource(currentNews.getNewsPic().getSrc());
        newsTitle.setText(currentNews.getTitle());
        pubIns.setText(currentNews.getPubIns());
        pubTime.setText(currentNews.getPubTime());
        newsImgDesc.setText(currentNews.getNewsPic().getDesc());
        newsContent.setText(currentNews.getContent());
        if(currentNews.getType() == 1){
            newsImg2.setImageResource(currentNews.getNewsPic2().getSrc());
            newsImg3.setImageResource(currentNews.getNewsPic3().getSrc());
            newsImgDesc2.setText(currentNews.getNewsPic2().getDesc());
            newsImgDesc3.setText(currentNews.getNewsPic3().getDesc());
        }

        commentCount.setText(currentNews.getCommentCount()+"");
        //获取该新闻页的前3评论
        commentDao = new CommentDao(d);
        List<Comment> commentList = commentDao.findByNewsIdAndTopNum(currentNews.getId(),3);
        adapter = new CommentInNewsDetailAdapter(this,commentList);
        commentListView.setAdapter(adapter);
        //为listView添加头尾
        commentListView.addHeaderView(newsBody);
        commentListView.addHeaderView(commentTop3Header);
        commentListView.addFooterView(commentTop3Footer);

        //点击调转至评论区事件
        if(commentList.size() == 0){
            ((TextView)commentTop3Footer.findViewById(R.id.title)).setText("暂无评论！");
        }
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    toCommentArea();
                }
            }
        });

        commentEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.loginUser == null){
                    Utils.toast(NewsDetailActivity.this,"登录后即可参与评论哦！");
                    return;
                }
                commentEditAvatar.setVisibility(View.INVISIBLE);
                commentEditor.setVisibility(View.VISIBLE);
                commentEditContent.requestFocus();
                InputMethodManager imm = (InputMethodManager) NewsDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
        //发表评论
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                String commentContent = commentEditContent.getText().toString();
                Comment refComment = new Comment();
                refComment.setId(0);
                comment.setContent(commentContent);
                comment.setNewsInfo(currentNews);
                comment.setUserInfo(Utils.loginUser);
                comment.setComment(refComment);
                comment.setCommentTime(Utils.formatTime(new Date()));
                commentDao.save(comment);//保存评论
                newsDao.updateCommentCount(currentNews);//新闻评论数+1
                commentCount.setText(currentNews.getCommentCount()+1+"");
                currentNews.setCommentCount(currentNews.getCommentCount()+1);

                Utils.toast(NewsDetailActivity.this,"评论成功！");
                toCommentArea(true);

                commentEditContent.setText("");
                commentBtn.setEnabled(false);
                commentBtn.setBackgroundResource(R.drawable.shape_btn_disabled);
                commentEditor.setVisibility(View.INVISIBLE);
                commentEditAvatar.setVisibility(View.VISIBLE);

//                Utils.isNewsNeedChange = true;
                Utils.isCollectionNeedChange = true;
            }
        });
    }
    //点击调转至评论区事件
    public void toCommentArea(View v){
        toCommentArea();
    }
    public void toCommentArea(){
        toCommentArea(false);
    }
    public void toCommentArea(boolean hasComment){
        Intent it = new Intent(NewsDetailActivity.this, CommentActivity.class);
        it.putExtra("currentNews", currentNews);
        it.putExtra("hasComment", hasComment);
        startActivityForResult(it,1);
    }

    @Override
    public void onBackPressed() {
        if(commentEditor.getVisibility() == View.VISIBLE){
            commentEditor.setVisibility(View.INVISIBLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Utils.hasNewChange){
            //获取该新闻页的前3评论
            List<Comment> commentList = commentDao.findByNewsIdAndTopNum(currentNews.getId(),Utils.commentTopNum);
            adapter.setList(commentList);
            adapter.notifyDataSetChanged();

            currentNews = newsDao.findById(currentNews.getId());
            Integer commentCount = currentNews.getCommentCount();
            this.commentCount.setText(commentCount+"");
        }
    }
}

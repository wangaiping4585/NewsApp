package com.example.tt.newsapp;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tt.adapter.NewsAdapter;
import com.example.tt.adapter.NewsFragmentPagerAdapter;
import com.example.tt.dao.NewsInfoDao;
import com.example.tt.dao.NewsTypeDao;
import com.example.tt.dao.UserInfoDao;
import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.NewsInfo;
import com.example.tt.pojo.NewsType;
import com.example.tt.pojo.UserInfo;
import com.example.tt.util.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RadioGroup mainNav;
    private View nav1News,nav2Collection,nav3Profile;
    private DatabaseHelp dbHelper;
    private RadioButton navTab1,navTab2,navTab3;
    Rect tabIconBound = new Rect(0,0,67,67);
    Drawable newsTabIcon, collectionTabIcon, profileTabIcon;
    //news
    private TabLayout newsNav;
    private ViewPager newsPager;
    private NewsTypeDao newsTypeDao;
    private List<NewsType> list;
    private NewsFragmentPagerAdapter adapter;
    //collection
    private ListView collectionListView;
    private NewsAdapter collectionAdapter;
    private List<NewsInfo> collectionList;
    private NewsInfoDao newsDao;
    private TextView collectionCount;
    private TextView goToLogin,goToReadNews;
    private View noLoginCollectionHint,noDataCollectionHint,multiChoiceMode;
    private TextView checkedCount;
    private Button cancelCollectionBtn;
    private CheckBox selectAll, selectReverse;
    //profile
    private Button btnLogin;
    private TextView registerBtn,logout,updatePassword;
    private UserInfoDao userDao;
    private ImageView userIcon;
    private TextView userNickname, userRegisterTime;
    private View noLoginZone, hasLoginZone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,WelcomeActivity.class));
            }
        }).start();
        dbHelper = new DatabaseHelp(this);
        userDao = new UserInfoDao(dbHelper);
        newsDao = new NewsInfoDao(dbHelper);
        //用户验证
        SharedPreferences sp = getSharedPreferences("news_preferences.xml",MODE_PRIVATE);
        int loginUserId = sp.getInt("loginUserId",0);
        if(loginUserId != 0){
            UserInfo loginUser = userDao.findById(loginUserId);
            if(loginUser != null){
                Utils.loginUser = loginUser;
            }
        }
        //tabs
        mainNav = (RadioGroup) findViewById(R.id.mainNav);
        navTab1 = (RadioButton) findViewById(R.id.navTab1);
        navTab2 = (RadioButton) findViewById(R.id.navTab2);
        navTab3 = (RadioButton) findViewById(R.id.navTab3);
        newsTabIcon = getResources().getDrawable(R.drawable.selector_nav_tab_news);
        newsTabIcon.setBounds(tabIconBound);
        collectionTabIcon = getResources().getDrawable(R.drawable.selector_nav_tab_collection);
        collectionTabIcon.setBounds(tabIconBound);
        profileTabIcon = getResources().getDrawable(R.drawable.selector_nav_tab_profile);
        profileTabIcon.setBounds(tabIconBound);
        navTab1.setCompoundDrawables(null,newsTabIcon,null,null);
        navTab2.setCompoundDrawables(null,collectionTabIcon,null,null);
        navTab3.setCompoundDrawables(null,profileTabIcon,null,null);
        //news
        nav1News = findViewById(R.id.nav1News);
        newsNav = (TabLayout) findViewById(R.id.newsNav);
        newsPager = (ViewPager) findViewById(R.id.newsPager);
        newsTypeDao = new NewsTypeDao(dbHelper);
        list = newsTypeDao.findAll();
        adapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(),list);
        newsPager.setAdapter(adapter);
        newsNav.setupWithViewPager(newsPager);

        //collection
        nav2Collection = findViewById(R.id.nav2Collection);
        collectionListView = (ListView) findViewById(R.id.collectionListView);
        collectionCount = (TextView) findViewById(R.id.collectionCount);
        noLoginCollectionHint = findViewById(R.id.noLoginCollectionHint);
        noDataCollectionHint = findViewById(R.id.noDataCollectionHint);
        goToLogin = (TextView) findViewById(R.id.goToLogin);
        goToReadNews = (TextView) findViewById(R.id.goToReadNews);
        multiChoiceMode = findViewById(R.id.multiChoiceMode);
        checkedCount = (TextView) findViewById(R.id.checkedCount);
        cancelCollectionBtn = (Button) findViewById(R.id.cancelCollectionBtn);
        selectAll = (CheckBox) findViewById(R.id.selectAll);
        selectReverse = (CheckBox) findViewById(R.id.selectReverse);
        collectionList = newsDao.findByUserCollection(Utils.loginUser==null?0:Utils.loginUser.getId());
        collectionAdapter = new NewsAdapter(collectionList,this);
        collectionListView.setAdapter(collectionAdapter);
        collectionCount.setText(collectionList.size()==0?"":"("+collectionList.size()+")");
        //进入新闻详细页
        collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(collectionListView.getChoiceMode()==ListView.CHOICE_MODE_MULTIPLE){
                    refreshCollection();
                    checkedCount.setText("选中："+collectionListView.getCheckedItemCount());
                    return;
                }
                Intent it = new Intent(MainActivity.this, NewsDetailActivity.class);
                it.putExtra("news",collectionList.get(i));
                startActivity(it);
            }
        });
        //多选
        collectionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(multiChoiceMode.getVisibility()==View.GONE){
                    AnimationSet as = (AnimationSet) AnimationUtils.loadAnimation(MainActivity.this,R.anim.set_down_appear);
                    multiChoiceMode.setVisibility(View.VISIBLE);
                    multiChoiceMode.startAnimation(as);

                    collectionListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    collectionListView.setItemChecked(i,true);
                    //refreshCollection();
                    checkedCount.setText("选中："+collectionListView.getCheckedItemCount());
                }
                return true;
            }
        });
        //取消收藏
        cancelCollectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedCount = collectionListView.getCheckedItemCount();
                if(checkedCount>0){
                    for (int x = 0; x < collectionList.size(); x++){
                        if(collectionListView.isItemChecked(x)){
                            newsDao.disCollected(collectionList.get(x).getId(),Utils.loginUser.getId());
                        }
                    }
                    Utils.toast(MainActivity.this,"已将 "+checkedCount+" 条新闻移出收藏");
                    startActivity(new Intent(MainActivity.this,RefreshActivity.class));
                }
                finishMultiChoice();

                AnimationSet as = (AnimationSet) AnimationUtils.loadAnimation(MainActivity.this,R.anim.set_up_disappear);
                multiChoiceMode.startAnimation(as);
            }
        });
        //全选
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (int x = 0; x < collectionList.size(); x++){
                    collectionListView.setItemChecked(x,b);
                }
                checkedCount.setText("选中："+collectionListView.getCheckedItemCount());
            }
        });
        selectReverse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                for (int x = 0; x < collectionList.size(); x++){
                    collectionListView.setItemChecked(x,!collectionListView.isItemChecked(x));
                }
                checkedCount.setText("选中："+collectionListView.getCheckedItemCount());
            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
        goToReadNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav1News.setVisibility(View.VISIBLE);
                nav2Collection.setVisibility(View.GONE);
                nav3Profile.setVisibility(View.GONE);
                mainNav.check(R.id.navTab1);
            }
        });
        //profile
        nav3Profile = findViewById(R.id.nav3Profile);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        logout = (TextView) findViewById(R.id.logout);
        updatePassword = (TextView) findViewById(R.id.updatePassword);
        registerBtn = (TextView) findViewById(R.id.registerBtn);
        noLoginZone = findViewById(R.id.noLoginZone);
        hasLoginZone = findViewById(R.id.hasLoginZone);
        userIcon = (ImageView) findViewById(R.id.userIcon);
        userNickname = (TextView) findViewById(R.id.userNickname);
        userRegisterTime = (TextView) findViewById(R.id.userRegisterTime);
        inflateUserProfile();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.loginUser==null)
                    goToLogin();
                else
                    startActivity(new Intent(MainActivity.this,ChangePasswordActivity.class));
            }
        });
        userNickname.setOnClickListener(new View.OnClickListener() {
            EditText txt;
            @Override
            public void onClick(View view) {
                RelativeLayout root = new RelativeLayout(MainActivity.this);
                RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                txtParams.setMargins(60,20,60,0);
                txt = new EditText(MainActivity.this);
                txt.setLayoutParams(txtParams);
                txt.setText(userNickname.getText().toString());
                root.addView(txt);
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("修改昵称")
                        .setView(root)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               String newNickname = txt.getText().toString().trim();
                                if(newNickname.equals(Utils.loginUser.getNickname()))
                                    return;
                                if(newNickname.isEmpty()){
                                    Utils.toast(MainActivity.this,"昵称不能为空！");
                                    return;
                                }
                                Utils.loginUser.setNickname(newNickname);
                                userDao.updateNickname(Utils.loginUser.getId(),newNickname);
                                Utils.toast(MainActivity.this,"昵称修改成功！");
                                userNickname.setText(newNickname);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        //注销
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.loginUser==null)
                    goToLogin();
                else{
                    AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                    b.setTitle("注销")
                            .setMessage("确认注销么？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(MainActivity.this,LogoutActivity.class));
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                }
            }
        });
        //tab
        mainNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.navTab1:
                        nav1News.setVisibility(View.VISIBLE);
                        nav2Collection.setVisibility(View.GONE);
                        nav3Profile.setVisibility(View.GONE);
                        finishMultiChoice();
                        break;
                    case R.id.navTab2:
                        nav1News.setVisibility(View.GONE);
                        nav2Collection.setVisibility(View.VISIBLE);
                        nav3Profile.setVisibility(View.GONE);
                        if(Utils.loginUser==null){
                            noLoginCollectionHint.setVisibility(View.VISIBLE);
                            noDataCollectionHint.setVisibility(View.GONE);
                            collectionListView.setVisibility(View.GONE);
                        }else if(collectionAdapter.getCount() == 0){
                            noLoginCollectionHint.setVisibility(View.GONE);
                            noDataCollectionHint.setVisibility(View.VISIBLE);
                            collectionListView.setVisibility(View.GONE);
                        }else{
                            noLoginCollectionHint.setVisibility(View.GONE);
                            noDataCollectionHint.setVisibility(View.GONE);
                            collectionListView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.navTab3:
                        nav1News.setVisibility(View.GONE);
                        nav2Collection.setVisibility(View.GONE);
                        nav3Profile.setVisibility(View.VISIBLE);
                        if(Utils.loginUser==null){
                            noLoginZone.setVisibility(View.VISIBLE);
                            hasLoginZone.setVisibility(View.INVISIBLE);
                        }else{
                            noLoginZone.setVisibility(View.INVISIBLE);
                            hasLoginZone.setVisibility(View.VISIBLE);
                        }
                        finishMultiChoice();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //用户登录后
        if(Utils.isNewLoginUser){
            inflateUserProfile();
            Utils.isNewLoginUser = false;
        }
        //收藏状态更新后
        if(Utils.isCollectionNeedChange){
            refreshCollection();//更新收藏
            Utils.isCollectionNeedChange = false;
        }
    }
    private void refreshCollection(){
        collectionList = newsDao.findByUserCollection(Utils.loginUser==null?0:Utils.loginUser.getId());
        collectionAdapter.setList(collectionList);
        collectionAdapter.notifyDataSetChanged();
        collectionCount.setText(collectionList.size()==0?"":"("+collectionList.size()+")");
        if(nav2Collection.getVisibility() == View.VISIBLE){
            if(Utils.loginUser==null){
                noLoginCollectionHint.setVisibility(View.VISIBLE);
                noDataCollectionHint.setVisibility(View.GONE);
                collectionListView.setVisibility(View.GONE);
            }else if(collectionAdapter.getCount() == 0){
                noLoginCollectionHint.setVisibility(View.GONE);
                noDataCollectionHint.setVisibility(View.VISIBLE);
                collectionListView.setVisibility(View.GONE);
            }else{
                noLoginCollectionHint.setVisibility(View.GONE);
                noDataCollectionHint.setVisibility(View.GONE);
                collectionListView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper != null){
            dbHelper.close();
        }
    }
    private void goToLogin(){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
    private void inflateUserProfile(){
        UserInfo loginUser = Utils.loginUser;
        if(loginUser==null){
            noLoginZone.setVisibility(View.VISIBLE);
            hasLoginZone.setVisibility(View.INVISIBLE);
        }else{
            noLoginZone.setVisibility(View.INVISIBLE);
            hasLoginZone.setVisibility(View.VISIBLE);
            userIcon.setImageResource(loginUser.getSrc());
            userNickname.setText(loginUser.getNickname());
            userRegisterTime.setText("注册于 "+loginUser.getRegisterTime());
        }
    }
    long time = 0;
    @Override
    public void onBackPressed() {
        if(collectionListView.getChoiceMode()==ListView.CHOICE_MODE_MULTIPLE){
            finishMultiChoice();

            AnimationSet as = (AnimationSet) AnimationUtils.loadAnimation(MainActivity.this,R.anim.set_up_disappear);
            multiChoiceMode.startAnimation(as);
        }else{
            long currentTime = System.currentTimeMillis();
            if(currentTime - time > 2000){
                time = currentTime;
                Utils.toast(this,"再次点击返回键退出");
            }else{
                finish();
                System.exit(0);
            }
        }
    }
    private void finishMultiChoice(){
        collectionListView.clearChoices();
        collectionListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        selectAll.setChecked(false);
        selectReverse.setChecked(false);
        refreshCollection();
        multiChoiceMode.setVisibility(View.GONE);
    }
}

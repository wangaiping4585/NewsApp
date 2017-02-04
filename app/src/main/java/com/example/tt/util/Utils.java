package com.example.tt.util;

import android.content.Context;
import android.widget.Toast;

import com.example.tt.newsapp.R;
import com.example.tt.pojo.Comment;
import com.example.tt.pojo.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TT on 2016/9/18.
 */
public class Utils {
    public static final int commentPageSize = 3;
    public static final int commentTopNum = 3;

    public static final int hasNewChange = 1;

//    public static boolean isNewsNeedChange;
    public static boolean isCollectionNeedChange;
    public static boolean isNewLoginUser;
    public static UserInfo loginUser;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String configPath = "news_preferences.xml";

    public static void toast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    public static String formatTime(Date date){
        return sdf.format(date);
    }
}

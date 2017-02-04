package com.example.tt.databasehelp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tt.newsapp.R;

/**
 * Created by TT on 2016/9/18.
 */
public class DatabaseHelp extends SQLiteOpenHelper {
    private final String NEWS_INFO =
            "create table news_info(" +
            "id integer primary key autoincrement," +
            "title," +
            "content," +
            "news_type," +
            "pub_time," +
            "pub_ins," +
            "comment_count," +
            "news_pic," +
            "news_pic2," +
            "news_pic3," +
            "showType)";
    private final String NEWS_TYPE =
            "create table news_type(" +
            "id integer primary key autoincrement," +
            "name)";
    private final String NEWS_PIC =
            "create table news_pic(" +
            "id integer primary key autoincrement," +
            "src," +
            "desc)";
    private final String COMMENT =
            "create table comment(" +
            "id integer primary key autoincrement," +
            "content," +
            "news_info," +
            "comment," +
            "user_info," +
            "comment_time," +
            "up_count)";
    private final String USER_INFO =
            "create table user_info(" +
            "id integer primary key autoincrement," +
            "username," +
            "password," +
            "nickname," +
            "icon," +
            "register_time," +
            "collection_count)";
    private final String USER_UP_COMMENT =
            "create table user_up_comment(" +
            "id integer primary key autoincrement," +
            "user_info," +
            "comment," +
            "is_up)";
    private final String USER_COLLECT_NEWS =
            "create table user_collect_news(" +
            "id integer primary key autoincrement," +
            " user_info," +
            " news_info," +
            " has_collected)";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NEWS_INFO);
        db.execSQL(NEWS_TYPE);
        db.execSQL(COMMENT);
        db.execSQL(NEWS_PIC);
        db.execSQL(USER_INFO);
        db.execSQL(USER_UP_COMMENT);
        db.execSQL(USER_COLLECT_NEWS);

        db.execSQL("insert into news_type values(0,'综合')");
        db.execSQL("insert into news_type values(null,'国内')");
        db.execSQL("insert into news_type values(null,'国际')");
        db.execSQL("insert into news_type values(null,'娱乐')");
        db.execSQL("insert into news_type values(null,'体育')");
        db.execSQL("insert into news_type values(null,'科技')");
        db.execSQL("insert into news_type values(null,'军事')");
        db.execSQL("insert into news_type values(null,'时尚')");

        db.execSQL("insert into news_info values(null,'李克强总理离开加拿大抵达古巴','9月24日上午，李总理结束对加拿大为期三天的访问，乘专机离开蒙特利尔，于当地时间24日下午抵达古巴首都哈瓦那何塞·马蒂国际机场，古巴国务委员会第一副主席兼部长会议第一副主席迪亚斯-卡内尔到机场迎接。这是中古建交56年来中国总理对古巴的首次正式访问。'," +
                "1,'2016-09-25 14:41:15','央视',15,1,0,0,0)");
        db.execSQL("insert into news_info values(null,'今明两日华北黄淮等地区有中度霾 局地重度污染','中新网9月25日电 据中央气象台网站消息，中央气象台今日发布环境气象公报显示，25日至26日华北中南部、黄淮西部、江淮西部、陕西关中等地的部分地区有轻到中度霾，并伴有轻到中度污染，局地重度污染。\n24日05时至25日05时，华北中南部、黄淮西部、江汉等地的部分地区出现轻度霾，局地中度霾，上述部分地区PM2.5平均浓度超过75微克/立方米，出现轻度污染，局地中到重度污染。'," +
                "1,'2016-09-25 09:07:00','中国新闻网',0,2,0,0,0)");
        db.execSQL("insert into news_info values(null,'美国华盛顿枪击案已致5死 嫌犯为拉丁裔年轻男子','央广网北京9月25日消息 据中国之声《新闻纵横》报道，美国西北部的华盛顿州当地时间23号晚，位于该州的伯灵顿市一家购物中心发生枪击事件，截止到目前，已经造成5人死亡，死者为4女1男。'," +
                "2,'2016-09-25 08:30:00','央广网',0,3,0,0,2)");
        db.execSQL("insert into news_info values(null,'扎克伯格联手霍金 耗资1亿美元探测外星讯号','中新网9月25日电 据香港《文汇报》报道，社交网站Facebook创办人扎克伯格、俄罗斯企业家米尔纳及英国著名物理学家霍金，正式展开耗费1亿美元的“突破聆听”计划，利用最先进的天文望远镜，探测来自“比邻星b”的讯号。\n" +
                "科学家早前发现在距离地球仅4光年的地方，存在一颗酷似地球的岩石行星“比邻星b”，相信具备孕育生命的条件，令不少科学家大受鼓舞。'," +
                "2,'2016-09-25 09:46:00','中国新闻网',0,4,0,0,2)");
        db.execSQL("insert into news_info values(null,'王思聪双手插裤兜似老干部'," +
                "'9月24日，有网友在微博晒出偶遇王思聪和王健林在合肥同行的照片，王思聪双手插兜，穿粉色衬衣似老干部，两人的出现也引来众人围观。'," +
                "3,'2016-09-25 10:12','网易娱乐',0,5,6,7,1)");
        db.execSQL("insert into news_info values(null,'韩国新女团“宇宙少女”程潇零整容神似唐嫣','韩国Starship新女团“宇宙少女”，12位成员中有一名中国女生，除了长得漂亮，身材高，还是零整容天然美女，还长得像唐嫣，而且还会后空翻，猴赛雷啊！！！'," +
                "3,'2016-03-03 09:39:19','爱秀美',0,8,0,0,2)");
        db.execSQL("insert into news_info values(null,'丁俊晖10-6成功复仇塞尔比 再夺上海赛冠军'," +
                "'2016年斯诺克上海大师赛决赛，上半场丁俊晖6-3领先，下半场丁俊晖在被塞尔比连扳三局的情况下，又连胜4局，遏制住对手强制反扑，报得2016年世锦赛惜败之仇，并打破上海大师赛“九年九冠”魔咒，以10-6勇夺个人第二个上海大师赛冠军。'," +
                "4,'2016-09-25 22:48','视觉中国',0,9,0,0,2)");
        db.execSQL("insert into news_info values(null,'Note7电池故障是三星过分偏执后的宿命'," +
                "'在经历空前规模的召回和空前统一的全球航空公司禁用令后，三星Galaxy Note 7的知名度恐怕也是达到了一个空前的历史水平。'," +
                "5,'2016-09-25 08:18:50','网易科技报道',0,10,0,0,0)");
        db.execSQL("insert into news_info values(null,'中国海警舰船编队24日进入钓鱼岛领海巡航'," +
                "'中新社北京9月24日电 (记者 阮煜琳)中国国家海洋局24日在北京发布消息称，中国海警舰船编队当天在中国钓鱼岛领海巡航。据官方消息显示，这是今年9月份以来，中国海警舰船编队第二次在中国钓鱼岛领海巡航。'," +
                "6,'2016-09-25 10:22:36','中国新闻网',0,11,0,0,2)");
        db.execSQL("insert into news_info values(null,'日本女生最流行的肌断食法你get到了吗'," +
                "'肌断食什么意思?你是否听说过有这么一种护肤方法叫做肌断食?有人可能会想这是不是什么都护肤品都不用，也可能有人很直接地回答“不知道”。没关系，不管你是不是知道这种流传在美容达人圈的肌断食护肤方法,今天就让你三分钟了解肌断食疗法!'," +
                "7,'2016-04-15 07:10:07','网易女人',0,12,0,0,0)");




        //db.execSQL("insert into news_pic values(null,"+ R.drawable.doge+",'doge')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.guonei1+",'9月24日下午，李克强总理抵达古巴首都哈瓦那')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.guonei2+",'图片来自：中央气象台网站')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.guoji1+",'美国华盛顿州枪击案已致5死 嫌犯为拉丁裔年轻男子')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.guoji2+",'资料图：“比邻星b”可能具有适合生命繁衍的环境。')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.yule1_1+",'王思聪双手插兜，穿粉色衬衣似老干部')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.yule1_2+",'偶遇王思聪和王健林在合肥同行')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.yule1_3+",'两人的出现也引来众人围观')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.yule2+",'宇宙少女中国妹子程潇 会空翻的萌妹子')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.tiyu1+",'丁俊晖接过沉甸甸的冠军奖杯')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.keji1+",'电池为什么会爆炸？')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.junshi1+",'中国海警舰船编队24日进入钓鱼岛领海巡航')");
        db.execSQL("insert into news_pic values(null,"+ R.drawable.shishang1+",'最好的懒人护肤方法')");

        db.execSQL("insert into user_info values(null,'tt','tt','tt',"+ R.drawable.doge+",'2011-01-01 12:12:12',1)");

        db.execSQL("insert into comment values(null,'支持！！！',1,0,1,'2016-02-03 11:11:11',10)");
        db.execSQL("insert into comment values(null,'顶',1,1,1,'2016-02-03 11:11:11',14)");
        db.execSQL("insert into comment values(null,'同意',1,1,1,'2016-02-03 11:11:11',80)");
        db.execSQL("insert into comment values(null,'扬我国威！',1,0,1,'2016-02-03 11:11:11',998)");
        db.execSQL("insert into comment values(null,'今晚吃啥',1,3,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚1吃啥',1,3,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚2吃啥',1,1,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚3吃啥',1,0,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚4吃啥',1,0,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚5吃啥',1,0,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚6吃啥',1,0,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚7吃啥',1,4,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚8吃啥',1,0,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚9吃啥',1,0,1,'2016-02-03 11:11:11',100)");
        db.execSQL("insert into comment values(null,'今晚10吃啥',1,0,1,'2016-02-03 11:11:11',100)");

        db.execSQL("insert into user_up_comment values(null,1,1,1)");

        db.execSQL("insert into user_collect_news values(null,1,2,1)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public DatabaseHelp(Context context) {
        super(context, "newsapp.db", null, 1);
    }
}

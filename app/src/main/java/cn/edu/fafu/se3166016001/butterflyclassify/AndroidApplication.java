package cn.edu.fafu.se3166016001.butterflyclassify;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.xuexiang.xui.XUI;

import cn.edu.fafu.se3166016001.butterflyclassify.DataBase.DBManager;
import cn.edu.fafu.se3166016001.butterflyclassify.DataBase.DBUtils;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.InsectOrder;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.User;
import okhttp3.OkHttpClient;

public class AndroidApplication extends Application {
    private static AndroidApplication instance;

//    private String host = "http://192.168.1.3";
//    private String port = "8080";

    private String host = "http://118.25.129.71";
    private String port = "28080";

    private  DBUtils dbUtils;
    private DBManager dbManager;


    // 全局用户信息
    private static User user;
    @Override
    public void onCreate() {
        instance = this;
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        super.onCreate();
        dbUtils = new DBUtils(this,"user.db",null,1);
        SQLiteDatabase db = dbUtils.getWritableDatabase();
        dbManager = DBManager.getInstance(db);
    }
    public String getHostNPort(){
        return host.concat(":").concat(port);
    }

    public DBManager getDBManager() {
        return dbManager;
    }

    public static AndroidApplication getInstance(){
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // order详情暂存的order对象
    private InsectOrder tempOrder;

    public InsectOrder getTempOrder() {
        return tempOrder;
    }

    public void setTempOrder(InsectOrder tempOrder) {
        this.tempOrder = tempOrder;
    }
}

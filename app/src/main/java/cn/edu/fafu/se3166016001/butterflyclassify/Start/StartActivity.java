package cn.edu.fafu.se3166016001.butterflyclassify.Start;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.DataBase.DBManager;
import cn.edu.fafu.se3166016001.butterflyclassify.DataBase.DBUtils;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.MainActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.User;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {
    private ImageView startImg;
    private TextView text;
    private DBUtils dbUtils;

    private Activity instance;
    private AndroidApplication application;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        instance = this;
        application = (AndroidApplication) instance.getApplication();
        address = application.getHostNPort();
        findViewById(R.id.startImg).post(new Runnable() {
            @Override
            public void run() {
                getUserInfo();
            }
        });
    }

    private void getUserInfo() {

        DBManager dm = application.getDBManager();

        // 判断有无缓存用户信息,
        User user = dm.queryLastUser();
        if(user != null) {
            if(user.getToken().equals("")) {
                instance.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(instance,"没有用户token，请先登录",Toast.LENGTH_LONG).show();
                    }
                });
                gotoLoginActivity();
                return;
            }

            Map<String,String> param = new HashMap<>();
            param.put("userName",user.getUserName());
            param.put("token", user.getToken());
            HttpUtil.doPost(address.concat("/user/token-login"), param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    instance.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(instance,"请求服务器失败，请检查网络",Toast.LENGTH_LONG).show();
                        }
                    });
                    gotoLoginActivity();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject resData = null;
                    try {
                        resData = new JSONObject(response.body().string());
                        JSONObject userJson = resData.getJSONObject("data");
                        Gson gson = new Gson();
                        User user = gson.fromJson(userJson.toString(),User.class);
                        application.setUser(user);

                        dm.updateUserToken(user);

                        gotoMainActivity();
                    } catch (JSONException e) {
                        instance.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(instance,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                        gotoLoginActivity();
                    }
                }
            });
        } else {
            instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(instance,"没有用户信息，请先登录",Toast.LENGTH_LONG).show();
                }
            });
            gotoLoginActivity();
        }


    }
    private void gotoLoginActivity(){
        Intent toLogin = new Intent(instance, LoginActivity.class);
        toLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toLogin);
        finish();
    }

    private void gotoMainActivity(){
        Intent toHome = new Intent(instance, MainActivity.class);
        toHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toHome);
        finish();
    }


}

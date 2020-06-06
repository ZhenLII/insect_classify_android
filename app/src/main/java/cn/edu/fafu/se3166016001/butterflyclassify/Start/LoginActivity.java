package cn.edu.fafu.se3166016001.butterflyclassify.Start;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.DataBase.DBManager;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.MainActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.User;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView userNameText;
    private EditText passwordText;
    private RoundButton loginButton;
    private TextView registerButton;

    private Activity instance;
    private AndroidApplication application;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance = this;
        application = (AndroidApplication) instance.getApplication();
        address = application.getHostNPort();

        userNameText = findViewById(R.id.userNameText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        // 自动填写最后一次登录的用户名
        DBManager dm = application.getDBManager();
        User user = dm.queryLastUser();

        if(user != null) {
            userNameText.setText(user.getUserName());
        }

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });

    }

    private void handleLogin() {
        DBManager dm = application.getDBManager();
        boolean validate = true;
        userNameText.setError(null);
        passwordText.setError(null);

        String userName = userNameText.getText().toString();
        String password = passwordText.getText().toString();

        if(TextUtils.isEmpty(userName)) {
            userNameText.setError("用户名不能为空");
            validate = false;
        }
        if(TextUtils.isEmpty(password)) {
            passwordText.setError("密码不能为空");
            validate = false;
        }

        if (validate) {
            loginButton.setEnabled(false);
            loginButton.setText("正在登陆");
            Map<String,String> param = new HashMap<>();
            param.put("userName",userName);
            param.put("userPassword",password);
            HttpUtil.doPost(address.concat("/user/login"), param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginButton.setEnabled(true);
                            Toast.makeText(instance,"登录失败，请检查网络",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject resData = null;
                    try {
                        resData = new JSONObject(response.body().string());
                        if(resData.getInt("code") == 20000) {
                            JSONObject userJson = resData.getJSONObject("data");
                            Gson gson = new Gson();
                            User user = gson.fromJson(userJson.toString(),User.class);
                            application.setUser(user);
                            if(dm.queryLastUser() == null) {
                                dm.insertUser(user);
                            } else {
                                dm.updateUserToken(user);
                            }
                            gotoMainActivity();
                        } else {
                            String errMessage = resData.getString("message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(instance, errMessage,Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginButton.setEnabled(true);
                            loginButton.setText("登陆");
                        }
                    });
                }
            });
        }


    }
    private void gotoMainActivity(){
        Intent toHome = new Intent(instance, MainActivity.class);
        toHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toHome);
        finish();
    }
    private void gotoRegister(){
        Intent toRegister = new Intent(instance, RigisterActivity.class);
        toRegister.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toRegister);
        finish();
    }

}


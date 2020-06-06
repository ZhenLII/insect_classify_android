package cn.edu.fafu.se3166016001.butterflyclassify.Start;

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RigisterActivity extends AppCompatActivity {
    private AutoCompleteTextView userNameText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private RoundButton register;
    private RoundButton back;

    private Activity instance;
    private AndroidApplication application;
    private String address;

    private boolean isUserRegistered = false;
    private boolean isPasswordConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);

        instance = this;
        application = (AndroidApplication) instance.getApplication();
        address = application.getHostNPort();

        userNameText = findViewById(R.id.userNameText);
        passwordText = findViewById(R.id.passwordText);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        register = findViewById(R.id.register);
        back = findViewById(R.id.back);

        passwordText.setText("");
        confirmPasswordText.setText("");

        userNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    checkUserName();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });


    }
    @Override
    public void onBackPressed() {
        gotoLogin();
    }
    private boolean checkPasswordConfirm(){
        String password = passwordText.getText().toString();
        String confirmpwd = confirmPasswordText.getText().toString();
        if(!TextUtils.isEmpty(password) &&!TextUtils.isEmpty(confirmpwd))
            return password.equals(confirmpwd);
        else
            return false;
    }
    private void checkUserName() {
        userNameText.setError(null);
        String userName = userNameText.getText().toString();
        Map<String,String> param = new HashMap<>();
        param.put("userName",userName);
        HttpUtil.doPost(address.concat("/user/check-username"), param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject resData = null;

                try {
                    resData = new JSONObject(response.body().string());

                    Boolean isRegistered = resData.getBoolean("data");

                    if (isRegistered) {
                        isUserRegistered = true;
                        instance.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userNameText.setError("用户已注册");
                            }
                        });
                    } else{
                        isUserRegistered = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleRegister(){
        boolean validate = true;
        DBManager dm = application.getDBManager();
        userNameText.setError(null);
        passwordText.setError(null);
        confirmPasswordText.setError(null);

        String userName = userNameText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmpwd = confirmPasswordText.getText().toString();

        if(TextUtils.isEmpty(userName)){
            userNameText.setError("用户名不能为空");
            validate = false;
        }
        if(TextUtils.isEmpty(password)){
            passwordText.setError("密码不能为空");
            validate = false;
        }
        if(TextUtils.isEmpty(confirmpwd)){
            confirmPasswordText.setError("请确认密码");
            validate = false;
        }

        if(!checkPasswordConfirm()){
            confirmPasswordText.setError("两次输入密码不一致");
            validate = false;
        }

        if(validate) {
            register.setEnabled(false);
            register.setText("正在注册并登陆");
            Map<String,String> param = new HashMap<>();
            param.put("userName",userName);
            param.put("userPassword",password);

            HttpUtil.doPost(address.concat("/user/regist-user"), param, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            register.setEnabled(true);
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
                            dm.insertUser(user);
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
                            register.setText("注册并登录");
                            register.setEnabled(true);
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

    private void gotoLogin() {
        Intent toLogin = new Intent(instance, LoginActivity.class);
        toLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toLogin);
        finish();
    }
}

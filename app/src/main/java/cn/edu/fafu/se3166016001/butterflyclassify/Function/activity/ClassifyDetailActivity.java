package cn.edu.fafu.se3166016001.butterflyclassify.Function.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.Family;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ClassifyDetailActivity extends AppCompatActivity {
    TitleBar titleBar;
    TextView order;
    TextView family;
    TextView desc;
    LinearLayout imgContainner;
    private AndroidApplication application;
    public static Activity instance;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_detail);
        instance = this;
        application = (AndroidApplication) instance.getApplication();
        address = application.getHostNPort();
        titleBar = findViewById(R.id.titleBar);
        imgContainner = findViewById(R.id.imgContainner);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.finish();
            }
        });
        String category = instance.getIntent().getStringExtra("category");
        order = findViewById(R.id.order);
        family = findViewById(R.id.family);
        desc = findViewById(R.id.desc);
        String[] categoryPieces = category.split("-");
        order.setText(categoryPieces[0]);
        family.setText(categoryPieces[1]);

        HttpUtil.doGet(address.concat("/insect-classify/family?familyName=").concat(categoryPieces[1]), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                instance.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(instance,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject resData = null;
                try {
                    resData = new JSONObject(response.body().string());
                    JSONObject json = resData.getJSONObject("data");
                    if(json == null ) {
                        instance.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                desc.setText("暂无该分类信息");
                            }
                        });
                        return;
                    }
                    Gson gson = new Gson();
                    Family family = gson.fromJson(json.toString(),Family.class);
                    instance.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("".equals(family.getDes())||family.getDes() == null){
                                desc.setText("暂无该分类信息");
                            }else{
                                desc.setText(family.getDes());
                            }
                            String[] pics = family.getPic().split(";");
                            for(String pic : pics){
                                ImageView img = new ImageView(instance);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(820,820);
                                img.setLayoutParams(params);
                                imgContainner.addView(img);
                                Glide.with(instance).load(address.concat("/").concat(pic)).into(img);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

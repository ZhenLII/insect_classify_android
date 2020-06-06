package cn.edu.fafu.se3166016001.butterflyclassify.Function.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.Insect;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.InsectOrder;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.User;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PlaygroundItemDetail extends AppCompatActivity {
    private AndroidApplication app;
    private Activity instance;
    private InsectOrder temporder;
    private String address;

    private User user;

    private TitleBar titleBar;
    private RadiusImageView userImg;
    private TextView userName;
    private TextView createTime;

    private TextView insectName;
    private TextView insectAlias;
    private TextView insectScientificName;
    private TextView insectCategory;
    private TextView insectDetail;
    private TextView insectFeature;
    private ImageView insectImg;


    private TextView supportCount;
    private TextView supportText;
    private ImageView supportButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground_item_detail);

        instance = this;
        app = (AndroidApplication)instance.getApplication();
        address = app.getHostNPort();

        user = app.getUser();

        titleBar = findViewById(R.id.titleBar);
        userImg = findViewById(R.id.userImg);
        userName = findViewById(R.id.userName);
        createTime = findViewById(R.id.createTime);

        insectName = findViewById(R.id.insectName);
        insectAlias = findViewById(R.id.insectAlias);
        insectScientificName = findViewById(R.id.insectScientificName);
        insectCategory = findViewById(R.id.insectCategory);
        insectDetail = findViewById(R.id.insectDetail);
        insectFeature = findViewById(R.id.insectFeature);
        insectImg = findViewById(R.id.insectImg);

        supportCount = findViewById(R.id.supportCount);
        supportText = findViewById(R.id.supportText);
        supportButton = findViewById(R.id.supportButton);

        Intent intent = instance.getIntent();
        int orderId = intent.getIntExtra("orderId",0);



        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.finish();
                Intent back = new Intent(instance,Playground.class);
                startActivityForResult(back,RESULT_OK);
            }
        });

        getOrder(orderId);

        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!temporder.isHasSupported()){
                    if(temporder.getCreator().equals(user.getUserName()))
                    {
                        Toast.makeText(instance,"不能给自己点赞",Toast.LENGTH_LONG).show();
                        return;
                    }
                    plusSupported(orderId);
                }else minusSupported(orderId);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        instance.finish();
        Intent back = new Intent(instance,Playground.class);
        startActivityForResult(back,RESULT_OK);
        return true;
    }
        return super.onKeyDown(keyCode, event);
    }
    private void plusSupported(int orderId){
        HttpUtil.doGet(address.concat("/insect-order/plus-supported?orderId=").concat(String.valueOf(orderId)), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getOrder(orderId);
            }
        });
    }
    private void minusSupported(int orderId){
        HttpUtil.doGet(address.concat("/insect-order/minus-supported?orderId=").concat(String.valueOf(orderId)), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getOrder(orderId);
            }
        });
    }


    private void getOrder(int orderId){
        HttpUtil.doGet(address.concat("/insect-order/get-order?orderId=").concat(String.valueOf(orderId)), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject resData = new JSONObject(response.body().string());
                    JSONObject resJson= resData.getJSONObject("data");
                    Gson gson = new Gson();
                    InsectOrder order = gson.fromJson(resJson.getJSONObject("order").toString(), InsectOrder.class);
                    Boolean hasSupported = resJson.getBoolean("hasSupported");
                    order.setHasSupported(hasSupported);
                    temporder = order;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUI(order);
                        }
                    });
                }catch (JSONException je){

                }catch (IOException ioe) {

                }
            }
        });
    }
    private void setUI(InsectOrder order) {
        String picPath = address.concat("/");
        if(order.getUserPic()!=null) {
            picPath = picPath.concat(order.getUserPic());
        } else{
            picPath = picPath.concat("defaultUserPic.jpg");
        }

        Glide.with(instance).load(picPath).into(userImg);
        userName.setText(order.getCreator());
        createTime.setText(order.getCreateTimeStr());

        insectName.setText(order.getInsectName());
        insectAlias.setText(order.getInsectAlias());
        insectScientificName.setText(order.getScientificName());

        String category = order.getOrder().concat("-");
        if(order.getFamily()!= null) {
            category=category.concat(order.getFamily());
        }
        if(order.getGenus()!=null) {
            category=category.concat("-").concat(order.getGenus());
        }

        insectCategory.setText(category);
        insectDetail.setText(order.getDescription());
        insectFeature.setText(order.getFeature());
        supportCount.setText(String.valueOf(order.getSupportCount()).concat(" 赞同"));

        String insectPicPath = order.getPic();
        if(insectPicPath!=null && !insectPicPath.equals("")){
            Glide.with(instance).load(
                    address.concat("/upload/")
                        .concat(String.valueOf(order.getOrderId())
                        .concat("/"))
                        .concat(order.getPic())
                    )
                    .into(insectImg);
            insectImg.setVisibility(ImageView.VISIBLE);
        }
        if(order.isHasSupported()) {
            supportButton.setImageResource(R.drawable.supported);
            supportText.setText("您已点赞");
        } else {
            supportButton.setImageResource(R.drawable.unsupported);
            supportText.setText("如果您觉得本条内容不错您可以");
        }
    }
}

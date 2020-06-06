package cn.edu.fafu.se3166016001.butterflyclassify.Home.activity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.MainActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.Insect;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ItemDetail extends AppCompatActivity {

    public static Activity instance;
    TitleBar title;
    SimpleImageBanner itemBanner;

    TextView insctName;
    TextView detailCategory;
    TextView scientificName;
    TextView alias;
    TextView insectDetail;
    TextView updateInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        AndroidApplication application = (AndroidApplication) instance.getApplication();
        setContentView(R.layout.activity_item_detail);
        // 设置标题栏
        title = findViewById(R.id.detailTitleBar);
        title.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ItemDetail.this,MainActivity.class);
//                startActivity(intent);
                instance.finish();
            }
        });
        itemBanner = findViewById(R.id.itemBanner);
        insctName = findViewById(R.id.insctName);
        detailCategory = findViewById(R.id.detailCategory);
        scientificName = findViewById(R.id.scientificName);
        alias = findViewById(R.id.alias);
        insectDetail = findViewById(R.id.insectDetail);
        updateInfoButton = findViewById(R.id.updateInfoButton);

        String address = application.getHostNPort();
        Intent intent = this.getIntent();
        Integer id = intent.getIntExtra("insectID",0);
        updateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateIntent = new Intent(instance,UpdateInsectInfo.class);
                updateIntent.putExtra("insectID", id);
                startActivity(updateIntent);
            }
        });
        if(id != 0) {
            HttpUtil.doGet(address.concat("/insect-info/insect-android?insectId=".concat(String.valueOf(id))), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("resMasg", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    Log.i("http", "sucsess");
                    showInfo(response,true);
                }
            });

        } else {
            String queryKey = intent.getStringExtra("queryKey");
            HttpUtil.doGet(address.concat("/insect-info/internet-search?query=".concat(queryKey)), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("resMasg", e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) {
                    Log.i("http", "sucsess");
                    showInfo(response,false);
                }
            });
        }

        RelativeLayout.LayoutParams RL = (RelativeLayout.LayoutParams) insectDetail.getLayoutParams();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;




    }
    private void showInfo(Response response, Boolean buildPicUrl) {
        try {
            JSONObject resData = new JSONObject(response.body().string());
            JSONObject insectJson = resData.getJSONObject("data");
            Gson gson = new Gson();
            Insect insect = gson.fromJson(insectJson.toString(),Insect.class);
            Log.i("pic",insect.getPic());
            instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<BannerItem> bannerItems = getBannerItem(insect,buildPicUrl);
                    itemBanner.setSource(bannerItems).startScroll();
                    insctName.setText(insect.getInsectName());
                    String category = insect.getOrder().concat("-");
                    if(insect.getFamily()!= null) {
                        category=category.concat(insect.getFamily());
                    }
                    if(insect.getGenus()!=null) {
                        category=category.concat("-").concat(insect.getGenus());
                    }
                    detailCategory.setText(category);
                    if (insect.getScientificName() != null) {
                        scientificName.setText(insect.getScientificName());
                    }else {
                        scientificName.setText(insect.getInsectName());
                    }
                    if (insect.getInsectAlias() != null) {
                        alias.setText(insect.getInsectAlias());
                    }else {
                        alias.setText(insect.getInsectName());
                    }
                    insectDetail.setText(insect.getDescription());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<BannerItem> getBannerItem(Insect insect,Boolean buildPicUrl) {
        instance = this;

        AndroidApplication application = (AndroidApplication) instance.getApplication();
        String[] urls = insect.getPic().split(";");
        String address = application.getHostNPort();
        List<BannerItem> bannerItems = new ArrayList<>();
        for(int i = 0;i<urls.length;i++ ) {
            BannerItem item = new BannerItem();
            if(buildPicUrl)
                item.setImgUrl(address.concat("/").concat(urls[i]));
            else
                item.setImgUrl(urls[i]);
            item.setTitle(insect.getInsectName());
            bannerItems.add(item);
        }
        return bannerItems;
    }
}

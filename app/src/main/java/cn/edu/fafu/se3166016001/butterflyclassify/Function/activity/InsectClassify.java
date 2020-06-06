package cn.edu.fafu.se3166016001.butterflyclassify.Function.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Home.activity.ItemDetail;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wildma.pictureselector.PictureSelector;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class InsectClassify extends AppCompatActivity {

    public static Activity instance;
    TitleBar title;
    ImageView classImg;
    RoundButton classButton;
    String picPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insect_classify);
        instance = this;
        title = findViewById(R.id.titleBar);
        title.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.finish();
            }
        });

        classImg = findViewById(R.id.classImg);
        //所选相册图片的路径(原图/压缩后/剪裁后)
        String albumPath = "";
        //用来转换相机路径用的
        classImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector
                        .create(instance, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(true, 1000, 1000, 1, 1);
            }
        });

        classButton = findViewById(R.id.classButton);
        classButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResDialog(picPath);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/

        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                String picturePath = data.getStringExtra(PictureSelector.PICTURE_PATH);
//                Bitmap bitmap = getLoacalBitmap(picturePath);
//                classImg.setImageBitmap(bitmap);
                classImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                picPath = picturePath;
                Log.i("img",picturePath);
            }
        }
    }
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void getResDialog(String picPath){
        if (picPath == null || picPath =="") return;

        View resView = LayoutInflater.from(instance).inflate(R.layout.classify_info_dialog, null);
        ImageView resImg = resView.findViewById(R.id.resImg);
        TextView resName = resView.findViewById(R.id.resName);
        resName.setText("...加载中...");
        RoundButton resButton = resView.findViewById(R.id.resButton);
        resButton.setVisibility(View.INVISIBLE);
//
//
        resButton.setOnClickListener(null);
//        Glide.with(resView).load("https://img.zcool.cn/community/017c3556c5618032f875520f678eb2.jpg@1280w_1l_2o_100sh.jpg").into(resImg);
        resImg.setImageBitmap(getLoacalBitmap(picPath));
        Activity activity = instance;
        Application application = activity.getApplication();
        String address = ((AndroidApplication) application).getHostNPort();
        OkHttpClient client = HttpUtil.getInstance();
        File mFile = new File(picPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), mFile);

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", mFile.getName(), requestBody)
                .build();
        Request request = new Request.Builder()
                .url(address.concat("/insect-classify/classify"))
                .post(multipartBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("resMasg",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject resData = null;
                try {
                    resData = new JSONObject(response.body().string());
                    String category = resData.getString("data");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resName.setText("分类结果：".concat(category));
                            resButton.setVisibility(View.VISIBLE);
                            resButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(instance, ClassifyDetailActivity.class);
                                    String c = category;
                                    intent.putExtra("category", category);
                                    startActivity(intent);
                                }
                            });
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        new MaterialDialog.Builder(instance)
                .customView(resView, true)
                .title("分类结果")
                .positiveText("OK")
                .show();
    }
}

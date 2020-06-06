package cn.edu.fafu.se3166016001.butterflyclassify.Function.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wildma.pictureselector.PictureSelector;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddInsectInfo extends AppCompatActivity {
    public static Activity instance;
    TitleBar title;
    MaterialEditText insectName;
    MaterialEditText insectAlias;
    MaterialEditText insectScientificName;
    MaterialEditText insectOrder;
    MaterialEditText insectFamily;
    MaterialEditText insectGenus;

    MultiLineEditText insectDetail;
    MultiLineEditText insectFeature;

    ImageView updateImg;
    RoundButton submitButton;

    String address;
    String picPath = null;
    int insectId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_insect_info);

        instance = this;
        AndroidApplication application = (AndroidApplication) instance.getApplication();
        address = application.getHostNPort();

        // 设置标题返回键
        title = findViewById(R.id.updateTitleBar);
        title.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.finish();
            }
        });

        insectName = findViewById(R.id.insectName);
        insectAlias = findViewById(R.id.insectAlias);
        insectScientificName = findViewById(R.id.insectScientificName);
        insectOrder = findViewById(R.id.insectOrder);
        insectFamily = findViewById(R.id.insectFamily);
        insectGenus = findViewById(R.id.insectGenus);
        insectDetail = findViewById(R.id.insectDetail);
        insectFeature = findViewById(R.id.insectFeature);

        updateImg = findViewById(R.id.updateImg);
        submitButton = findViewById(R.id.submitButton);

        updateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector
                        .create(instance, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(true, 1000, 1000, 1, 1);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> param = getParam();
                List<String> filePaths = new ArrayList<>();
                if (picPath != null)
                    filePaths.add(picPath);
                HttpUtil.doPost(address.concat("/insect-order/add-order"), param, filePaths, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("resMasg", e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("http", "sucsess");
                        try {
                            JSONObject resData = new JSONObject(response.body().string());
                            Integer code = resData.getInt("code");
                            if(code == 20000) {
                                instance.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(instance,"请求已提交，管理员近期会进行审批",Toast.LENGTH_LONG);
                                        instance.finish();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                updateImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                picPath = picturePath;
                Log.i("img",picturePath);
            }
        }
    }
    private Map<String, String> getParam() {
        Map<String, String> param = new HashMap<>();
        param.put("insectId",String.valueOf(insectId));
        param.put("insectName", insectName.getEditValue());
        param.put("insectAlias",insectAlias.getEditValue());
        param.put("scientificName",insectScientificName.getEditValue());
        param.put("order",insectOrder.getEditValue());
        param.put("family",insectFamily.getEditValue());
        param.put("genus",insectGenus.getEditValue());
        param.put("description",insectDetail.getContentText());
        param.put("feature",insectFeature.getContentText());
        return param;

    }
}

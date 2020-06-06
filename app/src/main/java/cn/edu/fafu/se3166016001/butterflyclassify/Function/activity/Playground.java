package cn.edu.fafu.se3166016001.butterflyclassify.Function.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.InsectOrder;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Playground extends AppCompatActivity {
    TitleBar titleBar;
//    SearchView search;
    ListView itemFlow;
    AndroidApplication app;
    String address;
    Activity instance;

    PlaygroundItemAdapter playgroundItemAdapter;
    List<InsectOrder> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);
        instance = this;
        app = (AndroidApplication) getApplication();
        address = app.getHostNPort();
//        search = findViewById(R.id.search);
        itemFlow = findViewById(R.id.itemFlow);

        titleBar = findViewById(R.id.titleBar);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.finish();
            }
        });

        orders = new ArrayList<>();

        playgroundItemAdapter = new PlaygroundItemAdapter(instance,orders);

        itemFlow.setAdapter(playgroundItemAdapter);

        getHttpOrderList();

        itemFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(instance, PlaygroundItemDetail.class);
                intent.putExtra("orderId",orders.get(position).getOrderId());
                instance.finish();
                startActivity(intent);
            }
        });
        orders.size();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            getHttpOrderList();
        }
    }
    private void getHttpOrderList() {
        HttpUtil.doGet(address.concat("/insect-order/playgroud-list?pageSize=200&currentPage=1&query="), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                instance.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(instance,"网络请求出错",Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                setUI(response);
            }
        });
    }
    private void setUI(Response response){
        try {
            JSONObject resData = new JSONObject(response.body().string());
            JSONArray orderJsonArray = resData.getJSONArray("data");
            List<InsectOrder> httporders = new ArrayList<>();
            for (int i = 0; i < orderJsonArray.length(); i++) {
                Gson gson = new Gson();
                InsectOrder order = gson.fromJson(orderJsonArray.getJSONObject(i).toString(), InsectOrder.class);
                httporders.add(order);
            }

            instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    orders.clear();
                    orders.addAll(httporders);
                    playgroundItemAdapter.notifyDataSetChanged();
                }
            });

        }catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOExc",e.getMessage());
        }
    }

}

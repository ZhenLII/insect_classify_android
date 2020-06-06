package cn.edu.fafu.se3166016001.butterflyclassify.Home;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Home.activity.ItemDetail;
import cn.edu.fafu.se3166016001.butterflyclassify.Home.adapter.HomeItemAdapter;
import cn.edu.fafu.se3166016001.butterflyclassify.Home.eventListener.SeachListener;

import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.MainActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.Insect;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HomeFragment extends Fragment {
    TitleBar title;
    SearchView search;
    ListView itemFlow;


    HomeItemAdapter homeItemAdapter;
    List<Insect> insects;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        AndroidApplication application = (AndroidApplication) getActivity().getApplication();
        FragmentActivity mainActivity =  getActivity();
        String address = application.getHostNPort();

        // 设置标题
        title = view.findViewById(R.id.titleBar);
        title.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("back:","back");
            }
        });
        // 设置搜索框
        search = view.findViewById(R.id.search);
        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                HttpUtil.doGet(address.concat("/insect-info/list-android?pageSize=1000&currentPage=1&query=".concat(query)), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("resMasg",e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("http","sucsess");
                        getInsectList(response);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                HttpUtil.doGet(address.concat("/insect-info/list-android?pageSize=1000&currentPage=1&query=".concat(newText)), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("resMasg",e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("http","sucsess");
                        getInsectList(response);
                    }
                });
                return false;
            }
        });
        //设置列表布局
        //获得MainActivity
        Resources resources = mainActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        //获得MainActivty中 底部导航栏高度和屏幕高度
        int navHeight = resources.getDimensionPixelSize(resourceId);
        int windowHeight = 1200;

        // 设置适配器
        insects = new ArrayList<>();

        homeItemAdapter = new HomeItemAdapter(mainActivity, insects);
        itemFlow = view.findViewById(R.id.itemFlow);
        itemFlow.setAdapter(homeItemAdapter);
        // 动态设置列表布局
        RelativeLayout.LayoutParams RL = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RL.addRule(RelativeLayout.BELOW, R.id.search);
        RL.setMargins(0,20, 0, navHeight+windowHeight/100);
        itemFlow.setLayoutParams(RL);
        itemFlow.requestLayout();

        //设置点击事件
        itemFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ItemDetail.class);
                intent.putExtra("insectID", insects.get(position).getInsectId());
                startActivity(intent);
            }
        });



        HttpUtil.doGet(address.concat("/insect-info/list-android?query=&pageSize=1000&currentPage=1"), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("resMasg",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("http","sucsess");
                getInsectList(response);
            }
        });


    }
    private void getInsectList(Response response) {
        AndroidApplication application = (AndroidApplication) getActivity().getApplication();
        FragmentActivity mainActivity =  getActivity();
        String address = application.getHostNPort();
        try {
            JSONObject resData = new JSONObject(response.body().string());
            JSONArray insectJsonArray = resData.getJSONArray("data");
            List<Insect> httpinsects = new ArrayList<>();
            for(int i = 0;i< insectJsonArray.length();i++){
                Gson gson = new Gson();
                Insect insect = gson.fromJson(insectJsonArray.getJSONObject(i).toString(),Insect.class);
                insect.setPic(address.concat("/").concat(insect.getPic()));
                httpinsects.add(insect);

            }
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    insects.clear();
                    insects.addAll(httpinsects);
                    homeItemAdapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}

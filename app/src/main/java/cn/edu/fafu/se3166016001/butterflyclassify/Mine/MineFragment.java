package cn.edu.fafu.se3166016001.butterflyclassify.Mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.User;
import cn.edu.fafu.se3166016001.butterflyclassify.Others.AboutUS;
import cn.edu.fafu.se3166016001.butterflyclassify.Others.History;
import cn.edu.fafu.se3166016001.butterflyclassify.Others.Version;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import cn.edu.fafu.se3166016001.butterflyclassify.Start.LoginActivity;

public class MineFragment extends Fragment {

    private RadiusImageView radiusImageView;
    private TextView userName;
    private TextView userIntro;
    private RelativeLayout historyLayout;
    private RelativeLayout aboutAppLayout;
    private RelativeLayout versionLayout;
    private RelativeLayout logoutLayout;

    private Activity activity;
    private  AndroidApplication application;
    private String address;
    public MineFragment() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = this.getActivity();
        application = (AndroidApplication) getActivity().getApplication();
        address = application.getHostNPort();

        radiusImageView = view.findViewById(R.id.userImg);
        radiusImageView.setCircle(true);
        radiusImageView.setBorderWidth(1);
//        radiusImageView.setBorderColor(0x909399);
        userName = view.findViewById(R.id.userName);
        userIntro = view.findViewById(R.id.userIntro);
//        historyLayout = view.findViewById(R.id.historyLayout);
        aboutAppLayout = view.findViewById(R.id.aboutAppLayout);
        versionLayout = view.findViewById(R.id.versionLayout);
        logoutLayout = view.findViewById(R.id.logoutLayout);


        getUserInfo();

//        historyLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), History.class);
//                startActivity(intent);
//            }
//        });
        aboutAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUS.class);
                startActivity(intent);
            }
        });
        versionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Version.class);
                startActivity(intent);
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = application.getUser();
                user.setToken("");
                application.getDBManager().updateUserToken(user);
                gotoLoginActivity();
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }
    private void getUserInfo() {
        User user = application.getUser();
        if ("".equals(user.getPic())||user.getPic() == null) {
            Glide.with(activity).load(address.concat("/defaultUserPic.jpg")).into(radiusImageView);
        } else {
            Glide.with(activity).load(address.concat("/").concat(user.getPic())).into(radiusImageView);
        }

        if ("".equals(user.getNickName())||user.getNickName() == null) {
            userName.setText(user.getUserName());
        } else {
            userName.setText(user.getNickName());
        }

        if("".equals(user.getIntroduce())||user.getIntroduce() == null) {
            userIntro.setText("这个人很懒，什么也没留下");
        } else {
            userIntro.setText(user.getIntroduce());
        }
    }
    private void gotoLoginActivity(){
        Intent toLogin = new Intent(activity, LoginActivity.class);
        toLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toLogin);
        activity.finish();
    }
}

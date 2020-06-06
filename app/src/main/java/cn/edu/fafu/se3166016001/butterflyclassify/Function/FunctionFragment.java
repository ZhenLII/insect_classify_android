package cn.edu.fafu.se3166016001.butterflyclassify.Function;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.fafu.se3166016001.butterflyclassify.Function.activity.AddInsectInfo;
import cn.edu.fafu.se3166016001.butterflyclassify.Function.activity.InsectClassify;
import cn.edu.fafu.se3166016001.butterflyclassify.Function.activity.Playground;
import cn.edu.fafu.se3166016001.butterflyclassify.R;

public class FunctionFragment extends Fragment {
    RelativeLayout classifyInfoLayout;
    RelativeLayout updateInfoLayout;
    RelativeLayout playgroundInfoLayout;
    String picPath = null;
    public FunctionFragment() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        classifyInfoLayout = view.findViewById(R.id.classifyInfoLayout);
        classifyInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToClassify = new Intent(getActivity(),InsectClassify.class);
                startActivity(moveToClassify);
            }
        });
        updateInfoLayout = view.findViewById(R.id.updateInfoLayout);
        updateInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToUpdate = new Intent(getActivity(),AddInsectInfo.class);
                startActivity(moveToUpdate);
            }
        });

        playgroundInfoLayout = view.findViewById(R.id.playgroundInfoLayout);
        playgroundInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToPlayground = new Intent(getActivity(),Playground.class);
                startActivity(moveToPlayground);
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
        return inflater.inflate(R.layout.fragment_function, container, false);
    }

}

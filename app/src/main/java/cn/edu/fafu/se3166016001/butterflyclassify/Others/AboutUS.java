package cn.edu.fafu.se3166016001.butterflyclassify.Others;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xuexiang.xui.widget.actionbar.TitleBar;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.R;

public class AboutUS extends AppCompatActivity {
    public static Activity instance;
    TitleBar title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_about_us);
        title = findViewById(R.id.aboutUsTitleBar);
        title.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.finish();
            }
        });
    }
}

package cn.edu.fafu.se3166016001.butterflyclassify.Others;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xuexiang.xui.widget.actionbar.TitleBar;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.fafu.se3166016001.butterflyclassify.R;

public class Version extends AppCompatActivity {
    public static Activity instance;
    TitleBar title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        instance = this;
        title = findViewById(R.id.veisonTitleBar);
        title.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.finish();
            }
        });
    }
}

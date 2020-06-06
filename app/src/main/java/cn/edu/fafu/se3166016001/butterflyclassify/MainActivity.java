package cn.edu.fafu.se3166016001.butterflyclassify;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wildma.pictureselector.PictureSelector;
import com.xuexiang.xui.XUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.edu.fafu.se3166016001.butterflyclassify.Function.FunctionFragment;
import cn.edu.fafu.se3166016001.butterflyclassify.Home.HomeFragment;
import cn.edu.fafu.se3166016001.butterflyclassify.Mine.MineFragment;


public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    Fragment home = null;
    Fragment func = null;
    Fragment mine = null;
    private List<Fragment> mFragements;
    private int containerId;
    //初始化当前可见页码为0
    private int lastfragment=0;
    public BottomNavigationView navigation;
    public int widthPixels = 0;
    public int heightPixels = 0;
    private void initFragment(){
        home = new HomeFragment();
        func = new FunctionFragment();
        mine = new MineFragment();
        mFragements = new ArrayList<Fragment>();
        mFragements.addAll(Arrays.asList(home,func,mine));
        mFragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.containerFragment, home);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:{
                    if(lastfragment != 0){
                        switchFragment(lastfragment,0);
                        lastfragment = 0;
                    }
                    return true;
                }
                case R.id.navigation_dashboard:
                    if(lastfragment != 1){
                        switchFragment(lastfragment,1);
                        lastfragment = 1;
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(lastfragment != 2){
                        switchFragment(lastfragment,2);
                        lastfragment = 2;
                    }
                    return true;
            }
            return false;
        }
    };
    private void switchFragment(int lastfragment,int index){
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(mFragements.get(lastfragment));//隐藏上个Fragment
        if(mFragements.get(index).isAdded()==false){
            transaction.add(R.id.containerFragment, mFragements.get(index));
        }
        transaction.show(mFragements.get(index)).commitAllowingStateLoss();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        widthPixels = outMetrics.widthPixels;
        heightPixels = outMetrics.heightPixels;
        setContentView(R.layout.activity_main);
        initFragment();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment current = mFragements.get(lastfragment);
        /*结果回调*/
        current.onActivityResult(requestCode, resultCode, data);
    }
}

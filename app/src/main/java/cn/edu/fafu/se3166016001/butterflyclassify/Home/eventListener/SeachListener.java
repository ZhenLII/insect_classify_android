package cn.edu.fafu.se3166016001.butterflyclassify.Home.eventListener;


import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import cn.edu.fafu.se3166016001.butterflyclassify.R;

public class SeachListener implements SearchView.OnQueryTextListener {
    View view;
    SearchView search;
    public SeachListener(@NonNull View view) {
        this.view = view;
        search = view.findViewById(R.id.search);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        search.setQueryHint("现在你提交过搜索了");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("search:","onQueryTextChange");
        return false;
    }

}

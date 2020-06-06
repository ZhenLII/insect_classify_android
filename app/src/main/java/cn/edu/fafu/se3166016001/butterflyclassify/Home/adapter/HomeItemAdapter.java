package cn.edu.fafu.se3166016001.butterflyclassify.Home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.List;

import cn.edu.fafu.se3166016001.butterflyclassify.Model.Insect;

import cn.edu.fafu.se3166016001.butterflyclassify.R;



public class HomeItemAdapter extends BaseAdapter {
    private Context homeContext;
    private View homeFragment;
    private List<Insect> insects;
    public HomeItemAdapter(Context context, List<Insect> insectList){
        this.homeContext = context;
        this.insects = insectList;
    }



    @Override
    public int getCount() {
        return this.insects.size();
    }

    @Override
    public Object getItem(int position) {
        return this.insects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view = LayoutInflater.from(homeContext).inflate(R.layout.home_item_flow, null);
        TextView name = view.findViewById(R.id.listItemName);
        name.setText(this.insects.get(position).getInsectName());
        TextView info = view.findViewById(R.id.listItemInfo);
        info.setText(this.insects.get(position).getDescription());
        RadiusImageView imageView = view.findViewById(R.id.insectListImg);
        imageView.setCircle(true);
        imageView.setBorderWidth(2);
        Glide.with(view).load(insects.get(position).getPic()).into(imageView);
        return view;
    }
}

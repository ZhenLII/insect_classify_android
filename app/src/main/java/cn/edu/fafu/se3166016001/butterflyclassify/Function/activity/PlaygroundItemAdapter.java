package cn.edu.fafu.se3166016001.butterflyclassify.Function.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.DownImage;
import cn.edu.fafu.se3166016001.butterflyclassify.Http.HttpUtil;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.Insect;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.InsectOrder;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.User;
import cn.edu.fafu.se3166016001.butterflyclassify.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PlaygroundItemAdapter extends BaseAdapter {

    private Context context;
    private List<InsectOrder> orders;
    private LayoutInflater mInflater;
    AndroidApplication app = AndroidApplication.getInstance();
    String address = app.getHostNPort();
    public PlaygroundItemAdapter(Context context, List<InsectOrder> orders){
        this.context = context;
        this.orders = orders;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //判断是否有缓存
        if (convertView == null) {
            holder = new ViewHolder();
            //通过LayoutInflater实例化布局
            convertView = mInflater.inflate(R.layout.playgroud_item_layout, null);
            holder.userImg = (RadiusImageView) convertView.findViewById(R.id.userImg);
            holder.userName = (TextView) convertView.findViewById(R.id.userName);
            holder.createTime = (TextView) convertView.findViewById(R.id.createTime);
            holder.insectName = convertView.findViewById(R.id.insectName);
            holder.insectScientificName = convertView.findViewById(R.id.insectScientificName);
            holder.category  =convertView.findViewById(R.id.order_family_genus);
            holder.description = convertView.findViewById(R.id.description);
            holder.supportCount = convertView.findViewById(R.id.supportCount);
            convertView.setTag(holder);
        } else {
            //通过tag找到缓存的布局
            holder = (ViewHolder) convertView.getTag();
        }
        //设置布局中控件要显示的视图


        String userPic = null;
        View finalConvertView = convertView;
        ViewHolder finalHolder = holder;
        String picPath = address;

        InsectOrder order = orders.get(position);
        if(order.getUserPic()!=null) {
            picPath = picPath.concat("/").concat(order.getUserPic());
        } else{
            picPath = picPath.concat("/defaultUserPic.jpg");
        }
        DownImage downImage = new DownImage(picPath);
        downImage.loadImage(new DownImage.ImageCallBack() {
            @Override
            public void getDrawable(Drawable drawable) {
                finalHolder.userImg.setImageDrawable(drawable);
            }
        });
        holder.userName.setText(order.getCreator());
        holder.createTime.setText(order.getCreateTimeStr());
        holder.insectName.setText(order.getInsectName());
        holder.insectScientificName.setText(order.getScientificName());


        String category = order.getOrder().concat("-");
        if(order.getFamily()!= null) {
            category=category.concat(order.getFamily());
        }
        if(order.getGenus()!=null) {
            category=category.concat("-").concat(order.getGenus());
        }

        holder.category.setText(category);
        holder.description.setText(order.getDescription());
        holder.supportCount.setText(String.valueOf(order.getSupportCount()).concat(" 赞同"));
        return convertView;

    }

    public final class ViewHolder {
        public RadiusImageView userImg;
        public TextView userName;
        public TextView createTime;
        public TextView insectName;
        public TextView  insectScientificName;
        public TextView category;
        public TextView description;
        public TextView supportCount;
    }
}

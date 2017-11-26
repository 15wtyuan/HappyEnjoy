package com.example.bill.happyenjoy.activity.homePage;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.model.IssueDate;
import com.example.bill.happyenjoy.model.UserData;
import com.example.bill.happyenjoy.networkTools.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by TT on 2017/11/3.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder>{

    private static final int TYPE_HAVETUPIAN = 1;
    private static final int TYPE_NORMAL = 2;
    private static final int TYPE_SEARCH_BUTTON =3;
    private static final int TYPE_SELECT = 4;

    private List<IssueDate> issueDates = new ArrayList<>();
    private HomePageActivity activity;
    private List<UserData> userDatas = new ArrayList<>();


    public IssueAdapter(List<IssueDate> issueDates,List<UserData> userDatas,HomePageActivity activity){
        this.issueDates = issueDates;
        this.activity = activity;
        this.userDatas = userDatas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView touxiang;
        TextView user_name;
        TextView time;
        TextView kind_name;
        ImageView kind;
        TextView biaoti;
        TextView neirong;
        TextView pinlun_num;
        TextView zan_num;
        ImageView zan;
        TextView price;

        public ViewHolder(View view){
            super(view);
            touxiang = (CircleImageView)view.findViewById(R.id.touxiang);
            user_name = (TextView)view.findViewById(R.id.user_name);
            time = (TextView)view.findViewById(R.id.time);
            kind_name = (TextView)view.findViewById(R.id.kind_name);
            kind = (ImageView)view.findViewById(R.id.kind);
            biaoti = (TextView)view.findViewById(R.id.biaoti);
            neirong = (TextView)view.findViewById(R.id.neirong);
            pinlun_num = (TextView)view.findViewById(R.id.pinlun_num);
            zan_num = (TextView)view.findViewById(R.id.zan_num);
            zan = (ImageView)view.findViewById(R.id.zan);
            price = (TextView)view.findViewById(R.id.price);
        }
    }

    public class SelectButton extends ViewHolder{

        public SelectButton(View view){
            super(view);
        }
    }

    public class SearchButton extends ViewHolder{

        ImageButton searchview_button;
        ImageButton select_all_botton;
        public SearchButton(View view){
            super(view);
            searchview_button = (ImageButton)view.findViewById(R.id.searchview_button);
            select_all_botton = (ImageButton)view.findViewById(R.id.select_all_botton);
        }
    }

    public class NormalViewHolder extends ViewHolder{

        public NormalViewHolder(View view){
            super(view);
        }
    }

    public class TupianViewHolder extends ViewHolder{

        RecyclerView tupian;
        public TupianViewHolder(View view){
            super(view);
            tupian = view.findViewById(R.id.tupian);
        }
    }

    @Override
    public int getItemViewType(int position) {
        IssueDate issueDate = issueDates.get(position);
        if (issueDate.getId()==-10086){
            return TYPE_SEARCH_BUTTON;
        }else if (issueDate.getId()==-10000){
            return TYPE_SELECT;
        }
        if (issueDate.getPicture1().equals("")||issueDate.getPicture1()==null){//判断有没有第一张图片，没有的话就是不带图片的
            return TYPE_NORMAL;
        }else {
            return TYPE_HAVETUPIAN;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HAVETUPIAN:
                viewHolder = new TupianViewHolder(inflater.inflate(R.layout.issue_have_tupian, parent, false));
                break;
            case TYPE_NORMAL:
                viewHolder = new NormalViewHolder(inflater.inflate(R.layout.issue, parent, false));
                break;
            case TYPE_SEARCH_BUTTON:
                viewHolder = new SearchButton(inflater.inflate(R.layout.search_button,parent,false));
                break;
            case TYPE_SELECT:
                viewHolder = new SelectButton(inflater.inflate(R.layout.select_label_memu,parent,false));
        }
        return viewHolder;
    }
    public static String TimeStamp2Date(String timestampString, String formats) {//将Unix时间截转换成能看懂的字符串
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(formats, Locale.CHINA).format(new java.util.Date(timestamp));
        return date;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final IssueDate issueDate = issueDates.get(position);
        final UserData userData = userDatas.get(position);

        if (holder instanceof TupianViewHolder||holder instanceof NormalViewHolder){
            holder.biaoti.setText(issueDate.getTitle());//标题
            holder.neirong.setText(issueDate.getBrief());//内容
            holder.price.setText("￥"+issueDate.getPrice());//价格
            holder.zan_num.setText(Integer.toString(issueDate.getZan()));//赞的个数
            holder.pinlun_num.setText(Integer.toString(issueDate.getPingLun()));//评论的个数
            holder.time.setText(TimeStamp2Date(issueDate.getIssueTime(),"yyyy-MM-dd HH:mm:ss"));//时间

            if (issueDate.getZanStatus()==0){//是否已经点赞，设置点赞的图标
                holder.zan.setImageResource(R.mipmap.zan_no);
            }else {
                holder.zan.setImageResource(R.mipmap.zan_yes);
            }
            holder.zan.setOnClickListener(new View.OnClickListener() {//点赞功能的监听
                @Override
                public void onClick(View view) {
                    if (issueDate.getZanStatus()==0){
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id",Integer.toString(userData.getUid()))
                                .add("issue_id",Integer.toString(issueDate.getId()))
                                .build();
                        HttpUtil.sendOkHttpRequest("http://139.199.202.23/School/public/index.php/index/Issue/newZan",requestBody,new okhttp3.Callback(){
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("test","点赞失败");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("test","点赞成功");
                                issueDate.setZanStatus(1);
                                issueDate.setZan(issueDate.getZan()+1);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyItemChanged(position);
                                    }
                                });
                            }
                        });
                    }else {
                        RequestBody requestBody = new FormBody.Builder()
                                .add("user_id",Integer.toString(userData.getUid()))
                                .add("issue_id",Integer.toString(issueDate.getId()))
                                .build();
                        HttpUtil.sendOkHttpRequest("http://139.199.202.23/School/public/index.php/index/Issue/deleteZan",requestBody,new okhttp3.Callback(){
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("test","取消赞失败");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("test","取消赞成功");
                                issueDate.setZanStatus(0);
                                issueDate.setZan(issueDate.getZan()-1);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyItemChanged(position);
                                    }
                                });
                            }
                        });
                    }
                }
            });

            String temp = "";//防止头像的url为空
            try{
                temp = userData.getImage();
            }catch (Exception e) {
                e.printStackTrace();
            }
            Glide//加载头像
                    .with(activity)
                    .load(temp)
                    .centerCrop()
                    .placeholder(R.drawable.jiazai)
                    .into(holder.touxiang);

            holder.user_name.setText(userData.getFlowerName());//设置标签
            if (issueDate.getLabel().equals("11")){
                holder.kind_name.setText("拼车");
                holder.kind.setImageResource(R.mipmap.pinche);
            }else if (issueDate.getLabel().equals("12")){
                holder.kind_name.setText("跑腿");
                holder.kind.setImageResource(R.mipmap.paotui);
            }else if (issueDate.getLabel().equals("13")){
                holder.kind_name.setText("寻物");
                holder.kind.setImageResource(R.mipmap.xunwu);
            }else if (issueDate.getLabel().equals("14")){
                holder.kind_name.setText("其他");
                holder.kind.setImageResource(R.mipmap.qita);
            }else if (issueDate.getLabel().equals("21")){
                holder.kind_name.setText("英语");
                holder.kind.setImageResource(R.mipmap.yingyu);
            }else if (issueDate.getLabel().equals("22")){
                holder.kind_name.setText("社科");
                holder.kind.setImageResource(R.mipmap.sheke);
            }else if (issueDate.getLabel().equals("31")){
                holder.kind_name.setText("数码");
                holder.kind.setImageResource(R.mipmap.shuma);
            }else if (issueDate.getLabel().equals("32")){
                holder.kind_name.setText("居家");
                holder.kind.setImageResource(R.mipmap.jujia);
            }else if (issueDate.getLabel().equals("33")){
                holder.kind_name.setText("图书");
                holder.kind.setImageResource(R.mipmap.tushu);
            }else if (issueDate.getLabel().equals("34")){
                holder.kind_name.setText("其他");
                holder.kind.setImageResource(R.mipmap.xianzhi_qita);
            }
        }

        if(holder instanceof TupianViewHolder){//如果有图片的话，设置图片的列表
            final List<String> tupianURLs = new ArrayList<>();
            if (!issueDate.getPicture1().equals("")){
                tupianURLs.add(issueDate.getPicture1());
            }
            if (!issueDate.getPicture2().equals("")){
                tupianURLs.add(issueDate.getPicture2());
            }
            if (!issueDate.getPicture3().equals("")){
                tupianURLs.add(issueDate.getPicture3());
            }
            if (!issueDate.getPicture4().equals("")){
                tupianURLs.add(issueDate.getPicture4());
            }
            if (!issueDate.getPicture5().equals("")){
                tupianURLs.add(issueDate.getPicture5());
            }
            if (!issueDate.getPicture6().equals("")){
                tupianURLs.add(issueDate.getPicture6());
            }
            if (!issueDate.getPicture7().equals("")){
                tupianURLs.add(issueDate.getPicture7());
            }
            if (!issueDate.getPicture8().equals("")){
                tupianURLs.add(issueDate.getPicture8());
            }
            if (!issueDate.getPicture9().equals("")){
                tupianURLs.add(issueDate.getPicture9());
            }
            final TupianViewHolder tupianViewHolder = (TupianViewHolder)holder;
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            tupianViewHolder.tupian.setLayoutManager(layoutManager);
            ImageAdapter imageAdapter = new ImageAdapter(tupianURLs,activity);
            tupianViewHolder.tupian.setAdapter(imageAdapter);
        }
        if (holder instanceof SearchButton){
            final SearchButton searchButton = (SearchButton)holder;
            searchButton.searchview_button.setOnClickListener(new View.OnClickListener() {//设置点击搜索按钮的监听
                @Override
                public void onClick(View view) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("test","点击了搜索按钮");
                            activity.openSearch();
                        }
                    });
                }
            });

            searchButton.select_all_botton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (issueDates.get(1).getId()!=-10000){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.addSelectMune();
                            }
                        });
                    }else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.removeMune();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return issueDates.size();
    }
}

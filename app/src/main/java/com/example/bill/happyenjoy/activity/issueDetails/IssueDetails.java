package com.example.bill.happyenjoy.activity.issueDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.activity.homePage.ImageAdapter;
import com.example.bill.happyenjoy.activity.homePage.IssueAdapter;
import com.example.bill.happyenjoy.model.IssueDate;
import com.example.bill.happyenjoy.model.IssueDetailsJson;
import com.example.bill.happyenjoy.model.UserData;
import com.example.bill.happyenjoy.model.UserDataJson;
import com.example.bill.happyenjoy.networkTools.HttpUtil;
import com.example.bill.happyenjoy.view.ToolBarHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IssueDetails extends BaseActivity {

    private String issueId = null;
    private String typeString = null;
    private IssueDate issueDate = null;
    private String userId = null;
    private UserData userData = null;

    private CircleImageView touxiang;
    private TextView user_name;
    private TextView time;
    private TextView kind_name;
    private ImageView kind;
    private TextView biaoti;
    private TextView neirong;
    private TextView pinlun_num;
    private TextView zan_num;
    private ImageView zan;
    private TextView price;

    private RecyclerView tupian;
    private IssueDetails activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        issueId = intent.getStringExtra("id");//读取issue的id
        userId = intent.getStringExtra("useId");
        Log.d("test","issue id 为"+issueId);
        typeString = intent.getStringExtra("type");//读取issue的类型，看看是不是带图片的
        activity = this;
        int typeInt = Integer.parseInt(typeString);//将读取的字符串“类型”变成整形“类型”
        if (typeInt == IssueAdapter.TYPE_NORMAL){
            setContentView(R.layout.activity_issue_details);
        }else if (typeInt == IssueAdapter.TYPE_HAVETUPIAN){
            setContentView(R.layout.issue_details_have_tupian);
            tupian = findViewById(R.id.tupian);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.white_toolbar);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("发布详情");
        toolbar = toolbarHelper.getToolbar();
        setSupportActionBar(toolbar);

        touxiang = (CircleImageView)findViewById(R.id.touxiang);//绑定各组件
        user_name = (TextView)findViewById(R.id.user_name);
        time = (TextView)findViewById(R.id.time);
        kind_name = (TextView)findViewById(R.id.kind_name);
        kind = (ImageView)findViewById(R.id.kind);
        biaoti = (TextView)findViewById(R.id.biaoti);
        neirong = (TextView)findViewById(R.id.neirong);
        pinlun_num = (TextView)findViewById(R.id.pinlun_num);
        zan_num = (TextView)findViewById(R.id.zan_num);
        zan = (ImageView)findViewById(R.id.zan);
        price = (TextView)findViewById(R.id.price);

        initIssueDate();
    }

    private void initIssueDate(){//从网络获取资料，这部分主要是获取用户的信息
        if (userId !=null){
            RequestBody requestBody = new FormBody.Builder()
                    .add("id",userId)
                    .build();
            HttpUtil.sendOkHttpRequest("http://139.199.202.23/School/public/index.php/index/User/personalInformation",requestBody,new okhttp3.Callback(){
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    UserDataJson userDataJson = gson.fromJson(responseData, UserDataJson.class);//使用Gson处理获取的json
                    if(userDataJson.getMessage().equals("success")){
                        userData = userDataJson.getData();
                        if (userData != null){
                            Log.d("test","读取用户成功");
                        }
                    }
                    initIssueDate2();//从网络获取资料2，这部分主要是获取issue的信息
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    linkFailure();
                }
            });
        }
    }

    private void initIssueDate2(){//从网络获取资料，分两部分是因为合在一起有点乱
        if (issueId != null){
            RequestBody requestBody = new FormBody.Builder()
                    .add("id",issueId)
                    .build();
            HttpUtil.sendOkHttpRequest("http://139.199.202.23/School/public/index.php/index/Issue/issueInformation",requestBody,new okhttp3.Callback(){
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    IssueDetailsJson issueDetailsJson = gson.fromJson(responseData, IssueDetailsJson.class);//使用Gson处理获取的json
                    if (issueDetailsJson.getMessage().equals("success")){
                        issueDate = issueDetailsJson.getData();
                        if (issueDate != null){
                            Log.d("test","读取成功");
                        }
                    }
                    if (issueDate != null){
                        addData();//更新ui
                    }else {
                        Log.d("test","失败");
                    }
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    linkFailure();
                }
            });
        }
    }

    private void linkFailure(){//网络错误后
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToase("链接失败");
            }
        });
    }

    private static String TimeStamp2Date(String timestampString, String formats) {//将Unix时间截转换成能看懂的字符串
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(formats, Locale.CHINA).format(new java.util.Date(timestamp));
        return date;
    }

    private void addData(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                biaoti.setText(issueDate.getTitle());//标题
                neirong.setText(issueDate.getBrief());//内容
                price.setText("￥"+issueDate.getPrice());//价格
                zan_num.setText(Integer.toString(issueDate.getZan()));//赞的个数
                pinlun_num.setText(Integer.toString(issueDate.getPingLun()));//评论的个数
                time.setText(TimeStamp2Date(issueDate.getIssueTime(),"yyyy-MM-dd HH:mm:ss"));//时间
                if (issueDate.getZanStatus()==0){//是否已经点赞，设置点赞的图标
                    zan.setImageResource(R.mipmap.zan_no);
                }else {
                    zan.setImageResource(R.mipmap.zan_yes);
                }
                zan.setOnClickListener(new View.OnClickListener() {//点赞功能的监听
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
                        .with(getBaseContext())
                        .load(temp)
                        .centerCrop()
                        .placeholder(R.drawable.jiazai)
                        .into(touxiang);

                user_name.setText(userData.getFlowerName());//设置标签
                if (issueDate.getLabel().equals("11")){
                    kind_name.setText("拼车");
                    kind.setImageResource(R.mipmap.pinche);
                }else if (issueDate.getLabel().equals("12")){
                    kind_name.setText("跑腿");
                    kind.setImageResource(R.mipmap.paotui);
                }else if (issueDate.getLabel().equals("13")){
                    kind_name.setText("寻物");
                    kind.setImageResource(R.mipmap.xunwu);
                }else if (issueDate.getLabel().equals("14")){
                    kind_name.setText("其他");
                    kind.setImageResource(R.mipmap.qita);
                }else if (issueDate.getLabel().equals("21")){
                    kind_name.setText("英语");
                    kind.setImageResource(R.mipmap.yingyu);
                }else if (issueDate.getLabel().equals("22")){
                    kind_name.setText("社科");
                    kind.setImageResource(R.mipmap.sheke);
                }else if (issueDate.getLabel().equals("31")){
                    kind_name.setText("数码");
                    kind.setImageResource(R.mipmap.shuma);
                }else if (issueDate.getLabel().equals("32")){
                    kind_name.setText("居家");
                    kind.setImageResource(R.mipmap.jujia);
                }else if (issueDate.getLabel().equals("33")){
                    kind_name.setText("图书");
                    kind.setImageResource(R.mipmap.tushu);
                }else if (issueDate.getLabel().equals("34")){
                    kind_name.setText("其他");
                    kind.setImageResource(R.mipmap.xianzhi_qita);
                }

                int typeInt = Integer.parseInt(typeString);//将读取的字符串“类型”变成整形“类型”
                if (typeInt == IssueAdapter.TYPE_HAVETUPIAN){
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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    tupian.setLayoutManager(layoutManager);
                    ImageAdapter imageAdapter = new ImageAdapter(tupianURLs,activity);
                    tupian.setAdapter(imageAdapter);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm != null && fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

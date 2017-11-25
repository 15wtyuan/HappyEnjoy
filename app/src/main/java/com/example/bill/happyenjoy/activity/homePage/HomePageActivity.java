package com.example.bill.happyenjoy.activity.homePage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.ActivityCollector;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.activity.login.MainActivity;
import com.example.bill.happyenjoy.activity.publish.EditActivity;
import com.example.bill.happyenjoy.model.IssueDate;
import com.example.bill.happyenjoy.model.IssueDateJson;
import com.example.bill.happyenjoy.model.UserData;
import com.example.bill.happyenjoy.model.UserLoginData;
import com.example.bill.happyenjoy.networkTools.HttpUtil;
import com.example.bill.happyenjoy.showimagesdialogdemo.base.Config;
import com.example.bill.happyenjoy.view.ToolBarHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomePageActivity extends BaseActivity {


    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private SearchDialog searchDialog;
    private Boolean isSearch = false;
    private BoomMenuButton bmb;

    private int uid;//存取用户的id
    private int i;//提取issue 用；api有要求
    private List<IssueDate> issueDates = new ArrayList<>();
    private List<UserData> userDatas = new ArrayList<>();

    private SpringView springView;//上拉下拉

    private IssueAdapter issueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getDeviceDensity();
        initBoomMenuButton();//悬浮按钮 添加 的初始化

        RecyclerView issueList = (RecyclerView)findViewById(R.id.issueList);//消息列表
        LinearLayoutManager layoutManagerIssue = new LinearLayoutManager(this);
        issueAdapter = new IssueAdapter(issueDates,userDatas,this);
        issueList.setLayoutManager(layoutManagerIssue);
        issueList.setAdapter(issueAdapter);
        initIssueDate();//初始化首页前十条内容
        ((SimpleItemAnimator)issueList.getItemAnimator()).setSupportsChangeAnimations(false);//取消item变化的动画，不取消的话更新会闪烁
        issueList.addOnScrollListener(new RecyclerViewScrollListener() {//消息列表上滑悬浮按钮消失，下滑时出现
            @Override
            public void hide() {
                bmb.animate().translationY(1000).setInterpolator(new AccelerateDecelerateInterpolator());
            }

            @Override
            public void show() {
                bmb.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator());
            }
        });


        springView = (SpringView) findViewById(R.id.springview);//上拉下拉刷新
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {//下拉刷新
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                        initIssueDate();
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {//下拉加载更多
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                        addIssueDate();
                    }
                }, 2000);
            }
        });
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));

        searchDialog = new SearchDialog(this);

        toolbar = (Toolbar) findViewById(R.id.green_toolbar_homepage);//标题栏的绑定
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("乐享校园");
        toolbar = toolbarHelper.getToolbar();
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.home_page);//右滑菜单
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.my_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DrawerAdapter adapter = new DrawerAdapter(this);
        adapter.setOnItemClickListener(new MyOnItemClickListener());
        recyclerView.setAdapter(adapter);

    }

    private void initIssueDate(){
        issueDates.clear();
        List<UserData> userDatastemp = DataSupport.findAll(UserData.class);//获取用户数据，在这里用来获取用户id
        UserData userData = new UserData();
        for (UserData temp:userDatastemp){
            userData = temp;
        }
        uid = userData.getUid();//获取用户id
        i = 0;
        IssueDate search_button = new IssueDate();
        search_button.setId(-10086);
        issueDates.add(0,search_button);
        UserData search_button2 = new UserData();
        search_button2.setUid(-10086);
        userDatas.add(0,search_button2);
        addIssueDate();
    }

    /**
     * 获取当前设备的屏幕密度等基本参数
     */
    protected void getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Config.EXACT_SCREEN_HEIGHT = metrics.heightPixels;
        Config.EXACT_SCREEN_WIDTH = metrics.widthPixels;
    }

    private void addIssueDate(){//往issue添加数据
        RequestBody requestBody = new FormBody.Builder()
                .add("label","0")
                .add("i",Integer.toString(i))
                .add("user_id",Integer.toString(uid))
                .build();
        HttpUtil.sendOkHttpRequest("http://139.199.202.23/School/public/index.php/index/Issue/showIssue",requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                parseIssueJSON(responseData);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                linkFailure();
            }
        });
    }

    private void linkFailure(){//网络错误后
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToase("链接失败");
            }
        });
    }

    private void parseIssueJSON(final String responseData){//处理获取的json
        Gson gson = new GsonBuilder().serializeNulls().create();
        IssueDateJson issueDateJson = gson.fromJson(responseData, IssueDateJson.class);
        if (issueDateJson.getMessage().equals("success")){
            issueDates.addAll(issueDateJson.getData().getData());
            userDatas.addAll(issueDateJson.getData().getUser());
            i = issueDateJson.getData().getI();
        }
        Log.d("test",Integer.toString(issueDates.size()));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                issueAdapter.notifyDataSetChanged();
            }
        });
    }


    public void openSearch(){
        searchDialog.show();
        isSearch = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isSearch){
            isSearch = false;
            return false;
        }else {
            ActivityCollector.finishAll();
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initBoomMenuButton(){
        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            //Toast.makeText(TextOutsideCircleButtonActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .shadowEffect(false)
                    .normalColor(Color.parseColor("#00000000"))
                    .normalImageRes(addImageRes(i))
                    .normalText(addTextRes(i));
            bmb.addBuilder(builder);
        }
        bmb.setButtonRadius(Util.dp2px(30));
        bmb.setNormalColor(getResources().getColor(R.color.colorPrimary));
        bmb.setUnableColor(getResources().getColor(R.color.orange));
        bmb.setHighlightedColor(getResources().getColor(R.color.orange));
        bmb.setDotRadius(0);
        bmb.setUse3DTransformAnimation(true);
        bmb.setShadowEffect(true);
        bmb.setShadowColor(Color.parseColor("#55000000"));
        bmb.setShadowOffsetX(-4);
        bmb.setShadowOffsetY(4);
        bmb.setShadowRadius(4);
    }

    private int addImageRes(int i){
        switch (i){
            case 0:
                return R.mipmap.add_digital_button_icon;
            case 1:
                return R.mipmap.add_book_button_icon;
            case 2:
                return R.mipmap.add_rest_button_icon;
        }
        return R.mipmap.add_digital_button_icon;
    }

    private String addTextRes(int i){
        switch (i){
            case 0:
                return "数码/居家/图书";
            case 1:
                return "英语/社科";
            case 2:
                return "拼车/跑腿/寻物/其他";
        }
        return "";
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alert_item,menu);
        return true;
    }

    public class MyOnItemClickListener implements DrawerAdapter.OnItemClickListener {

        @Override
        public void itemClick(DrawerAdapter.DrawerItemNormal drawerItemNormal) {
            switch (drawerItemNormal.name) {
                case "我的发布":
                    Intent intent2 = new Intent(HomePageActivity.this,EditActivity.class);
                    startActivity(intent2);

                    break;
                case "我的消息":
                    showToase("我的消息");
                    break;
                case "我的收藏":
                    showToase("我的收藏");
                    break;
                case "设置":
                    showToase("设置");
                    List<UserLoginData> userLoginDatas = DataSupport.findAll(UserLoginData.class);
                    UserLoginData userLoginData = new UserLoginData();
                    for(UserLoginData temp:userLoginDatas){
                        userLoginData = temp;
                    }
                    userLoginData.setPassword("0");
                    userLoginData.setPhoneNumber("0");
                    userLoginData.save();
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.alert_button:
                showToase("新消息");
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
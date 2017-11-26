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
import com.example.bill.happyenjoy.activity.publish.IdleEditActivity;
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
                bmb.animate().translationY(250).setInterpolator(new AccelerateDecelerateInterpolator());
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

        searchDialog = new SearchDialog(this);//初始化搜索Dialog

        toolbar = (Toolbar) findViewById(R.id.green_toolbar_homepage);//标题栏的绑定
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("乐享校园");
        toolbar = toolbarHelper.getToolbar();
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.home_page);//右滑菜单
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.my_item);//右滑菜单用RecyclerView实现
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DrawerAdapter adapter = new DrawerAdapter(this);
        adapter.setOnItemClickListener(new MyOnItemClickListener());
        recyclerView.setAdapter(adapter);

    }

    private void initIssueDate(){
        issueDates.clear();//清空所有消息
        List<UserData> userDatastemp = DataSupport.findAll(UserData.class);//获取用户数据，在这里用来获取用户id
        UserData userData = new UserData();
        for (UserData temp:userDatastemp){
            userData = temp;
        }
        uid = userData.getUid();//获取用户id
        i = 0;//初始化i
        IssueDate search_button = new IssueDate();//第一个被设置为搜索按钮
        search_button.setId(-10086);
        issueDates.add(0,search_button);
        UserData search_button2 = new UserData();//第一个被设置为搜索按钮
        search_button2.setUid(-10086);
        userDatas.add(0,search_button2);
        addIssueDate();//添加10条信息
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

    private void addIssueDate(){//往issue添加10条数据
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
        IssueDateJson issueDateJson = gson.fromJson(responseData, IssueDateJson.class);//使用Gson处理获取的json
        if (issueDateJson.getMessage().equals("success")){
            issueDates.addAll(issueDateJson.getData().getData());
            userDatas.addAll(issueDateJson.getData().getUser());
            i = issueDateJson.getData().getI();
        }
        Log.d("test",Integer.toString(issueDates.size()));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                issueAdapter.notifyDataSetChanged();//更新issue的RecyclerView
            }
        });
    }


    public void openSearch(){//打开搜索Dialog
        searchDialog.show();
        isSearch = true;
    }

    public void addSelectMune(){
        UserData selectMune1 = new UserData();
        selectMune1.setUid(-10000);
        userDatas.add(1,selectMune1);
        IssueDate selectMune2 = new IssueDate();
        selectMune2.setId(-10000);
        issueDates.add(1,selectMune2);
        issueAdapter.notifyItemInserted(1);
    }

    public void removeMune(){
        userDatas.remove(1);
        issueDates.remove(1);
        issueAdapter.notifyItemRemoved(1);
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

    private void initBoomMenuButton(){//初始化悬浮按钮
        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {//添加子按钮
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {//子按钮的监听
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            //Toast.makeText(TextOutsideCircleButtonActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                            switch (index){
                                case 0:
                                   Intent intent = new Intent(HomePageActivity.this,IdleEditActivity.class);
                                   startActivity(intent);
                                   break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                            }


                        }
                    })
                    .shadowEffect(false)//子按钮的阴影
                    .normalColor(Color.parseColor("#00000000"))
                    .normalImageRes(addImageRes(i))//子按钮的背景图片
                    .normalText(addTextRes(i));//子按钮的文字描述
            bmb.addBuilder(builder);
        }
        bmb.setButtonRadius(Util.dp2px(30));
        bmb.setNormalColor(getResources().getColor(R.color.colorPrimary));
        bmb.setUnableColor(getResources().getColor(R.color.orange));
        bmb.setHighlightedColor(getResources().getColor(R.color.orange));
        bmb.setDotRadius(0);
        bmb.setUse3DTransformAnimation(true);
        bmb.setShadowEffect(true);//设置大悬浮按钮的阴影是否显示
        bmb.setShadowColor(Color.parseColor("#55000000"));//大悬浮按钮阴影颜色
        bmb.setShadowOffsetX(-4);//阴影偏移
        bmb.setShadowOffsetY(4);
        bmb.setShadowRadius(4);//阴影的圆角
    }

    private int addImageRes(int i){//悬浮按钮的子按钮背景图片
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

    private String addTextRes(int i){//悬浮按钮的子按钮文字描述
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
    public boolean onCreateOptionsMenu(Menu menu) {//新消息按钮
        getMenuInflater().inflate(R.menu.alert_item,menu);
        return true;
    }

    public class MyOnItemClickListener implements DrawerAdapter.OnItemClickListener {//侧滑菜单的item 监听

        @Override
        public void itemClick(DrawerAdapter.DrawerItemNormal drawerItemNormal) {
            switch (drawerItemNormal.name) {
                case "我的发布":
                    Intent intent2 = new Intent(HomePageActivity.this,IdleEditActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {//导航栏菜单的监听
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);//打开侧滑菜单
                break;
            case R.id.alert_button://新消息
                showToase("新消息");
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
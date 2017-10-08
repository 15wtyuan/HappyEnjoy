package com.example.bill.happyenjoy.activity.homePage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.ActivityCollector;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.activity.login.MainActivity;
import com.example.bill.happyenjoy.activity.publish.EditActivity;
import com.example.bill.happyenjoy.bakerj.backgroundblurpopupwindow.BackgroundBlurPopupWindow;
import com.example.bill.happyenjoy.model.UserLoginData;
import com.example.bill.happyenjoy.view.ToolBarHelper;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

import org.litepal.crud.DataSupport;

import java.util.List;

public class HomePageActivity extends BaseActivity {

    private BackgroundBlurPopupWindow mPopupWindow;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private View search;
    private View searchMune;
    private TextView mTextView;
    private Boolean isSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        search = (RelativeLayout)findViewById(R.id.search);
        searchMune = (RelativeLayout)findViewById(R.id.search_mune);

        toolbar = (Toolbar) findViewById(R.id.green_toolbar_homepage);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("乐享校园");
        toolbar = toolbarHelper.getToolbar();
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.home_page);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.my_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DrawerAdapter adapter = new DrawerAdapter();
        adapter.setOnItemClickListener(new MyOnItemClickListener());
        recyclerView.setAdapter(adapter);

        ImageButton searchViewButton = (ImageButton)findViewById(R.id.searchview_button);
        searchViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        initBoomMenuButton();

        mTextView = new TextView(this);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        mTextView.setPadding(10, 10, 10, 10);
        mTextView.setGravity(Gravity.CENTER);
        mPopupWindow = new BackgroundBlurPopupWindow(mTextView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, this, true);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private void openSearch(){
        toolbar.setVisibility(View.INVISIBLE);
        search.setVisibility(View.VISIBLE);
        searchMune.setVisibility(View.INVISIBLE);
        mPopupWindow.setBlurRadius(BackgroundBlurPopupWindow.DEFAULT_BLUR_RADIUS);
        mPopupWindow.setDownScaleFactor(BackgroundBlurPopupWindow.DEFAULT_BLUR_DOWN_SCALE_FACTOR);
        mPopupWindow.setDarkColor(Color.parseColor("#a0000000"));
        mPopupWindow.resetDarkPosition();
        mPopupWindow.darkBelow(search);
        mPopupWindow.showAsDropDown(search, Gravity.CENTER, 0);
        isSearch = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isSearch){
            isSearch = false;
            mPopupWindow.dismiss();
            toolbar.setVisibility(View.VISIBLE);
            search.setVisibility(View.INVISIBLE);
            searchMune.setVisibility(View.VISIBLE);
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initBoomMenuButton(){
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
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
                    //.shadowRadius(Util.dp2px(36))
                    //.maxLines(2)
                    .normalColor(Color.parseColor("#00000000"))
                    //.imagePadding(new Rect(1, 1, 1, 1))
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

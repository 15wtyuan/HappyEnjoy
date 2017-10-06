package com.example.bill.happyenjoy.activity.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.ActivityCollector;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.activity.login.MainActivity;
import com.example.bill.happyenjoy.model.UserLoginData;
import com.example.bill.happyenjoy.view.ToolBarHelper;

import org.litepal.crud.DataSupport;

import java.util.List;

public class HomePageActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.green_toolbar_homepage);
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
                    showToase("我的发布");
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

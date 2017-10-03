package com.example.bill.happyenjoy.activity.homePage;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.view.ToolBarHelper;

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
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}

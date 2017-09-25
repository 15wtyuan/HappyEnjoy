package com.example.bill.happyenjoy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bill.happyenjoy.view.ToolBarHelper;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by bill on 2017/9/3.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SHOW_RESPONSE=0;//用于更新操作
    public TextView responseText;

    //用于处理和发送消息的Hander


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("登录");
        toolbar = toolbarHelper.getToolbar();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Button login = (Button) findViewById(R.id.login_login_Button);
        TextView forget_text = (TextView)findViewById(R.id.textView03) ;
        //responseText=(TextView)findViewById(R.id.response_text);
        login.setOnClickListener(this);
        forget_text.setOnClickListener(this);
    }


    //设置组件的点击属性
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login_login_Button){
            Toast.makeText(this,"你点了登录",Toast.LENGTH_SHORT).show();
        }

        if(v.getId()==R.id.textView03){
            Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(intent);
        }
    }

//设置组件的点击属性


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

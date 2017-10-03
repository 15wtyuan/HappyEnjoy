package com.example.bill.happyenjoy;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bill.happyenjoy.view.ToolBarHelper;

import static com.example.bill.happyenjoy.R.id.forget_ensure_button;

/**
 * Created by bill on 2017/9/24.
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("忘记密码");
        toolbar = toolbarHelper.getToolbar();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Button ensure = (Button)findViewById(forget_ensure_button);
        Button send_code = (Button)findViewById(R.id.forget_send_code);


        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"你点了确定",Toast.LENGTH_SHORT).show();
            }
        });


        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"你点了发送验证码",Toast.LENGTH_SHORT).show();
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

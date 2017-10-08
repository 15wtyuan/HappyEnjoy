package com.example.bill.happyenjoy.activity.publish;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;

/**
 * Created by bill on 2017/10/7.
 */

public class EditActivity extends BaseActivity {



    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
    }





}

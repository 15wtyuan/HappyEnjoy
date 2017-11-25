package com.example.bill.happyenjoy.activity.homePage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.showimagesdialogdemo.base.Config;

/**
 * Created by TT on 2017/11/25.
 */

public class SearchDialog extends Dialog {

    private Context context;
    private View mView;
    private EditText mEditText;

    public SearchDialog(@NonNull Context context){
        super(context, R.style.transparentBgDialog);
        this.context= context;
        initView();
    }

    private void initView() {
        mView = View.inflate(context, R.layout.search_dialog, null);
        mEditText =(EditText)mView.findViewById(R.id.search_button);
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.height = Config.EXACT_SCREEN_HEIGHT;
        wl.width = Config.EXACT_SCREEN_WIDTH;
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }
}

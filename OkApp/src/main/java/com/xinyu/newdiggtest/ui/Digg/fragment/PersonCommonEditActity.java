package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.FormatUtil;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonCommonEditActity extends BaseNoEventActivity {

    @BindView(R.id.et_name)
    public EditText grname;


    @BindView(R.id.tv_title)
    public TextView tv_title;


    Context context;

    String emptyStr = "";
    String type = "-1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initView();
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_edit_name;
    }

    private void initView() {
        
        initItentData();

        String content = getIntent().getStringExtra(IntentParams.GROUP_NAME);
        grname.setText(content);
        grname.setSelection(content.length());//将光标移至文字末尾
        grname.setFocusable(true);
        grname.setFocusableInTouchMode(true);
        grname.requestFocus();//获取焦点 光标出现

    }

    private void initItentData() {

        type = getIntent().getStringExtra(IntentParams.Edit_Type);
        switch (type) {

            case "1":
                tv_title.setText("设置昵称");
                emptyStr = "昵称";
                break;

            case "2":
                tv_title.setText("设置手机号码");
                emptyStr = "手机号码";
                break;

            case "3":
                tv_title.setText("设置邮箱");
                emptyStr = "邮箱";
                break;


        }
    }


    @OnClick(R.id.tv_save)
    public void save() {
        editGrName();
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }


    private void editGrName() {

        if (TextUtils.isEmpty(grname.getText().toString().trim())) {
            ToastUtils.getInstanc(this).showToast(emptyStr + "不能为空,请重新输入!");
            return;
        }

        switch (type) {
            case "1":
                Intent intent = new Intent();
                intent.putExtra("IntentData", grname.getText().toString().trim());
                setResult(1, intent);
                finish();
                break;

            case "2":

                boolean isPhone = checkIfPhone();
                if (!isPhone) {
                    ToastUtils.getInstanc().showToast("请输入合法的手机号码!");
                    return;
                }

                Intent intent1 = new Intent();
                intent1.putExtra("IntentData", grname.getText().toString().trim());
                setResult(2, intent1);
                finish();


                break;

            case "3":
                boolean isMail = checkIfMail();

                if (!isMail) {
                    ToastUtils.getInstanc().showToast("请输入合法的邮箱地址!");
                    return;

                }
                Intent intent2 = new Intent();
                intent2.putExtra("IntentData", grname.getText().toString().trim());
                setResult(3, intent2);
                finish();

                break;
        }


    }


    /**
     * 检查格式
     *
     * @return
     */
    private boolean checkIfMail() {

        if (FormatUtil.isEmail(grname.getText().toString().trim())) {
            return true;
        }
        return false;

    }

    /**
     * 检查格式
     *
     * @return
     */
    private boolean checkIfPhone() {

        if (FormatUtil.isMobile(grname.getText().toString().trim())) {
            return true;
        }
        return false;
    }


}

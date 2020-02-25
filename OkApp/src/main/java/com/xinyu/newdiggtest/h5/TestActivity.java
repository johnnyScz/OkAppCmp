package com.xinyu.newdiggtest.h5;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xinyu.newdiggtest.APPConstans;
import com.xinyu.newdiggtest.R;

public class TestActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.test_layout1);

        initView();
    }

    private void initView() {

        ImageView img = this.findViewById(R.id.mImageView);

        if (APPConstans.bitmap != null) {
            img.setImageBitmap(APPConstans.bitmap);
        }


    }
}

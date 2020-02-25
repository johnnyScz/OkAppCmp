package com.xinyu.newdiggtest.ui.Digg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.chechezhi.ui.guide.AbsGuideActivity;
import com.chechezhi.ui.guide.SinglePage;
import com.xinyu.newdiggtest.R;

import java.util.ArrayList;
import java.util.List;

public class GuidActivity extends AbsGuideActivity {
    @Override
    public List<SinglePage> buildGuideContent() {
        List<SinglePage> guideContent = new ArrayList<SinglePage>();

        SinglePage page1 = new SinglePage();
//        page1.mBackground = getResources().getDrawable(R.mipmap.guide_1);
        guideContent.add(page1);

        SinglePage page2 = new SinglePage();
//        page2.mBackground = getResources().getDrawable(R.mipmap.guide_2);
        guideContent.add(page2);

        SinglePage page3 = new SinglePage();
//        page3.mBackground = getResources().getDrawable(R.mipmap.guide_3);
        guideContent.add(page3);

        SinglePage page4 = new SinglePage();
//        page4.mBackground = getResources().getDrawable(R.mipmap.guide_4);
        guideContent.add(page4);

        SinglePage page5 = new SinglePage();
        page5.mCustomFragment = new EntryFragment();
        guideContent.add(page5);

        return guideContent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBar();
    }

    private void initBar() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    @Override
    public boolean drawDot() {
        return true;
    }


    @Override
    public Bitmap dotDefault() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_dot_default);
    }

    @Override
    public Bitmap dotSelected() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_dot_selected);
    }

    @Override
    public int getPagerId() {
        return R.id.guide_container;
    }

    public void entryApp() {
        startActivity(new Intent(this, HomeDiggActivity.class));
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeDiggActivity.class));
    }
}

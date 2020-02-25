package com.xinyu.newdiggtest.ui.calendar.callen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;


import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getSupportActionBar().hide();

        init();
    }

    private void init() {

        final CalendarView cleadar = this.findViewById(R.id.btn_query);
        Button query = (Button) findViewById(R.id.btn_query);
        Button delete = (Button) findViewById(R.id.btn_delete);

//        query.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<DateObject> result = ProviderMannager.query(CalendarActivity.this, cleadar.getCurrentPagerSelect(), true);
//                if (result.size() == 0) {
//                    Log.e("amtf", "空数据");
//                }
//            }
//        });
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ProviderMannager.deleteAll(CalendarActivity.this);
//            }
//        });
    }
}

package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.VersionCheckBean;

import java.util.List;

public class AppUploadFragmentDialog extends DialogFragment implements View.OnClickListener {


    OnPopClickListner listner;
    VersionCheckBean data;

    TextView verson;
    ListView listView;

    List<String> datalist;

    public void setOnPopListner(OnPopClickListner mlistner) {
        this.listner = mlistner;
    }


    public interface OnPopClickListner {

        void onCancle();

        void upload();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cancel:

                if (listner != null)
                    listner.onCancle();
                break;


            case R.id.upload:
                if (listner != null)
                    listner.upload();
                break;


        }
    }

    View dialogView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }


    public void setVersionData(VersionCheckBean mdata) {
        data = mdata;
    }

    private void setData(List<String> data) {
        datalist = data;
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView = inflater.inflate(R.layout.app_download_dialog, container);
        setListner(dialogView);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialogView;

    }

    private void setListner(View root) {
        verson = root.findViewById(R.id.tx);
        listView = root.findViewById(R.id.listview);
        root.findViewById(R.id.cancel).setOnClickListener(this);
        root.findViewById(R.id.upload).setOnClickListener(this);


        if (data != null) {
            verson.setText("最新版本(V" + data.getVersion() + "):");
            if (data.getDatalist() != null && data.getDatalist().size() > 0) {
                datalist = data.getDatalist();
                setData(datalist);
            }
        }

    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public String getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String item = datalist.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_app_uploadinfo, null);
                TextView tvname = convertView.findViewById(R.id.tv_name);
                tvname.setText((position + 1) + "." + item);
            }


            return convertView;
        }
    }


}

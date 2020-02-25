package com.xinyu.newdiggtest.ui.chat;

import android.view.View;
import android.widget.ImageView;

public interface onItemClickListener {


    void onHeaderClick(MessageInfo data);

    void onImageClick(View view, String positionUrl);

    void onVoiceClick(ImageView imageView, MessageInfo data);
}

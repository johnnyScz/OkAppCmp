package com.xinyu.newdiggtest.ui.chat;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 随手记
 */
public class HandNoteViewHolder extends BaseViewHolder<MessageInfo> {

    @BindView(R.id.chat_item_content_text)
    GifTextView chatItemContentText;
    @BindView(R.id.chat_item_content_image)
    BubbleImageView chatItemContentImage;

    @BindView(R.id.left_ll_voice)
    RelativeLayout voiceLayout;

    @BindView(R.id.chat_item_voice)
    ImageView chatItemVoice;

    @BindView(R.id.chat_item_voice_time)
    TextView chatItemVoiceTime;

    @BindView(R.id.date)
    TextView dateTx;

    @BindView(R.id.hhmm)
    TextView hhmmtX;


    private HandNoteAdapter.onItemClickListener onItemClickListener;
    private Handler handler;

    public HandNoteViewHolder(ViewGroup parent, HandNoteAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_handnote_accept);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
    }

    @Override
    public void setData(final MessageInfo data) {


        showTime(data);


        if (data.getMsgType().equals("0")) {
//            chatItemContentText.setSpanText(handler, MyTextUtil.getDecodeStr(data.getContent()), true);
            chatItemContentImage.setVisibility(View.GONE);
            voiceLayout.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.VISIBLE);


        } else if (data.getMsgType().equals("1")) {
            voiceLayout.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.VISIBLE);

            Glide.with(getContext()).load(getImg(data.getContent().toString())).into(chatItemContentImage);

            chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onImageClick(chatItemContentImage, getImg(data.getImageUrl()));
                }
            });
        } else if (data.getMsgType().equals("2")) {

            chatItemContentImage.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.GONE);
            voiceLayout.setVisibility(View.VISIBLE);

            if (data.getVoiceTime() >= 1) {
                chatItemVoiceTime.setText(data.getVoiceTime() + "″");
            }
            chatItemVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onVoiceClick(chatItemVoice, data);
                }
            });

        }
    }

    private void showTime(MessageInfo data) {
        if (!MyTextUtil.isEmpty(data.getTime())) {
            String timeStr = DateUtil.longToHm(Long.parseLong(data.getTime()));

            if (MyTextUtil.isEmpty(timeStr) || timeStr.length() < 10)
                return;

            String date = timeStr.substring(0, 10);

            String hhmm = timeStr.substring(timeStr.length() - 5, timeStr.length());

            dateTx.setText(date);
            hhmmtX.setText(hhmm);


        }

    }


    private String getImg(String imageUrl) {

        String url = "";
        try {
            JSONObject object = new JSONObject(imageUrl);
            url = object.getString("thumbnail");

        } catch (JSONException e) {
            url = imageUrl;
            e.printStackTrace();
        }

        return url;
    }

}

package com.xinyu.newdiggtest.ui.chat;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.h5.WebViewUrlActivity;
import com.xinyu.newdiggtest.seriable.TextLongClickListner;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatSendViewHolder extends BaseViewHolder<MessageInfo> {

    @BindView(R.id.chat_item_date)
    TextView chatItemDate;
    @BindView(R.id.chat_item_header)
    ImageView chatItemHeader;
    @BindView(R.id.chat_item_content_text)
    GifTextView chatItemContentText;
    @BindView(R.id.chat_item_content_image)
    BubbleImageView chatItemContentImage;


    @BindView(R.id.chat_item_fail)
    ImageView chatItemFail;
    @BindView(R.id.chat_item_progress)
    ProgressBar chatItemProgress;
    @BindView(R.id.chat_item_voice)
    ImageView chatItemVoice;

    @BindView(R.id.chat_item_voice_time)
    TextView chatItemVoiceTime;
    private ChatAdapter.onItemClickListener onItemClickListener;
    private Handler handler;

    @BindView(R.id.rl_voice)
    RelativeLayout rlVoice;

    @BindView(R.id.tv_r_name)
    TextView rName;

    @BindView(R.id.name_below)
    View chatItem;

    @BindView(R.id.ll_link)
    View linkItem;

    @BindView(R.id.iv_type)
    ImageView shareIcon;

    @BindView(R.id.share_content)
    TextView content;

    TextLongClickListner listner;


    public void setTxLongListner(TextLongClickListner mlistner) {
        this.listner = mlistner;
    }


    public ChatSendViewHolder(ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_send);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
    }


    @Override
    public void setData(final MessageInfo data) {
        rName.setText(data.getName());
        chatItemDate.setText(data.getTime() != null ? data.getTime() : "周日 9:30");


        if (MyTextUtil.isEmpty(data.getHeader())) {
            chatItemHeader.setImageResource(R.drawable.icon_no_download);
        } else {
            Picasso.with(getContext()).load(data.getHeader()).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(chatItemHeader);
        }


        chatItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onHeaderClick(data);
            }
        });

        chatItem.setVisibility(View.VISIBLE);

        if (data.getMsgType().equals("0") || data.getMsgType().equals("4")) {

            String msgContent = (String) data.getContent();

            boolean isLink = data.getMsgType().equals("4") ? true : false;

            if (isLink) {
                msgContent = data.getOriginal_url();
                chatItemContentText.setText(msgContent);
            } else {
                chatItemContentText.setSpanText(handler, msgContent, false);
            }

            chatItem.setVisibility(View.VISIBLE);
            linkItem.setVisibility(View.GONE);


            final String finalMsgContent = msgContent;
            chatItemContentText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (listner != null) {
                        listner.onLongClick(v, finalMsgContent);
                    }
                    return true;
                }
            });


            rlVoice.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.VISIBLE);
            chatItemContentImage.setVisibility(View.GONE);


            if (data.getMsgType().equals("4")) {

                final String urlTet = msgContent;
                chatItemContentText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gowebActy(urlTet);
                    }
                });
            }


        } else if (data.getMsgType().equals("1")) {

            Object tt = data.getContent();

            String type = "";

            if (tt != null && !(tt instanceof String)) {

                String json = new Gson().toJson(tt);

                try {
                    JSONObject obj = new JSONObject(json);

                    type = obj.getString("type");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                showImg(data);
            }


            if (type.equals("2")) {

                chatItem.setVisibility(View.VISIBLE);
                linkItem.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.GONE);
                rlVoice.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.VISIBLE);

                chatItemContentText.setText("[ 文件 ]");


            } else if (type.equals("1")) {
                showImg(data);
            }


        } else if (data.getMsgType().equals("2")) {
            chatItem.setVisibility(View.VISIBLE);
            linkItem.setVisibility(View.GONE);
            rlVoice.setVisibility(View.VISIBLE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.GONE);
            if (!MyTextUtil.isEmpty(data.getVoiceTime() + "")) {
//                chatItemVoiceTime.setText(TimeUtil.formatTime(data.getVoiceTime()));
                chatItemVoiceTime.setText(data.getVoiceTime() + "″");
            }

            data.setFileUrl(data.getFileUrl());

            chatItemVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onVoiceClick(chatItemVoice, data);
                }
            });
        }
        switch (data.getSendState()) {
            case Constants.CHAT_ITEM_SENDING:
                chatItemProgress.setVisibility(View.VISIBLE);
                chatItemFail.setVisibility(View.GONE);
                break;
            case Constants.CHAT_ITEM_SEND_ERROR:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.VISIBLE);
                break;
            case Constants.CHAT_ITEM_SEND_SUCCESS:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.GONE);
                break;
        }
    }

    private void showImg(final MessageInfo data) {
        chatItem.setVisibility(View.VISIBLE);
        linkItem.setVisibility(View.GONE);
        rlVoice.setVisibility(View.GONE);
        chatItemContentText.setVisibility(View.GONE);
        chatItemContentImage.setVisibility(View.VISIBLE);
        if (!MyTextUtil.isEmpty(data.getImageUrl())) {
            Glide.with(getContext()).load(getImg(data.getImageUrl())).into(chatItemContentImage);

            chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onImageClick(chatItemContentImage, getImg(data.getImageUrl()));
                }
            });
        }
    }


    /**
     * 调到URL
     *
     * @param urlTet
     */
    private void gowebActy(String urlTet) {
        Intent broser = new Intent(getContext(), WebViewUrlActivity.class);
        broser.putExtra("newBrowserUrl", urlTet);
        getContext().startActivity(broser);
    }

    private String getImg(String imageUrl) {

        String url = "";
        if (imageUrl.contains("thumbnail")) {
            try {
                JSONObject object = new JSONObject(imageUrl);
                url = object.getString("thumbnail");
            } catch (JSONException e) {
                url = imageUrl;
                e.printStackTrace();
            }
        } else {
            url = imageUrl;
        }
        return url;
    }


}

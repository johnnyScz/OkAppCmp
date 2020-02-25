package com.xinyu.newdiggtest.ui.chat;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class ChatAcceptViewHolder extends BaseViewHolder<MessageInfo> {

    @BindView(R.id.chat_item_date)
    TextView chatItemDate;
    @BindView(R.id.chat_item_header)
    ImageView chatItemHeader;
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

    @BindView(R.id.receive_name)
    TextView name;

    @BindView(R.id.name_below)
    View chatItem;

    @BindView(R.id.ll_link)
    View linkItem;

    @BindView(R.id.iv_type)
    ImageView shareIcon;

    @BindView(R.id.share_content)
    TextView content;


    TextLongClickListner listner;

    private ChatAdapter.onItemClickListener onItemClickListener;
    private Handler handler;


    public void setTxLongListner(TextLongClickListner mlistner) {
        this.listner = mlistner;
    }


    public ChatAcceptViewHolder(ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_accept);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
    }

    @Override
    public void setData(final MessageInfo data) {
        name.setText(data.getName());
        chatItem.setVisibility(View.VISIBLE);
        chatItemDate.setText(data.getTime() != null ? data.getTime() : "");
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

            chatItem.setVisibility(View.VISIBLE);
            linkItem.setVisibility(View.GONE);

            boolean isLink = data.getMsgType().equals("4") ? true : false;

            String msgContent = (String) data.getContent();

            if (isLink) {
                msgContent = data.getOriginal_url();
                chatItemContentText.setText(msgContent);
            } else {
                chatItemContentText.setSpanText(handler, msgContent, false);
            }


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


            chatItemContentImage.setVisibility(View.GONE);
            voiceLayout.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.VISIBLE);

            if (data.getMsgType().equals("4")) {
                final String finalMsgContent1 = msgContent;
                chatItemContentText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        goActy(finalMsgContent1);
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
                voiceLayout.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.VISIBLE);

                chatItemContentText.setText("[ 文件 ]");


            } else if (type.equals("1")) {
                showImg(data);
            }
        } else if (data.getMsgType().equals("2")) {

            chatItem.setVisibility(View.VISIBLE);
            linkItem.setVisibility(View.GONE);

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

    private void showImg(final MessageInfo data) {
        chatItem.setVisibility(View.VISIBLE);
        linkItem.setVisibility(View.GONE);

        voiceLayout.setVisibility(View.GONE);
        chatItemContentText.setVisibility(View.GONE);
        chatItemContentImage.setVisibility(View.VISIBLE);

        if (!MyTextUtil.isEmpty(getImg(data.getImageUrl()))) {
            Glide.with(getContext()).load(getImg(data.getImageUrl())).into(chatItemContentImage);
        }

        chatItemContentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onImageClick(chatItemContentImage, getImg(data.getImageUrl()));
            }
        });
    }

    private void goActy(String msgContent) {

        Intent broser = new Intent(getContext(), WebViewUrlActivity.class);
        broser.putExtra("newBrowserUrl", msgContent);
        getContext().startActivity(broser);

    }


    private String getImg(String imageUrl) {

        if (MyTextUtil.isEmpty(imageUrl)) {
            return "";
        }

        if (imageUrl.startsWith("http")) {
            return imageUrl;

        }

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

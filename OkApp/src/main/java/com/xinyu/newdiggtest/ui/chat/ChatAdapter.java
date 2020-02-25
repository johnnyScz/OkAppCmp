package com.xinyu.newdiggtest.ui.chat;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.xinyu.newdiggtest.seriable.TextLongClickListner;

import org.json.JSONObject;


/**
 * 作者：Rance on 2016/11/29 10:46
 * 邮箱：rance935@163.com
 */
public class ChatAdapter extends RecyclerArrayAdapter<MessageInfo> {

    private onItemClickListener onItemClickListener;

    private TextLongClickListner listener;

    public Handler handler;

    public ChatAdapter(Context context) {
        super(context);
        handler = new Handler();
    }

    public void setLongClickListner(TextLongClickListner mlistner) {
        this.listener = mlistner;
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                viewHolder = new ChatAcceptViewHolder(parent, onItemClickListener, handler);
                if (listener != null)
                    ((ChatAcceptViewHolder) viewHolder).setTxLongListner(listener);

                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(parent, onItemClickListener, handler);

                if (listener != null)
                    ((ChatSendViewHolder) viewHolder).setTxLongListner(listener);

                break;
        }
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHeaderClick(MessageInfo data);

        void onImageClick(View view, String positionUrl);

        void onShareClick(JSONObject json);

        void onVoiceClick(ImageView imageView, MessageInfo data);

    }
}

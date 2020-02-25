package com.xinyu.newdiggtest.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.bean.TodoUserBean;
import com.xinyu.newdiggtest.utils.MyTextUtil;

import java.net.URL;
import java.util.List;

/**
 * 回复列表
 */
public class HomeReplyAdapter extends BaseQuickAdapter<RetListBean.InvitesBean, BaseViewHolder> {


    public HomeReplyAdapter(int layoutResId, @Nullable List<RetListBean.InvitesBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, RetListBean.InvitesBean item) {


        ImageView head = helper.getView(R.id.iv_iconhead);

        TodoUserBean userBean = item.getOwnermap();

        String mHead = MyTextUtil.isEmpty(userBean.getHead()) ? userBean.getCustom_head() : userBean.getHead();

        Picasso.with(mContext).load(mHead).error(R.drawable.icon_no_download).into(head);

        helper.setText(R.id.tv_name, userBean.getNickname());

        String html = item.getF_title();

        if (!MyTextUtil.isEmpty(html) && html.contains("<img src=")) {
            TextView view = helper.getView(R.id.cotent);
            showHtml(view, html);
        } else {
            helper.setText(R.id.cotent, checkPhp(item.getF_title()));
        }


        String time = item.getF_create_date().replace("T", " ");

        helper.setText(R.id.tv_date, time.substring(0, time.length() - 3));

    }


    /**
     * 显示富文本
     *
     * @param html
     */
    private void showHtml(final TextView tvDemo, String html) {


        tvDemo.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(final String source) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        mDrawable.setBounds(0, 0, 200, 200);

                        Bitmap bitmap;
                        try {
                            bitmap = BitmapFactory.decodeStream(new URL(source).openStream());
                            BitmapDrawable drawable = new BitmapDrawable(null, bitmap);
                            mDrawable.addLevel(1, 1, drawable);
                            mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                            mDrawable.setLevel(1);

                            tvDemo.post(new Runnable() {
                                @Override
                                public void run() {
                                    CharSequence charSequence = tvDemo.getText();
                                    tvDemo.setText(charSequence);
                                    tvDemo.invalidate();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                return mDrawable;
            }
        }, null));


    }

    private LevelListDrawable mDrawable = new LevelListDrawable();


    private String checkPhp(String f_title) {


        String result = f_title.replaceAll("<p>", "");

        String result22 = result.replaceAll("</p>", "");


        return result22;
    }


}

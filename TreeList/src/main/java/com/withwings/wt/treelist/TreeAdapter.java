package com.withwings.wt.treelist;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.withwings.wt.treelist.bean.TreeData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 嵌套式层级列表 RecyclerView 实现
 * 创建：WithWings 时间 19/1/2
 * Email:wangtong1175@sina.com
 *
 * @param <VH> RecyclerView ViewHolder 要求的泛型
 * @param <D> 关于嵌套式数据的数据格式泛型
 */
public abstract class TreeAdapter<VH extends RecyclerView.ViewHolder, D extends TreeData> extends RecyclerView.Adapter<VH> {

    /**
     * 嵌套式数据源
     */
    private List<D> mData;

    /**
     * 用以展示的数据内容
     */
    private List<D> mAllData = new ArrayList<>();

    /**
     * 要求必须传递数据
     * @param data 数据源
     */
    public TreeAdapter(List<D> data) {
        mData = data;
    }

    /**
     * 重写 getItemViewType 以扩展参数
     * @param position 下标
     * @return itemType
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mAllData.get(position).getTreeLevel());
    }

    /**
     * 仿照原生类默认返回 0
     * @param position 下标
     * @param level 层级
     * @return 0
     */
    public int getItemViewType(int position, int level) {
        return 0;
    }

    /**
     * 重写 onBindViewHolder 以便扩展参数并且添加菜单开关监听
     * @param vh ViewHolder
     * @param position 下标
     */
    @Override
    public void onBindViewHolder(@NonNull final VH vh, int position) {
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View view) {
                D d = mAllData.get(vh.getAdapterPosition());
                if (d.isTreeOpen()) {
                    d.setTreeOpen(!d.isTreeOpen());
                    int treeLevel = d.getTreeLevel();
                    if (vh.getAdapterPosition() != mAllData.size() - 1) {
                        for (int i = vh.getAdapterPosition() + 1; i < mAllData.size(); i++) {
                            if (treeLevel == mAllData.get(i).getTreeLevel()) {
                                notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                } else if (d.getChildren() != null) {
                    d.setTreeOpen(!d.isTreeOpen());
                    mAllData.addAll(vh.getAdapterPosition() + 1, (Collection<? extends D>) d.getChildren());
                    notifyDataSetChanged();
                } else {
                    d.setTreeOpen(!d.isTreeOpen());
                    notifyDataSetChanged();
                }
            }
        });
        onBindViewHolder(vh, position, mAllData.get(position));
    }

    /**
     *  扩展的 onBindViewHolder
     * @param vh ViewHolder
     * @param position 下标
     * @param data 数据
     */
    public abstract void onBindViewHolder(@NonNull VH vh, int position, D data);

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return getCountForTree(mData, 1, 0);
    }

    /**
     * 计算 Tree 列一共多少项：如果对象内存的路径指向对象本身，会造成死循环，千万注意
     * @param data 数据列
     * @param level 层级
     * @param index 集合下标
     * @return 总数
     */
    @SuppressWarnings("unchecked")
    private int getCountForTree(List<D> data, int level, int index) {
        int itemCount = 0;
        mAllData.addAll(index, data);
        itemCount += data.size();
        for (TreeData datum : data) {
            datum.setTreeLevel(level);
            if (datum.getChildren() != null && datum.isTreeOpen()) {
                List<D> children = (List<D>) datum.getChildren();
                index = mAllData.indexOf(datum) + 1;
                itemCount += getCountForTree(children, level + 1, index);
            }
        }
        return itemCount;
    }
}

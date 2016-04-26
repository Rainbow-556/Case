package com.rainbow556.carlli.rainbow556.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rainbow556.carlli.rainbow556.interfaces.AbsFooter;

import java.util.List;

/**
 * Created by Rainbow556 on 4/2/2016.
 */
public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    protected static final int TYPE_HEADER = 1;
    protected static final int TYPE_ITEM = 2;
    protected static final int TYPE_FOOTER = 3;
    protected View mHeaderView;
    protected AbsFooter mFooter;
    protected List<T> mDatas;
    protected int mItemLayoutId;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private RecyclerView mRecyclerView;

    public CommonRecyclerViewAdapter(Context context, int itemLayoutId, List<T> datas){
        this.mItemLayoutId = itemLayoutId;
        this.mDatas = datas;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mRecyclerView == null){
            mRecyclerView = (RecyclerView) parent;
        }
        if(viewType == TYPE_HEADER){
            return new HeaderAndFooterViewHolder(mHeaderView);
        }else if(viewType == TYPE_FOOTER){
            View footer = null;
            if(mFooter.getFooterView() != null){
                footer = mFooter.getFooterView();
            }else if(mFooter.getFooterLayoutId() != 0){
                footer = mLayoutInflater.inflate(mFooter.getFooterLayoutId(), parent, false);
                mFooter.setFooterView(footer);
            }
            return new HeaderAndFooterViewHolder(footer);
        }else{
            return onCreateItemViewHolder(parent, viewType);
        }
    }

    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position){
        if(holder instanceof HeaderAndFooterViewHolder){
            return;
        }
        final int real = getRealPosition(position);
        //添加item点击监听
        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mOnItemClickListener.onItemClick(mRecyclerView, view, real);
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    mOnItemLongClickListener.onItemLongClick(mRecyclerView, view, real);
                    return true;
                }
            });
        }

        onBind(holder, real, mDatas.get(real));
    }

    public abstract void onBind(RecyclerView.ViewHolder holder, int position, T data);

    @Override
    public int getItemCount(){
        int count = 0;
        if(mHeaderView != null){
            count++;
        }
        if(mFooter != null){
            count++;
        }
        return mDatas.size() + count;
    }

    /**
     * 如果需求为item的type不同，则子类adapter需重新重写该方法
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){
        if(mHeaderView != null && position == 0){
            return TYPE_HEADER;
        }else if(mFooter != null && position == getItemCount() - 1){
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }
    }

    /**
     * 处理当为GridLayoutManager时，header和footer独占一行
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                @Override
                public int getSpanSize(int position){
                    int type = getItemViewType(position);
                    if(type == TYPE_HEADER || type == TYPE_FOOTER){
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    /**
     * 处理当为StaggeredGridLayoutManager时，header和footer独占一行
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder){
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            int type = getItemViewType(holder.getLayoutPosition());
            p.setFullSpan(type == TYPE_HEADER || type == TYPE_FOOTER);
        }
    }

    protected int getRealPosition(int position){
        int real = position;
        if(mHeaderView != null){
            real--;
        }
        return real;
    }

    public void setDatas(List<T> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void clearDatas(){
        if(mDatas != null){
            mDatas.clear();
        }
    }

    public void addDatas(List<T> datas){
        int size = datas.size();
        mDatas.addAll(datas);
        notifyItemRangeInserted(mDatas.size() - size, size);
//        notifyItemRangeChanged(mDatas.size() - size, size);
//        notifyDataSetChanged();
    }

    public void addData(T data){
        mDatas.add(data);
        notifyItemInserted(getItemCount());
    }

    public void setHeaderView(View headerView){
        this.mHeaderView = headerView;
    }

    public void setFooter(AbsFooter footer){
        this.mFooter = footer;
    }

    public void updateFooter(int newState){
        if(mFooter != null){
            mFooter.onStateChange(newState);
        }
    }

    public void setOnItemClickListener(OnItemClickListener l){
        this.mOnItemClickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l){
        this.mOnItemLongClickListener = l;
    }

    public boolean hasHeader(){
        return mHeaderView != null;
    }

    public boolean hasFooter(){
        return mFooter != null;
    }

    private static class HeaderAndFooterViewHolder extends RecyclerView.ViewHolder{
        public HeaderAndFooterViewHolder(View itemView){
            super(itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView recyclerView, View itemView, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(RecyclerView recyclerView, View itemView, int position);
    }
}

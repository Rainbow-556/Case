package com.rainbow556.carlli.rainbow556.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.rainbow556.carlli.rainbow556.adapter.CommonRecyclerViewAdapter;
import com.rainbow556.carlli.rainbow556.interfaces.AbsFooter;

/**
 * Created by Rainbow556 on 4/3/2016.
 */
public class AutoLoadMoreRecyclerView extends RecyclerView{
    private OnLoadMoreListener mLoadMoreListener;
    private AutoLoadScrollListener mScrollListener;
    private boolean isLoadingMore, hasMoreData = true;

    public AutoLoadMoreRecyclerView(Context context){
        this(context, null);
    }

    public AutoLoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs, 0);
    }

    public AutoLoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.mLoadMoreListener = listener;
        if(mScrollListener == null){
            mScrollListener = new AutoLoadScrollListener();
        }
        addOnScrollListener(mScrollListener);
    }

    public void setHasMoreData(boolean hasMoreData){
        this.hasMoreData = hasMoreData;
    }

    public void setLoadFinishState(int state){
        isLoadingMore = false;
        Adapter adapter = getAdapter();
        if(adapter instanceof CommonRecyclerViewAdapter){
            CommonRecyclerViewAdapter a = (CommonRecyclerViewAdapter) adapter;
            a.updateFooter(state);
        }
    }

    public boolean isLoadingMore(){
        return isLoadingMore;
    }

    @Override
    protected void onAttachedToWindow(){
        super.onAttachedToWindow();
    }

    private class AutoLoadScrollListener extends OnScrollListener{
        private static final int LINEAR_LAYOUT_MANAGER = 1;
        private static final int GRID_LAYOUT_MANAGER = 2;
        private static final int STAGGERED_GRID_LAYOUT_MANAGER = 3;
        protected int layoutManagerType;
        private int[] lastPositions;
        private int lastVisibleItemPosition;
        private boolean isScrolled;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy){
            super.onScrolled(recyclerView, dx, dy);
            //dy>0为向上滑动，dy<0为向下滑动
            isScrolled = (dy != 0);
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if(layoutManagerType == 0){
                if(layoutManager instanceof LinearLayoutManager){
                    layoutManagerType = LINEAR_LAYOUT_MANAGER;
                }else if(layoutManager instanceof GridLayoutManager){
                    layoutManagerType = GRID_LAYOUT_MANAGER;
                }else if(layoutManager instanceof StaggeredGridLayoutManager){
                    layoutManagerType = STAGGERED_GRID_LAYOUT_MANAGER;
                }else{
                    throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }
            switch(layoutManagerType){
                case LINEAR_LAYOUT_MANAGER:
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case GRID_LAYOUT_MANAGER:
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID_LAYOUT_MANAGER:
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    if(lastPositions == null){
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    break;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState){
            super.onScrollStateChanged(recyclerView, newState);
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if(visibleItemCount > 0 && totalItemCount >= visibleItemCount && isScrolled && hasMoreData
                    && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition >= totalItemCount - 1){
                if(isLoadingMore){
                    return;
                }
                //加载更多
                Adapter adapter = getAdapter();
                if(adapter instanceof CommonRecyclerViewAdapter){
                    CommonRecyclerViewAdapter a = (CommonRecyclerViewAdapter) adapter;
                    a.updateFooter(AbsFooter.STATE_LOADING);
                }
                isLoadingMore = true;
                mLoadMoreListener.onLoadMore();
            }else if(visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE){
            }
        }

        private int findMax(int[] lastPositions){
            int max = lastPositions[0];
            for(int value : lastPositions){
                if(value > max){
                    max = value;
                }
            }
            return max;
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}

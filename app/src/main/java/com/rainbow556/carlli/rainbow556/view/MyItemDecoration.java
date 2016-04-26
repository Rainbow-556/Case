package com.rainbow556.carlli.rainbow556.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rainbow556.carlli.rainbow556.R;

/**
 * Created by Rainbow556 on 12/6/2015.
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration{
    private Drawable mDivider;

    public MyItemDecoration(Context context) {
        mDivider=context.getResources().getDrawable(R.drawable.divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount=parent.getChildCount();
        for (int i=0;i<childCount;i++){
//            if(i == 0 || i == childCount-1){
//                mDivider.setBounds(0, 0, 0, 0);
//                mDivider.draw(c);
//                continue;
//            }
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams lp= (RecyclerView.LayoutParams) child.getLayoutParams();
            int left=child.getLeft();
            int right=child.getRight();
            int top=child.getBottom()+lp.bottomMargin;
            int bottom=top+mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
//        if(position == 0 || position == itemCount-1){
//            outRect.set(0,0,0,0);
//        }else{
//            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
//        }
        outRect.set(0,0,0,mDivider.getIntrinsicHeight());
    }
}

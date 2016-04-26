package com.rainbow556.carlli.rainbow556.interfaces;

import android.view.View;

/**
 * Created by Carl.li on 2016/4/25.
 */
public abstract class AbsFooter{
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_FAIL = 3;
    protected View mFooter;
    protected int mFooterLayoutId;

    public AbsFooter(View mFooter){
        this.mFooter = mFooter;
    }

    public AbsFooter(int mFooterLayoutId){
        this.mFooterLayoutId = mFooterLayoutId;
    }

    public abstract void onStateChange(int newState);

    public View getFooterView(){
        return mFooter;
    }

    public void setFooterView(View mFooter){
        this.mFooter = mFooter;
    }

    public int getFooterLayoutId(){
        return mFooterLayoutId;
    }

    public void setFooterLayoutId(int mFooterLayoutId){
        this.mFooterLayoutId = mFooterLayoutId;
    }
}

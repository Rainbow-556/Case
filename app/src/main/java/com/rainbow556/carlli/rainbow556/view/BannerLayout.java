package com.rainbow556.carlli.rainbow556.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.rainbow556.carlli.rainbow556.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl.li on 2016/4/13.
 */
public class BannerLayout extends RelativeLayout{
    private int WHAT_AUTO_PLAY = 1000;
    private Context mContext;
    private ViewPager mViewPager;
    private List<View> mViews = new ArrayList<>(5);
    private List<String> mImgUrls;
    private int mItemCount;
    private boolean isAutoPlay;
    private long mAutoPlayDuration = 3000;
    private int mDefaultImgResId = R.mipmap.ic_launcher;
    private OnBannerItemClickListener mOnBannerItemClickListener;
    private ImageDownloader mImageDownloader;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == WHAT_AUTO_PLAY){
                if(mViewPager != null){
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                    mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration);
                }
            }
        }
    };

    public BannerLayout(Context context){
        this(context, null);
    }

    public BannerLayout(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView(){
        //初始化pager
        mViewPager = new ViewPager(getContext());
        addView(mViewPager);
    }

    private ImageView getImageView(final int position){
        ImageView imageView = new ImageView(getContext());
        imageView.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                if(mOnBannerItemClickListener != null){
                    mOnBannerItemClickListener.onBannerItemClick(position);
                }
            }
        });
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(mDefaultImgResId);
        if(mImageDownloader != null){
            mImageDownloader.loadImg(mImgUrls.get(position), imageView);
        }
        return imageView;
    }

    private void switchIndicator(int position){

    }

    public void setImgUrls(List<String> urls){
        if(urls == null || urls.size() == 0){
            return;
        }
        mItemCount = urls.size();
        mImgUrls = urls;
        //主要是解决当item为小于3个的时候滑动有问题，这里将其拼凑成3个以上
        if(mItemCount < 1){//当item个数0
            throw new IllegalStateException("item count not equal zero");
        }else if(mItemCount < 2){ //当item个数为1
            mViews.add(getImageView(0));
//            mViews.add(getImageView(0, 0));
//            mViews.add(getImageView(0, 0));
        }else if(mItemCount < 3){//当item个数为2
            mViews.add(getImageView(0));
            mViews.add(getImageView(1));
            mViews.add(getImageView(0));
//            mViews.add(getImageView(0, 1));
        }else{
            for(int i = 0; i < urls.size(); i++){
                mViews.add(getImageView(i));
            }
        }
        LoopPagerAdapter pagerAdapter = new LoopPagerAdapter(mViews);
        mViewPager.setAdapter(pagerAdapter);
        int targetItemPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mItemCount;
        mViewPager.setCurrentItem(targetItemPosition);
        switchIndicator(targetItemPosition % mItemCount);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                switchIndicator(position % mItemCount);
            }
        });
        startAutoPlay();
    }

    public void setDefaultImgResId(int id){
        mDefaultImgResId = id;
    }

    public void setAutoPlayDuration(int duration){
        mAutoPlayDuration = duration;
    }

    public void startAutoPlay(){
        if(mItemCount == 1){
            return;
        }
        stopAutoPlay(); // 避免重复消息
        isAutoPlay = true;
        mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration);
    }

    public void stopAutoPlay(){
        isAutoPlay = false;
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setSliderTransformDuration(int duration){
        try{
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), null, duration);
            mScroller.set(mViewPager, scroller);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener listener){
        this.mOnBannerItemClickListener = listener;
    }

    public void setImageDownloader(ImageDownloader downloader){
        this.mImageDownloader = downloader;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility){
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void onDestroy(){
        stopAutoPlay();
        mHandler = null;
        mOnBannerItemClickListener = null;
        mImageDownloader = null;
    }

    public class LoopPagerAdapter extends PagerAdapter{
        private List<View> views;

        public LoopPagerAdapter(List<View> views){
            this.views = views;
        }

        @Override
        public int getCount(){
            if(mItemCount == 1){
                return 1;
            }
            //Integer.MAX_VALUE = 2147483647
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object){
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            if(views.size() > 0){
                //position % view.size()是指虚拟的position会在[0，view.size()）之间循环
                View view = views.get(position % views.size());
                if (container.equals(view.getParent())) {
                    container.removeView(view);
                }
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
        }
    }

    public class FixedSpeedScroller extends Scroller{
        private int mDuration = 1000;

        public FixedSpeedScroller(Context context){
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator){
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration){
            this(context, interpolator);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration){
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy){
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    public interface OnBannerItemClickListener{
        void onBannerItemClick(int position);
    }

    public interface ImageDownloader{
        void loadImg(String url, ImageView imageView);
    }
}

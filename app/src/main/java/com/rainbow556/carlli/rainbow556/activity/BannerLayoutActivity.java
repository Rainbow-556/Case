package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.view.BannerLayout;

import java.util.ArrayList;
import java.util.List;

public class BannerLayoutActivity extends AppCompatActivity{
    private BannerLayout mBannerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_layout);
        initView();
    }

    private void initView(){
        mBannerLayout = (BannerLayout) findViewById(R.id.banner_layout);
        List<String> urls = new ArrayList<>();
        urls.add("url0");
        urls.add("url1");
//        urls.add("url2");
//        urls.add("url3");
        mBannerLayout.setSliderTransformDuration(1000);
        mBannerLayout.setAutoPlayDuration(2000);
        mBannerLayout.setImgUrls(urls);
        mBannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener(){
            @Override
            public void onBannerItemClick(int position){
                Toast.makeText(BannerLayoutActivity.this, "click "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startAutoPlay(View view){
        mBannerLayout.startAutoPlay();
    }

    public void stopAutoPlay(View view){
        mBannerLayout.stopAutoPlay();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mBannerLayout.onDestroy();
    }
}

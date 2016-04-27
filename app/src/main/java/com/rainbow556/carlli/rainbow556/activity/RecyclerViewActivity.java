package com.rainbow556.carlli.rainbow556.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.adapter.CommonRecyclerViewAdapter;
import com.rainbow556.carlli.rainbow556.interfaces.AbsFooter;
import com.rainbow556.carlli.rainbow556.util.JLog;
import com.rainbow556.carlli.rainbow556.view.AutoLoadMoreRecyclerView;
import com.rainbow556.carlli.rainbow556.view.MyItemDecoration;
import com.rainbow556.carlli.rainbow556.view.SimpleItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends BaseActivity{

    private AutoLoadMoreRecyclerView mRecyclerView;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mData = new ArrayList<>();
        for(int i=0; i<15; i++){
            mData.add("item"+i);
        }
        initView();
    }

    private void initView(){
        mRecyclerView = findView(R.id.recycler_view);
        MyItemDecoration decoration = new MyItemDecoration(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.addItemDecoration(decoration);
        final MyAdapter adapter = new MyAdapter(this, R.layout.item, mData);
        mRecyclerView.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener(){
            @Override
            public void onLoadMore(){
                JLog.e("onLoadMore");
                mRecyclerView.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        JLog.e("load finish");
                        List<String> data = new ArrayList<String>(2);
                        data.add("new data1");
                        data.add("new data2");
                        adapter.addDatas(data);
//                        adapter.setDatas(data);
//                        adapter.addData("add");
                        mRecyclerView.setLoadFinishState(AbsFooter.STATE_SUCCESS);
                    }
                }, 1000);

            }
        });
        adapter.setFooter(new MyFooter(R.layout.footer));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new CommonRecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position){
                JLog.e(position);
            }
        });
        adapter.setOnItemLongClickListener(new CommonRecyclerViewAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClick(RecyclerView recyclerView, View itemView, int position){
                JLog.e(position);
            }
        });
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(this, adapter.hasHeader(), adapter.hasFooter(), R.drawable.divider));
        mRecyclerView.setAdapter(adapter);
    }

    class MyAdapter extends CommonRecyclerViewAdapter<String>{

        public MyAdapter(Context context, int itemLayoutId, List<String> datas){
            super(context, itemLayoutId, datas);
        }

        @Override
        public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType){
            View itemView = mLayoutInflater.inflate(R.layout.item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBind(RecyclerView.ViewHolder holder, int position, String data){
            TextView tv = ((MyViewHolder)holder).tv;
            tv.setText(data);
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView tv;

            public MyViewHolder(View itemView){
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }

    class MyFooter extends AbsFooter{

        public MyFooter(View mFooter){
            super(mFooter);
        }

        public MyFooter(int mFooterLayoutId){
            super(mFooterLayoutId);
        }

        @Override
        public void onStateChange(int newState){
            View pb = mFooter.findViewById(R.id.pb);
            TextView tv = (TextView) mFooter.findViewById(R.id.tv);
            switch(newState){
                case STATE_LOADING:
                    pb.setVisibility(View.VISIBLE);
                    tv.setText("loading...");
                    break;
                case STATE_SUCCESS:
                    pb.setVisibility(View.INVISIBLE);
                    tv.setText("success");
                    break;
                case STATE_FAIL:
                    pb.setVisibility(View.INVISIBLE);
                    tv.setText("fail");
                    break;

            }
        }
    }

}

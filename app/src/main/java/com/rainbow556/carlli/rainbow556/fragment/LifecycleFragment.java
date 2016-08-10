package com.rainbow556.carlli.rainbow556.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rainbow556.carlli.rainbow556.util.JLog;

/**
 * Created by Carl.li on 2016/7/29.
 */
public class LifecycleFragment extends Fragment{
    private String text;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        JLog.e("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        JLog.e("onCreate");
        text = getArguments().getString("text", "default text");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        JLog.e("onCreateView");
        Button btn = new Button(getContext());
        btn.setText(text);
        btn.setTextSize(30);
        return btn;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        JLog.e("onActivityCreated");
    }

    @Override
    public void onStart(){
        super.onStart();
        JLog.e("onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        JLog.e("onResume");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        JLog.e("onViewStateRestored");
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        JLog.e("onSaveInstanceState");
    }

    @Override
    public void onPause(){
        super.onPause();
        JLog.e("onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        JLog.e("onStop");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        JLog.e("onDestroyView");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        JLog.e("onDestroy");
    }

    @Override
    public void onDetach(){
        super.onDetach();
        JLog.e("onDetach");
    }
}

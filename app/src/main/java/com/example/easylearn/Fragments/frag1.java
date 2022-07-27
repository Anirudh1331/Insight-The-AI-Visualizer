package com.example.easylearn.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easylearn.Adapters.ViewPagerAdapter;
import com.example.easylearn.R;

import java.util.Timer;
import java.util.TimerTask;

public class frag1 extends Fragment {
    ViewPager mViewPager;
    Timer timer;
    Handler handler;
    public frag1() {
        // Required empty public constructor
    }
    int[] images = {R.drawable.chair,
            R.drawable.chair1,
            R.drawable.chair2,
            R.drawable.chair3};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_frag1, container, false);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager_dashboard);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getActivity(), images);
        mViewPager.setAdapter(mViewPagerAdapter);
        handler=new Handler();
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int x=mViewPager.getCurrentItem();
                        if(x==images.length-1){
                            x=0;
                            mViewPager.setCurrentItem(x,true);
                        }
                        else {
                            mViewPager.setCurrentItem(x + 1, true);
                        }
                    }
                });
            }
        },3000,3000);
        return view;
    }
}
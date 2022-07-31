package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easylearn.Adapters.ViewPagerBookmarkAdapter;
import com.example.easylearn.Fragments.frag1;
import com.example.easylearn.Fragments.frag2;
import com.google.android.material.tabs.TabLayout;

public class HomePage extends FragmentActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        tabLayout=findViewById(R.id.tabLayout_blog);
        viewPager=findViewById(R.id.viewPager_blog);
        ViewPagerBookmarkAdapter viewPagerBookmarkAdapter=new ViewPagerBookmarkAdapter(getSupportFragmentManager());
        viewPagerBookmarkAdapter.addFragment(new frag1(),"Home");
        viewPagerBookmarkAdapter.addFragment(new frag2(),"Account");
        viewPager.setAdapter(viewPagerBookmarkAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
    public void myFancyMethod(View v) {
        Toast.makeText(HomePage.this, "Please go with the Home, Furniture for the demo time", Toast.LENGTH_SHORT).show();
    }
}
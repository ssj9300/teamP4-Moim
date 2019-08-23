package com.example.moimproject4t;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.moimproject4t.adapter.MypageAdapter;
import com.example.moimproject4t.model.Mypage;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class SearchActivity extends AppCompatActivity
        implements View.OnClickListener {
    Button buttonHome, buttonSearch, buttonSet;


    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        buttonHome = findViewById(R.id.buttonHome);
        buttonSearch= findViewById(R.id.buttonSearch);
        buttonSet= findViewById(R.id.buttonSet);

        buttonHome.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonHome:
                //intent = new Intent(this, MypageActivity.class);
                //startActivityForResult(intent.addFlags(FLAG_ACTIVITY_NO_HISTORY));
                finish();
                Toast.makeText(this, "HomeButton", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonSearch:
                Toast.makeText(this, "buttonSearch", Toast.LENGTH_SHORT).show();
                break;

            case R.id.buttonSet:
                intent = new Intent(this, SetActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void startActivityForResult(Intent addFlags) {
    }



}

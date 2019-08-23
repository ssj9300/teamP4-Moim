package com.example.moimproject4t;

import android.content.Intent;
import android.support.annotation.Nullable;
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

public class MypageActivity extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemClickListener {
    Button buttonHome, buttonSearch, buttonSet;
    Button buttonMake, buttonBackToHome;
    FloatingActionButton fabWrite, floatButton;
    ArrayList<Mypage> list;
    MypageAdapter adapter;
    GridView gridView;
    LinearLayout fabLayoutSubTex, fabLayoutMain, fabLayoutSub;
    LinearLayout linearLayoutMy, linearLayoutMakeMo;
    Animation fab_open, fab_close;
    Boolean isFabOpen = false;

    Intent intent;
    //Intent intent = getIntent();
    String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fabLayoutSubTex = (LinearLayout) findViewById(R.id.fabLayoutSubTex);
        fabLayoutMain = (LinearLayout) findViewById(R.id.fabLayoutMain);
        fabLayoutSub = (LinearLayout) findViewById(R.id.fabLayoutSub);
        linearLayoutMy = findViewById(R.id.linearLayoutMy) ;
        linearLayoutMakeMo = findViewById(R.id.linearLayoutMakeMo);
        buttonHome = findViewById(R.id.buttonHome);
        buttonSearch= findViewById(R.id.buttonSearch);
        buttonSet= findViewById(R.id.buttonSet);
        buttonMake= findViewById(R.id.buttonSave);
        buttonBackToHome= findViewById(R.id.buttonBackToHome);
        gridView = findViewById(R.id.gridView);

        list = new ArrayList<>();
        adapter = new MypageAdapter(this, R.layout.grid_item,list);


        fabWrite = (FloatingActionButton) findViewById(R.id.fabWrite);
        floatButton = (FloatingActionButton) findViewById(R.id.floatButton);

        // 리스트뷰에 어뎁터 연결
        gridView.setAdapter(adapter);
        //  Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.ListView.setAdapter(android.widget.ListAdapter)' on a null object reference
        //  at com.example.a3_listadapterexam.MainActivity.onCreate(MainActivity.java:30)

        fabLayoutSub.setVisibility(View.INVISIBLE);

        linearLayoutMy.setVisibility(View.VISIBLE);
        linearLayoutMakeMo.setVisibility(View.GONE);


        buttonBackToHome.setOnClickListener(this);
        fabWrite.setOnClickListener(this);
        floatButton.setOnClickListener(this);
        buttonMake.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonSet.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
        addDate();

        //로그인 사용자 정보 불러오기

        user_id=getIntent().getStringExtra("user_id");

        /**
         *
         * 가입된 모임 불러오는 코드 넣을 영역
         *
         * **/


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonHome:
                Toast.makeText(this, "Home now", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonSearch:
                intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, 200);
                break;
            case R.id.buttonSet:

                Intent intent = new Intent(MypageActivity.this, SetActivity.class);
                //intent.putExtra("user_id", user_id); //유저의 아이디값을 인텐트로 넘김(db조회를 위함)
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                break;
            case R.id.floatButton:
                anim();
                break;
            case R.id.fabWrite:
                anim();
                linearLayoutMy.setVisibility(View.GONE);
                linearLayoutMakeMo.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonSave: // 모임 만들면서 우선은 홈으로 가기
                linearLayoutMakeMo.setVisibility(View.GONE);
                linearLayoutMy.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonBackToHome: // 모임 만들기에서 뒤로가기
                linearLayoutMakeMo.setVisibility(View.GONE);
                linearLayoutMy.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {

        }
    }

    private void addDate() {
        adapter.add(new Mypage(1, "논현동",  "열심히 했다면 쉬어가면서 해도대요", "영어로 말하고 공부하는 모임", "jello.jpg","gloomyGreen"));
        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","navy"));
        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","lightViolet"));
        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","violet"));
        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","darkWine"));
        adapter.add(new Mypage(1, "논현동",  "힘내자!!", "영어로 말하고 공부하는 모임", "jello.jpg","wine"));
        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","darkBrown"));
        // adapter.add(new Mypage(1, "논현동",  "당신은 할수있어요", "영어로 말하고 공부하는 모임", "jello.jpg","darkGold"));
//        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","khaki"));
//        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","sadGreen"));
//        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","strongGreen"));//
//        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg","strongGreen"));
//        adapter.add(new Mypage(1, "논현동",  "공부하자", "영어로 말하고 공부하는 모임", "jello.jpg", "absoluteBlack"));
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // ArrayList 객체를 ResultActivity로 전달
        intent = new Intent(this, MoimActivity.class);
        //intent.putExtra("list", list);
        //intent.putExtra("position", position);
        startActivity(intent);
    }

    public void anim() {
        //Log.d("[INFO]" , "test" + isFabOpen);
        if (isFabOpen) {

            fabLayoutMain.setVisibility(View.VISIBLE);

            fabLayoutSubTex.setVisibility(View.GONE);
            floatButton.animate().rotationBy(60);
            //fabLayout.setVisibility(View.VISIBLE);
            fabWrite.startAnimation(fab_close);
            fabWrite.setClickable(false);
            isFabOpen = false;

        } else {
            fabLayoutSub.setVisibility(View.VISIBLE);
            fabLayoutSubTex.setVisibility(View.VISIBLE);
            floatButton.animate().rotationBy(-60);
            //fabLayoutSub.setVisibility(View.GONE);
            fabWrite.startAnimation(fab_open);
            fabWrite.setClickable(true);
            isFabOpen = true;
        }
    }
}
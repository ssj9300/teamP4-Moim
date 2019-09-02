package com.example.meetingactivity.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetingactivity.Fragment.BoardFragment;
import com.example.meetingactivity.Fragment.CalendarFragment;
import com.example.meetingactivity.Fragment.InforFragment;
import com.example.meetingactivity.Fragment.PhotoFragment;
import com.example.meetingactivity.R;
import com.example.meetingactivity.Response.MemberResponse;
import com.example.meetingactivity.adapter.BoardAdapter;
import com.example.meetingactivity.adapter.ContentsPagerAdapter;
import com.example.meetingactivity.adapter.MemberAdapter;
import com.example.meetingactivity.model.Board;
import com.example.meetingactivity.model.MemberTest;
import com.example.meetingactivity.model.MoimUser;
import com.example.meetingactivity.model.Mypage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class MoimActivity extends AppCompatActivity implements View.OnClickListener, InforFragment.OnFragmentInteractionListener,
        BoardFragment.OnFragmentInteractionListener, CalendarFragment.OnFragmentInteractionListener, PhotoFragment.OnFragmentInteractionListener {

    private Context mContext;
    private TabLayout mTabLayout;

    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentsPagerAdapter;



    //    navigation 기능 객체 선언
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    AsyncHttpClient client;
    List<MemberTest> memberlist;
    ListView drawer_right;
    MemberAdapter memberadapter;
    MemberResponse memberresponse;

    Toolbar toolbar;
    String user_id;
    Mypage item1;



    MoimUser moimUser;
    @Override
    protected void onResume() {
        super.onResume();
        // getIntent 는 onCreate 에 넣을것*
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        item1= (Mypage) getIntent().getSerializableExtra("item");

        mContentsPagerAdapter.setIntent(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //세로모드고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_moim);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        setSupportActionBar(toolbar); //툴바를 액션바와 같게 만들어 준다.

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoimActivity.this, MypageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });



        mContext = getApplicationContext();
        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);

        mTabLayout.addTab(mTabLayout.newTab().setText("정보"));
        mTabLayout.addTab(mTabLayout.newTab().setText("게시판"));
        mTabLayout.addTab(mTabLayout.newTab().setText("사진첩"));
        mTabLayout.addTab(mTabLayout.newTab().setText("일정"));

        mViewPager = (ViewPager) findViewById(R.id.pager_content);

        mContentsPagerAdapter = new ContentsPagerAdapter(
                getSupportFragmentManager(), mTabLayout.getTabCount());

        mViewPager.setAdapter(mContentsPagerAdapter);


        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override

            public void onTabSelected(TabLayout.Tab tab) {
                // MoimActivity.java 에서 안들어감 * 여기는 문제가 없음*
                mViewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        System.out.println("0번 Fragment에 연결되었습니다.");
                        break;
                    case 1:
                        System.out.println("1번 Fragment에 연결되었습니다.");
                        break;
                    case 2:
                        System.out.println("2번 Fragment에 연결되었습니다.");
                        break;
                    case 3:
                        System.out.println("3번 Fragment에 연결되었습니다.");
                        break;
                    case 4:
                        System.out.println("4번 Fragment에 연결되었습니다.");
                        break;
                }

            }

            @Override

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //    navigation 기능 객체 초기화
        mDrawerLayout = (DrawerLayout) findViewById(R.id.moim_main);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        client = new AsyncHttpClient();
        memberlist = new ArrayList<>();
        memberadapter = new MemberAdapter(this, R.layout.member_item, memberlist);
        drawer_right = findViewById(R.id.drawer_right);
        drawer_right.setAdapter(memberadapter);
        memberresponse = new MemberResponse(memberadapter);

    }



    private View createTabView(String tabName) {
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView txt_name = (TextView) tabView.findViewById(R.id.txt_name);
        txt_name.setText(tabName);
        return tabView;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return  super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //각각의 버튼을 클릭할때의 수행할것을 정의해 준다.
        switch (item.getItemId()){
            case R.id.action_search:
                Toast.makeText(getApplicationContext(), "버튼1을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                return  true;
            case R.id.action_member:
                //닫힘 동작
                if(mDrawerLayout.isDrawerOpen(drawer_right)){
                    mDrawerLayout.closeDrawer(drawer_right);
                }
                //열림 동작
                else if(!mDrawerLayout.isDrawerOpen(drawer_right)){
                    mDrawerLayout.openDrawer(drawer_right);
                    if(!memberadapter.isEmpty()) {
                        memberadapter.clear();
                    }
                    String member_URL = "http://192.168.0.93:8080/moim.4t.spring/testMoimUsets.tople";
                    RequestParams params = new RequestParams();
                    params.put("moimcode", 4);
                    client.get(member_URL, params, memberresponse);
                }

                return  true;


            case  R.id.mewnu_fav:
                boolean favValue=Boolean.parseBoolean(item1.getFav());
                if(favValue){
                    favValue = false;
                }else if(favValue==false){
                    favValue=true;
                }
                // item1.setFav(String.valueOf(favValue));
                Toast.makeText(getApplicationContext(),"즐찾여부"+favValue,Toast.LENGTH_SHORT).show();
                RequestParams params = new RequestParams();
                params.put("fav",favValue);

                return true;


            case R.id.menu_manage:
                Toast.makeText(this,"안뇽",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,AdminActivity.class);
                // 인텐트에 데이터 저장
                intent.putExtra("item", item1);
                intent.putExtra("user_id", user_id);
                intent.putExtra("moimCode",item1.getMoimcode());
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View v) {

    }
}

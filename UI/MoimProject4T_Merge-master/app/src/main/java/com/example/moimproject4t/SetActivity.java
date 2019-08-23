package com.example.moimproject4t;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetActivity extends AppCompatActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    Button buttonBackToHome, buttonToEdit;
    Button buttonBackToSet, buttonSave;

    ImageView imageViewToEdit;
    LinearLayout layoutInfo, layoutEdit;
    RadioGroup radioGroupGen;

    TextView textView18, textViewName;
    Button button_logoff;

    String user_id = "";

    String access_token = "";
    String refresh_token = "";

    String profile = "";
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        //타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        button_logoff = findViewById(R.id.button_logoff);
        textView18 = findViewById(R.id.textView18);
        textViewName=findViewById(R.id.textViewName_set);
        radioGroupGen = findViewById(R.id.radioGroupGen);
        buttonBackToSet = findViewById(R.id.buttonBackToSet);
        buttonSave = findViewById(R.id.buttonSave);


        buttonBackToHome = findViewById(R.id.buttonBackToHome);
        buttonToEdit = findViewById(R.id.buttonToEdit);
        imageViewToEdit = findViewById(R.id.imageViewToEdit);
        layoutInfo = findViewById(R.id.layoutInfo);
        layoutEdit = findViewById(R.id.layoutEdit);

        layoutEdit.setVisibility(View.GONE);
        layoutInfo.setVisibility(View.VISIBLE);

        imageViewToEdit.setOnClickListener(this);
        radioGroupGen.setOnCheckedChangeListener(this);
        buttonBackToHome.setOnClickListener(this);
        buttonToEdit.setOnClickListener(this);
        buttonBackToSet.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        button_logoff.setOnClickListener(this);

        requestMe();
    }

    //로그아웃 = 세션 닫기
    private void logout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Toast.makeText(SetActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Log.d("[profile]", "로그아웃 성공");
            }

            @Override
            public void onSuccess(Long result) {
                Toast.makeText(SetActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Log.d("[profile]", "로그아웃 성공 : " + result);
                Intent intent_goLogin = new Intent(SetActivity.this, MainActivity.class);
                intent_goLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_goLogin);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logoff:
                logout();
                break;
            case R.id.buttonBackToHome:
                Intent intent = new Intent(this, MypageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonToEdit:
                layoutInfo.setVisibility(View.GONE);
                layoutEdit.setVisibility(View.VISIBLE);
                intent = new Intent(SetActivity.this, UpdateActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.buttonBackToSet:
                layoutInfo.setVisibility(View.VISIBLE);
                layoutEdit.setVisibility(View.GONE);
                break;
            case R.id.buttonSave:
                intent = new Intent(this, MypageActivity.class);
                startActivity(intent);
                finish();
                // 선택된 라이도 버튼의 ID값 얻기
                int checkedId = radioGroupGen.getCheckedRadioButtonId();
                // 획득한 ID값으로 라디오버튼 객체 할당
                RadioButton radioButton = findViewById(checkedId);
                //문자열 얻기
                String text = radioButton.getText().toString();
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.radioGroupGen:
                RadioButton radioButton = findViewById(checkedId);
                //textView2.setText(radioButton.getText().toString());
                break;
        }
    }

    private void requestMe() {
        //카카오서버에 기존 요청 정보외에 추가 정보(커스텀 파라미터등)을 요청하기 위한 키값
        final List<String> keys = new ArrayList<>();



        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            //v1에서는 세션종료에러, 비회원에러, 그외 에러가 있었으나 기존 v1이 종료되고 v2가 적용됨에 따라 비회원 에러는 사라짐

            //의도치 않은 세션 종료로 인한 에러
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(SetActivity.this, "세션 종료", Toast.LENGTH_SHORT);
                Log.e("[error]", "세션 종료로 인한 에러 발생");

            }

            //세션 종료로 인한 에러를 제외한 모든 에러
            @Override
            public void onFailure(ErrorResult errorResult) {
                String error_message = "SessionClosed외의 에러 발생" + errorResult;
                Logger.d(error_message);
                Log.d("[error]", error_message);
            }

            @Override
            public void onSuccess(MeV2Response response) {


                Long id = response.getId();
                user_id = Long.toString(id);
                name = response.getNickname();
                UserAccount account = response.getKakaoAccount();
                String thumb = response.getThumbnailImagePath();
                profile = response.getProfileImagePath();
                Map prop = response.getProperties();
                String region = (String) prop.get("region");

                Log.d("[profile]", "user_id_set:" + user_id);
                Log.d("[profile]", "name_set:" + name);
                Log.d("[profile]", "thumb_set:" + thumb);
                Log.d("[profile]", "profile_set:" + profile);
                Log.d("[profile]", "region_set:"+region);
                Log.d("[profile]", "prop_set: "+ prop);


                requestAccessTokenInfo();
                handleScopeError(account);

            }

        });
    }


    //SDK환경하에서 액세스토큰의 유효성을 검사하는 메소드
    private void requestAccessTokenInfo() {
        AuthService.getInstance().requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(SetActivity.this, "세션 종료", Toast.LENGTH_SHORT);
                Log.e("[error]", "세션 종료로 인한 에러 발생");
            }


            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e("failed to get access token info. msg=" + errorResult);
            }

            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                String userId2 = Long.toString(userId);
                Logger.d("this access token is for userId=" + userId);
                Log.d("[profile]", "userId2 : " + userId2);

                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                String exTime = Long.toString(expiresInMilis);
                Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");
                Log.d("[profile]", "토큰유효시간 :" + exTime + "m/s");

                Glide.with(SetActivity.this).load(profile).into(imageViewToEdit);
                textViewName.setText(name);
            }
        });
    }

    //동적 동의 일종의 퍼미션체크 + 액세스토큰과 리프레쉬토큰에 대한 발급을 담당
    private void handleScopeError(UserAccount account) {
        List<String> neededScopes = new ArrayList<>();
        if (account.needsScopeAccountEmail()) {
            neededScopes.add("account_email");
        }
        if (account.needsScopeGender()) {
            neededScopes.add("gender");
        }
        Session.getCurrentSession().updateScopes(this, neededScopes, new
                AccessTokenCallback() {
                    @Override
                    public void onAccessTokenReceived(AccessToken accessToken) {
                        // 유저에게 성공적으로 동의를 받음. 부가정보에 접근 가능한 토큰을 재발급 받게 됨.
                        //Log.d("[token]", "sucess/"+accessToken);
                        access_token= accessToken.getAccessToken();
                        refresh_token = accessToken.getRefreshToken();

                        Log.d("[token]", "ACCESS TOKEN_1 : " + access_token); //REST-API에 접근할 수 있는 토큰
                        Log.d("[token]", "RefreshToken_1 : " + refresh_token);

                    }

                    @Override
                    public void onAccessTokenFailure(ErrorResult errorResult) {
                        // 동의 얻기 실패
                        Log.d("[token]", "fail/" + errorResult);
                    }
                }
                );


    }
}
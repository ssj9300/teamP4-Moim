package com.example.moimproject4t;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.User;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SessionCallback callback;

    Button button;
    Button button_logout;
    LoginButton btn_kakao;

    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button_login);
        btn_kakao=findViewById(R.id.btn_kakao_login);
        button_logout=findViewById(R.id.button_logout);

        button.setOnClickListener(this);
        button_logout.setOnClickListener(this);

        getHash();
        permissionCheck();


        //세션콜
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        if(Session.getCurrentSession().isOpened()) {
            requestMe();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    skipLogin();

                }
            }, 2000);// 2초 정도 딜레이를 준 후 시작
        }



    }


    //해쉬키얻기 + 퍼미션 체크 영역
    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }
    }

    private void getHash() {

        try{                                                                    //manifest에서 확인 가능한 패키지 네임
            PackageInfo info = getPackageManager().getPackageInfo("com.example.moimproject4t", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("[hash]", "hash="+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                btn_kakao.performClick();
                break;

            case R.id.button_logout:
                logout();
                break;
        }

    }


    //온클릭시 동작할 메소드

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //세션상태 변경시 동작


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback{
        //세션 오픈 성공
        @Override
        public void onSessionOpened() {
            //카카오톡에서 정보를 받아올 메소드
            requestMe();
        }

        //세션 오픈 실패
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception!=null){
                Logger.e(exception);
            }

        }
    }

    private void requestMe() {
        List<String> keys = new ArrayList<>();

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            //v1에서는 세션종료에러, 미가입자에러, 그외 에러가 있었으나 기존 v1이 종료되고 v2가 적용됨에 따라 비회원 에러는 사라짐

            //의도치 않은 세션 종료로 인한 에러
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(MainActivity.this, "세션 종료", Toast.LENGTH_SHORT);
                Log.e("[error]","세션 종료로 인한 에러 발생");

            }

            //세션 종료로 인한 에러를 제외한 모든 에러
            @Override
            public void onFailure(ErrorResult errorResult) {
                String error_message ="SessionClosed외의 에러 발생"+errorResult;
                Logger.d(error_message);
                Log.d("[error]", error_message);
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Long id = response.getId();
                user_id=Long.toString(id);
                String name=response.getNickname();
                UserAccount account = response.getKakaoAccount();
                String thumb = response.getThumbnailImagePath();
                String profile = response.getProfileImagePath();

                Log.d("[profile]", "token:"+user_id);
                Log.d("[profile]", "name:"+name);
                Log.d("[profile]", "thumb:"+thumb);
                Log.d("[profile]", "profile:"+profile);

                requestAccessTokenInfo();
               handleScopeError(account);
            }
        });
    }

    private void requestAccessTokenInfo() {
        AuthService.getInstance().requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(MainActivity.this, "세션 종료", Toast.LENGTH_SHORT);
                Log.e("[error]","세션 종료로 인한 에러 발생");
            }



            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e("failed to get access token info. msg=" + errorResult);
            }

            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                String token2=Long.toString(userId);
                Logger.d("this access token is for userId=" + userId);
                Log.d("[profile]", "토큰2 : " + token2);

                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
                String exTime = Long.toString(expiresInMilis);
                Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");
                Log.d("[profile]", "토큰유효시간 :"+ exTime +"m/s");

                if (!token2.equals(null)){
                    skipLogin();
                }

            }
        });
    }

    //로그아웃
    private void logout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Toast.makeText(MainActivity.this,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                Log.d("[profile]", "로그아웃 성공");
            }

            @Override
            public void onSuccess(Long result) {
                Toast.makeText(MainActivity.this,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                Log.d("[profile]", "로그아웃 성공 : " + result);
            }
        });
    }

    private void skipLogin() { //세션이 연결되어 있으면 자동으로 넘기기

        Intent intent =new Intent(MainActivity.this, ListActivity.class);

        intent.putExtra("user_id", user_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    //동적 동의 일종의 퍼미션체크
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
                        // 유저에게 성공적으로 동의를 받음. 토큰을 재발급 받게 됨.
                        //Log.d("[token]", "sucess/"+accessToken);
                       String AcessToken = accessToken.getAccessToken();
                        String RefreshToken = accessToken.getRefreshToken();

                        Log.d("[token]","ACCESS TOKEN_1 : " + AcessToken);
                        Log.d("[token]", "RefreshToken_1 : "+RefreshToken);

                    }

                    @Override
                    public void onAccessTokenFailure(ErrorResult errorResult) {
                        // 동의 얻기 실패
                        Log.d("[token]", "fail/"+errorResult);
                    }
                });
    }

}

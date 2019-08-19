package com.example.moimproject4t;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText1, editText2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        editText1=findViewById(R.id.editText_name);
        editText2=findViewById(R.id.editText_region);
        requestMe();

    }

    @Override
    public void onClick(View v) {

    }

    private void requestMe() {
        List<String> keys = new ArrayList<>();

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            //v1에서는 세션종료에러, 미가입자에러, 그외 에러가 있었으나 기존 v1이 종료되고 v2가 적용됨에 따라 비회원 에러는 사라짐

            //의도치 않은 세션 종료로 인한 에러
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(UpdateActivity.this, "세션 종료", Toast.LENGTH_SHORT);
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
                String user_id=Long.toString(id);
                String name=response.getNickname();
                UserAccount account = response.getKakaoAccount();
                String thumb = response.getThumbnailImagePath();
                String profile = response.getProfileImagePath();

                Log.d("[profile]", "user_id_3:"+user_id);
                Log.d("[profile]", "name_3:"+name);
                Log.d("[profile]", "thumb_3:"+thumb);
                Log.d("[profile]", "profile_3:"+profile);


                handleScopeError(account);
            }
        });
    }

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
                       // Log.d("[token]", "sucess/"+accessToken);
                        String AcessToken = accessToken.getAccessToken();
                        String RefreshToken = accessToken.getRefreshToken();

                        Log.d("[token]","ACCESS TOKEN_3 : " + AcessToken);
                        Log.d("[token]", "RefreshToken_3 : "+RefreshToken);

                    }

                    @Override
                    public void onAccessTokenFailure(ErrorResult errorResult) {
                        // 동의 얻기 실패
                        Log.d("[token]", "fail/"+errorResult);
                    }
                });
    }
}

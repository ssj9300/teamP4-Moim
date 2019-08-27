package com.example.moimproject4t;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moimproject4t.helper.PhotoHelper;
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
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.helper.log.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SetActivity extends AppCompatActivity
        implements View.OnClickListener {
    LinearLayout layoutInfo, layoutEdit;

    /**
     * 레이아웃 인포 영역
     **/
    Button buttonBackToHome, buttonToEdit;
    ImageView imageViewToEdit;
    TextView textViewName;
    Button button_logoff;


    /**
     * 레이아웃 에딧 영역
     **/
    Button buttonBackToSet, buttonSave, buttonOut;
    ImageView imageView_profile;
    EditText editText_name, editText_intro, editText_pnum, editText_gender;
    TextView set_birth, set_region;

    /**
     * 기타 전역 변수 및 Async 객체 초기화
     **/

    String access_token = "";  //REST-API접근을 위한 액세스토큰
    String refresh_token = ""; //액세스 토큰 리프레쉬 토큰

    String profile = ""; //프로필 사진 URL
    String name = "";   //닉네임
    String birthday = "";   //생일(mm/dd)
    String region = ""; //거주지역(기본값 null)
    String user_id = ""; //user 개별 id
    String phone_num = ""; //핸드폰 번호
    String self_int = ""; //자기소개
    String gender = ""; //성별


    String filePath = ""; //사진경로

    String URL_Photo = ""; //사진 저장용 URL
    String URL_Fire = ""; //회원 탈퇴용 URL

    HttpResponse response;

    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);


        layoutInfo = findViewById(R.id.layoutInfo);
        layoutEdit = findViewById(R.id.layoutEdit);

        /**레이아웃 인포 영역**/
        buttonBackToHome = findViewById(R.id.buttonBackToHome); // 홈화면으로 돌아가기
        imageViewToEdit = findViewById(R.id.imageViewToEdit); //인포 화면 프로필 사진
        textViewName = findViewById(R.id.textViewName_set); //인포 화면 이름 출력
        buttonToEdit = findViewById(R.id.buttonToEdit); //정보 수정화면으로 이동 버튼


        /**레이아웃 에딧 영역**/
        buttonSave = findViewById(R.id.buttonSave); //저장
        button_logoff = findViewById(R.id.button_logoff); //카카오 회원 로그 아웃
        buttonOut = findViewById(R.id.buttonOut); //회원탈퇴
        buttonBackToSet = findViewById(R.id.buttonBackToSet); //정보수정화면에서 인포화면으로 이동

        imageView_profile = findViewById(R.id.imageViewFace); // 정보수정화면 프로필 출력

        set_region = findViewById(R.id.set_region); //지역 설정

        editText_name = findViewById(R.id.editText_name); //이름 수정
        editText_intro = findViewById(R.id.editText_intro); // 자기소개 수정
        editText_gender = findViewById(R.id.editText_gender); //성별 수정
        editText_pnum = findViewById(R.id.editText_pnum); //전화번호 수정

        /** 기타 **/
        client = new AsyncHttpClient();
        response = new HttpResponse(this);

        imageViewToEdit.setOnClickListener(this);
        buttonBackToHome.setOnClickListener(this);
        buttonToEdit.setOnClickListener(this);
        buttonBackToSet.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        button_logoff.setOnClickListener(this);
        set_region.setOnClickListener(this);
        buttonOut.setOnClickListener(this);
        imageView_profile.setOnClickListener(this); //사진 선택을 위해 이미지뷰에 온클릭리스너 설정함.

        /** 레이아웃 변환 **/
        layoutEdit.setVisibility(View.GONE);
        layoutInfo.setVisibility(View.VISIBLE);

        requestMe(); //카카오 프로필 정보 읽어오기


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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                break;
            case R.id.buttonToEdit:
                layoutInfo.setVisibility(View.GONE);
                layoutEdit.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonBackToSet:
                layoutInfo.setVisibility(View.VISIBLE);
                layoutEdit.setVisibility(View.GONE);
                break;
            case R.id.buttonSave:
                Log.d("[update]", "test");
                update_profile();
                break;

            case R.id.set_region:
                Log.d("[info]", "test");
                region_set();
                break;

            case R.id.buttonOut:
                onClickUnlink();
                break;

            case R.id.imageViewFace:
                selectPhoto();

        }
    }

    class HttpResponse extends AsyncHttpResponseHandler {

        Activity activity;
        ProgressDialog dialog;

        public HttpResponse(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onStart() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("잠시만 기다려 주세요...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        public void onFinish() {
            dialog.dismiss();
            dialog = null;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            String strJson = new String(responseBody);
            try {
                JSONObject json = new JSONObject(strJson);
                String rt = json.getString("rt");

                if (rt.equals("OK")) {
                    Toast.makeText(activity, "사진저장성공", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(activity, "사진저장실패", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(activity, "통신실패" + statusCode, Toast.LENGTH_SHORT).show();
        }
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
                intent_goLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_goLogin.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_goLogin);
                finish();
            }
        });
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
                profile = response.getProfileImagePath();
                Map prop = response.getProperties();
                region = (String) prop.get("region");
                birthday = response.getKakaoAccount().getBirthday();
                self_int = (String) prop.get("self_int");
                phone_num = (String) prop.get("phone_num");
                Gender gender2 = response.getKakaoAccount().getGender();
                gender = String.valueOf(gender2);


                Log.d("[profile]", "user_id_set:" + user_id);
                Log.d("[profile]", "name_set:" + name);
                Log.d("[profile]", "profile_set:" + profile);
                Log.d("[profile]", "region_set:" + region);
                Log.d("[profile]", "prop_set: " + prop);
                Log.d("[profile]", "birthday_set:" + birthday);
                Log.d("[profile]", "phone_num_set:" + phone_num);
                Log.d("[profile]", "self_int_set:" + self_int);
                Log.d("[profile]", "gender:" + gender);


                requestAccessTokenInfo();
                handleScopeError(account);


                if ((name != null)) {//이름 출력
                    editText_name.setText(name);
                    textViewName.setText(name);
                }
                if ((self_int != null)) { //자기소개 출력
                    editText_intro.setText(self_int);
                }
                if ((phone_num != null)) { //전화번호 출력
                    editText_pnum.setText(phone_num);
                }
                if ((region != null)) { //거주지역 출력
                    set_region.setText(region);
                }
                if ((birthday != null)) { //생일 출력
                    String birth_mm = birthday.substring(0, 2);
                    String birth_dd = birthday.substring(2);
                    set_birth.setText(birth_mm + "월 " + birth_dd + "일");
                }

                if (gender != "null") { //성별 출력
                    if (gender == "MALE") {
                        editText_gender.setText("남성");
                    } else if (gender == "FEMALE") {
                        editText_gender.setText("여성");
                    }
                }

                if (profile != null) { //프로필 이미지 출력
                    Glide.with(SetActivity.this).load(profile).into(imageViewToEdit);
                    Glide.with(SetActivity.this).load(profile).into(imageView_profile);
                }

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
                        access_token = accessToken.getAccessToken();
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

    //회원탈퇴
    private void onClickUnlink() {
        final String appendMessage = "회원 탈퇴 하시겠습니까?";
        new AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() {

                                    }

                                    @Override
                                    public void onSuccess(Long userId) {
                                        Toast.makeText(SetActivity.this, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

        /**
         *
         * 회원 탈퇴 DB처리 코드 넣을 영역
         * 탈퇴처리를 위해 유저아이디 값을 보낼 서버 URL등록 필요
         *
         */

        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        client.post(URL_Fire, params, response);
    }

    private void region_set() {
        final String[] items = new String[]{"서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"};
        final int[] selectedIndex = {0};

        AlertDialog.Builder dialog = new AlertDialog.Builder(SetActivity.this);
        dialog.setTitle("지역 선택").setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex[0] = which;
            }
        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                set_region.setText(items[selectedIndex[0]]);
            }
        });
        dialog.show();
    }


    private void update_profile() {


        String update_nickname = editText_name.getText().toString();
        String update_selfInt = editText_intro.getText().toString();
        String update_region = set_region.getText().toString();
        String update_pNum = editText_pnum.getText().toString();

        Log.d("[update]", update_nickname + "/" + update_pNum + "/" + update_region + "/" + update_selfInt);

        //사진 서버 저장
        RequestParams params = new RequestParams();
        try {
            params.put("photo", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(URL_Photo, params, response);

        //프로파일 URL
        String profile_url = "";

        //카카오 저장
        final Map<String, String> properties = new HashMap<String, String>();

        if (update_nickname != null) {
            properties.put("nickname", update_nickname);
        }

        if (update_region != null) {
            properties.put("region", update_region);
        }

        if (update_selfInt != null) {
            properties.put("self_int", update_selfInt);
        }
        if (update_pNum != null) {
            properties.put("phone_num", update_pNum);
        }


        UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
            @Override
            public void onSuccess(Long userId) {
                Toast.makeText(SetActivity.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                layoutEdit.setVisibility(View.GONE);
                layoutInfo.setVisibility(View.VISIBLE);
                requestMe();
            }

            @Override
            public void onNotSignedUp() {
                Log.d("[update]", "test2");
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.d("[error]", message);
                Toast.makeText(SetActivity.this, "수정실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("[update]", "test3");
            }
        }, properties);


    }

    private void selectPhoto() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        filePath = PhotoHelper.getInstance().getNewPhotoPath();
                        //카메라 앱을 위한 압묵적 인텐트
                        File file = new File(filePath);
                        Uri uri = null;
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", file);
                            camera_intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            camera_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        Log.d("[Info]", uri.toString());
                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(camera_intent, 100);

                        break;

                    case 1:
                        Intent gintent = null;
                        if (Build.VERSION.SDK_INT >= 19) {
                            gintent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            gintent.addCategory(Intent.CATEGORY_OPENABLE);
                        } else {
                            gintent = new Intent(Intent.ACTION_GET_CONTENT);
                        }

                        //이미지 파일 필터링
                        gintent.setType("image/*");
                        gintent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(gintent, 101);

                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 100:
                //카메라 촬영결과를 갤러리에 등록
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath));
                Log.d("[Info]", "filepath : " + filePath);
                sendBroadcast(photoIntent);
                break;
            case 101:

                filePath = com.example.moimproject4t.helper.FileUtils.getPath(this, data.getData());
                Log.d("[Info]", "filepath : " + filePath);


                break;
        }

        Log.d("[test]", filePath);
        Glide.with(this).load(filePath).into(imageView_profile);

    }
}
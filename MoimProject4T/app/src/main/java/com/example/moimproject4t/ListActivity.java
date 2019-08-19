package com.example.moimproject4t;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    Button button;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        textView=findViewById(R.id.textView);
        button=findViewById(R.id.button_logoff);
        button2=findViewById(R.id.button_update);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);


        Intent intent = getIntent();

        String user_id = intent.getStringExtra("user_id");

        Log.d("[getData]", "user_id_2 : "+user_id);

        textView.setText("id : "+user_id);
    }

    private void logout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Toast.makeText(ListActivity.this,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                Log.d("[profile]", "로그아웃 성공");
            }

            @Override
            public void onSuccess(Long result) {
                Toast.makeText(ListActivity.this,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                Log.d("[profile]", "로그아웃 성공 : " + result);
                Intent intent_goLogin = new Intent(ListActivity.this, MainActivity.class);
                intent_goLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_goLogin);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_logoff:
                logout();
                break;

            case R.id.button_update:
                Intent intent = new Intent(ListActivity.this, UpdateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }
}

package com.example.meetingactivity.Response;

import com.example.meetingactivity.adapter.CalendarAdapter;
import com.example.meetingactivity.model.Calendar;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CalendarResponse extends AsyncHttpResponseHandler {
    CalendarAdapter adapter;

    public CalendarResponse(CalendarAdapter adapter) {
        this.adapter = adapter;
    }

    // 통신 성공시
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

        // 통신 데이터 처리
        String content = new String(responseBody);
        try {
            JSONObject json = new JSONObject(content);
            JSONArray items = json.getJSONArray("items");
//            검색 결과 처리
            for(int i = 0; i < items.length(); i++){
                JSONObject jsonObject = items.getJSONObject(i);

                Calendar calendar = new Calendar();
                calendar.setSch_amount(jsonObject.getInt("sch_amount"));
//                calendar.setSch_moimcode();
//                중복 체크 확인할 것
                adapter.add(calendar);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 통신 실패시
    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
    }
}

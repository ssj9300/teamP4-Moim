package com.example.meetingactivity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.meetingactivity.R;
import com.example.meetingactivity.model.MemberTest;

import java.util.List;

public class MemberAdapter extends ArrayAdapter<MemberTest> {
    Activity activity;
    int resource;

    public MemberAdapter(Context context, int resource, List<MemberTest> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        MemberTest item = getItem(position);
        if(item != null){
            TextView memitem_name = convertView.findViewById(R.id.memitem_name);
            TextView memitem_permit = convertView.findViewById(R.id.memitem_permit);
            ImageView memitem_img = convertView.findViewById(R.id.memitem_img);

            if(!item.getThumbnail_image().equals("")){
                Glide.with(memitem_img).load(item.getThumbnail_image()).apply(RequestOptions.circleCropTransform()).error(R.drawable.ic_error_w).placeholder(R.drawable.ic_empty_b).into(memitem_img);
            }
            memitem_name.setText(item.getNickname());
            memitem_permit.setText(Integer.toString(item.getPermit()));
        }
        return convertView;
    }
}

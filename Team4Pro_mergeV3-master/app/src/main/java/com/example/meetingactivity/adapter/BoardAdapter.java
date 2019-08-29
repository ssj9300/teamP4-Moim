package com.example.meetingactivity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.meetingactivity.R;
import com.example.meetingactivity.model.Board;

import java.util.List;

public class BoardAdapter extends ArrayAdapter<Board> {
    Activity activity;
    int resource;

    public BoardAdapter(Context context, int resource,List<Board> objects) {
        super(context, resource, objects);
        activity=(Activity)context;
        this.resource=resource;

    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);
        }

        Board item = getItem(position);

        if (item != null) {
            TextView textViewSubject = convertView.findViewById(R.id.textViewSubject);
            TextView textViewContent = convertView.findViewById(R.id.textViewContent);

            textViewSubject.setText(item.getSubject());
            textViewContent.setText(item.getContent());
        }

        return convertView;
    }
}

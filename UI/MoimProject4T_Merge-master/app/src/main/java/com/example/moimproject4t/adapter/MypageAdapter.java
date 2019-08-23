package com.example.moimproject4t.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.moimproject4t.R;
import com.example.moimproject4t.model.Mypage;

import java.util.List;

public class MypageAdapter extends ArrayAdapter<Mypage> {
    Activity activity;
    int resource;

    public MypageAdapter(Context context, int resource, List<Mypage> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        System.out.println("position = " + position);
        // 212, 244, 250

        //Always set the item clicked blue background
        View itemView = convertView;
        if(itemView == null) {
            itemView = activity.getLayoutInflater().inflate(resource, null);
        }
        Mypage item = getItem(position); // position : List클래스의 위치값 getList -> getItem

        if(item != null) {
            // ☆ ★
            int wineInt = itemView.getResources().getColor(R.color.wine);
            int darkBrownInt = itemView.getResources().getColor(R.color.darkBrown);
            int darkGoldInt = itemView.getResources().getColor(R.color.darkGold);
            int khakiInt = itemView.getResources().getColor(R.color.khaki);
            int sadGreenInt = itemView.getResources().getColor(R.color.sadGreen);
            int strongGreenInt = itemView.getResources().getColor(R.color.strongGreen);
            int gloomyGreenInt = itemView.getResources().getColor(R.color.gloomyGreen);
            int navyInt = itemView.getResources().getColor(R.color.navy);
            int lightVioletInt = itemView.getResources().getColor(R.color.lightViolet);
            int violetInt = itemView.getResources().getColor(R.color.violet);
            int darkWineInt = itemView.getResources().getColor(R.color.darkWine);
            int absoluteBlackInt = itemView.getResources().getColor(R.color.absoluteBlack);

            String wine = "wine";
            String darkBrown ="darkBrown";
            String darkGold ="darkGold";
            String khaki ="khaki";
            String sadGreen ="sadGreen";
            String strongGreen ="strongGreen";
            String gloomyGreen ="gloomyGreen";
            String navy ="navy";
            String lightViolet ="lightViolet";
            String violet ="violet";
            String darkWine ="darkWine";
            String absoluteBlack ="absoluteBlack";



//            int wine = parent.getResources().getColor(R.color.wine);
//            int darkBrown = parent.getResources().getColor(R.color.darkBrown);
//            int darkGold = parent.getResources().getColor(R.color.darkGold);
//            int khaki = parent.getResources().getColor(R.color.khaki);
//            int sadGreen = parent.getResources().getColor(R.color.sadGreen);
//            int strongGreen = parent.getResources().getColor(R.color.strongGreen);
//            int gloomyGreen = parent.getResources().getColor(R.color.gloomyGreen);
//            int navy = parent.getResources().getColor(R.color.navy);
//            int lightViolet = parent.getResources().getColor(R.color.lightViolet);
//            int violet = parent.getResources().getColor(R.color.violet);
//            int darkWine = parent.getResources().getColor(R.color.darkWine);
//            int absoluteBlack = parent.getResources().getColor(R.color.absoluteBlack);

            System.out.println("absoluteBlackInt = " + absoluteBlackInt);


            if (item.getColor().equals(String.valueOf(wine))) {
                itemView.setBackgroundColor(wineInt);
            }
            if (item.getColor().equals(String.valueOf(darkBrown))) {
                itemView.setBackgroundColor(darkBrownInt);
            }
            if (item.getColor().equals(String.valueOf(darkGold))) {
                itemView.setBackgroundColor(darkGoldInt);
            }
            if (item.getColor().equals(String.valueOf(khaki))) {
                itemView.setBackgroundColor(khakiInt);
            }
            if (item.getColor().equals(String.valueOf(sadGreen))) {
                itemView.setBackgroundColor(sadGreenInt);
            }
            if (item.getColor().equals(String.valueOf(strongGreen))) {
                itemView.setBackgroundColor(strongGreenInt);
            }
            if (item.getColor().equals(String.valueOf(gloomyGreen))) {
                itemView.setBackgroundColor(gloomyGreenInt);
            }
            if (item.getColor().equals(String.valueOf(navy))) {
                itemView.setBackgroundColor(navyInt);
            }
            if (item.getColor().equals(String.valueOf(lightViolet))) {
                itemView.setBackgroundColor(lightVioletInt);
            }
            if (item.getColor().equals(String.valueOf(violet))) {
                itemView.setBackgroundColor(violetInt);
            }
            if (item.getColor().equals(String.valueOf(darkWine))) {
                itemView.setBackgroundColor(darkWineInt);
            }
            if (item.getColor().equals(String.valueOf(absoluteBlack))) {
                itemView.setBackgroundColor(absoluteBlackInt);
            }



            TextView textView1 = itemView.findViewById(R.id.textMoimname);
            TextView textView2 = itemView.findViewById(R.id.textProd);
            TextView textView3 = itemView.findViewById(R.id.textCount);
            TextView textView4 = itemView.findViewById(R.id.textFav);

            textView1.setText(item.getMoimname());
            textView2.setText(item.getProd());
            textView3.setText(item.getColor()); // next Time!
            textView4.setText(item.getLoca()); // next Time!
        }
        //4. 1줄화면을 되돌려줌
        return itemView;
    }
}










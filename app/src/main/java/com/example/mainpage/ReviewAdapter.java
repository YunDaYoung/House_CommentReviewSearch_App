package com.example.mainpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mainpage.user.Review;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Review> data;
    private int layout;

    public ReviewAdapter(Context context, int layout, ArrayList<Review> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}

    @Override
    public Review getItem(int position){return data.get(position);}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }

        Review review = data.get(position);
        TextView name1=(TextView)convertView.findViewById(R.id.reviewUserMail);
        name1.setText("사용자 이메일 : " + review.getUser_mail());
        TextView reviewText1=(TextView)convertView.findViewById(R.id.reviewText);
        reviewText1.setText(review.getUser_review());

        return convertView;
    }
}

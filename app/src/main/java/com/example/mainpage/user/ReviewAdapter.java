package com.example.mainpage.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainpage.House;
import com.example.mainpage.R;
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
        TextView text1=(TextView)convertView.findViewById(R.id.rwText1);
        text1.setText("리뷰 내용 : " + review.getUser_review());


        return convertView;
    }
}

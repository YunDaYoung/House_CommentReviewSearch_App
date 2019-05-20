package com.example.mainpage;

import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<House> data;
    private int layout;

    public ListViewAdapter(Context context, int layout, ArrayList<House> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}

    @Override
    public House getItem(int position){return data.get(position);}

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }

        House house = data.get(position);
        ImageView pic=(ImageView)convertView.findViewById(R.id.imageview);
        pic.setImageResource(house.getHousePic());
        TextView name1=(TextView)convertView.findViewById(R.id.text1);
        name1.setText("가격 : " + house.getHousePrice());
        TextView name2=(TextView)convertView.findViewById(R.id.text2);
        name2.setText("면적 : " +house.getHouseSpace());
        TextView name3=(TextView)convertView.findViewById(R.id.text3);
        name3.setText("주소 : " + house.getHouseAddress());
        TextView name4=(TextView)convertView.findViewById(R.id.text4);
        name4.setText("기타설명 : " + house.getHouseComment());

        return convertView;
    }
}
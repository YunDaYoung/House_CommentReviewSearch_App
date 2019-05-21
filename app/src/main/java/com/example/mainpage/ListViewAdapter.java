package com.example.mainpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<House> data;
    private int layout;

    ImageView pic;
    Bitmap bmImg;
    DownloadImageTask task;

    House house;

    public ListViewAdapter(Context context, int layout, ArrayList<House> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public House getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        house = data.get(position);
        new DownloadImageTask((ImageView) convertView.findViewById(R.id.imageview)).execute(("http://54.180.79.233:3000/" + house.getHousePic()));
        TextView name1 = (TextView) convertView.findViewById(R.id.text1);
        name1.setText("가격 : " + house.getHousePrice());
        TextView name2 = (TextView) convertView.findViewById(R.id.text2);
        name2.setText("면적 : " + house.getHouseSpace());
        TextView name3 = (TextView) convertView.findViewById(R.id.text3);
        name3.setText("주소 : " + house.getHouseAddress1() + house.getHouseAddress2() + house.getHouseAddress3());
        TextView name4 = (TextView) convertView.findViewById(R.id.text4);
        name4.setText("기타설명 : " + house.getHouseComment());

        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
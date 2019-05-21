package com.example.mainpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class OwnerMypageListViewAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private ArrayList<House> data;
    private int layout;

    ImageView pic;
    Bitmap bmImg;
    DownloadImageTask task;

    House house;

    public OwnerMypageListViewAdapter(Context context, int layout, ArrayList<House> data){
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

        house = data.get(position);
        ImageView pic=(ImageView)convertView.findViewById(R.id.imageview);
        new DownloadImageTask((ImageView) convertView.findViewById(R.id.imageview)).execute(("http://54.180.79.233:3000/" + house.getHousePic()));


        Button updateBtn = (Button)convertView.findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button deleteBtn = (Button)convertView.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://54.180.79.233:3000/houseDelete/:" + house.getHouseIdx();   //인덱스 post로 보내주기

                HouseDelete hd = new HouseDelete();
                hd.execute(url);
            }
        });

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

    public class HouseDelete extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls){
            try {
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.

                    con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.connect();//연결 수행

                    return null;

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }

                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}

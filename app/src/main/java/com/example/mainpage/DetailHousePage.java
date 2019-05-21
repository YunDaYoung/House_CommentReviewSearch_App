package com.example.mainpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailHousePage extends AppCompatActivity{

    String url;

    ArrayList<House> houseList = new ArrayList<House>();
    ArrayList<Review> reviewList = new ArrayList<Review>();

    Intent intent;

    ReviewAdapter adapter;
    CheckBox goodBtn;
    ImageView imageView;
    TextView price, address, space, comment;
    ListView reviewListView;
    Button logoutBtn;
    ScrollView sv2;

    JSONTask3 reviewOutput = new JSONTask3();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.detail_house);

            goodBtn = (CheckBox) findViewById(R.id.goodBtn1);
            imageView = (ImageView) findViewById(R.id.h_image);
            price = (TextView) findViewById(R.id.price);
            address = (TextView) findViewById(R.id.address);
            space = (TextView) findViewById(R.id.space);
            comment = (TextView) findViewById(R.id.comment);
            reviewListView = (ListView) findViewById(R.id.reviewListView);
            logoutBtn = (Button) findViewById(R.id.logoutButton);
            sv2 = (ScrollView) findViewById(R.id.sv2);

            //String houseIdx = intent.getParcelableExtra("HouseIndex");
            //url = "http://54.180.79.233:3000/houseView/:houseIdx";
            url = "http://54.180.79.233:3000/recently";

            reviewOutput.execute(url);


            reviewListView.setOnTouchListener(new View.OnTouchListener() {        //리스트뷰 터취 리스너
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    sv2.requestDisallowInterceptTouchEvent(true);    // 리스트뷰에서 터치가되면 스크롤뷰만 움직이게
                    return false;
                }
            });
        }
    public class JSONTask3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls){
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);//url을 가져온다.

                    con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.connect();//연결 수행


                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

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
            //Log.d("recently", result);
            try {
                JSONObject getKey= new JSONObject(result);

                //Log.d("jsonObject: ", getKey.getString("data").toString());
                JSONArray jsonArray1 = new JSONArray(getKey.getString("data").toString());

                for(int i =0; i< jsonArray1.length(); i++){
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    houseList.add(new House(
                            jsonObject.getString("houseIdx"),
                            jsonObject.getString("housePic"),
                            jsonObject.getString("housePrice"),
                            jsonObject.getString("houseSpace"),
                            jsonObject.getString("houseComment"),
                            jsonObject.getString("houseAddress1"),
                            jsonObject.getString("houseAddress2"),
                            jsonObject.getString("houseAddress3"),
                            jsonObject.getString("userMail")

                    ));
                    Log.d("House" + i + ":", houseList.get(i).toString());
                }

                /*
                JSONArray jsonArray2 = new JSONArray(getKey.getString("review").toString());
                for(int i =0; i< jsonArray2.length(); i++){
                    JSONObject jsonObject = jsonArray2.getJSONObject(i);
                    reviewList.add(new Review(
                            jsonObject.getString("user_Email"),
                            jsonObject.getString("review")
                    ));
                    Log.d("Review" + i + ":", reviewList.get(i).toString());
                }*/

                reviewList.add(new Review("ydiosa98", "좋아용!"));
                reviewList.add(new Review("윤다영", "좋아용!!"));
                reviewList.add(new Review("dkgkrltlfgek", "좋아용!!!"));

                adapter = new ReviewAdapter(DetailHousePage.this, R.layout.reveiw_list_item, reviewList);
                reviewListView.setAdapter(adapter);

                //imageView.setImageResource(houseList.get(0).getHousePic());
                price.setText(houseList.get(0).getHousePrice());
                address.setText(houseList.get(0).getHouseAddress1() + houseList.get(0).getHouseAddress2() + houseList.get(0).getHouseAddress3());
                space.setText(houseList.get(0).getHouseSpace());
                comment.setText(houseList.get(0).getHouseComment());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

package com.example.mainpage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainpage.user.Review;

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

public class DetailHousePage extends AppCompatActivity{

    String url;

    ArrayList<House> houseList = new ArrayList<House>();
    ArrayList<Review> reviewList = new ArrayList<Review>();

    ReviewAdapter adapter;
    CheckBox goodBtn;
    ImageView imageView;
    TextView price, address, space, comment;
    ListView reviewListView;
    // Button logoutBtn;
    Button houseDeleteBtn, houseUpdateBtn;
    ScrollView sv2;

    Bitmap bmImg;

    String houseIdx;

    JSONTask3 reviewOutput = new JSONTask3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_house);

        goodBtn = (CheckBox) findViewById(R.id.goodBtn1);
        price = (TextView) findViewById(R.id.price);
        address = (TextView) findViewById(R.id.address);
        space = (TextView) findViewById(R.id.space);
        comment = (TextView) findViewById(R.id.comment);
        reviewListView = (ListView) findViewById(R.id.reviewListView);
        sv2 = (ScrollView) findViewById(R.id.sv2);
        imageView = (ImageView)findViewById(R.id.h_image);


        houseDeleteBtn = (Button)findViewById(R.id.houseDeleteBtn) ;
        houseUpdateBtn = (Button)findViewById(R.id.houseUpdateBtn);


        Intent intent1 = getIntent();
        houseIdx = intent1.getExtras().getString("HouseIndex");
        Log.d("Index", houseIdx);
        url = "http://54.180.79.233:3000/houseView/" + houseIdx;


        houseDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://54.180.79.233:3000/houseDelete/" + houseIdx;   //인덱스 post로 보내주기
                Log.d("idx", houseIdx + ":" + url);

                HouseDelete hd = new HouseDelete();
                hd.execute(url);

            }
        });

        houseUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DetailHousePage.this, HouseUpdate.class);
                intent2.putExtra("HouseIndex", houseIdx);
                startActivity(intent2);
            }
        });


        reviewOutput.execute(url);

        if(SaveSharedPreference.getUserName(DetailHousePage.this).length() != 0){
            if(SaveSharedPreference.getUserCheck(DetailHousePage.this).equals("1")){
                goodBtn.setVisibility(View.INVISIBLE);
          }
            else {
                houseDeleteBtn.setVisibility(View.INVISIBLE);
                houseUpdateBtn.setVisibility(View.INVISIBLE);
            }
        }

        if(SaveSharedPreference.getUserName(DetailHousePage.this).length() == 0){
            goodBtn.setVisibility(View.INVISIBLE);
            houseDeleteBtn.setVisibility(View.INVISIBLE);
            houseUpdateBtn.setVisibility(View.INVISIBLE);
        }

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
                JSONObject jsonObject1 = new JSONObject(getKey.getString("data").toString());
                JSONArray jsonArray1 = new JSONArray(jsonObject1.getString("house"));
                JSONArray jsonArray2 = new JSONArray(jsonObject1.getString("review"));

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
                for(int i =0; i< jsonArray2.length(); i++){
                    JSONObject jsonObject = jsonArray2.getJSONObject(i);
                    reviewList.add(new Review(
                            jsonObject.getString("userMail"),
                            jsonObject.getString("reviewComment"),
                            jsonObject.getString("houseIdx")
                    ));
                    Log.d("Review" + i + ":", reviewList.get(i).toString());
                }


                adapter = new ReviewAdapter(DetailHousePage.this, R.layout.reveiw_list_item, reviewList);
                reviewListView.setAdapter(adapter);


                new DownloadImageTask((ImageView)findViewById(R.id.h_image)).execute(("http://54.180.79.233:3000/" + houseList.get(0).getHousePic()));
                price.setText(houseList.get(0).getHousePrice());
                address.setText(houseList.get(0).getHouseAddress1() + " " + houseList.get(0).getHouseAddress2() + " " + houseList.get(0).getHouseAddress3());
                space.setText(houseList.get(0).getHouseSpace());
                comment.setText(houseList.get(0).getHouseComment());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


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
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
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
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if (con != null) {
                        con.disconnect();
                    }

                    try {
                        //버퍼를 닫아준다.
                        if (reader != null) {
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
            try {
                JSONObject postData = new JSONObject(result);
                if (postData.getString("result").equals("1")) {
                    Toast.makeText(getApplicationContext(), "삭제 성공!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailHousePage.this, OwnerMypage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "삭제 실패...", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
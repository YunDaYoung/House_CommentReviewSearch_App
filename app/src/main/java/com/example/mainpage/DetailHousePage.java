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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainpage.user.Like;
import com.example.mainpage.user.Review;
import com.example.mainpage.user.ReviewAdapter;
import com.example.mainpage.user.ReviewRegister;
import com.example.mainpage.user.ReviewUpdate;
import com.example.mainpage.user.UserMypage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailHousePage extends AppCompatActivity{
    String url;
    String idx;

    House house = new House();
    ArrayList<Review> reviewList = new ArrayList<Review>();
    ReviewAdapter adapter;
    Button goodBtn;
    ImageView imageView;
    TextView price, address, space, comment;
    ListView reviewListView;
    Button reviewRegistBtn;
    Button houseDeleteBtn, houseUpdateBtn;
    //ScrollView sv2;
    Bitmap bmImg;
    String houseIdx;
    Like like;

    JSONTask3 reviewOutput = new JSONTask3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_house);

        //String url = "http://54.180.79.233:3000/houseLike";
        Intent intent1 = getIntent();
        idx = intent1.getStringExtra("HouseIndex");

        goodBtn = (Button) findViewById(R.id.goodBtn1);
        price = (TextView) findViewById(R.id.price);
        address = (TextView) findViewById(R.id.address);
        space = (TextView) findViewById(R.id.space);
        comment = (TextView) findViewById(R.id.comment);
        reviewListView = (ListView) findViewById(R.id.reviewListView);
        //logoutBtn = (Button) findViewById(R.id.logoutButton);
        //sv2 = (ScrollView) findViewById(R.id.sv2);
        imageView = (ImageView)findViewById(R.id.h_image) ;

        reviewRegistBtn=(Button)findViewById(R.id.reviewRegistBtn);


        houseDeleteBtn = (Button)findViewById(R.id.houseDeleteBtn) ;
        houseUpdateBtn = (Button)findViewById(R.id.houseUpdateBtn);

        like = new Like();

        Intent intent0 = getIntent();
        houseIdx = intent0.getExtras().getString("HouseIndex");
        url = "http://13.125.87.255:3000/houseView/" + houseIdx;



        houseDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://13.125.87.255:3000/houseDelete/" + houseIdx;   //인덱스 post로 보내주기
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

        goodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    like.setUserMail(SaveSharedPreference.getUserMail(DetailHousePage.this));
                    like.setHouseIdx(idx);
                    like.setFavoriteCheck("1");
                    Log.d("likeOBJ",like.toString());
                    new ServerConnect((like.getUserMail()), (like.getHouseIdx()),(like.getFavoriteCheck())).execute("http://13.125.87.255:3000/houseLike");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                        Intent intent2 = new Intent(DetailHousePage.this, UserMypage.class);
                        startActivity(intent2);
                    }
            }
        });


        reviewRegistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String houseIdx=house.getHouseIdx();
                Log.d("houseIndex",houseIdx);
                Intent intent = new Intent(DetailHousePage.this, ReviewRegister.class);
                intent.putExtra("HouseIndex",houseIdx);
                startActivity(intent);

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

                Log.d("jsonObject: ", getKey.getString("data").toString());
                JSONObject jsonObject1 = new JSONObject(getKey.getString("data").toString());
                JSONArray jsonArray2 = new JSONArray(jsonObject1.getString("review"));
                JSONObject houseObjet = new JSONObject(jsonObject1.getString("house"));

                    house = new House(
                            houseObjet.getString("houseIdx"),
                            houseObjet.getString("housePic"),
                            houseObjet.getString("housePrice"),
                            houseObjet.getString("houseSpace"),
                            houseObjet.getString("houseComment"),
                            houseObjet.getString("houseAddress1"),
                            houseObjet.getString("houseAddress2"),
                            houseObjet.getString("houseAddress3"),
                            houseObjet.getString("userMail")
                    );

                    Log.d("House: ", house.toString());
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


                new DownloadImageTask((ImageView)findViewById(R.id.h_image)).execute(("http://13.125.87.255:3000/" + house.getHousePic()));
                price.setText(house.getHousePrice());
                address.setText(house.getHouseAddress1() + " " + house.getHouseAddress2() + " " + house.getHouseAddress3());
                space.setText(house.getHouseSpace());
                comment.setText(house.getHouseComment());

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

    public class ServerConnect extends AsyncTask<String, String, String> {


        private String userMail;
        private String houseIdx;
        private String favoriteCheck;


        public ServerConnect(String userMail, String houseIdx, String favoriteCheck){
            this.userMail = userMail;
            this.houseIdx = houseIdx;
            this.favoriteCheck = favoriteCheck;

        }


        @Override
        protected String doInBackground(String... urls) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("userMail", userMail);
                jsonObject.accumulate("houseIdx", houseIdx);
                jsonObject.accumulate("favoriteCheck", favoriteCheck);
                Log.d("jsonObject", jsonObject.toString());
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);

                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");       //POST방식으로 보냄
//                    con.setRequestProperty("Cache-Control", "no-cache");        //캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");     //application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");     //서버에 response 데이터를 html로 받음
                    con.setDoInput(true);
                    con.setDoOutput(true);                              //Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.connect();

//서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("postData", result);
            try {
                JSONObject postData = new JSONObject(result);
                if(postData.getString("result").equals("1")) {
                    Intent intent = new Intent(DetailHousePage.this, UserMypage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "좋아요", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
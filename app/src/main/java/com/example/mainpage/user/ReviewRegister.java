package com.example.mainpage.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mainpage.DetailHousePage;
import com.example.mainpage.House;
import com.example.mainpage.R;
import com.example.mainpage.SaveSharedPreference;

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

public class ReviewRegister extends AppCompatActivity {
    Button reviewBtn;
    EditText reviewComment;
    String houseIdx;
    Review review = new Review();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_register);

        reviewComment = (EditText)findViewById(R.id.reviewComments);
        reviewBtn = (Button)findViewById(R.id.reviewAddBtn);
        Intent intent=getIntent();
        houseIdx = intent.getStringExtra("HouseIndex");

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setUser_mail(SaveSharedPreference.getUserMail(ReviewRegister.this));
                review.setUser_review(reviewComment.getText().toString());
                review.setHouse_idx(houseIdx);
                if((review.getUser_review()).equals("")){
                    Toast.makeText(getApplicationContext(), "리뷰를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    new ServerConnect((review.getUser_mail()),(review.getUser_review()),(review.getHouse_idx())).execute("http://13.125.87.255:3000/reviewRegister"); //AsyncTask 시작시킴
                }
            }
        });
    }

    //서버 통신
    public class ServerConnect extends AsyncTask<String, String, String> {

        private String userMail;
        private String reviewComment;
        private String houseIdx;


        public ServerConnect(String userMail, String reviewComment,String houseIdx){
            this.userMail = userMail;
            this.reviewComment = reviewComment;
            this.houseIdx = houseIdx;
        }


        @Override
        protected String doInBackground(String... urls) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("userMail", userMail);
                jsonObject.accumulate("reviewComment", reviewComment);
                jsonObject.accumulate("houseIdx",houseIdx);
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
                    writer.write(jsonObject.toString()); //json 데이터를  서버로 보냄
                    writer.flush();
                    writer.close();//버퍼를 닫아줌

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
            //Log.d("postData", result);
            try {
                JSONObject postData = new JSONObject(result);
                if(postData.getString("result").equals("1")) {
                    Intent intent = new Intent(ReviewRegister.this, DetailHousePage.class);
                    intent.putExtra("HouseIndex", houseIdx);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "리뷰등록 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
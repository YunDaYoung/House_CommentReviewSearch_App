package com.example.mainpage.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mainpage.DetailHousePage;
import com.example.mainpage.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReviewUpdate extends AppCompatActivity {
    Button reviewBtn;
    EditText reviewComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_update);

        final Review review = new Review();
        reviewComment = (EditText)findViewById(R.id.reviewComments);
        reviewBtn = (Button)findViewById(R.id.reviewAddBtn);

        review.setUser_review(reviewComment.getText().toString());
        //review.setUser_mail();


        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setUser_review(reviewComment.getText().toString());
                //review.setUser_mail();


                if((review.getUser_review()).equals("")){
                    Toast.makeText(getApplicationContext(), "리뷰를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else {
                   // new ReviewUpdate.ServerConnect((review.getUser_mail()), (review.getUser_review()),(review.getHouse_idx())).execute("http://54.180.79.233:3000/reviewUpdate"+review.getHouse_idx()+review.getUser_mail()); //AsyncTask 시작시킴
                }
            }
        });


    }
    public static class ServerConnect extends AsyncTask<String, String, String> {

        private String reviewComment;
        private String userMail;
        private String houseIdx;


        public ServerConnect(String userMail, String reviewComment, String houseIdx){
            this.userMail = userMail;
            this.reviewComment = reviewComment;
            this.houseIdx = houseIdx;
        }


        @Override
        protected String doInBackground(String... urls) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("user_Mail", userMail);
                jsonObject.accumulate("user_review", reviewComment);
                jsonObject.accumulate("house_idx",houseIdx);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);

                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");       //POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");        //캐시 설정
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

                    return "ok";

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

//            if(result.equals("ok")) {
//                Intent intent = new Intent(ReviewUpdate.this, DetailHousePage.class);
//                startActivity(intent);
//                finish();
//            } else {
//                Toast.makeText(getApplicationContext(), "리뷰 수정 실패", Toast.LENGTH_SHORT).show();
//            }
        }
    }


}

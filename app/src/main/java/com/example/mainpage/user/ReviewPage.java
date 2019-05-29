package com.example.mainpage.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mainpage.DetailHousePage;
import com.example.mainpage.MainActivity;
import com.example.mainpage.OwnerMypage;
import com.example.mainpage.R;
import com.example.mainpage.SaveSharedPreference;

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

public class ReviewPage extends AppCompatActivity {


    String url = "http://13.125.87.255:3000/reviewList/";
    ArrayList<Review> reviewList = new ArrayList<Review>();

    ReviewAdapter adapter;
    ListView rwListview;
    TextView rwUserName;
    Button rwLogoutButton;
    TextView rwText1;
    ScrollView rw;

    JSONTask Json = new JSONTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list_page);

        rwListview = (ListView) findViewById(R.id.rwListview);
        rwLogoutButton = (Button) findViewById(R.id.rwLogoutButton);
        rwUserName = (TextView) findViewById(R.id.rwUserName);
        rw = (ScrollView) findViewById(R.id.rw);
        rwText1 = (TextView) findViewById(R.id.rwText1);

        Intent intent = getIntent();
        Json.execute(url + SaveSharedPreference.getUserMail(ReviewPage.this));
        rw.requestDisallowInterceptTouchEvent(true);

        rwLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewPage.this, MainActivity.class);
                SaveSharedPreference.clearUserName(ReviewPage.this);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (SaveSharedPreference.getUserMail(ReviewPage.this).length() != 0) {
            Intent intent = new Intent(ReviewPage.this, MainActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    public class JSONTask extends AsyncTask<String, String, String> {

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

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject getKey = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(getKey.getString("data").toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    reviewList.add(new Review(
                            jsonObject.getString("userMail"),
                            jsonObject.getString("reviewComment"),
                            jsonObject.getString("houseIdx")
                    ));
                    Log.d("Review" + i + ":", reviewList.get(i).toString());
                }

                ReviewAdapter adapter = new ReviewAdapter(ReviewPage.this, R.layout.reveiw_list_item, reviewList);
                rwListview.setAdapter(adapter);
                rwListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ReviewPage.this, ReviewUpdate.class);
                        String hIdx = reviewList.get(position).getHouse_idx();
                        String rwtext=reviewList.get(position).getUser_review();
                        intent.putExtra("HouseIndex", hIdx);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

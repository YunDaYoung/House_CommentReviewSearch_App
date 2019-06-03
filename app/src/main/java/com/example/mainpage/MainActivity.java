package com.example.mainpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mainpage.user.SearchPage;

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

public class MainActivity extends AppCompatActivity {

    TextView reviewHouse;

    String url = "http://13.125.87.255:3000/recently";
    ArrayList<House> houseList = new ArrayList<House>();

    //ArrayList<Integer> picArr = new ArrayList<>();
    //ArrayList<ListViewItem> listViewData = new ArrayList<>();
    ListViewAdapter adapter;
    ListView listView;
    Button loginButton, joinButton;
    ImageButton searchButton;

    JSONTask Json = new JSONTask();

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        Json.execute(url);

        listView = (ListView) findViewById(R.id.listview);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        joinButton = (Button) findViewById(R.id.joinButton);
        reviewHouse = (TextView) findViewById(R.id.reviewHouse);

        reviewHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

       if(SaveSharedPreference.getUserName(MainActivity.this).length() != 0){
           if(SaveSharedPreference.getUserCheck(MainActivity.this).equals("1")){
               loginButton.setText("마이페이지");
               joinButton.setVisibility(View.INVISIBLE);
               searchButton.setVisibility(View.INVISIBLE);

           }
           else{
               loginButton.setText("마이페이지");
               joinButton.setVisibility(View.INVISIBLE);
           }
        }


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchPage.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinPage.class);
                startActivity(intent);
            }
        });



    }

    public class JSONTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... urls){
            try {
                /*
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "androidTest");
                jsonObject.accumulate("name", "yun");*/

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
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

                //picArr.add(R.drawable.house1);
                //picArr.add(R.drawable.house2);

                //Log.d("jsonObject: ", getKey.getString("data").toString());
                JSONArray jsonArray = new JSONArray(getKey.getString("data").toString());
                for(int i =0; i< jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
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

                adapter = new ListViewAdapter(MainActivity.this, R.layout.item, houseList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, DetailHousePage.class);
                        String hIdx = houseList.get(position).getHouseIdx();
                        intent.putExtra("HouseIndex", hIdx);
                        startActivity(intent);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}

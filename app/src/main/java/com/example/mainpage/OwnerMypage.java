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

public class OwnerMypage extends AppCompatActivity {

    String url = "http://54.180.79.233:3000/recently";
    ArrayList<House> houseList = new ArrayList<House>();

    OwnerMypageListViewAdapter adapter;
    ListView listView;
    TextView ownerName;
    Button logoutButton;
    ScrollView sv;

    JSONTask2 Json2 = new JSONTask2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_mypage);

        Json2.execute(url);

        listView = (ListView) findViewById(R.id.listview2);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        ownerName = (TextView) findViewById(R.id.ownerName);
        sv = (ScrollView) findViewById(R.id.sv);

        listView.setOnTouchListener(new View.OnTouchListener() {        //리스트뷰 터취 리스너
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv.requestDisallowInterceptTouchEvent(true);    // 리스트뷰에서 터취가되면 스크롤뷰만 움직이게
                return false;
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerMypage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public class JSONTask2 extends AsyncTask<String, String, String>{

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
            JSONArray jsonArray = new JSONArray(getKey.getString("data").toString());
            for(int i =0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                houseList.add(new House(
                        jsonObject.getString("houseIdx"),
                        R.drawable.house1,
                        jsonObject.getString("housePrice"),
                        jsonObject.getString("houseSpace"),
                        jsonObject.getString("houseComment"),
                        jsonObject.getString("houseAddress"),
                        jsonObject.getString("userMail")

                ));
                Log.d("House" + i + ":", houseList.get(i).toString());
            }

            adapter = new OwnerMypageListViewAdapter(OwnerMypage.this, R.layout.owner_mypage_item, houseList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(OwnerMypage.this, DetailHousePage.class);
                    String hIdx = houseList.get(position).getHouseIdx();
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
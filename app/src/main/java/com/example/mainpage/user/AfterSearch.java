package com.example.mainpage.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mainpage.DetailHousePage;
import com.example.mainpage.House;
import com.example.mainpage.ListViewAdapter;
import com.example.mainpage.MainActivity;
import com.example.mainpage.OwnerMypage;
import com.example.mainpage.R;
import com.example.mainpage.SaveSharedPreference;

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

public class AfterSearch extends AppCompatActivity {

    ArrayList<House> houseList = new ArrayList<House>();
    ArrayList<Review> reviewList = new ArrayList<Review>();
    ImageButton searchBtn;
    Button logoutBtn;

    String address1;
    String address2;
    String address3;
    String price1;
    String area1;
    String price2;
    String area2;
    String searchword;


    Intent intent;


    ListViewAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_search);

        searchBtn = (ImageButton) findViewById(R.id.searchButton0);
        logoutBtn = (Button) findViewById(R.id.logoutButton0);
        listView = (ListView) findViewById(R.id.listview0);

        intent = getIntent();

        address1 = intent.getStringExtra("address1");
        address2 = intent.getStringExtra("address2");
        address3 = intent.getStringExtra("address3");
        price1 = intent.getStringExtra("price1");
        area1 = intent.getStringExtra("area1");
        price2 = intent.getStringExtra("price2");
        area2 = intent.getStringExtra("area2");
        searchword = intent.getStringExtra("reviewSearch");
        //Log.d("search2", address1 + " : " + address2 + " : " + address3 + " : " + price1 + " : " + area1 + " : " + price2 + " : " + area2);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterSearch.this, SearchPage.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AfterSearch.this, MainActivity.class);
                SaveSharedPreference.clearUserName(AfterSearch.this);
                startActivity(intent);
                finish();
            }
        });
        new ServerConnect(address1, address2, address3,
                price1, area1, price2, area2, searchword).execute("http://13.125.87.255:3000/houseSearch");
    }

    public class ServerConnect extends AsyncTask<String, String, String> {

        private String address1;
        private String address2;
        private String address3;
        private String price1;
        private String area1;
        private String price2;
        private String area2;
        private String reviewSearch;




        public ServerConnect(String address1, String address2, String address3, String price1, String area1, String price2, String area2, String reviewSearch){
            this.address1 = address1;
            this.address2 = address2;
            this.address3 = address3;
            this.price1 = price1;
            this.area1 = area1;
            this.price2 = price2;
            this.area2 = area2;
            this.reviewSearch = reviewSearch;
        }


        @Override
        protected String doInBackground(String... urls) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("housePrice1", price1);
                jsonObject.accumulate("housePrice2", price2);
                jsonObject.accumulate("houseSpace1",area1);
                jsonObject.accumulate("houseSpace2",area2);
                jsonObject.accumulate("houseAddress1", address1);
                jsonObject.accumulate("houseAddress2", address2);
                jsonObject.accumulate("houseAddress3", address3);
                jsonObject.accumulate("keyword", reviewSearch);


                Log.d("jsonObject", jsonObject.toString());
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);

                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");       //POST방식으로 보냄
                    //con.setRequestProperty("Cache-Control", "no-cache");        //캐시 설정
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
                JSONObject getKey= new JSONObject(result);

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
                    reviewList.add(new Review(
                            jsonObject.getString("userMail"),
                            jsonObject.getString("reviewComment"),
                            jsonObject.getString("houseIdx")
                    ));
                    Log.d("House" + i + ":", houseList.get(i).toString());
                }

                adapter = new ListViewAdapter(AfterSearch.this, R.layout.item, houseList, reviewList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(AfterSearch.this, DetailHousePage.class);
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
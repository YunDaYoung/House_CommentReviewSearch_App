package com.example.mainpage.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainpage.DetailHousePage;
import com.example.mainpage.House;
import com.example.mainpage.JoinPage;
import com.example.mainpage.ListViewAdapter;
import com.example.mainpage.LoginPage;
import com.example.mainpage.MainActivity;
import com.example.mainpage.R;

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

public class SearchPage extends AppCompatActivity {

    ArrayList<House> houseList = new ArrayList<House>();
    EditText address1,address2,address3,reviewSearch;
    Spinner rwPrice1,rwArea1,rwPrice2,rwArea2;
    Button rwSearchBtn;
    Search search;

    ListViewAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        search = new Search();

        address1 = (EditText)findViewById(R.id.address1);
        address2 = (EditText)findViewById(R.id.address2);
        address3 = (EditText)findViewById(R.id.address3);

        rwPrice1 =  (Spinner) findViewById(R.id.price1);
        rwArea1 = (Spinner) findViewById(R.id.area1);
        rwPrice2 =  (Spinner) findViewById(R.id.price2);
        rwArea2 = (Spinner) findViewById(R.id.area2);
        reviewSearch = (EditText)findViewById(R.id.searchReview);

        rwSearchBtn = (Button)findViewById(R.id.searchBtn);

        rwPrice1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    search.setPrice1("0");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rwPrice2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    search.setPrice2("100000000");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rwArea1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    search.setArea1("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rwArea2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    search.setArea2("60");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rwSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setAddress1(address1.getText().toString());
                search.setAddress2(address2.getText().toString());
                search.setAddress3(address3.getText().toString());
                //search.setPrice1(rwPrice1.getText().toString());
                //search.setArea1(rwArea1.getText().toString());
                search.setSearchword(reviewSearch.getText().toString());



                new SearchPage.ServerConnect((search.getAddress1()), (search.getAddress2()), (search.getAddress3()),
                        (search.getPrice1()),(search.getArea1()),(search.getPrice2()),(search.getArea2())).execute("http://13.125.87.255:3000/houseSearch");
            }
        });




    }
    public class ServerConnect extends AsyncTask<String, String, String> {

        private String address1;
        private String address2;
        private String address3;
        private String price1;
        private String area1;
        private String price2;
        private String area2;




        public ServerConnect(String address1, String address2, String address3, String price1, String area1, String price2, String area2){
            this.address1 = address1;
            this.address2 = address2;
            this.address3 = address3;
            this.price1 = price1;
            this.area1 = area1;
            this.price2 = price2;
            this.area2 = area2;

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
                jsonObject.accumulate("keyword", reviewSearch.getText().toString());


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
                    Log.d("House" + i + ":", houseList.get(i).toString());
                }


                adapter = new ListViewAdapter(SearchPage.this, R.layout.item, houseList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SearchPage.this, DetailHousePage.class);
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

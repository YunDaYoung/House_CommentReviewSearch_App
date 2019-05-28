package com.example.mainpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class HouseRegister extends AppCompatActivity {
    EditText price, address1, address2, address3, space, comment;
    Button btn1;
    String userMail;

    House house;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_register);

        price = (EditText) findViewById(R.id.price1);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        address3 = (EditText) findViewById(R.id.address3);
        space = (EditText) findViewById(R.id.space1);
        comment = (EditText) findViewById(R.id.comment1);
        btn1 = (Button) findViewById(R.id.btn1);

        house = new House();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                house.setHousePic(null);
                house.setHousePrice(price.getText().toString());
                house.setHouseAddress1(address1.getText().toString());
                house.setHouseAddress2(address2.getText().toString());
                house.setHouseAddress3(address3.getText().toString());
                house.setHouseSpace(space.getText().toString());
                house.setHouseComment(comment.getText().toString());
                userMail = SaveSharedPreference.getUserMail(HouseRegister.this);

                new ServerConnect(house).execute("http://13.125.87.255:3000/houseRegister"); //AsyncTask 시작시킴
            }
        });
    }

    public class ServerConnect extends AsyncTask<String, String, String> {

        House input;

        public ServerConnect(House house){
            this.input = house;

        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("housePic", input.getHousePic());
                jsonObject.accumulate("housePrice", input.getHousePrice());
                jsonObject.accumulate("houseSpace", input.getHouseSpace());
                jsonObject.accumulate("houseComment", input.getHouseComment());
                jsonObject.accumulate("houseAddress1", input.getHouseAddress1());
                jsonObject.accumulate("houseAddress2", input.getHouseAddress2());
                jsonObject.accumulate("houseAddress3", input.getHouseAddress3());
                jsonObject.accumulate("userMail", userMail);

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
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음

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

            try {
                JSONObject postData = new JSONObject(result);
                if(postData.getString("result").equals("1")) {
                    Intent intent = new Intent(HouseRegister.this, OwnerMypage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "등록 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


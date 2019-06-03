package com.example.mainpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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


public class JoinPage extends AppCompatActivity {

    Button joinBtn;
    EditText nameText, mailText, pwText;
    CheckBox chkOwner;

    boolean joinSeparation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);

        //Join클래스 객체 생성
        final Join join = new Join();

        //변수에 아이디 값 지정
        joinBtn = (Button) findViewById(R.id.JoinBtn);

        chkOwner = (CheckBox) findViewById(R.id.ChkOwner);

        nameText = (EditText) findViewById(R.id.NameText);
        mailText = (EditText) findViewById(R.id.MailText);
        pwText = (EditText) findViewById(R.id.PWText);


        //버튼 눌렀을 시
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join.setJoinName(nameText.getText().toString());
                join.setJoinMail(mailText.getText().toString());
                join.setJoinPassword(pwText.getText().toString());

                if(chkOwner.isChecked()==true) {
                    joinSeparation = true;
                }
                else {
                    joinSeparation = false;
                }
                join.setJoinSeparation(joinSeparation);

                if((join.getJoinName()).equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력하시오", Toast.LENGTH_SHORT).show();
                }
                else if ((join.getJoinMail()).equals("")){
                    Toast.makeText(getApplicationContext(), "이메일 주소를 입력하시오", Toast.LENGTH_SHORT).show();
                }
                else if ((join.getJoinPassword()).equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하시오", Toast.LENGTH_SHORT).show();
                }
                else {
                    new ServerConnect((join.getJoinName()), (join.getJoinMail()), (join.getJoinPassword()), (join.getJoinSeparation())).execute("http://13.125.87.255:3000/join"); //AsyncTask 시작시킴
                }
            }
        });
    }


    public class ServerConnect extends AsyncTask<String, String, String> {

        private String joinName;
        private String joinMail;
        private String joinPassword;
        private boolean joinSeparation;

        public ServerConnect(String joinName, String joinMail, String joinPassword, boolean joinSeparation){
            this.joinName = joinName;
            this.joinMail = joinMail;
            this.joinPassword = joinPassword;
            this.joinSeparation = joinSeparation;
        }


        @Override
        protected String doInBackground(String... urls) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("userName", joinName);
                jsonObject.accumulate("userMail", joinMail);
                jsonObject.accumulate("userPassword", joinPassword);
                jsonObject.accumulate("userCheck", joinSeparation);
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
                if(result.equals("중복")){
                    Toast.makeText(getApplicationContext(), "이미 존재하는 이메일 입니다. ", Toast.LENGTH_SHORT).show();
                }
                else {
                    JSONObject postData = new JSONObject(result);
                    if(postData.getString("result").equals("1")) {
                        Intent intent = new Intent(JoinPage.this, LoginPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

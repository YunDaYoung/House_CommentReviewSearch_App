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

import com.example.mainpage.user.User;

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

public class LoginPage extends AppCompatActivity {

    Button joinBtn, loginBtn;
    EditText email,password;
    CheckBox chkBox;
    boolean loginSeparation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        final Login login = new Login();

        joinBtn = (Button) findViewById(R.id.joinButton);
        loginBtn = (Button) findViewById(R.id.loginButton);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        chkBox = (CheckBox)findViewById(R.id.ChkBox);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, JoinPage.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setLoginMail(email.getText().toString());
                login.setLoginPassword(password.getText().toString());

                if(chkBox.isChecked()==true) {
                    loginSeparation = true;
                }
                else {
                    loginSeparation = false;
                }
                login.setLoginSeparation(loginSeparation);

                if((login.getLoginMail()).equals("")){
                    Toast.makeText(getApplicationContext(), "이메일 주소를 입력하시오", Toast.LENGTH_SHORT).show();
                }
                else if ((login.getLoginPassword()).equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하시오", Toast.LENGTH_SHORT).show();
                }
                else {
                    new ServerConnect((login.getLoginMail()), (login.getLoginPassword()),(login.getLoginSeparation())).execute("http://54.180.79.233:3000/login"); //AsyncTask 시작시킴
                }

            }
        });

    }

    public class ServerConnect extends AsyncTask<String, String, String> {

        private String email;
        private String password;
        private boolean loginSeparation;

        public ServerConnect(String email, String password, boolean loginSeparation){
            this.email = email;
            this.password = password;
            this.loginSeparation = loginSeparation;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("userMail", email);
                jsonObject.accumulate("userPassword", password);
                jsonObject.accumulate("userCheck", loginSeparation);
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
            Log.d("postData", result);
            try {
                JSONObject postData = new JSONObject(result);
                if(postData.getString("result").equals("1")) {
                    JSONObject data = new JSONObject(postData.getString("data"));
                    Intent intent = new Intent(getApplicationContext(), OwnerMypage.class);


                    User user = new User(data.getString("userMail"),
                            data.getString("userName"),
                            data.getString("userCheck"));
                    SaveSharedPreference.setUserMail(LoginPage.this , user.getUserMail());
                    SaveSharedPreference.setUserName(LoginPage.this , user.getUserName());
                    SaveSharedPreference.setUserCheck(LoginPage.this , user.getUserCheck());

//                    intent.putExtra("session", user);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}

package com.example.mainpage;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
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
    Button picBtn, btn1;
    String userMail;

    ImageView img;
    String path;
    String fileName1;

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
        img = (ImageView) findViewById(R.id.imgView);
        picBtn = (Button) findViewById(R.id.picRegisterBtn);

        house = new House();

        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName1 = path.substring(path.lastIndexOf("/") + 1);
                Log.d("fileName :", fileName1);

                String urlString = "http://13.125.87.255:3000/houseRegisterPic";
                DoFileUpload(urlString , path);

                house.setHousePic(fileName1);
                house.setHousePrice(price.getText().toString());
                house.setHouseAddress1(address1.getText().toString());
                house.setHouseAddress2(address2.getText().toString());
                house.setHouseAddress3(address3.getText().toString());
                house.setHouseSpace(space.getText().toString());
                house.setHouseComment(comment.getText().toString());
                userMail = SaveSharedPreference.getUserMail(HouseRegister.this);
            }
        });
    }

    public void DoFileUpload(String apiUrl, String absolutePath) {
        new httpFileUpload(apiUrl, "", absolutePath).execute(apiUrl);
    }

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    private class httpFileUpload extends AsyncTask<String, String, String> {

        private String urlString;
        private String params;
        private String fileName;

        public httpFileUpload(String urlString, String params, String fileName){
            this.urlString = urlString;
            this.params = params;
            this.fileName = fileName;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try{
                    FileInputStream mFileInputStream = new FileInputStream(fileName);
                    URL connectUrl = new URL(urlString);
                    Log.d("Test", "mFileInputStream  is " + mFileInputStream);

                    // open connection
                    conn = (HttpURLConnection)connectUrl.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("photo", fileName);

                    // write data
                    OutputStream outStream = conn.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(outStream);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + fileName+"\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    int bytesAvailable = mFileInputStream.available();
                    int maxBufferSize = 1 * 1024 * 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                    Log.d("Test", "image byte is " + bytesRead);

                    // read image
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = mFileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                    }

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    dos.flush(); // finish upload...

                    // close streams
                    Log.e("Test" , "File is written");
                    mFileInputStream.close();
                    dos.close();

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.d("Error", "http response code is " + conn.getResponseCode());
                        return null;
                    }

                    //서버로 부터 데이터를 받음
                    InputStream stream = conn.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer1 = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer1.append(line);
                    }

                    return buffer1.toString();
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(conn != null){
                        conn.disconnect();
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
                Log.d("Test", "exception " + e.getMessage());
                // TODO: handle exception
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.d("postData", result);
            try {
                JSONObject postData = new JSONObject(result);
                if(postData.getString("result").equals("1")) {
                    new ServerConnect(house).execute("http://13.125.87.255:3000/houseRegister"); //AsyncTask 시작시킴
                } else {
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Log.d("PATH", (data.getData()).getPath().toString());
                    Uri uri = data.getData();
                    path = getPathFromURI(uri);
                    img.setImageURI(data.getData());
                    img.setVisibility(View.VISIBLE);
                }
        }
    }

    public String getPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        Log.d("Path", cursor.getString(columnIndex));
        return cursor.getString(columnIndex);
    }
}


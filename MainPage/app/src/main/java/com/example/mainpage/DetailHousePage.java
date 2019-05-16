package com.example.mainpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailHousePage extends AppCompatActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.detail_house);

            setTitle("상세보기화면");

            TextView textView = (TextView) findViewById(R.id.text1);

            textView.setText("집 상세화면 입니다. ");
        }
}

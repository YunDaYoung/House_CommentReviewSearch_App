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

    EditText address1,address2,address3,reviewSearch;
    Spinner rwPrice1,rwArea1,rwPrice2,rwArea2;
    Button rwSearchBtn;
    Search search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        search = new Search();

        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        address3 = (EditText) findViewById(R.id.address3);

        rwPrice1 = (Spinner) findViewById(R.id.price1);
        rwArea1 = (Spinner) findViewById(R.id.area1);
        rwPrice2 = (Spinner) findViewById(R.id.price2);
        rwArea2 = (Spinner) findViewById(R.id.area2);
        reviewSearch = (EditText) findViewById(R.id.searchReview);

        rwSearchBtn = (Button) findViewById(R.id.searchBtn);

        rwPrice1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    search.setPrice1("0");
                }

                else if (position == 1) {
                    search.setPrice1("100000001");
                }

                else if (position == 2) {
                    search.setPrice1("200000001");
                }

                else if (position == 3) {
                    search.setPrice1("300000001");
                }

                else if (position == 4) {
                    search.setPrice1("400000001");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                search.setPrice1("0");
            }
        });

        rwPrice2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    search.setPrice2("100000000");
                }

                else if (position == 1) {
                    search.setPrice2("200000000");
                }

                else if (position == 2) {
                    search.setPrice2("300000000");
                }

                else if (position == 3) {
                    search.setPrice2("400000000");
                }

                else if (position == 4) {
                    search.setPrice2("500000000");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                search.setPrice2("500000000");
            }
        });

        rwArea1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    search.setArea1("0");
                }

                else if (position == 1) {
                    search.setArea1("21");
                }

                else if (position == 2) {
                    search.setArea1("41");
                }

                else if (position == 3) {
                    search.setArea1("61");
                }

                else if (position == 4) {
                    search.setArea1("101");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                search.setArea1("0");
            }
        });

        rwArea2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    search.setArea2("20");
                }

                else if (position == 1) {
                    search.setArea2("40");
                }

                else if (position == 2) {
                    search.setArea2("60");
                }

                else if (position == 3) {
                    search.setArea2("100");
                }

                else if (position == 4) {
                    search.setArea2("150");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                search.setArea2("150");
            }
        });

        rwSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setAddress1(address1.getText().toString());
                search.setAddress2(address2.getText().toString());
                search.setAddress3(address3.getText().toString());
                search.setSearchword(reviewSearch.getText().toString());

                Intent intent = new Intent(SearchPage.this, AfterSearch.class);

                Log.d("search", search.toString());
                intent.putExtra("address1", search.getAddress1());
                intent.putExtra("address2", search.getAddress2());
                intent.putExtra("address3", search.getAddress3());
                intent.putExtra("price1", search.getPrice1());
                intent.putExtra("area1", search.getArea1());
                intent.putExtra("price2", search.getPrice2());
                intent.putExtra("area2", search.getArea2());
                intent.putExtra("reviewSearch", search.getSearchword());

                startActivity(intent);
            }

        });
    }

}

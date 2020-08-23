package com.example.sharedprefs_2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    public static final String PREFS = "large_text";
    public static final String PREFS_TEXT = "text";
    public static final String PREFS_TEXT_LENGTH = "leng";
    private List<Map<String, String>> content;
    private ListView listView;
    private TextView textViewTitle;
    private TextView textViewSubtitle;
    private String noteTxt = "";
    private String noteTxtLength = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleAdapter adapter;
    private String[] arrayContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        savePrefs();
        fillContent();

        String[] from = new String[]{"title", "subtitle"};
        int[] to = new int[]{R.id.textViewTitle, R.id.textViewSubtitle};
        adapter = new SimpleAdapter(this, content, R.layout.list_item, from, to);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                content.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillContent();
                getDateFromSharedPref();

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void init() {
        listView = findViewById(R.id.listView);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewSubtitle = findViewById(R.id.textViewSubtitle);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        arrayContent = getString(R.string.large_text).split("\\n\\n");
    }

    private void savePrefs() {
        sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPreferences.edit();
        for (int i = 0; i < arrayContent.length; i++) {
            noteTxt = arrayContent[i];
            noteTxtLength = String.valueOf(arrayContent[i].length());
        }

        myEditor.putString(PREFS_TEXT, noteTxt);
        myEditor.putString(PREFS_TEXT_LENGTH, noteTxtLength);
        myEditor.apply();
    }


    private void fillContent() {
        content = new ArrayList<>();
        Map<String, String> map;
        for (int i = 0; i < arrayContent.length; i++) {
            map = new HashMap<>();
            map.put("title", arrayContent[i]);
            map.put("subtitle", String.valueOf(arrayContent[i].length()));
            content.add(map);
        }
    }

    private void getDateFromSharedPref() {
        noteTxt = sharedPreferences.getString(PREFS_TEXT, "");
        textViewTitle.setText(noteTxt);
        noteTxtLength = sharedPreferences.getString(PREFS_TEXT_LENGTH, "");
        textViewSubtitle.setText(noteTxtLength);
    }
}
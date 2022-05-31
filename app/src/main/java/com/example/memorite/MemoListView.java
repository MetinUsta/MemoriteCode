package com.example.memorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.memorite.adapter.EntryAdapter;
import com.example.memorite.model.Entry;

import java.util.ArrayList;

public class MemoListView extends AppCompatActivity {

    private RecyclerView memoRV;

    private ArrayList<Entry> entryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list_view);

        memoRV = findViewById(R.id.memoRecyclerView);

        entryArrayList = new ArrayList<>();
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));

        EntryAdapter entryAdapter = new EntryAdapter(this, entryArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        memoRV.setLayoutManager(linearLayoutManager);
        memoRV.setAdapter(entryAdapter);
    }
}
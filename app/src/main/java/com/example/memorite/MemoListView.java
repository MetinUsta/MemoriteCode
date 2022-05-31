package com.example.memorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.memorite.adapter.EntryAdapter;
import com.example.memorite.model.Entry;

import java.util.ArrayList;

public class MemoListView extends AppCompatActivity {

    private RecyclerView memoRV;

    static ArrayList<Entry> entryArrayList;
    static EntryAdapter entryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list_view);

        permission();

        memoRV = findViewById(R.id.memoRecyclerView);

        entryArrayList = new ArrayList<>();
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));
        entryArrayList.add(new Entry("Test", "31.05.2022", "Bugün Android Projesi ile uğraştım", 1, 2.0, 3.0, 4.0));

        entryAdapter = new EntryAdapter(this, entryArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        memoRV.setLayoutManager(linearLayoutManager);
        memoRV.setAdapter(entryAdapter);
    }

    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MemoListView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
package com.example.memorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.memorite.adapter.EntryAdapter;
import com.example.memorite.model.Entry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MemoListView extends AppCompatActivity {

    private RecyclerView memoRV;
    private FloatingActionButton addEntryButton;
    private FloatingActionButton showLocationsButton;

    static ArrayList<Entry> entryArrayList;
    static EntryAdapter entryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list_view);

        permission();

        memoRV = findViewById(R.id.memoRecyclerView);
        addEntryButton = findViewById(R.id.addEntryButton);
        showLocationsButton = findViewById(R.id.showLocationsButton);

        entryArrayList = new ArrayList<>();

        try {
            FileInputStream fis = this.openFileInput("memos");
            ObjectInputStream is = new ObjectInputStream(fis);

            entryArrayList = (ArrayList<Entry>) is.readObject();
            is.close();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        entryAdapter = new EntryAdapter(this, entryArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        memoRV.setLayoutManager(linearLayoutManager);
        memoRV.setAdapter(entryAdapter);

        memoRV.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && addEntryButton.isShown())
                {
                    addEntryButton.hide();
                    showLocationsButton.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    addEntryButton.show();
                    showLocationsButton.show();
                }

                if(!memoRV.canScrollVertically(1)){
                    if(!memoRV.canScrollVertically(-1)){
                        addEntryButton.show();
                        showLocationsButton.show();
                    }else{
                        addEntryButton.hide();
                        showLocationsButton.hide();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MemoListView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void addNewEntry(View view){
        Entry newEntry = new Entry("", "", null, null, null, null);
        entryArrayList.add(newEntry);

        entryAdapter.notifyItemInserted(entryArrayList.indexOf(newEntry));
        entryAdapter.notifyItemRangeChanged(0, entryArrayList.size());

        Intent intent = new Intent(this, EntryEditView.class);
        intent.putExtra("position", entryArrayList.indexOf(newEntry));
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        FileOutputStream fos = null;
        try {
            fos = this.openFileOutput("memos", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(entryArrayList);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    public void showAllLocations(View view){
        Intent intent = new Intent(this, MapAllLocations.class);
        startActivity(intent);

    }
}
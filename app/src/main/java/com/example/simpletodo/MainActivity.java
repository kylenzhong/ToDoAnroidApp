package com.example.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.Charset.*;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    Button addButton;
    EditText addText;
    RecyclerView listRecycler;
    ItemsAdapter ia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        addText = findViewById(R.id.addText);
        listRecycler = findViewById(R.id.listRecycler);

        loadData();

        addText.setText("enter a new item");
        ItemsAdapter.OnItemLongClickListener lcl = new ItemsAdapter.OnItemLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                ia.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "to-do item deleted", Toast.LENGTH_SHORT).show();
                saveData();
            }
        };
        ia = new ItemsAdapter(items, lcl);
        listRecycler.setAdapter(ia);
        listRecycler.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if( addText.getText().toString().equals("") == false) {
                    items.add(addText.getText().toString());
                    addText.setText("");
                    ia.notifyItemInserted(items.size() - 1);
                    Toast.makeText(getApplicationContext(), "New to-do item added", Toast.LENGTH_SHORT).show();
                    saveData();
                }
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    private void loadData(){
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), String.valueOf(Charset.defaultCharset())));
        }catch(IOException e){
            Log.e("MainActivity", "Error reading items");
            items = new ArrayList<String>();
        }
    }
    private void saveData(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        }catch(IOException e){
            Log.e("MainActivity", "Error writing items");
        }
    }
}
package com.example.wz.txtbook;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    int readLast = 0;
    int readLen = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //wz 获取lesson 的名字
        final ListView list_book = (ListView)findViewById(R.id.list_book);
        ArrayList<String>leesonName = getLessonFromAssets();

        //绑定数据并显示
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this , R.layout.array_item , leesonName );
        list_book.setAdapter(adapter);

        //item的点击时间监听
        list_book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = (String)list_book.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        name + "has be choosed",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( MainActivity.this , BookShow.class );
                intent.putExtra("lesson" , name );
                startActivity(intent);
            }
        });
    }

    /**
     * 从assets文件夹中读取列表
     * @param menu
     * @return
     */
    private ArrayList<String> getLessonFromAssets(){
        ArrayList<String> list = new ArrayList<String>();
        String suffix = null;
        String fileName=null;

        AssetManager assetManager = this.getAssets();
        try {
            String[] filePathList = assetManager.list("nce4");
            for (String path : filePathList){
                list.add(path);
            }
        }catch (IOException e){
            Log.e("ioe",e.getMessage());
        }
        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}

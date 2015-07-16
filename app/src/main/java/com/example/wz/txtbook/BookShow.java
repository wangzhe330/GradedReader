package com.example.wz.txtbook;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class BookShow extends ActionBarActivity {

    TextView tv;
    InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_show);

        //获取从MainActivity 传递过来的 数据： lesson文件的名字
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String lessonName = bundle.getString("lesson");
        String fileName = "nce4/"+lessonName;

        //设置TextView为可滚动的
        tv = (TextView)findViewById(R.id.show_tv0);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        AssetManager assetManager = this.getAssets();
        //inputStream = getResources().openRawResource(R.raw.nce4_book);  //之前放在raw文件夹里面测试用的
        try {
            inputStream = assetManager.open(fileName);
        }catch (IOException e){
            Log.e("ioe",e.getMessage());
        }
        //从inputStream中获取String数据
        tv.setText(getString(inputStream));
    }

    /**
     * 从txt的stream中获取string
     * @param inputStream
     * @return String
     */
    public static String getString(InputStream inputStream) {

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;

        try {
            while (   ( (line = reader.readLine()) != null) ) {
                line = reader.readLine();
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

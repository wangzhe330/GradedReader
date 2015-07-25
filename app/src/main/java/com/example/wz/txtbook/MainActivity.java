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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    int readLast = 0;
    int readLen = 100;

    InputStream inputStreamLight;       //高亮关键词的输入流
    HashMap<String,Integer> hmLight = new HashMap<String,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WordLib wordLib = (WordLib)getApplicationContext();

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

        if( !wordLib.getSetStatue()) {
            //从raw中的分级表获取分级信息,存入hmLight
            inputStreamLight = getResources().openRawResource(R.raw.nce4_words);
            InputStreamReader inputStreamLightReader = null;
            try {
                inputStreamLightReader = new InputStreamReader(inputStreamLight, "gbk");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(inputStreamLightReader);
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    String word;
                    int level;
                    String[] temp = line.split("\\t");
                    if (temp.length == 2 && isNumeric(temp[1])) {
                        word = temp[0];
                        level = Integer.parseInt(temp[1]);
                        hmLight.put(word, level);
                    }
                }
            wordLib.setWordLib(hmLight);
            wordLib.setSetStatue();


            } catch (IOException e) {
                Log.e("ioe", e.getMessage());
            }
        }
    }
    /**
     * 判断String是否是纯数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
    /**
     * 从assets文件夹中读取列表
     * @param menu
     * @return
     */
    private ArrayList<String> getLessonFromAssets(){
        ArrayList<String> list = new ArrayList<String>();           //中间过程list
        ArrayList<String> listResult = new ArrayList<String>();     //最后返回的结果list

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

        //list中存储了从assetManager获得的文件名，下面将按文件名的数字来对文件名进行排序，并添加到listResult中，并返回
        if(  !list.isEmpty()) {
           // String[] temp = new String[list.size()];
            HashMap<String,Integer> map = new HashMap<String,Integer>();
            for(String s:list){
                //用正则 表达式匹配文件名中的数字
                String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(s);
                String num = m.replaceAll("").trim();
                Log.d("wzdebug" ,"lesson========"+ num);
                int index = Integer.parseInt(num);
                map.put(s,index);                           //key是文件名   value是文件名的中数字
            }

            List<Map.Entry<String, Integer>> infoIds =
                    new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
            Collections.sort(infoIds , new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return ( o1.getValue() - o2.getValue());
                    //return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            //按value排序后
            for (int i = 0; i < infoIds.size(); i++) {
                String name = infoIds.get(i).getKey();
                listResult.add(name);
            }
        }

        return listResult;
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

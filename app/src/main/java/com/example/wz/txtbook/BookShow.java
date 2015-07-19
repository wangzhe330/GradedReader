package com.example.wz.txtbook;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;


public class BookShow extends ActionBarActivity {

    TextView tv;
    TextView tvLigthLevel;
    InputStream inputStream;
    InputStream inputStreamLight;       //高亮关键词的输入流
    String txt;                         //内存中存储单篇lesson的String
    //HashMap hmLight = new HashMap();    //key:高亮关键词 value:高亮等级
    HashMap<String,Integer> hmLight = new HashMap<String,Integer>();
    HashSet<String> hsLight = new HashSet<String>();
    int lightLevel = 0;                 //高亮等级为1级
    int ligtStatus_open = 1;            //高亮状态：打开
    int ligtStatus_close = 2;        //高亮状态：关闭
    int lightStatusSwitch = ligtStatus_close;       //高亮开关，初始化为关闭
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

        //显示高亮级别的textview
        tvLigthLevel = (TextView)findViewById(R.id.show_tv1);
        tvLigthLevel.setText("LightLevel:"+lightLevel );

        AssetManager assetManager = this.getAssets();
        //inputStream = getResources().openRawResource(R.raw.nce4_book);  //之前放在raw文件夹里面测试用的
        try {
            inputStream = assetManager.open(fileName);
        }catch (IOException e){
            Log.e("ioe",e.getMessage());
        }
        //从inputStream中获取String数据:txt
        txt = getString(inputStream);
        tv.setText(txt);

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
        try{
            while( (line =reader.readLine()) != null ){
                String word;
                int level;
                String[] temp = line.split("\\t");
                if (temp.length == 2 && isNumeric(temp[1])){
                    word = temp[0];
                    level =  Integer.parseInt(temp[1]);
                    hmLight.put( word , level );
                    hsLight.add(word);
                }
            }
        }catch (IOException e){
            Log.e("ioe",e.getMessage());
        }

        //seekbar 获取用户设置的高亮等级
        SeekBar seekBar = (SeekBar)findViewById(R.id.show_sb0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lightLevel = progress;
                //显示高亮级别
                tvLigthLevel.setText("LightLevel:"+lightLevel );
                if(lightStatusSwitch == ligtStatus_open) {
                    lightByLevel(lightLevel);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Button 设置 是否高亮
        final Button btnLight = (Button)findViewById(R.id.show_btn0);
        btnLight.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lightStatusSwitch == ligtStatus_open) {

                    tv.setText(txt);
                    btnLight.setText("高亮生词");
                    lightStatusSwitch = ligtStatus_close;
                }else{
                    lightByLevel(lightLevel);
                    btnLight.setText("关闭高亮");
                    lightStatusSwitch = ligtStatus_open;
                }
            }
        });

    }

    /**
     * 高亮功能
     * @param level 指定高亮级别
     */
    public void lightByLevel(int level ){
        if(level>6||level<0){
            Log.e("wzerror","level out of limits");
            return ;
        }

        SpannableString sp = new SpannableString(txt);

        //遍历字符串 如果在hashSet中找到了就高亮，实现了未分级高亮功能
        int start,end;
        boolean flag  = false;
        int i = 0;
        char[] ch = txt.toCharArray();
        try {
            while (i < txt.length()) {
                while (i < txt.length() && !Character.isLetter(ch[i])) {
                    i++;
                }
                start = i;
                while (i < txt.length() && Character.isLetter(ch[i])) {
                    i++;
                }
                end = i;
                String target = txt.substring(start, end);
                if ((start < end) && target != null) {
                    if(hmLight.containsKey(target)){
                        int levelTmp = hmLight.get(target);
                        if(levelTmp < lightLevel) {
                            sp.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Log.d("wzdebug", "高亮一个词" + txt.substring(start, end));
                        }
                    }
                }
            }
            Log.d("wzdebug","高亮 done");
            tv.setText(sp);
        }catch (Exception e){
            Log.e("wzdebug",e.getMessage());
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



        return super.onOptionsItemSelected(item);
    }
}

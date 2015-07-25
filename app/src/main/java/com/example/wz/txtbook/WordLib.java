package com.example.wz.txtbook;

import android.app.Application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wz on 2015/7/25.
 */
public class WordLib extends Application{

    //记录单词等级的数据结构
    private HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
    //记录是否读取过单词分级文件的变量
    private boolean hasSetted = false;

    public HashMap<String,Integer>  getWordLib(){
        return hashMap;
    }
    public void setWordLib( HashMap<String,Integer> para){

        Iterator iter = para.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            hashMap.put((String)entry.getKey(),(Integer)entry.getValue());
        }
    }
    public void setSetStatue(){
        hasSetted = true;
    }
    public boolean getSetStatue (){
        return hasSetted;
    }
}

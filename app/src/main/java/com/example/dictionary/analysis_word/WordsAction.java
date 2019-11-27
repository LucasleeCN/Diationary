package com.example.dictionary.analysis_word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.example.dictionary.Tools.FileUtil;
import com.example.dictionary.Tools.HttpCallBackListener;
import com.example.dictionary.Tools.HttpUtil;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WordsAction {
    private  static  WordsAction wordsAction;
    private final String TABLE_WORDS = "Words";//数据库表名;
    private SQLiteDatabase db;


    private MediaPlayer player = null;

    private WordsAction(Context context){
        WordsSQLiteOpenHelper helper = new WordsSQLiteOpenHelper(context,TABLE_WORDS,null,1);
        db= helper.getReadableDatabase();
    }
    public static WordsAction getInstance(Context context){
        if(wordsAction==null){
            synchronized (WordsAction.class){
                if(wordsAction == null){
                    wordsAction = new WordsAction(context);
                }
            }
        }
        return wordsAction;
    }
    public boolean saveWords(Words words){//向数据库中保存新的words
     if(words.getSent().length()>0){
         ContentValues values = new ContentValues();
         values.put("isChinese",""+words.getIsChinese());
         values.put("word_key", words.getKey());
         values.put("fy", words.getFy());
         values.put("psE", words.getPsE());
         values.put("pronE", words.getPronE());
         values.put("psA", words.getPsA());
         values.put("pronA", words.getPronA());
         values.put("posAcceptation", words.getPosAcceptation());
         values.put("sent", words.getSent());
         db.insert(TABLE_WORDS, null, values);
         values.clear();
         return true;
        }
     else
         return  false;
    }
    public boolean saveWordsToMySQL(Words words){//向数据库中保存新的words
        if(words.getSent().length()>0){
            ContentValues values = new ContentValues();
            values.put("isChinese",""+words.getIsChinese());
            values.put("word_key", words.getKey());
            values.put("fy", words.getFy());
            values.put("psE", words.getPsE());
            values.put("pronE", words.getPronE());
            values.put("psA", words.getPsA());
            values.put("pronA", words.getPronA());
            values.put("posAcceptation", words.getPosAcceptation());
            values.put("sent", words.getSent());
            db.insert("MyWords", null, values);
            values.clear();
            return true;
        }
        else
            return  false;
    }


    public Words getWordsFromSQLite(String key){
        Words words = new Words();
        Cursor cursor = db.query(TABLE_WORDS,null,"word_key=?",new String[]{key},null,null,null);
        if(cursor.getCount()>0){
            Log.d("TAG","查到了");

            if(cursor.moveToFirst()){
                Log.d("测试",cursor.getString(cursor.getColumnIndex("word_key")));
                do{
                    String isChinese = cursor.getString(cursor.getColumnIndex("isChinese"));
                    if(isChinese.equals("true")){
                        words.setIsChinese(true);
                    }
                    else if (isChinese.equals("false")){
                        words.setIsChinese(false);
                    }
                    words.setKey(cursor.getString(cursor.getColumnIndex("word_key")));
                    words.setFy(cursor.getString(cursor.getColumnIndex("fy")));
                    words.setPsE(cursor.getString(cursor.getColumnIndex("psE")));
                    words.setPronE(cursor.getString(cursor.getColumnIndex("pronE")));
                    words.setPsA(cursor.getString(cursor.getColumnIndex("psA")));
                    words.setPronA(cursor.getString(cursor.getColumnIndex("pronA")));
                    words.setPosAcceptation(cursor.getString(cursor.getColumnIndex("posAcceptation")));
                    words.setSent(cursor.getString(cursor.getColumnIndex("sent")));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }else{
            Log.d("TAG","没找到");
            cursor.close();
        }
        return words;
    }
    public Words[] getWordsFromMySQLite(){
        Words words = new Words();
        Words[] wordsArr={} ;
        Cursor cursor = db.query("MyWords",null,null,null,null,null,null);
        if(cursor.getCount()>0){
            Log.d("TAG","我的单词查到了");
            wordsArr=new Words[cursor.getCount()];
            if(cursor.moveToFirst()){

                do{
//                                    Log.d("测试",cursor.getString(cursor.getColumnIndex("word_key")));

                    String isChinese = cursor.getString(cursor.getColumnIndex("isChinese"));
                    if(isChinese.equals("true")){
                        words.setIsChinese(true);
                    }
                    else if (isChinese.equals("false")){
                        words.setIsChinese(false);
                    }
                    words.setKey(cursor.getString(cursor.getColumnIndex("word_key")));
                    words.setFy(cursor.getString(cursor.getColumnIndex("fy")));
                    words.setPsE(cursor.getString(cursor.getColumnIndex("psE")));
                    words.setPronE(cursor.getString(cursor.getColumnIndex("pronE")));
                    words.setPsA(cursor.getString(cursor.getColumnIndex("psA")));
                    words.setPronA(cursor.getString(cursor.getColumnIndex("pronA")));
                    words.setPosAcceptation(cursor.getString(cursor.getColumnIndex("posAcceptation")));
                    words.setSent(cursor.getString(cursor.getColumnIndex("sent")));
                    wordsArr[cursor.getPosition()]=words;
                    words= new Words();
                    Log.d("test","cursor的位置"+wordsArr[cursor.getPosition()].getKey());

                }while(cursor.moveToNext());
            }
            cursor.close();
        }else{
            Log.d("TAG","没找到");
            cursor.close();
        }
        return wordsArr;
    }
    public boolean deleteFromMySQLite(String key){
//         db.rawQuery("delete from MyWords where word_key=?",new String[]{key});
         db.delete("MyWords","word_key=?",new String[]{key});
        Log.d("测试","已删除"+key);
         return true;

    }
    public boolean changeTheWord(String Nkey,String Okey){
        db.execSQL("update MyWords set word_key = ? where word_key=?",new String[]{Nkey,Okey});
        Log.d("测试","已经删除"+Okey);
        Log.d("测试","已经更新"+Nkey);
        return true;
    }


    public Words[] getDimWordsFromMySQLite(String dimKey){
        Words words = new Words();
        Words[] wordsArr={} ;
        Cursor  cursor=db.query("MyWords", null,"word_key"+"  like '%" + dimKey + "%'", null, null, null, null);
        if(cursor.getCount()>0){
            Log.d("TAG","我的单词查到了");
            wordsArr=new Words[cursor.getCount()];
            if(cursor.moveToFirst()){

                do{
//                                    Log.d("测试",cursor.getString(cursor.getColumnIndex("word_key")));

                    String isChinese = cursor.getString(cursor.getColumnIndex("isChinese"));
                    if(isChinese.equals("true")){
                        words.setIsChinese(true);
                    }
                    else if (isChinese.equals("false")){
                        words.setIsChinese(false);
                    }
                    words.setKey(cursor.getString(cursor.getColumnIndex("word_key")));
                    words.setFy(cursor.getString(cursor.getColumnIndex("fy")));
                    words.setPsE(cursor.getString(cursor.getColumnIndex("psE")));
                    words.setPronE(cursor.getString(cursor.getColumnIndex("pronE")));
                    words.setPsA(cursor.getString(cursor.getColumnIndex("psA")));
                    words.setPronA(cursor.getString(cursor.getColumnIndex("pronA")));
                    words.setPosAcceptation(cursor.getString(cursor.getColumnIndex("posAcceptation")));
                    words.setSent(cursor.getString(cursor.getColumnIndex("sent")));
                    wordsArr[cursor.getPosition()]=words;
                    words= new Words();
                    Log.d("test","cursor3的位置"+wordsArr[cursor.getPosition()].getKey());

                }while(cursor.moveToNext());
            }
            cursor.close();
        }else{
            Log.d("TAG","没找到");
            cursor.close();
        }
        return wordsArr;
    }

    public String getAddressForWords(final String key){
        String address_p1 = "http://dict-co.iciba.com/api/dictionary.php?w=";
        String address_p2 = "";
        String address_p3 = "&key=EAFEB21925A3BE14FCA5C434D2CDFCAA";
        if(isChinese(key)){
            try{
                address_p2="_"+ URLEncoder.encode(key,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.d("Connected","链接失败");
                e.printStackTrace();
            }
        }
        else
            address_p2 =key;
        return address_p1+address_p2+address_p3;
    }
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
    /**
     * 保存words的发音MP3文件到SD卡
     * 先请求Http，成功后保存
     *
     * @param words words实例
     */
    public void saveWordsMP3(Words words) {
        String addressE = words.getPronE();
        String addressA = words.getPronA();
        if (addressE != "") {//判断有地址才发送网络请求
            final String filePathE = words.getKey();
            HttpUtil.sentHttpRequest(addressE, new HttpCallBackListener() {

                @Override
                public void onError(Exception e) {

                }

                //请求完成后的回调方法

                public void onFinish(InputStream inputStream) {
                    //调用FileUtil的方法将MP3文件保存到SD卡
                    FileUtil.getInstance().writeToSD(filePathE, "E.mp3", inputStream);
                }

            });
        }
        if (addressA != "") {
            final String filePathA = words.getKey();
            HttpUtil.sentHttpRequest(addressA, new HttpCallBackListener() {


                @Override
                public void onError(Exception e) {

                }
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathA, "A.mp3", inputStream);
                }

            });
        }
    }

    /**
     * 播放words的发音
     *
     * @param wordsKey 单词的key
     * @param ps       E 代表英式发音
     *                 A 代表美式发音
     * @param context  上下文
     */
    public void playMP3(String wordsKey, String ps, Context context) {
        String fileName = wordsKey + "/" + ps + ".mp3";
        String adrs = FileUtil.getInstance().getPathInSD(fileName);
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
        if (adrs != "") {//有内容则播放
            player = MediaPlayer.create(context, Uri.parse(adrs));
            Log.d("测试", "播放");
            player.start();
        } else {//没有内容则重新去下载
            Words words = getWordsFromSQLite(wordsKey);
            saveWordsMP3(words);
        }
    }

}

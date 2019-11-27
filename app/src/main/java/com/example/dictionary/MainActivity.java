package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionary.Tools.HttpCallBackListener;
import com.example.dictionary.Tools.HttpUtil;
import com.example.dictionary.analysis_word.WordsAction;
import com.example.dictionary.analysis_word.ParseXML;
import com.example.dictionary.analysis_word.Words;
import com.example.dictionary.analysis_word.WordsHandler;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public Button button_voiceE;
    public Button button_voiceA;
    public SearchView search_word;
    public TextView search_word_key;
    public TextView search_posE;
    public TextView search_posA;
    public TextView search_psA;
    public TextView search_psE;
    public TextView search_posAcceptation;
    public TextView search_sent;
    public Words words;
    public WordsAction wordsAction;
    public Button add_word ;

    public void init() {
        button_voiceE = findViewById(R.id.button_words_voiceE);
        button_voiceA = findViewById(R.id.button_words_voiceA);
        search_word = findViewById(R.id.search_word);
        search_word.setSubmitButtonEnabled(true);//设置显示搜索按钮
        search_word.setIconifiedByDefault(false);
        search_word_key = findViewById(R.id.search_words_key);
        search_posE = findViewById(R.id.search_words_posE);
        search_posA = findViewById(R.id.search_words_posA);
        search_psA = findViewById(R.id.search_words_psA);
        search_psE = findViewById(R.id.search_words_psE);
        search_posAcceptation = findViewById(R.id.search_words_posAcceptation);
        search_sent = findViewById(R.id.search_words_sent);
        add_word = findViewById(R.id.button_add_words);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.word_text:
                Intent intent = new Intent(MainActivity.this, MyWord_Test.class);
                startActivity(intent);
                break;
            case R.id.help:
                Toast.makeText(this,"copright@LucasleeCN",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    //判断网络查找不到该词的情况
                    if (words.getSent().length() > 0) {
                        upDateView();
                    } else {
                        search_word.setVisibility(View.GONE);
                        button_voiceA.setVisibility(View.GONE);
                        button_voiceE.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "抱歉！找不到该词！", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("测试", "tv保存2");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
        wordsAction=WordsAction.getInstance(this);
        button_voiceA.setOnClickListener(this);
        button_voiceE.setOnClickListener(this);
        add_word.setOnClickListener(this);
        search_word.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadWords(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        search_word_key.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_words_voiceA:
                wordsAction.playMP3(words.getKey(), "A", MainActivity.this);
            case R.id.button_words_voiceE:
                wordsAction.playMP3(words.getKey(), "E", MainActivity.this);
            case R.id.button_add_words:
               wordsAction.saveWordsToMySQL(words);
//                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                Log.d("测试","添加成功");

        }
    }
    private void loadWords(String key) {
        words = wordsAction.getWordsFromSQLite(key);//从数据库里查词
        if ("" == words.getKey()) {
            String address = wordsAction.getAddressForWords(key);
            HttpUtil.sentHttpRequest(address, new HttpCallBackListener() {

                @Override
                public void onFinish(InputStream inputStream) {
                    WordsHandler wordsHandler = new WordsHandler();
                    ParseXML.parse(wordsHandler, inputStream);
                    words = wordsHandler.getWords();
                    wordsAction.saveWords(words);
                    wordsAction.saveWordsMP3(words);
                    handler.sendEmptyMessage(111);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }


            });
        } else {
            upDateView();
        }

    }

    @SuppressLint("StringFormatInvalid")
    public void  upDateView(){
        if(words.getIsChinese()){
            search_posAcceptation.setText(words.getFy());
            search_posA.setVisibility(View.GONE);
            search_posE.setVisibility(View.GONE);
        }
        else{
            search_posAcceptation.setText(words.getPosAcceptation());
            if(words.getPsE()!=""){
                search_psE.setText( words.getPsE());
                search_psE.setVisibility(View.VISIBLE);
            }
            else
                search_psE.setVisibility(View.GONE);
            if(words.getPsA()!=""){
                search_psA.setText( words.getPsA());
                search_psA.setVisibility(View.VISIBLE);
            }
            else
                search_psA.setVisibility(View.GONE);
        }
        search_word_key.setText(words.getKey());
        search_sent.setText(words.getSent());
//
    }
    //加载actionbar的菜单

//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.actionbar_layout_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

}

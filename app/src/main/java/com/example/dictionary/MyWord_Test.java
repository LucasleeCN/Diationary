package com.example.dictionary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.dictionary.analysis_word.Words;
import com.example.dictionary.analysis_word.WordsAction;

import java.util.ArrayList;
import java.util.List;


public class MyWord_Test extends AppCompatActivity {
    SearchView searchView;
    WordsAction wordsAction;
    Words[] words;
    private String[] data ;
//    List<Words> wordsList = new ArrayList<Words>();
    ListView listView ;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_text);
        wordsAction= WordsAction.getInstance(this);
        searchView = findViewById(R.id.search_words_fromMySQL);
        listView = findViewById(R.id.word_list);

       init();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                 words=wordsAction.getDimWordsFromMySQLite(query);
                data=new String[words.length];
                Log.d("测试","words数组的长度"+words.length);
                if(words!=null) {
                    for (int i = 0; i < words.length; i++) {
                        Log.d("Words", "俺的单词查到啦" + words[i].getKey());
                        data[i] = words[i].getKey();
                    }
                    adapter = new ArrayAdapter<String>(MyWord_Test.this, android.R.layout.simple_list_item_1, data);
                    listView.setAdapter(adapter);

                }
                 return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Words word = wordsList.get(position);
//                wordsAction.deleteFromMySQLite(word.getKey());
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyWord_Test.this);
                dialog.setTitle("修改");
                LayoutInflater change_inflater = getLayoutInflater();
                final View view1 = change_inflater.inflate(R.layout.view,null);
                dialog.setView(view1);
                dialog.setMessage("请输入");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = view1.findViewById(R.id.ed1);
                        String str = data[position];
                        String newStr ="" ;
                        newStr=editText.getText().toString();
                        if(str!=""&&newStr!=null) {
                            Log.d("测试",""+str);
                            Log.d("测试",""+newStr);

                            if (wordsAction.changeTheWord(newStr, str)) {
                                init();
                                Log.d("测试", "已更新");
                            }
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String str = data[position];
                if(wordsAction.deleteFromMySQLite(str)){
                    init();
                    Toast.makeText(MyWord_Test.this,"删除成功",Toast.LENGTH_SHORT).show();
//                    Log.d("测试",data[position]+"已删除");
                }


                return true;
            }
        });
//        button_my_words.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                SQLiteDatabase db = wordsSQLiteOpenHelper.getReadableDatabase();
//                Cursor cursor = db.query(data_table,null,null,null,null,null,null);
//                cursor.moveToFirst();
////                if(cursor.getCount()>0){
////                    Log.d("测试","cursor非空");
////
////                    while(cursor.moveToNext()){
////                        Log.d("测试", cursor.getString(cursor.get
////                        ColumnIndex("word_key")));
////
//////                        data[cursor.getPosition()] =
////                        Log.d("TAG",cursor.getString(4) );
////
////                        wordList.add(cursor.getString(cursor.getColumnIndex("word_key")));
////                    }
////                }else
////                {
////                    Log.d("测试","cursor为空");
////                }
//
//                if(cursor.getCount()>0) {
//                    if (cursor.moveToFirst()) {
//                        Log.d("test", "执行到这里了");
//                        do {
//                            data[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex("word_key"));
//                            Log.d("TAG", data[cursor.getPosition()]);
//                        }
//                        while (cursor.moveToNext());
//
//                    } else {
//                        Log.d("测试", "数据库为空");
//                    }
//                }else{
//                    Log.d("测试","cursor为空");
//                }
//                cursor.close();
//
//            }
//        });


    }
    public  void init(){
        words = wordsAction.getWordsFromMySQLite();
        data=new String[words.length];
        Log.d("测试","words数组的长度"+words.length);
        if(words!=null) {
            for (int i = 0; i < words.length; i++) {
                Log.d("Words", "俺的单词查到啦" + words[i].getKey());
//                wordsList.add(words[i]);
                data[i] = words[i].getKey();
            }

            adapter = new ArrayAdapter<String>(MyWord_Test.this, android.R.layout.simple_list_item_1, data);
            listView.setAdapter(adapter);


        }
    }

}

package com.example.dictionary;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.dictionary.analysis_word.WordsSQLiteOpenHelper;

public class MyContentProvider extends ContentProvider {
  public static final int WORDS_DIR=0;
    public static final int WORDS_ITEM=1;
    public static final int MYWORDS_DIR=2;
    public static final int MYWORDS_ITEM=3;
    public static UriMatcher uriMatcher;
    public static final  String authority ="com.example.dictionary.provider";
    private WordsSQLiteOpenHelper wordsSQLiteOpenHelper ;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(authority,"Words",WORDS_DIR);
        uriMatcher.addURI(authority,"Words/#",WORDS_ITEM);
        uriMatcher.addURI(authority,"MyWords",MYWORDS_DIR);
        uriMatcher.addURI(authority,"MyWords/#",MYWORDS_ITEM);
    }

    public MyContentProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
      SQLiteDatabase db = wordsSQLiteOpenHelper.getReadableDatabase();
      int deleteRows=0;
      switch (uriMatcher.match(uri)){
          case WORDS_DIR:
              deleteRows=db.delete("Words",selection,selectionArgs);
              break;
          case WORDS_ITEM:
              String wordId1 = uri.getPathSegments().get(1);
              deleteRows = db.delete("Words","id=?",new String[]{wordId1});
              break;
          case MYWORDS_DIR:
              deleteRows=db.delete("MyWords",selection,selectionArgs);
              break;
          case MYWORDS_ITEM:
              String wordId2 = uri.getPathSegments().get(1);
              deleteRows = db.delete("MyWords","id=?",new String[]{wordId2});
              break;
              default:
                  break;
      }
      return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        String str = "";
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
                str="vnd.android.cursor.dir/vnd.com.example.dictionary.provider.Words";
                break;
            case WORDS_ITEM:
                str = "vnd.android.cursor.item/vnd.com.example.dictionary.provider.Words";
                break;
            case MYWORDS_DIR:
                str="vnd.android.cursor.dir/vnd.com.example.dictionary.provider.MyWords";
                break;
            case MYWORDS_ITEM:
                str = "vnd.android.cursor.item/vnd.com.example.dictionary.provider.MyWords";
                break;
        }
        return str;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = wordsSQLiteOpenHelper.getReadableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
            case WORDS_ITEM:
                long newId1 = db.insert("Words",null,values);
                uriReturn=Uri.parse("content://"+authority+"/Words/"+newId1);
                break;
            case MYWORDS_DIR:
            case  MYWORDS_ITEM:
                long newId2 = db.insert("MyWords",null,values);
                uriReturn =Uri.parse("content://"+authority+"/MyWords/"+newId2);
                break;
                default:
                    break;
        }
        return  uriReturn;

    }

    @Override
    public boolean onCreate() {
        wordsSQLiteOpenHelper = new WordsSQLiteOpenHelper(getContext(),"Words",null,1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = wordsSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor =null;
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
                cursor= db.query("Words",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WORDS_ITEM:
                cursor = db.query("Words",projection,"id=?", new String[]{"id"},null,null,sortOrder);
                break;
            case MYWORDS_DIR:
                cursor= db.query("MyWords",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MYWORDS_ITEM:
                cursor = db.query("MyWords",projection,"id=?", new String[]{"id"},null,null,sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = wordsSQLiteOpenHelper.getReadableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case WORDS_DIR:
                updateRows =db.update("Words",values,selection,selectionArgs);
                break;
            case WORDS_ITEM:
                String id1 = uri.getPathSegments().get(1);
                updateRows =db.update("Words",values,"id=?",new String[]{id1});
                break;
            case MYWORDS_DIR:
                updateRows =db.update("MyWords",values,selection,selectionArgs);
                break;
            case MYWORDS_ITEM:
                String id2 = uri.getPathSegments().get(1);
                updateRows =db.update("MyWords",values,"id=?",new String[]{id2});
                break;
                default:
                    break;
        }
        return updateRows;
    }
}

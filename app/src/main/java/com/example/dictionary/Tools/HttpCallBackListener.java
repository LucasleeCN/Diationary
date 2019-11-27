package com.example.dictionary.Tools;

import java.io.InputStream;

public interface HttpCallBackListener {

    void onError(Exception e);
    void onFinish(InputStream inputStream);
}

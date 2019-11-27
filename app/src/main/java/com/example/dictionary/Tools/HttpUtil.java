package com.example.dictionary.Tools;


import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
   /*
     * 在新线程中发送网络请求
     * @param address  网络地址
     * @param listener HttpCallBackListener接口的实现类;
    */

    public static void sentHttpRequest(final String address, final HttpCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    if (listener != null) {
                        listener.onFinish(inputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /*HttpUtil.sendHttpRequest(address new HttpCallbackListener()){


     */
}

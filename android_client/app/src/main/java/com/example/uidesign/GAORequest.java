package com.example.uidesign;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GAORequest {
    static private String getUrl = "http://192.168.1.147/arduino/";
    static private String testUrl = "https://smilegaoranger.herokuapp.com/";
    static public Map<String, Integer> motorInitial = new HashMap<String, Integer>(){{
        put("Base", 0);
        put("Shoulder", 40);
        put("Elbow", 180);
        put("Wrist", 0);
        put("Rotate", 170);
        put("Gripper", 73);
    }};

    static public String getUrlHost(boolean isTest){
        return (isTest)?testUrl:getUrl;
    }

    static public void sendRequest(String url, boolean isTest, Callback callback){
        OkHttpClient client = new OkHttpClient();

        String targetUrl = getUrlHost(isTest) + url;

        Request request = new Request.Builder()
                .url(targetUrl)
                .build();

        client.newCall(request).enqueue(callback);
    }
}

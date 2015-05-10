package com.example.maoz.hellowworld;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * แปลงข้อมูล JSON to ArratList
 */
public abstract class JSONParsing {

    /**
     * อ่านข้อมูลจาก JSONFILE
     * @param URL คือที่ตั้งไฟล์
     * @return ข้อมูล JSON ทั้งหน้า
     * */
    public static String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("STATUSCODE", String.valueOf(statusCode));
                Log.d("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            if (e.getLocalizedMessage() != null){
                Log.d("readJSONFeed", e.getLocalizedMessage());
            }
        }
        return stringBuilder.toString();
    }
}

package com.example.maoz.hellowworld;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pongpat705 on 4/13/2015.
 */
public class JSONRouteWaypoint {
    public String getPath(LatLng start, LatLng end) {
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&mode=walking";
        String d = null;

        StringBuilder stringBuilder = new StringBuilder();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                inputStream.close();
            }else{

                System.out.println("JSON Failed to download file");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("readJSONFeed"+e.getLocalizedMessage());
        }
        d = stringBuilder.toString();

        return d;
    }

    public ArrayList<HashMap<String,String>> convert(JSONObject jObject) {

        ArrayList<HashMap<String,String>> routes = new ArrayList<>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        JSONObject jDistance = null;


        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    HashMap<String, String> allDistance = new HashMap<String, String>();
                    allDistance.put("distance", jDistance.getString("text"));
                    routes.add(allDistance);

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String instructions = "";
                        instructions =  ((JSONObject) jSteps.get(k)).get("html_instructions").toString().replaceAll("\\<.*?>"," ");
                        HashMap<String, String> direction = new HashMap<String, String>();
                        direction.put("direction",instructions);

                        jDistance = ((JSONObject) jSteps.get(k)).getJSONObject("distance");
                        direction.put("distance", jDistance.getString("text"));

                        routes.add(direction);

                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;

    }

    public ArrayList<HashMap<String,String>> getWaypoint(String... jsondata){
        JSONObject jObject;
        ArrayList<HashMap<String,String>> routes = null;
        try{
            jObject = new JSONObject(jsondata[0]);
            JSONRouteWaypoint parser = new JSONRouteWaypoint();
            // Starts parsing data
            routes = parser.convert(jObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;
    }
}

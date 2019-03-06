package com.hallo;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpManager {
    private String SERVER_DOMAIN = "http://10.0.2.2/";
    private String path = "~/nicholas/holla/";
    /**
     * Asynchronously execute DoInBackground by passing in the URL as param[0]
     * Send HTTP GET request
     */
    private class GetData extends AsyncTask<String, Void, JSONObject> {

        /**
         * The Async Method that send the HTTP GET request
         * @param params array of strings
         * @return JSONObject of the HTTP request result
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            StringBuilder total;
            JSONObject JsonObject=null;
            String response="";
            try {
                total = new StringBuilder();
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                //httpConnection.addRequestProperty(REQUEST_HEADER_ACCOUNT_KEY, API_KEY);
                //httpConnection.addRequestProperty(REQUEST_HEADER_UNIQUE_USER_ID, UNIQUE_USERID);
                //httpConnection.addRequestProperty(REQUEST_HEADER_ACCEPT, ACCEPT_HEADER);
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream content = httpConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                }
                JsonObject= new JSONObject(response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return JsonObject;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
        }

    }

    private class PostData extends AsyncTask<String, Void, JSONObject> {
        HashMap<String, String> parameters;
        protected PostData(HashMap<String, String> params){
            parameters = params;
        }
        /**
         * The Async Method that send the HTTP POST request
         * @param params array of strings
         * @return JSONObject of the HTTP request result
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            StringBuilder total;
            JSONObject JsonObject=null;
            String response="";
            try {
                total = new StringBuilder();
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();

                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("POST");
                //httpConnection.connect();
                httpConnection.setReadTimeout(15000);
                httpConnection.setConnectTimeout(15000);

                httpConnection.setDoInput(true);
                httpConnection.setDoOutput(true);
                OutputStream os = httpConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(parameters));

                writer.flush();
                writer.close();
                os.close();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream content = httpConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                }
                JsonObject= new JSONObject(response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return JsonObject;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
        }

    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public void Post(HashMap<String, String> param){
        PostData post = new PostData(param);
        post.execute();

    }

}
package com.vanbinh.chatting.apiconnect.baseconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vanbinh on 8/16/2017.
 */

public class JsonResponseFromUrl {
    String mUrl;
    String mData;
    private int responseCode;
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }


    public JsonResponseFromUrl(String url, String data) {
        this.mUrl = url;
        this.mData = data;
    }

    public String getResponsePost() {
        InputStreamReader reader = null;
        String strJson = null;
        HttpURLConnection conn = null;
        try {

            URL url = new URL(mUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json, charset=UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(mData.getBytes("UTF-8"));
            os.flush();
            os.close();
            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                strJson = sb.toString();
                bufferedReader.close();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return strJson;
    }

    public String getResponseGet() {
        InputStreamReader reader = null;
        String strJson = null;
        HttpURLConnection conn = null;
        try {
            String strUrlGet = mUrl + mData;
            URL url = new URL(strUrlGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                strJson = sb.toString();
                bufferedReader.close();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return strJson;
    }
    public String getResponseDelete() {
        InputStreamReader reader = null;
        String strJson = null;
        HttpURLConnection conn = null;
        try {
            String strUrlGet = mUrl + mData;
            URL url = new URL(strUrlGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                strJson = sb.toString();
                bufferedReader.close();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return strJson;
    }
}

package com.dapl.halaladvisor;

import com.dapl.halaladvisor.Utility.LogUtils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class JSONParser {

    private static final String TAG = JSONParser.class.getSimpleName();

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    int response_code;
    String response_status = "";

    // constructor
    public JSONParser() {

    }

    public String getJSONFromUrl(final String _url) {

        try {
            URL url = new URL(_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Check the connection status
            if (urlConnection.getResponseCode() == 200) {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                json = sb.toString();
                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            } else {
                // Do something
                json = "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        // Return the data from specified url
        return json;

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public String makeHttpRequest(String _url, String method, HashMap<String, String> params) {

        StringBuilder sbParams = new StringBuilder();
        String paramString = "";
        try {
            paramString = getPostDataString(params);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//    LogUtils.showPrintln("URL="+_url+"?"+paramString);

        // Making HTTP request
        try {

            // check for request method
            if (method == "POST") {

                // request method is POST

                URL url = new URL(_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");


                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

                urlConnection.setReadTimeout(40000);       // 50,000 sec
                urlConnection.setConnectTimeout(50000);   // 50,000 sec


                urlConnection.connect();

                //  String paramsString = sbParams.toString();

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(paramString);
                wr.flush();
                wr.close();
                if (urlConnection.getResponseCode() == 200) {
                    // if response code = 200 ok
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    // Read the BufferedInputStream
                    BufferedReader r = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        sb.append(line);
                        LogUtils.printf("json: " + line);
                    }
                    json = sb.toString();
                    // End reading...............
                    LogUtils.printf("json: " + json);
                    // Disconnect the HttpURLConnection
                    urlConnection.disconnect();
                } else {
                    // Do something
                    LogUtils.printf("json: " + json);
                    json = "";
                }


            } else if (method == "GET") {
                // request method is GET

                //if (sbParams.length() != 0) {
                _url += "?" + paramString;
                //}

                System.out.println("I am Line 1");

                URL url = new URL(_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                if (urlConnection.getResponseCode() == 200) {

                    System.out.println("I am Line 2");
                    // if response code = 200 ok
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    // Read the BufferedInputStream
                    BufferedReader r = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    System.out.println("I am Line 3");
                    int i = 0;
                    while ((line = r.readLine()) != null) {
                        sb.append(line);
                        i++;

                        System.out.println("I am Line line " + i);

                    }
                    json = sb.toString();
                    // End reading...............

                    // Disconnect the HttpURLConnection

                    System.out.println("I am Line 4");
                    urlConnection.disconnect();
                } else {
                    // Do something
                    json = "";
                    System.out.println("I am Line else");
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            json = "";
        } catch (IOException e) {
            e.printStackTrace();
            json = "";
        }
        LogUtils.printf("json: " + json);
        return json;

    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        //Log.e("POST PARAMS",""+params.toString());
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            if (entry.getValue() == null) {
                //to handle null value if passed
                result.append(URLEncoder.encode("", "UTF-8"));
            } else {
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }

        return result.toString();
    }

    public String PostAnswerJson(String _url, String jsonString) {
        try {
            URL url = new URL(_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();

            //  String paramsString = sbParams.toString();


            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(jsonString);
            wr.flush();
            wr.close();
            if (urlConnection.getResponseCode() == 200) {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                json = sb.toString();
                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            } else {
                // Do something
                json = "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            json = "";
        } catch (IOException e) {
            e.printStackTrace();
            json = "";
        }

        // Log.e("JSON_RESPONSE", ""+json);

        return json;
    }
}

package com.dapl.halaladvisor.extra;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class VollyCustomRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map params;

    public VollyCustomRequest(String url, Map<String, String> params,
                              Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        Log.e("TANAY", "URL " + url);
    }

    public VollyCustomRequest(String url,
                              Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        Log.e("TANAY", "URL " + url);
    }

    public VollyCustomRequest(int method, String url, Map params,
                              Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        Log.e("TANAY", "URL " + url);
    }

    protected Map getParams()
            throws com.android.volley.AuthFailureError {

        Log.e("TANAY", "PARAM " + params);

        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.e("TANAY", jsonString);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}

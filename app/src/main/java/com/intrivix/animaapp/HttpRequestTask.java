package com.intrivix.animaapp;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by devin on 6/19/14.
 */
public class HttpRequestTask extends AsyncTask<Object, Object, Object> {
    private static final String TAG = "HttpRequestTask";

    private int mHTTPResponseCode;
    private String mDownloadedContents;

    private String mUrl, mRequestParams;
    private Handler mHandler;

    public static final int GET = 1, PUT = 2, POST = 3;
    private int mReqType;

    public HttpRequestTask(Handler handler, String url, String requestParams, int reqType) {
        mHandler = handler;
        mUrl = url;
        mRequestParams = requestParams;
        mReqType = reqType;
    }

    @Override
    protected Object doInBackground(Object... args) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest httpReq = null;

        switch (mReqType) {
            case GET:
                httpReq = new HttpGet(mUrl);
                break;

            case PUT:
                HttpPut put = new HttpPut(mUrl);
                try {
                    put.setEntity(new StringEntity(mRequestParams));

                    put.setHeader("Content-Type", "application/json");
                } catch (UnsupportedEncodingException e1) {
                    // Consume exceptions
                    mHandler.onComplete("Error.");
                }
                httpReq = put;
                break;

            case POST:
                HttpPost post = new HttpPost(mUrl);
                try {
                    post.setEntity(new StringEntity(mRequestParams));
                    // System.out.println("API SENT THIS DATA: " + params);
                    // System.out.println("API SENT TO URL: " + url);
                    post.setHeader("Content-Type", "application/json");
                } catch (UnsupportedEncodingException e1) {
                    // Consume exceptions
                    mHandler.onComplete("Error.");
                }
                httpReq = post;
                break;
        }

        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpReq);

            // Get the status code
            mHTTPResponseCode = httpResponse.getStatusLine().getStatusCode();

            if (mHTTPResponseCode == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    httpEntity = new BufferedHttpEntity(httpEntity);
                    InputStream httpContent = null;
                    try {
                        httpContent = httpEntity.getContent();
                        mDownloadedContents = inputStreamToStringConverter(httpContent);

                    } finally {
                        if (httpContent != null) {
                            httpContent.close();
                        }
                    }
                }
            }
        } catch (IOException e) {
            // [TODO] Error handling
            return null;
        }

        return mDownloadedContents;
    }


    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mHandler != null) {
            mHandler.onComplete(result + "");
        }
    }

    public interface Handler {
        public void onComplete(String result);
    }

    public static String inputStreamToStringConverter(InputStream in) {
        if (in == null) {
            return null;
        }

        String strLine = "";
        StringBuilder sb = new StringBuilder();

        // Convert from InputStream to String
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            try {
                isr = new InputStreamReader(in, "utf-8");
                br = new BufferedReader(isr);

                strLine = br.readLine();
                while (strLine != null) {
                    sb.append(strLine);
                    sb.append("\n");
                    strLine = br.readLine();
                }
            } finally {
                if (br != null) {
                    br.close();
                }

                if (isr != null) {
                    isr.close();
                }
            }
        } catch (UnsupportedEncodingException e) {} catch (IOException e) {}

        return sb.toString();
    }

}


package com.hendra.middleware.inet;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Hendra Soewarno(0119067505) on 7/17/2017.
 */
public abstract class HTTPRequest {
    private static final String charset = "UTF-8";

    public int responseCode;

    public int getResponseCode() {
        return responseCode;
    }

    protected abstract void publish(int bytesread);

    public String post(String sourceUrl, String postData, HashMap<String, String> header) throws Exception {
        URL url = new URL(sourceUrl);
        Log.d("debug", "POST " + sourceUrl);
        HttpsURLConnection hurc = (HttpsURLConnection) url.openConnection();
        //HttpURLConnection hurc = (HttpURLConnection) url.openConnection();
        //hurc.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
        //hurc.setHostnameVerifier(new AllowAllHostnameVerifier());
        hurc.setReadTimeout(10000 /* milliseconds */);
        hurc.setConnectTimeout(15000 /* milliseconds */);
        hurc.setDoOutput(true); // Triggers POST.
        hurc.setRequestProperty("Accept-Charset", charset);
        //hurc.setRequestProperty("Accept", "application/json");
        if (header!=null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                hurc.setRequestProperty(entry.getKey(), entry.getValue());
                Log.d("Debug-HTTP-Header", entry.getKey() + ":" + entry.getValue());
            }
        }
        //else
        //    hurc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        hurc.setRequestMethod("POST");
        Log.d("Debug-HTTP-Payload", postData);
        OutputStreamWriter out = new OutputStreamWriter(hurc.getOutputStream());
        out.write(postData);
        out.close();
        hurc.connect();
        responseCode = hurc.getResponseCode();
        Log.d("Debug-HTTP-Response", "RESPONSE CODE:" + hurc.getResponseCode());
        InputStream is;
        if (responseCode >= 200 && responseCode < 400) {
            is = hurc.getInputStream();
        }
        else {
            is = hurc.getErrorStream();
        }
        byte[] b = new byte[4096];
        int totalRead = 0;
        int bytesRead;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((bytesRead = is.read(b, 0, b.length)) != -1) {
            baos.write(b, 0, bytesRead);
            totalRead += bytesRead;
            publish(totalRead);
        }
        is.close();
        hurc.disconnect();
        String response = baos.toString();
        Log.d("Debug-HTTP-Response", response);
        return  response;
    }

    public String get(String sourceUrl, HashMap<String, String> header) throws Exception {
        URL url = new URL(sourceUrl);
        Log.d("debug", "GET " + sourceUrl);
        HttpsURLConnection hurc = (HttpsURLConnection) url.openConnection();
        //HttpURLConnection hurc = (HttpURLConnection) url.openConnection();
        //hurc.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
        //hurc.setHostnameVerifier(new AllowAllHostnameVerifier());
        hurc.setReadTimeout(10000 /* milliseconds */);
        hurc.setConnectTimeout(15000 /* milliseconds */);
        hurc.setRequestProperty("Accept-Charset", charset);
        //hurc.setRequestProperty("Accept", "text/plain");
        if (header!=null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                hurc.setRequestProperty(entry.getKey(), entry.getValue());
                Log.d("Debug-HTTP-Header", entry.getKey() + ":" + entry.getValue());
            }
        }

        hurc.setRequestMethod("GET");
        hurc.connect();
        responseCode = hurc.getResponseCode();
        Log.d("Debug-HTTP-Response", "RESPONSE CODE:" + hurc.getResponseCode());
        InputStream is;
        if (responseCode >= 200 && responseCode < 400) {
            is = hurc.getInputStream();
        }
        else {
            is = hurc.getErrorStream();
        }
        byte[] b = new byte[4096];
        int totalRead = 0;
        int bytesRead;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((bytesRead = is.read(b, 0, b.length)) != -1) {
            baos.write(b, 0, bytesRead);
            totalRead += bytesRead;
            publish(totalRead);
        }
        is.close();
        hurc.disconnect();
        String response = baos.toString();
        Log.d("Debug-HTTP-Response", response);
        return  response;
    }
}

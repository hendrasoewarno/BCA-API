package com.hendra.bcaapi;

import com.hendra.middleware.inet.HTTPRequest;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by Hendra Soewarno(0119067505) on 12/10/2020.
 */
/*
Class ini akan melakukan transfer
*/

public class GeneralGET {
    private HashMap<String, String> header;

    public GeneralGET() {
        this.header = header = new HashMap<>();
    }

    //Untuk menambah header sesuai dengan dokumentasi, misalkan channel-id dan credential-id
    public void addHeader(String key, String value) {
        this.header.put(key, value);
    }

    //hide Constructor
    public JSONObject get(String URI, APIOAuthToken accessToken, Signature signature) throws Exception {
        HTTPRequest httpRequest = new HTTPRequest() {
            @Override
            protected void publish(int bytesread) {
            }
        };
        this.header.put("Authorization", "Bearer " + accessToken.getAccess_token());
        this.header.put("X-BCA-Signature", signature.getSignature());
        this.header.put("X-BCA-Key", Auth.APIkey);
        this.header.put("X-BCA-Timestamp", signature.getTimestamp());
        this.header.put("HTTPMethod", "GET");

        String rawResponse = httpRequest.get(Auth.URL + URI,  this.header);
        if (httpRequest.getResponseCode() == 200)
            return new JSONObject(rawResponse);
        else {
            System.out.println(rawResponse);
            throw new Exception(rawResponse);
        }
    }
}
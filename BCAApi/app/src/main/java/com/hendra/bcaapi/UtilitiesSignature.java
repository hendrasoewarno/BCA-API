package com.hendra.bcaapi;

import com.hendra.middleware.inet.HTTPRequest;
import com.hendra.middleware.library.Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Hendra Soewarno(0119067505) on 12/10/2020.
 */
/*
Hanya untuk Test Environment
 */
public class UtilitiesSignature extends Signature {
    private static String endpoint = "/utilities/signature";
    private HashMap<String, String> header;

    //hide Constructor
    public UtilitiesSignature(String URI, String method, APIOAuthToken accessToken, JSONObject obj) throws Exception {
        HTTPRequest httpRequest = new HTTPRequest() {
            @Override
            protected void publish(int bytesread) {
            }
        };

        this.header = new HashMap<>();
        header.put("Timestamp", Utils.getXbcaTimestamp());
        header.put("URI", URI);
        header.put("AccessToken", accessToken.getAccess_token());
        header.put("APISecret", Auth.APIsecret);
        header.put("Content-Type", "application/json");
        header.put("HTTPMethod", method);

        String strResponse;

        if (obj != null)
            strResponse = httpRequest.post(Auth.URL + endpoint, Utils.descape(obj.toString()), header);
        else
            strResponse = httpRequest.post(Auth.URL + endpoint, "{}", header);

        if (httpRequest.getResponseCode() == 200) {
            this.requestPayload = Utils.parseValue(strResponse, "RequestPayload");
            this.signature = Utils.parseValue(strResponse, "CalculatedHMAC");
            this.timestamp = Utils.parseValue(strResponse, "Timestamp");
        } else
            throw new Exception(strResponse);
    }
}
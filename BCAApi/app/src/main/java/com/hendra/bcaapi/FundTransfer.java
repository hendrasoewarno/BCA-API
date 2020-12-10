package com.hendra.bcaapi;

import com.hendra.middleware.inet.HTTPRequest;

import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by Hendra Soewarno(0119067505) on 12/10/2020.
 */
/*
POST /banking/corporates/transfers HTTP/1.1
Host: sandbox.bca.co.id
Authorization: Bearer [access_token]
Content-Type: application/json
Origin: [yourdomain.com]
X-BCA-Key: [api_key]
X-BCA-Timestamp: [timestamp]
X-BCA-Signature: [signature]

{
    "CorporateID" : "BCAAPI2016",
    "SourceAccountNumber" : "0201245680",
    "TransactionID" : "00000001",
    "TransactionDate" : "2016-01-30",
    "ReferenceID" : "12345/PO/2016",
    "CurrencyCode" : "IDR",
    "Amount" : "100000.00",
    "BeneficiaryAccountNumber" : "0201245681",
    "Remark1" : "Transfer Test",
    "Remark2" : "Online Transfer"
}

Response:
{
    "TransactionID" : "00000001",
    "TransactionDate" : "2016-01-30",
    "ReferenceID" : "12345/PO/2016",
    "Status" : "Success"
}
*/

public class FundTransfer {
    private static String endpoint = "/banking/corporates/transfers";
    private HashMap<String, String> header;
    private String transactionId;
    private String transactionDate;
    private String referenceId;
    private String status;

    //hide Constructor
    public FundTransfer(APIOAuthToken accessToken, Signature signature) throws Exception {
        HTTPRequest httpRequest = new HTTPRequest() {
            @Override
            protected void publish(int bytesread) {
            }
        };

        this.header = new HashMap<>();
        this.header.put("Authorization", "Bearer " + accessToken.getAccess_token());
        this.header.put("X-BCA-Signature", signature.getSignature());
        this.header.put("Content-Type", "application/json");
        this.header.put("X-BCA-Key", Auth.APIkey);
        this.header.put("X-BCA-Timestamp", signature.getTimestamp());
        this.header.put("HTTPMethod", "POST");

        String rawResponse = httpRequest.post(Auth.URL + endpoint, signature.getRequestPayload(), this.header);
        if (httpRequest.getResponseCode() == 200) {
            JSONObject obj = new JSONObject(rawResponse);
            this.transactionId = obj.getString("TransactionID");
            this.transactionDate = obj.getString("TransactionDate");
            this.referenceId = obj.getString("ReferenceID");
            this.status = obj.getString("Status");
        }
        else
            throw new Exception(rawResponse);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getStatus() {
        return status;
    }
}
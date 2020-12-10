package com.hendra.bcaapi;

import com.hendra.middleware.inet.HTTPRequest;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by Hendra Soewarno(0119067505) on 12/10/2020.
 */
/*
POST /banking/corporates/transfers/v2/domestic HTTP/1.1
Host: sandbox.bca.co.id
Authorization: Bearer [access_token]
Content-Type: application/json
Origin: [yourdomain.com]
X-BCA-Key: [api_key]
X-BCA-Timestamp: [timestamp]
X-BCA-Signature: [signature]
channel-id: [channel_id]
credential-id: [credential_id]

{
    "transaction_id" : "00000001",
    "transaction_date" : "2016-01-30",
    "source_account_number" : "0201245680",
    "beneficiary_account_number" : "0201245681",
    "beneficiary_bank_code" : "BRINIDJA",",
    "beneficiary_name" : "Tester",
    "amount" : "100000.00",
    "transfer_type" : "LLG",
    "beneficiary_cust_type" : "1",",
    "beneficiary_cust_residence" : "1",",
    "currency_code" : "IDR",
    "remark1" : "Transfer Test",
    "remark2" : "Online Transfer"
    "beneficiary_email" : "test@123.com"
}

Response LLG:
{
    "transaction_id" : "00000001",
    "transaction_date" : "2016-01-30",
    "filler_ppu_no"" : ""
}

Response ONL:
{
    "transaction_id" : "00000001",
    "transaction_date" : "2016-01-30",
}
*/

public class DomesticFundTransfer {
    private static String endpoint = "/banking/corporates/transfers/v2/domestic";
    private HashMap<String, String> header;
    private String transactionId;
    private String transactionDate;
    private String fillerPPUNo;

    //hide Constructor
    public DomesticFundTransfer(APIOAuthToken accessToken, Signature signature, String channelId, String credentialId) throws Exception {
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
        this.header.put("channel-id", channelId);
        this.header.put("credential-id", credentialId);

        String rawResponse = httpRequest.post(Auth.URL + endpoint, signature.getRequestPayload(), header);
        if (httpRequest.getResponseCode() == 200) {
            JSONObject obj = new JSONObject(rawResponse);
            this.transactionId = obj.getString("TransactionID");
            this.transactionDate = obj.getString("TransactionDate");
            this.fillerPPUNo = obj.getString("filler_ppu_no"); //LLG/RTGS only, return null if not exists
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

    public String getFillerPPUNo() {
        return fillerPPUNo;
    }
}
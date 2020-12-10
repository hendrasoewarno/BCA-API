# BCA-API
Framework untuk koneksi dengan BCA-API

# Pembuatan KEYs dan CREDENTIALS
1. Kunjungi https://developer.bca.co.id/
2. Lakukan Registrasi
3. Klik pada Application, dan Create Application
4. Isikan data, dan pilih API yang diinginkan dan save
5. Klik pada Application yang berhasil dibuat, dan Edit
6. Klik pada API KEYS dan Generate
7. Klik pada OAUTH CREDENTIALS dan Generate
Anda dapat klik pada Documentation dan Download Sandbox BCA API Example

# Setting Keys dan CREDENTIALS
Hasil dari keys dan Credential, kemudian ditempatkan pada Auth.java

```
package com.hendra.bcaapi;

/**
 * Created by Hendra Soewarno(0119067505) on 12/10/2020.
 */
/*
public class Auth {
    public static String URL="https://sandbox.bca.co.id:443";
    public static String clientId="a627ec5e-1102-4b1a-9a52-************";
    public static String clientSecret="a79912a9-e10a-4c05-920d-************";
    public static String APIkey="a73bb647-7ecf-4aca-89f8-************";
    public static String APIsecret="c5b55426-1c37-4930-9aa8-************";
}
```

# Melakukan Test
Buka file ExampleInstrumentedTest.java, dan tuliskan testcode anda

```
package com.hendra.bcaapi;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hendra.middleware.library.Utils;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        try {
            APIOAuthToken accessToken = APIOAuthToken.getInstance();
            /*
            //1. Balance Information
            String URI = "/banking/v3/corporates/BCAAPI2016/accounts/0063001004";
            LocalSignature signature1 = new LocalSignature(URI, "GET", accessToken, null);
            //Log.d("Debug", signature1.getRequestPayload());
            //Log.d("Debug", signature1.getSignature());
            (new GeneralGET()).get(URI,accessToken,signature1);
             */

            /*
            //2. Account Statement
            String URI = "/banking/v3/corporates/BCAAPI2016/accounts/0201245680/statements?StartDate=2016-09-01&EndDate=2016-09-01";
            LocalSignature signature2 = new LocalSignature(URI, "GET", accessToken, null);
            //Log.d("Debug", signature2.getRequestPayload());
            //Log.d("Debug", signature2.getSignature());
            (new GeneralGET()).get(URI,accessToken,signature2);
             */

            /*
            //Fund Transfer
            JSONObject obj = new JSONObject();
            obj.put("CorporateID", "BCAAPI2016");
            obj.put("SourceAccountNumber","0201245680");
            obj.put("TransactionID","00000001");
            obj.put("TransactionDate", "2020-12-07");
            obj.put("ReferenceID", "12345/PO/2020");
            obj.put("CurrencyCode", "IDR");
            obj.put("Amount", "100000.00");
            obj.put("BeneficiaryAccountNumber", "0201245681");
            obj.put("Remark1", "transfertest");
            obj.put("Remark2", "onlinetransfer");
            //UtilitiesSignature signature = new UtilitiesSignature("/banking/corporates/transfers", "POST", accessToken, obj);
            LocalSignature signature = new LocalSignature("/banking/corporates/transfers", "POST", accessToken, obj);
            Log.d("Debug", signature.getSignature());
            //Log.d("Debug", signature.getRequestPayload());
            FundTransfer transfer = new FundTransfer(accessToken, signature);
            Log.d("Debug", transfer.getStatus());
            */

            /*
            //Domestic Fund Transfer
            JSONObject obj = new JSONObject();
            obj.put("transaction_id", "00000001");
            obj.put("transaction_date", "2020-12-07");
            //obj.put("source_account_number","0201245680");
            obj.put("source_account_number","0201245681");
            obj.put("beneficiary_account_number", "0201245501");
            obj.put("beneficiary_bank_code", "BRONINJA");
            obj.put("beneficiary_name", "testing");
            obj.put("amount", "100000.00");
            obj.put("transfer_type", "LLG");
            obj.put("beneficiary_cust_type", "1");
            obj.put("beneficiary_cust_residence", "1");
            obj.put("currency_code", "IDR");
            obj.put("remark1", "transfertest");
            obj.put("remark2", "onlinetransfer");
            obj.put("beneficiary_email", "abc@gmail.com");
            //UtilitiesSignature signature = new UtilitiesSignature("/banking/corporates/transfers/v2/domestic", "POST", accessToken, obj);
            LocalSignature signature = new LocalSignature("/banking/corporates/transfers/v2/domestic", "POST", accessToken, obj);
            Log.d("Debug", signature.getSignature());
            //Log.d("Debug", signature.getRequestPayload());
            DomesticFundTransfer transfer = new DomesticFundTransfer(accessToken, signature, "95051", "BCAAPI");
            Log.d("Debug", transfer.getTransactionId());
             */

            /*
            //Account Statement Online
            String URI = "/banking/offline/corporates/accounts/0201245680/filestatements?StartDate=2016-06-24&EndDate=2016-06-24";
            LocalSignature signature2 = new LocalSignature(URI, "GET", accessToken, null);
            //LocalSignature signature = new LocalSignature(URI, "GET", accessToken, new JSONObject());
            Log.d("Debug", signature2.getRequestPayload());
            Log.d("Debug", signature2.getSignature());
            GeneralGET generalGET = new GeneralGET();
            generalGET.addHeader("ChannelID", "95051");
            generalGET.addHeader("CredentialID", "BCAAPI2016");
            generalGET.get(URI,accessToken,signature2);
             */

            /*
            //Inquiry Transaction Status
            String URI = "/banking/corporates/transfers/status/17071800840035?TransactionDate=2020-07-03&TransferType=BCA";
            LocalSignature signature2 = new LocalSignature(URI, "GET", accessToken, null);
            //LocalSignature signature = new LocalSignature(URI, "GET", accessToken, new JSONObject());
            Log.d("Debug", signature2.getRequestPayload());
            Log.d("Debug", signature2.getSignature());
            GeneralGET generalGET = new GeneralGET();
            generalGET.addHeader("ChannelID", "95051");
            generalGET.addHeader("CredentialID", "BCAAPI2016");
            generalGET.get(URI,accessToken,signature2);
             */

            /*
            //Inquiry Domestic Account
            String URI = "/banking/corporates/transfers/v2/domestic/beneficiaries/banks/BNIAIDJA/accounts/1231314113331";
            LocalSignature signature2 = new LocalSignature(URI, "GET", accessToken, null);
            //LocalSignature signature = new LocalSignature(URI, "GET", accessToken, new JSONObject());
            Log.d("Debug", signature2.getRequestPayload());
            Log.d("Debug", signature2.getSignature());
            GeneralGET generalGET = new GeneralGET();
            generalGET.addHeader("channel-id", "95051");
            generalGET.addHeader("credential-id", "BCAAPI2016");
            generalGET.get(URI,accessToken,signature2);
            */

            /*
            //Inquiry Transfer Status
            String URI = "/banking/corporates/transfers/status/00000001?TransactionDate=2020-12-06&TransferType=BCA";
            LocalSignature signature2 = new LocalSignature(URI, "GET", accessToken, null);
            //LocalSignature signature = new LocalSignature(URI, "GET", accessToken, new JSONObject());
            Log.d("Debug", signature2.getRequestPayload());
            Log.d("Debug", signature2.getSignature());
            GeneralGET generalGET = new GeneralGET();
            generalGET.addHeader("ChannelID", "95051");
            generalGET.addHeader("CredentialID", "BCAAPI2016");
            generalGET.get(URI,accessToken,signature2);
             */

            /*
            //Foreign Exchange Rate
            String URI = "/general/rate/forex";
            LocalSignature signature = new LocalSignature(URI, "GET", accessToken, null);
            //LocalSignature signature = new LocalSignature(URI, "GET", accessToken, new JSONObject());
            Log.d("Debug", signature.getRequestPayload());
            Log.d("Debug", signature.getSignature());
            (new GeneralGET()).get(URI,accessToken,signature);
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

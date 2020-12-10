package com.hendra.bcaapi;

import com.hendra.middleware.library.Utils;
import org.json.JSONObject;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Hendra Soewarno(0119067505) on 12/10/2020.
 */
/*
Generate Signature

SHA-256 HMAC is used to generate the signature with your API secret as the key.

Signature = HMAC-SHA256(apiSecret, StringToSign)

The StringToSign will be a colon-separated list derived from some request data as below:

StringToSign = HTTPMethod+":"+RelativeUrl+":"+AccessToken+":"+Lowercase(HexEncode(SHA-256(RequestBody)))+":"+Timestamp

    POST Example:

Data :
  - Method : POST
  - Relative URL : /banking/corporates/transfers
  - Access token : lIWOt2p29grUo59bedBUrBY3pnzqQX544LzYPohcGHOuwn8AUEdUKS
  - Request body :
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
  - Timestamp : 2016-02-03T10:00:00.000+07:00

API Secret : 22a2d25e-765d-41e1-8d29-da68dcb5698b

Lowercase(HexEncode(SHA-256(RequestBody))) : e3cf5797ac4ac02f7dad89ed2c5f5615c9884b2d802a504e4aebb76f45b8bdfb

StringToSign : POST:/banking/corporates/transfers:lIWOt2p29grUo59bedBUrBY3pnzqQX544LzYPohcGHOuwn8AUEdUKS:e3cf5797ac4ac02f7dad89ed2c5f5615c9884b2d802a504e4aebb76f45b8bdfb:2016-02-03T10:00:00.000+07:00

Signature : 69ad66589ade078a30922a0848725cf153aecfcca82eba94e3270285b4a9c604

For GET request (with no RequestBody), you still need to calculate SHA-256 of an empty string.

    GET Example:

Data:
  - Method : GET
  - Relative URL : /banking/v3/corporates/BCAAPI2016/accounts/0201245680/statements?StartDate=2016-09-01&EndDate=2016-09-01
  - Access token : lIWOt2p29grUo59bedBUrBY3pnzqQX544LzYPohcGHOuwn8AUEdUKS
  - Request body :
  - Timestamp : 2016-02-03T10:00:00.000+07:00

API Secret : 22a2d25e-765d-41e1-8d29-da68dcb5698b

Lowercase(HexEncode(SHA-256(RequestBody))) : e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855

StringToSign : GET:/banking/v2/corporates/BCAAPI2016/accounts/0201245680/statements?EndDate=2016-09-01&StartDate=2016-09-01:lIWOt2p29grUo59bedBUrBY3pnzqQX544LzYPohcGHOuwn8AUEdUKS:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855:2016-02-03T10:00:00.000+07:00
               GET:/banking/v2/corporates/BCAAPI2016/accounts/0201245680/statements?EndDate=2016-09-01&StartDate=2016-09-01:lIWOt2p29grUo59bedBUrBY3pnzqQX544LzYPohcGHOuwn8AUEdUKS:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855:2016-02-03T10:00:00.000+07:00

Signature : 3ac124303746d222387d4398dddf33201a384aa22137aa08f4d9843c6f467a48
            3ac124303746d222387d4398dddf33201a384aa22137aa08f4d9843c6f467a48

Details about the data used to derived The StringToSign is explained in the next sections.
 */
public class LocalSignature extends Signature {
    private static final String charset = "UTF-8";

    //hide Constructor
    public LocalSignature(String URI, String method, APIOAuthToken accessToken, JSONObject obj) throws Exception {
        if (obj != null)
            this.requestPayload = Utils.descape(obj.toString());
        else
            this.requestPayload = "";

        this.timestamp = Utils.getXbcaTimestamp();

        String stringToSign = method + ":" + Utils.reSortURIParameters(URI) + ":" + accessToken.getAccess_token() + ":" + Utils.toHexString(Utils.getSHA(this.requestPayload)).toLowerCase() + ":" + this.timestamp;

        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(Auth.APIsecret.getBytes(charset), "HmacSHA256");
        hmac.init(secret_key);
        byte[] result = hmac.doFinal(stringToSign.getBytes(charset));
        this.signature = Utils.toHexString(result);
    }
}
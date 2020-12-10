package com.hendra.bcaapi;

/**
 * Created by Hendra Soewarno(0119067505) on 12/10/2020.
 */
public class Signature {
    public String requestPayload;
    public String timestamp;
    public String signature;

    public String getRequestPayload() {
        return this.requestPayload;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getSignature() {
        return this.signature;
    }
}

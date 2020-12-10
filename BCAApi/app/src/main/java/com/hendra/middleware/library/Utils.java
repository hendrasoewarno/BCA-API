package com.hendra.middleware.library;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Hendra Soewarno(0119067505) on 12/10/2020.
 */
public class Utils {
    public static String charset="UTF-8";

    public static String getXbcaTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String strDate = dateFormat.format(new Date()); ////2020-11-30T06:33:35.609+0700
        return strDate.substring(0, strDate.length() - 2) + ":" + strDate.substring(strDate.length() - 2);
        //2020-11-30T06:33:35.609+07:00
    }

    public static String reSortURIParameters(String URI) {
        StringBuffer sb = new StringBuffer();
        String[] parameters = URI.split("\\?");
        sb.append(parameters[0]);
        if (parameters.length>1) {
            sb.append("?");
            String[] pair = parameters[1].split("&");
            Arrays.sort(pair);
            for (int i=0; i<pair.length; i++) {
                if (i>0)
                    sb.append("&");
                sb.append(pair[i]);
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static String parseValue(String text, String property) throws UnsupportedEncodingException {
        String part[]=text.split(property+": ");
        if (part.length==1) return null;
        String value[]=part[1].split(",\r\n");
        return value[0];
    }

    public static String descape(String s) {
        return s.replace("\\\\", "\\")
                .replace("\\t", "\t")
                .replace("\\b", "\b")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\f", "\f")
                .replace("\\'", "\'")
                .replace("\\\"", "\"")
                .replace("\\/", "/");
    }

    public static byte[] getSHA(String input) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(charset));
    }

    public static String toHexString(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

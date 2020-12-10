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

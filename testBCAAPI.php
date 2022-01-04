<?php
define("HOST","https://sandbox.bca.co.id:443");
define("clientId","8d418acc-8185-42ae-8aea-c18fc2f10d52");
define("clientSecret","2d67d158-ada7-4792-b452-a36033270e20");
define("APIkey","9798acf5-62c6-410e-a843-b6250b2b5ae6");
define("APIsecret","0fa94027-65a7-42fe-b63e-063670b361c3");

function httpRequest($url, $payload, $headers=array()) {
	$ch = curl_init();
	if ($payload!="")
		curl_setopt_array($ch, array(
			CHOSTOPT_HOST => $url, # HOST endpoint
			CHOSTOPT_HTTPHEADER => $headers, # HTTP Headers
			CHOSTOPT_RETURNTRANSFER => 1, # return hasil curl_exec ke variabel, tidak langsung dicetak
			CHOSTOPT_FOLLOWLOCATION => 1, # atur flag followlocation untuk mengikuti bila ada url redirect di server penerima tetap difollow
			CHOSTOPT_CONNECTTIMEOUT => 60, # set connection timeout ke 60 detik, untuk mencegah request gantung
			CHOSTOPT_POST => 1, # set flag request method POST
			CHOSTOPT_POSTFIELDS => $payload, # attached post data dalam bentuk JSON String
			CHOSTOPT_SSL_VERIFYPEER => 0
		));
	else
		curl_setopt_array($ch, array(
			CHOSTOPT_HOST => $url, # HOST endpoint
			CHOSTOPT_HTTPHEADER => $headers, # HTTP Headers
			CHOSTOPT_RETURNTRANSFER => 1, # return hasil curl_exec ke variabel, tidak langsung dicetak
			CHOSTOPT_FOLLOWLOCATION => 1, # atur flag followlocation untuk mengikuti bila ada url redirect di server penerima tetap difollow
			CHOSTOPT_CONNECTTIMEOUT => 60, # set connection timeout ke 60 detik, untuk mencegah request gantung
			CHOSTOPT_POST => 0, # set flag request method POST
			CHOSTOPT_SSL_VERIFYPEER => 0
		));
	
	$resp = curl_exec($ch);

	if (curl_errno($ch) == false) {
		$http_code = curl_getinfo($ch, CHOSTINFO_HTTP_CODE);
		if ($http_code == 200) { //success
			return $resp;
		} else {
			echo "Error HTTP Code : ".$http_code."\n";
			echo $resp;
			throw new Exception("Error HTTP Code : ".$http_code."\n");
		}
	} else {
		echo "Error while sending request, reason:".curl_error($ch);
		throw new Exception("Error while sending request, reason:".curl_error($ch));
	}
	curl_close($ch);
}

function getXbcaTimestamp() {
	//return X-BCA-Timestamp yyyy-MM-ddTHH:mm:ss.SSSTZD ex. 2017-07-05T10:28:00.000+07:00
	$timestamp = new DateTime();
	$milliseconds = substr("000" . (round(microtime(true) * 1000)%1000),-3);
	$iso8601=$timestamp->format("Y-m-d\TH:i:s." . $milliseconds . "O"); //2017-07-05T10:28:00.000+0700
	return substr_replace($iso8601, ":", strlen($iso8601)-2, 0);
}

function getAccessToken() {
	return httpRequest(HOST . "/api/oauth/token",
		"grant_type=client_credentials", 
		array(
			"Authorization:Basic " . base64_encode(clientId . ":" . clientSecret),
			"Content-Type:application/x-www-form-urlencoded"
		)
	);
}

function reSortURIParameters($url) {
	$parameters = explode("?",$url);
	$newURI = $parameters[0];
	if (sizeof($parameters)>1) {
		$newURI .= "?";
		$pair = explode("&",$parameters[1]);
		sort($pair);
	    for ($i=0; $i<sizeof($pair); $i++) {
            if ($i>0)
                $newURI .="&";
            $newURI .=$pair[$i];
        }
	}
	return $newURI;
}

function getLocalSignature($url, $method, $accessToken, $payload="") {
    $timestamp = getXbcaTimestamp();
    $stringToSign = $method . ":" . reSortURIParameters($url) . ":" . $accessToken . ":" . hash('sha256',$payload) . ":" . $timestamp;
	$signature = hash_hmac('sha256', $stringToSign, APIsecret);
	return $signature;
}

function callAPIGET($url, $accessToken, $APIkey) {

	$result = httpRequest(HOST . $url, "",
		array(
			"Authorization:Bearer " . $accessToken,
			"X-BCA-Signature:" . getLocalSignature($url, "GET", $accessToken),
			"X-BCA-Key:" . $APIkey,
			"X-BCA-Timestamp:" . getXbcaTimestamp(),
			"HTTPMethod:GET"
		)
	);
	
	return $result;
}

function callAPIPOST($url, $arrPayload, $accessToken, $APIkey) {
	$strJson = stripcslashes(json_encode($arrPayload));
	$result = httpRequest(HOST . $url, $strJson,
		array(
			"Authorization:Bearer " . $accessToken,
			"X-BCA-Signature:" . getLocalSignature($url, "POST", $accessToken, $strJson),
			"Content-Type:application/json",
			"X-BCA-Key:" . $APIkey,
			"X-BCA-Timestamp:" . getXbcaTimestamp(),
			"HTTPMethod:POST"
		)
	);
	
	return $result;
}

date_default_timezone_set('Asia/Jakarta');
var_dump(getAccessToken());
try {
	$token = json_decode(getAccessToken());
	echo callAPIGET("/general/rate/forex", $token->access_token, APIkey);
	echo callAPIGET("/banking/v3/corporates/BCAAPI2016/accounts/0063001004", $token->access_token, APIkey);
	
    $arrPayload = array(
            "CorporateID"=>"BCAAPI2016",
            "SourceAccountNumber"=>"0201245680",
            "TransactionID"=>"00000001",
            "TransactionDate"=> date("Y-m-d"),
            "ReferenceID"=>"12345/PO/2020",
            "CurrencyCode"=>"IDR",
            "Amount"=>"100000.00",
            "BeneficiaryAccountNumber"=>"0201245681",
            "Remark1"=>"transfertest",
            "Remark2"=>"onlinetransfer"
	);

	echo callAPIPOST("/banking/corporates/transfers", $arrPayload, $token->access_token, APIkey);
}
catch (Exception $e) {
	echo $e->getMessage();
}
?>

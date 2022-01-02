<?php
define("URL","https://sandbox.bca.co.id:443");
define("clientId","8d418acc-8185-42ae-8aea-c18fc2f*****");
define("clientSecret","2d67d158-ada7-4792-b452-a360332*****");
define("APIkey","9798acf5-62c6-410e-a843-b6250b2*****");
define("APIsecret","0fa94027-65a7-42fe-b63e-063670b*****");

function httpRequest($uri, $post_raw, $headers=array()) {
	$ch = curl_init();
	if ($post_raw!="")
		curl_setopt_array($ch, array(
			CURLOPT_URL => $uri, # URL endpoint
			CURLOPT_HTTPHEADER => $headers, # HTTP Headers
			CURLOPT_RETURNTRANSFER => 1, # return hasil curl_exec ke variabel, tidak langsung dicetak
			CURLOPT_FOLLOWLOCATION => 1, # atur flag followlocation untuk mengikuti bila ada url redirect di server penerima tetap difollow
			CURLOPT_CONNECTTIMEOUT => 60, # set connection timeout ke 60 detik, untuk mencegah request gantung
			CURLOPT_POST => 1, # set flag request method POST
			CURLOPT_POSTFIELDS => $post_raw, # attached post data dalam bentuk JSON String
			CURLOPT_SSL_VERIFYPEER => 0
		));
	else
		curl_setopt_array($ch, array(
			CURLOPT_URL => $uri, # URL endpoint
			CURLOPT_HTTPHEADER => $headers, # HTTP Headers
			CURLOPT_RETURNTRANSFER => 1, # return hasil curl_exec ke variabel, tidak langsung dicetak
			CURLOPT_FOLLOWLOCATION => 1, # atur flag followlocation untuk mengikuti bila ada url redirect di server penerima tetap difollow
			CURLOPT_CONNECTTIMEOUT => 60, # set connection timeout ke 60 detik, untuk mencegah request gantung
			CURLOPT_POST => 0, # set flag request method POST
			CURLOPT_SSL_VERIFYPEER => 0
		));
	
	$resp = curl_exec($ch);

	if (curl_errno($ch) == false) {
		$http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
		if ($http_code == 200) { //success
			return $resp;
		} else {
			echo "Error HTTP Code : ".$http_code."\n";
			echo $resp;
			throw("Error HTTP Code : ".$http_code."\n");
		}
	} else {
		echo "Error while sending request, reason:".curl_error($ch);
		throw("Error while sending request, reason:".curl_error($ch));
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
	return httpRequest(URL . "/api/oauth/token", "grant_type=client_credentials", 
		array(
			"Authorization:Basic " . base64_encode(clientId . ":" . clientSecret),
			"Content-Type:application/x-www-form-urlencoded"
		)
	);
}

function reSortURIParameters($uri) {
	$parameters = explode("?",$uri);
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

function getLocalSignature($uri, $method, $accessToken, $requestPayload="") {
    $timestamp = getXbcaTimestamp();
    $stringToSign = $method . ":" . reSortURIParameters($uri) . ":" . $accessToken . ":" . hash('sha256',$requestPayload) . ":" . $timestamp;
	$signature = hash_hmac('sha256', $stringToSign, APIsecret);
	return $signature;
}

function callAPIGET($uri, $accessToken, $APIkey) {

	$result = httpRequest(URL . $uri, "",
		array(
			"Authorization:Bearer " . $accessToken,
			"X-BCA-Signature:" . getLocalSignature($uri, "GET", $accessToken),
			"X-BCA-Key:" . $APIkey,
			"X-BCA-Timestamp:" . getXbcaTimestamp(),
			"HTTPMethod:GET"
		)
	);
	
	return $result;
}

date_default_timezone_set('Asia/Jakarta');
try {
	$token = json_decode(getAccessToken());
	echo callAPIGET("/general/rate/forex", $token->access_token, APIkey);
}
catch (Exception $e) {
	echo $e->getMessage();
}
?>

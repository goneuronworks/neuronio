<?php 
    $cookies = Array();

    login();   
    httpSend($cookies[0][1]);
    
    //로그인 
    function login(){        
        $user = array("username" => "example1@gmail.com",
                    "password" => "test1234");       
        $userData = json_encode($user);        
        $response = requestCurl("http://192.168.0.104:5000/login", $userData);
        print_r($response);
    }
    
    function httpSend($cookie){       
        $submitType = "submit";
        $targetOid = "T000000074";
        $stx = chr(0x3c);   //"<"
        $oid = "T000000074";
        $msg = "Hello, World";
        $ext = chr(0x3e);   //">"
        
        $submitData ="submitType=".$submitType."&targetOid=".$targetOid."&stx=".$stx."&oid=".$oid."&msg=".$msg."&etx=".$ext;
        
        $response = requestCurl("http://192.168.0.104:5000/NeuronioExample/HTTPExample/NEURONIOHTTPExampleProtocol", $submitData, $cookie);
        print_r($response);
    }
    
    //curl 콜백 호출 -> 쿠키 설정
    function curlResponseHeaderCallback($ch, $headerLine) {
        global $cookies;
        
        if (preg_match('/^Set-Cookie:\s*([^;]*)/mi', $headerLine, $cookie) == 1)
            $cookies[] = $cookie;
            return strlen($headerLine); // Needed by curl
    }
    
    function requestCurl($requestUrl, $postData, $cookie){
        
        $headers = array('Connection: keep-alive',
                         'Content-Length: ' . strlen($postData)
                        );
        
        if($cookie != null){
            array_push($headers,'Cookie:'.$cookie);
            array_push($headers,'Content-Type: application/x-www-form-urlencoded');
        }else{
            array_push($headers,'Content-Type: application/json');
        }        
        
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $requestUrl);
        curl_setopt($ch, CURLOPT_POST, true);   
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);
        curl_setopt($ch, CURLOPT_POSTFIELDSIZE, 0);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER,true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER,0);
        curl_setopt($ch, CURLOPT_HEADERFUNCTION, "curlResponseHeaderCallback");
        
        $response = curl_exec($ch);
        $status_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $result = json_decode($response, true);
        curl_close($ch);
        
        return $result;
    }

?>
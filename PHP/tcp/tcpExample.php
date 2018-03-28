<?php
    ob_implicit_flush();
    $service_port = '5000';
    $address = '192.168.0.104';
    $privateKey = base64_decode("vi0/UwhuXTM061TdhtyrcQ==");  
    $iv = base64_decode("VswjHWnW/LCUWrdwzfyzlA==");
    
    $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP); // TCP 통신용 소켓 생성 //
    
    if (!$socket) {
        echo "socket_create() failed: reason: " .socket_strerror(socket_last_error()) . "\n";
    }
    
    $result = socket_connect($socket, $address , $service_port);

    if(!$result) {
        echo 'socket_connect() failed.\nReason: ($result) ' .socket_strerror(socket_last_error($socket)) . '\n';
    }

    $STX = chr(0x3c) ; // "<"
    $OID = "T000000075";
    $MSG = "Hello, World";
    $ETX = chr(0x3e) ; // ">"
    
    $sendMessage = $STX.$OID.$MSG.$ETX;
    $encryptData = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $privateKey, pkcs5_pad($sendMessage), MCRYPT_MODE_CBC, $iv);    
    $encryptData = base64_encode($encryptData);
    
    socket_write($socket, $encryptData, strlen($encryptData));
   
    $decryptedData = decrypted($socket, $privateKey, $iv);
    echo $decryptedData;
    
    if($decryptedData == "SUCCESS"){
        $decryptedData = decrypted($socket, $privateKey, $iv);
        echo "Result ==> ".htmlspecialchars($decryptedData);
    }
        
    socket_close($socket);
    
    function decrypted($socket, $privateKey, $iv){
        $receiveMessage = socket_read($socket, 2048);
        $decryptedData = pkcs5_unpad(mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $privateKey, base64_decode($receiveMessage), MCRYPT_MODE_CBC, $iv));
        return $decryptedData;
    }

    function pkcs5_pad($text, $blocksize = 16) {
        $pad = $blocksize - (strlen($text) % $blocksize);
        return $text . str_repeat(chr($pad), $pad);
    }
    
    //PKCS5UnPadding
    function pkcs5_unpad($text) {
        $pad = ord($text{strlen($text)-1});
        if ($pad > strlen($text)) {
            return $text;
        }
        if (!strspn($text, chr($pad), strlen($text) - $pad)) {
            return $text;
        }
        return substr($text, 0, -1 * $pad);
    }
?>

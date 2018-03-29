<?php
    require_once("phpMQTT.php");
    
    $server = "192.168.0.6";     // change if necessary
    $port = 5000;               // change if necessary
    $username = "example1@gmail.com";                   // set your username
    $password = "test1234";
    $client_id = "T000000080"; // make sure this is unique for connecting to sever - you could use uniqid()
    $mqtt = new phpMQTT($server, $port, $client_id);
    
    if($mqtt->connect(true, NULL, $username, $password)) {
        $mqtt->publish("/NeuronioExample/MQTTExample/hello", "Hello, world Title" , 1);
        $mqtt->close();
    }
    else{
        echo "Time out!\n";
    }

?>
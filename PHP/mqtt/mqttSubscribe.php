<?php
    require_once("phpMQTT.php");
    
    $server = "192.168.0.6";
    $port = 5000;
    $username = "example1@gmail.com";
    $password = "test1234";
    $client_id = "T000000073";
    $mqtt = new phpMQTT($server, $port, $client_id);
    
    if(!$mqtt->connect(true, NULL, $username, $password)) {
        exit(1);
    }
    
    $topics['/NeuronioExample/MQTTExample/hello'] = array("qos" => 1, "function" => "procmsg");
    $mqtt->subscribe($topics, 0);
    
    while($mqtt->proc()){}
    
    function procmsg($topic, $msg){
        echo "Msg Recieved: " . date("r") . "\n";
        echo "Topic: {$topic}\n\n";
        echo "\t$msg\n\n";
        
        $mqtt->close();
    }
?>
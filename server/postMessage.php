<?php
$SQL_BEZER_HOST = 'localhost'; $SQL_BEZER_USER = 'Xxetxaburu001'; $SQL_BEZER_PASS = 'dn6fyRB8C';
$SQL_BEZER_DB = 'Xxetxaburu001_RandomTalk';
$mensaje = $_POST["mensaje"];
$rid = $_POST["rid"];
$con = mysqli_connect($SQL_BEZER_HOST, $SQL_BEZER_USER, $SQL_BEZER_PASS, $SQL_BEZER_DB) or die ("Error " . mysqli_error($con));
    
if (mysqli_connect_errno()) {
    echo "3";
    exit;
}
$emaitza = mysqli_query($con, "SELECT user2 FROM `conversacion` WHERE user1 = '".$rid."'");
$usuario = "";
while ($row = mysqli_fetch_array($emaitza)){    
    $usuario = $row['user2'];
    if($usuario != ""){
        break;
    } 
}

if($usuario == ""){
    $emaitza = mysqli_query($con, "SELECT user1 FROM `conversacion` WHERE user2 = '".$rid."'");
    while ($row = mysqli_fetch_array($emaitza)){    
        $usuario = $row['user1'];
        if($usuario != ""){
            break;
        } 
    }
}

mysqli_close($con);
if($usuario != ""){
    $registrationIds = [$usuario];
    define( 'API_ACCESS_KEY', 'AIzaSyCehY7hp8LZTeTR4H_la8w0AIrmEPz82wc' );

    // prep the bundle
    $msg = array
    (
        'message'   => $mensaje,
        'title'     => "",
        'subtitle'  => "/text",
        'tickerText'    => 'Ticker text here...Ticker text here...Ticker text here',
        'vibrate'   => 1,
        'sound'     => 1,
        'largeIcon' => 'large_icon',
        'smallIcon' => 'small_icon'
    );
    $fields = array
    (
        'registration_ids'  => $registrationIds,
        'data'          => $msg
    );
     
    $headers = array
    (
        'Authorization: key=' . API_ACCESS_KEY,
        'Content-Type: application/json'
    );
     
    $ch = curl_init();
    curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
    curl_setopt( $ch,CURLOPT_POST, true );
    curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
    curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
    curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
    curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
    $result = curl_exec($ch );
    curl_close( $ch );
    echo $usuario;
}
?>
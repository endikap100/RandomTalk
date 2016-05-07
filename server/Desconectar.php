<?php
try {
    $mysqli = mysqli_connect("localhost","Xjnieto011","LXr19KrVEe","Xjnieto011_...");
    $imei = $_GET["imei"];
    $id = $_GET["registrationId"];
   
    if ($mysqli->query("INSERT INTO Dispositivos (imei, id) values ('" . $imei . "','" . $id . "') ON DUPLICATE KEY UPDATE id = values(id)") === TRUE) {
        echo "OK";
    }else{
        printf("Error");
    }
    $mysqli->close();
} catch (Exception $e){
    printf("Error");
}
?>
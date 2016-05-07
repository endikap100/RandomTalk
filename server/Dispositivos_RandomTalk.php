<?php
try {
    $mysqli = mysqli_connect("localhost","Xxetxaburu001","dn6fyRB8C","Xxetxaburu001_RandomTalk");
    $rid = $_POST["rid"];
    if ($mysqli->query("INSERT INTO esperando (reg_id) values ('".$rid."') ON DUPLICATE KEY UPDATE reg_id = values(reg_id)") === TRUE) {
        echo "OK";
    }else{
        printf("Error");
    }
    $mysqli->close();
} catch (Exception $e){
    printf("Error");
}
?>
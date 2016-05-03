<?php
try {
    $mysqli = mysqli_connect("localhost","Xjnieto011","LXr19KrVEe","Xjnieto011_...");
    $name = $_GET["nombre"];
    $pass = $_GET["pass"];
    $email = $_GET["email"];
    $user = $_GET["user"];

    if ($mysqli->query("INSERT INTO Users (`Nombre`, `Usuario`, `Password`, `Email`) values ('" . $name ."','" . $user . "','" . $pass . "','" . $email . "')") === TRUE) {
        echo "New record created successfully";
    }else{
            printf("Error2");
    }
    $mysqli->close();
} catch (Exception $e){
    printf("Error1");
}
?>
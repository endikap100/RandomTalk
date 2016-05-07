<?php
            // Difinimos variables:
            $SQL_BEZER_HOST = 'localhost'; $SQL_BEZER_USER = 'Xxetxaburu001'; $SQL_BEZER_PASS = 'dn6fyRB8C';
            $SQL_BEZER_DB = 'Xxetxaburu001_RandomTalk';

            $rid = $_POST['rid'];

            // 01. Creamos una conexión a la base de datos:
            $con = mysqli_connect($SQL_BEZER_HOST, $SQL_BEZER_USER, $SQL_BEZER_PASS, $SQL_BEZER_DB) or die ("Error " . mysqli_error($con));
            // 02. Check connection:
            if (mysqli_connect_errno()) {
                echo "3";
                exit;
            }
            // 03. Perform queries:
            $pareja = FALSE;
            $emaitza = mysqli_query($con, "SELECT * FROM `esperando` WHERE reg_id <> '".$rid."'");
            $usuario = "";
            while ($row = mysqli_fetch_array($emaitza)){
                $usuario = $row['reg_id'];
                $loged = TRUE;
            }
            if ($usuario != "") {
                mysqli_query($con, "DELETE FROM `esperando` WHERE reg_id = '".$usuario."' OR reg_id = '".$rid."'");
                mysqli_query($con, "INSERT INTO `conversacion`(user1, user2) values ('".$usuario."','".$rid."')");

                $registrationIds = [$usuario, $rid];
            define( 'API_ACCESS_KEY', 'AIzaSyCehY7hp8LZTeTR4H_la8w0AIrmEPz82wc' );

                // prep the bundle
                $msg = array
                (
                    'message'   => "se ha creado la sesion.",
                    'title'     => "RandomTalk",
                    'subtitle'  => "",
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
                echo "Message sent";
                            }
                            mysqli_close($con);
                            
                            
?>
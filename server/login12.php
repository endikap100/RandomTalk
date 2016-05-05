<?php

			// Difinimos variables:
			$SQL_BEZER_HOST = 'localhost'; $SQL_BEZER_USER = 'Xxetxaburu001'; $SQL_BEZER_PASS = 'dn6fyRB8C';
			$SQL_BEZER_DB = 'Xxetxaburu001_RandomTalk';

			// 01. Creamos una conexión a la base de datos:
			$con = mysqli_connect($SQL_BEZER_HOST, $SQL_BEZER_USER, $SQL_BEZER_PASS, $SQL_BEZER_DB) or die ("Error " . mysqli_error($con));
			// 02. Check connection:
			if (mysqli_connect_errno()) {
				echo "3";
				exit;
			}
			// 03. Perform queries:
			$user = $_POST['user'];
			$password = $_POST['password'];
			//$id = $_POST['rId'];
			$loged = FALSE;
			$emaitza = mysqli_query($con, "SELECT * FROM `usuarios` WHERE nick = '".$user."' AND pass = '".$password."'");

			while ($row = mysqli_fetch_array($emaitza)){
				$loged = TRUE;
			}
			/*if ($mysqli->query("INSERT INTO 'usuarios' (reg_id) values ('".$id."') ON DUPLICATE KEY UPDATE reg_id = values('".$id."')") === TRUE){
            	$loged = FALSE;
            }*/
			// 04. Close connection:
			mysqli_close($con);
			echo $loged;


?>

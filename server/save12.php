<?php

			// Difinimos variables:
			$SQL_BEZER_HOST = 'localhost'; $SQL_BEZER_USER = 'Xxetxaburu001'; $SQL_BEZER_PASS = 'dn6fyRB8C';
			$SQL_BEZER_DB = 'Xxetxaburu001_RandomTalk';

			// 01. Creamos una conexión a la base de datos:
			$con = mysqli_connect($SQL_BEZER_HOST, $SQL_BEZER_USER, $SQL_BEZER_PASS, $SQL_BEZER_DB) or die ("Error " . mysqli_error($con));
			// 02. Check connection:
			if (mysqli_connect_errno()) {
				echo "1";
				exit;
			}
			// 03. Perform queries:
			$user = $_POST['user'];
			$password = $_POST['password'];
			mysqli_query($con, "INSERT INTO `usuarios` (`usuario`, `password`) VALUES ('".$user."','".$password."')");
			// 04. Close connection:
			mysqli_close($con);

			echo "0";

?>

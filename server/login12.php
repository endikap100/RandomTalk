<?php



			// Difinimos variables:
			$SQL_BEZER_HOST = 'localhost'; $SQL_BEZER_USER = 'Xealdecoa004'; $SQL_BEZER_PASS = '2y6TsoSj';
			$SQL_BEZER_DB = 'Xealdecoa004_DAS';

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
			//SELECT * FROM `usuarios` WHERE 1
			//mysqli_query($con, "INSERT INTO `usuarios` (`usuario`, `password`, `email`) VALUES ('".$user."','".$password."','".$email."')");
			$loged = FALSE;
			$emaitza = mysqli_query($con, "SELECT * FROM `usuarios` WHERE usuario = '".$user."' AND password = '".$password."'");
			//$emaitza = mysqli_query($con, "SELECT * FROM `usuarios`");			

			while ($row = mysqli_fetch_array($emaitza)){
				$loged = TRUE;
			}
			// 04. Close connection:
			mysqli_close($con);

			echo $loged;

?>

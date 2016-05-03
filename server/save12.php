<?php



			// Difinimos variables:
			$SQL_BEZER_HOST = 'localhost'; $SQL_BEZER_USER = 'Xealdecoa004'; $SQL_BEZER_PASS = '2y6TsoSj';
			$SQL_BEZER_DB = 'Xealdecoa004_DAS';

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
			$email = $_POST['email'];
			//INSERT INTO `Xealdecoa004_DAS`.`usuarios` (`id`, `usuario`, `password`, `email`) VALUES (NULL, 'endika', 'endika', 'endika@endika.com');
			mysqli_query($con, "INSERT INTO `usuarios` (`usuario`, `password`, `email`) VALUES ('".$user."','".$password."','".$email."')");
			// 04. Close connection:
			mysqli_close($con);

			echo "0";

?>

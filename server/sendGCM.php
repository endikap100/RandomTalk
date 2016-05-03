<?php
// API access key from Google API's Console
$registrationIds = [];
try {
	$mysqli = mysqli_connect("localhost","Xjnieto011","LXr19KrVEe","Xjnieto011_...");

	if ($stmt = $mysqli->prepare("SELECT id FROM Dispositivos")) {
    		// Execute the statement.
    		$stmt->execute();
    		// Get the variables from the query.
            $stmt->store_result();
            $num_of_rows = $stmt->num_rows;
    		$stmt->bind_result($token);
    		// Fetch the data.
    		while($stmt->fetch()){
                array_push($registrationIds, $token);
            }

    		$stmt->close();
	}else{
		printf("Error");
	}
	$mysqli->close();
} catch (Exception $e) {
	break;
}
define( 'API_ACCESS_KEY', 'AIzaSyCehY7hp8LZTeTR4H_la8w0AIrmEPz82wc' );

// prep the bundle
$msg = array
(
	'message' 	=> $_POST['message'],
	'title'		=> $_POST['title'],
	'subtitle'	=> $_POST['subtitle'],
	'tickerText'	=> 'Ticker text here...Ticker text here...Ticker text here',
	'vibrate'	=> 1,
	'sound'		=> 1,
	'largeIcon'	=> 'large_icon',
	'smallIcon'	=> 'small_icon'
);
$fields = array
(
	'registration_ids' 	=> $registrationIds,
	'data'			=> $msg
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
?>
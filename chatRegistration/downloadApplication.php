<?php
	session_start();
	/*define('DOCROOT' realpath(dirname(__FILE__)) . '/');
	require (DOCROOT . 'dbConn.php');
	$con = new dbConn();*/
	
	$conn = mysqli_connect("localhost","root","password","chat");	

	$email = $_POST['email'];
	
	$stm = "select email from users where email = '".$email."';";
	
	$res = mysqli_query($conn,$stm);
	
	if(mysqli_num_rows($res) > 0)
	{
		$file = $_POST['worked'];
		$file = $file . '.zip';
		header('Content-type: Application/zip');
		header("Cache-control: private");
		header('Content-disposition: attachment; filename="'.$file.'"');
		readfile('files/'.$file);
	}
	else
	{
		$_SESSION['homeMessage'] = "Email does not exists.";
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
	}
	mysqli_close($conn);
?>
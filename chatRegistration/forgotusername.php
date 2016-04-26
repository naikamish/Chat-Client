<?php
	session_start();
	define('DOCROOT' realpath(dirname(__FILE__)) . '/');	
	require(DOCROOT . 'activationAndNotifications.php');
	
	//require (DOCROOT . 'dbConn.php');
	//$con = new dbConn();
	
	$conn = mysqli_connect("localhost","root","password","chat");

	$email = $_POST['email'];
	
	$stm = "select username from users where email = '".$email."';";
	
	$res = mysqli_query($conn, $stm);
	
	if (mysqli_num_rows($res) > 0) 
	{
		$row = mysqli_fetch_assoc($res);
		
		$not = new notification();
		$body = "Your username associated with ".$email." is <b>".$row['username']."</b>";
		$not->email("mailtosecureyou@gmail.com","Administration","mailtosecureyou@gmail.com","mailstodeliver",$email,"Username Recovery",$body);
		$_SESSION['homeMessage'] = "Your username is sent to your inbox.";
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
	}
	
	mysqli_close($conn);
?>
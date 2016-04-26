<?php
	session_start();
	define('DOCROOT', realpath(dirname(__FILE__)) . '/');
	require (DOCROOT . 'dbConn.php');
	$con = new dbConn();
	
	$password = $_POST['password'];
	$email = $_SESSION['email'];
	require(DOCROOT . 'activationAndNotifications.php');
	$stm = "select pwcr from users where email = '".$email."';";
	$res = $con->execute($stm);
	
	if ($res->num_rows > 0) {
    	while($row = $res->fetch_assoc()) 
	{
	if($row['pwcr'] == 1)
	{
		$stm = "update users set password = '".$password."' where email='".$email."';";
	
		if($con->execute($stm) === true)
		{
			$not = new notification();
			$body = "Password changed successfully.";
			$not->email("mailtosecureyou@gmail.com","Administration","mailtosecureyou@gmail.com","mailstodeliver",$email,"Password Changed Successfully",$body);
			$stm = "update users set pcwr = 0 where email = '".$email."';";
			$con->execute($stm);
			$stm = "update users set activationCode = 0 where email = '".$email."';";
			$con->execute($stm);
			$_SESSION['homeMessage'] = "Password has been changed successfully.";
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		}
		else
		{
			$_SESSION['homeMessage'] = "Link has been expired.";
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		}	
	}
	}
	}
	$con->close();
?>
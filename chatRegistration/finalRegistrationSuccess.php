<?php
	session_start();
	if($_SESSION['emailSent'] == 1)
	{
		$_SESSION['homeMessage'] = "Successfully Created your account, use the activation link sent to your inbox or text message box to activate your account.";
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
	}
	else
	{
		$_SESSION['homeMessage'] = "Found a problem in sending a email to your account. Check your internet connection or type in correct Email ID.";
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
	}
	unset($_SESSION['emailSent']);
?>
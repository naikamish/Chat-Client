<?php 
		session_start();
		/*define('DOCROOT', realpath(dirname(__FILE__)) . '/');
		require (DOCROOT . 'dbConn.php');
		$con = new dbConn(); */
		
		$conn = mysqli_connect("localhost","root","password", "chat");

		$name = htmlspecialchars($_POST['name']);
		$email = htmlspecialchars($_POST['email']);
		$contactNumber = htmlspecialchars($_POST['contactNumber']);
		$profession = htmlspecialchars($_POST['profession']);
		$reason = htmlspecialchars($_POST['reason']);
		$countryCode = htmlspecialchars($_POST['country']);
		
		$contactNumber = $countryCode.$contactNumber;
		
		$stm = "insert into sourcecodereq(name, email, contactNumber, profession, reason) values('".$name."','".$email."',".$contactNumber.",'".$profession."','".$reason."');";
		
		if(mysqli_query($conn,$stm))
		{
			$_SESSION['homeMessage'] = "We will send you a link to download our source code. Thank you for requesting us.";
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		}
		else
		{
			$_SESSION['homeMessage'] = "Error";
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		}

        mysqli_close($conn);
?>
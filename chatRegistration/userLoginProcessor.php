<?php 
		session_start();
		//define('DOCROOT', realpath(dirname(__FILE__)) . '/');
		
		//require(DOCROOT . 'dbConn.php');
		//$con = new dbConn();
		
		$conn = mysqli_connect("localhost","root","password","chat");

		$userusername = htmlspecialchars($_POST['userusername']);
		$userpassword = htmlspecialchars($_POST['userpassword']);
		
		$stm = "";
		$res = "";
	
		$stm = "select * from users where username='".$userusername."' and activated = 1;";
		$_SESSION['code'] = 1;
		
		$res = mysqli_query($conn,$stm);
        
        if(mysqli_num_rows($res) > 0)
		{
			$r = mysqli_fetch_assoc($res);
			$usn = $r['username'];
			if($r['password'] == $userpassword)
			{
				$_SESSION['username'] = $usn;
			}
			else
			{
				$_SESSION['homeMessage'] = "Username or password is incorrect.";
				echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
			}
		}
		else
		{
			$_SESSION['homeMessage'] = "Username or password is incorrect or account might not have been activated, check your email/sms to activate your account.";
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		}
		
		 
        
        mysqli_close($conn);
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		?>
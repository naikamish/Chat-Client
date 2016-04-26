<?php 
		session_start();
		
        	
		//define('DOCROOT' realpath(dirname(__FILE__)) . '/');
		//require(DOCROOT . 'dbConn.php');
		//$con = new dbConn();
		$conn = mysqli_connect("localhost","root","password","chat");
		$adminusername = htmlspecialchars($_POST['adminusername']);
		$adminpassword = htmlspecialchars($_POST['adminpassword']);
		
		$stm = "";
		$res = "";
	
		$stm = "select * from admin where username='".$adminusername."';";
		$_SESSION['code'] = 2;
		
		$res = mysqli_query($conn, $stm);
        
        if(mysqli_num_rows($res) > 0)
		{
			$r = mysqli_fetch_assoc($res);
			$usn = $r['username'];
			if($r['password'] == $adminpassword)
			{
				$_SESSION['adminname'] = $usn;
			}
			else
			{
				$_SESSION['homeMessage'] = "Username or password is incorrect.";
				echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
			}
		}
		else
		{
			$_SESSION['homeMessage'] = "Username or password is incorrect.";
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		}
		
        mysqli_close($conn);
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		?>
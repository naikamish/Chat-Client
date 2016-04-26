<?php
	//$email = $_GET['email'];
	$code = intval($_GET['activationCode']);
	/*define('DOCROOT' realpath(dirname(__FILE__)) . '/');
	require(DOCROOT . 'dbConn.php');
	$con = new dbConn();*/
	
	$conn = mysqli_connect("localhost","root","password","chat");

	$stm = "select * from users where activationCode='".$code."';";
	
	//$res = $con->execute($stm);
	
	$res = mysqli_query($conn,$stm);
	
	if(mysqli_num_rows($res) > 0)
	{
		$row = mysqli_fetch_assoc($res);
			if($code == $row['activationCode'])
			{
				
				$stm = "update users set activated = 1 where email = '".$row['email']."';";
				mysqli_query($conn,$stm);
				$stm = "update users set activationCode = 0 where email = '".$row['email']."';";
				mysqli_query($conn,$stm);
				echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/successfullyActivated.php'>";
			}
			else
			{
				echo "<script> alert('Link has been expired.');</script>";
				echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
			}
	}
	mysqli_close($conn);
	
?>

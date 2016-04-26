<?php
	session_start();
	define('DOCROOT', realpath(dirname(__FILE__)) . '/');
	$tar_d = DOCROOT . "uploads/";
	$newFileName = $_SESSION['newlyRegistered'];
	$tar_file = $tar_d .basename($_FILES["fileToUpload"]["name"]);
	
	$imgType = pathinfo($tar_file, PATHINFO_EXTENSION);
	
	if(file_exists($tar_file))
	{
		unlink($tar_file);
	}
	
	$conn = mysqli_connect("localhost","root","password","chat");
	
	if($imgType == "jpg")
	{
		$newFileName = $newFileName . ".jpg";
	}
	else if($imgType == "gif")
	{
		$newFileName = $newFileName . ".gif";
	}
	else if($imgType == "jpeg")
	{
		$newFileName = $newFileName . ".jpeg";
	}
	else if($imgType == "png")
	{
		$newFileName = $newFileName . ".png";
	}
	else
	{
		$uOK = 0;
		$_SESSION['error'] = 1;
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/imageUpload.php'>";
	}
	
	$_FILES['fileToUpload']['name'] = $newFileName;
	
	$tar_file = "uploads/" .basename($_FILES['fileToUpload']['name']);
	
	$uOK = 1;
	
	if(isset($_POST["submit"]))
	{
		$chk = getimagesize($_FILES['fileToUpload']['tmp_name']);
		
		if($chk !== false)
		{
			$uOK = 1;
		}
		else
		{
			$_SESSION['error'] = 3;
			$uOK = 0;
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/imageUpload.php'>";
		}
	}
	
	if($_FILES["fileToUpload"]["size"] > 500000)
	{
		$_SESSION['error'] = 2;
		$uOK = 0;
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/imageUpload.php'>";
	}
	
	if($uOK == 1)
	{
		echo $tar_file;
		sleep(2);
		if(move_uploaded_file($_FILES['fileToUpload']['tmp_name'], $tar_file))
		{
			$_SESSION['error'] = 0;
			$_SESSION['image'] = $tar_file;
			
			$stm = "update users set imgName = '".$newFileName."' where username = '".$_SESSION['newlyRegistered']."';";
			mysqli_query($conn, $stm);
			mysqli_close($conn);
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/imageUpload.php'>";
		}
		else
		{
			$_SESSION['error'] = 3;
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/imageUpload.php'>";
		}
	}
?>

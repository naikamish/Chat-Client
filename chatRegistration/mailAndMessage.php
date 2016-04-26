<?php 
	session_start();
	define('DOCROOT', realpath(dirname(__FILE__)) . '/');

	require(DOCROOT . 'activationAndNotifications.php');
	$conn = mysqli_connect("localhost","root","password","chat");

	$fname = htmlspecialchars($_POST['fname']);
	$lname = htmlspecialchars($_POST['lname']);
	$email = htmlspecialchars($_POST['email']);
	$uname = htmlspecialchars($_POST['uname']);
	$password = htmlspecialchars($_POST['password']);
	$cnumber = htmlspecialchars($_POST['cnumber']);
	$cc = htmlspecialchars($_POST['country']);
	$activation = rand(1000000000,9999999999);

	if($cc == "INDIA")
	{
		$cc = 91;
	}
	else if($cc == "USA")
	{
		$cc = 1;
	}
	else if($cc == "Austrailia")
	{
		$cc = 61;
	}

	$stm = "select * from users where username='".$uname."';";
	//echo "<script> alert('0.75'); </script>";
	$res = mysqli_query($conn,$stm);
	//echo "<script> alert('1'); </script>";
	if(mysqli_num_rows($res) > 0)
	{
	//	echo "<script> alert('2'); </script>";
		$_SESSION['errorMess'] = 1;
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/registerHere.php'>";
	}
	else
	{
	//	echo "<script> alert('3'); </script>";
		$stm = "select * from users where email='".$email."'";
		$res = mysqli_query($conn,$stm);
			
		if(mysqli_num_rows($res) > 0)
		{
	//		echo "<script> alert('4'); </script>";
			$_SESSION['errorMess'] = 2;
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/registerHere.php'>";
	//		echo "<script> alert('5'); </script>";
		}
		else
		{
	//		echo "<script> alert('6'); </script>";
			$value = 0;
			$cnumber = $cc.$cnumber;
			$stm = "insert into users(name, lname, email, username, password, contactNumber, activationCode,activated) values('".$fname."','".$lname."','".$email."','".$uname."','".$password."',".$cnumber.",".$activation.",".$value.");";
			mysqli_query($conn,$stm);
	//		echo "<script> alert('7'); </script>";
			$not = new notification();
			$link = "http://32.208.103.211/chatRegistration/activateAccount.php?activationCode=".$activation."&email=".$email;
			$body = "Thank you for registering with us.<br><br><a href='".$link."'>Click here</a> to activate your account.";
			$ans = $not->email("mailtosecureyou@gmail.com","Administration","mailtosecureyou@gmail.com","mailstodeliver",$email,"Activation Email",$body);
			$_SESSION['emailSent'] = $ans;
			echo "<script> alert('".$ans."'); </script>";
			if($ans == 1)
			{
				$refCode = $not->message("12035432147",$cnumber,$link);
			}
	//		echo "<script> alert('8'); </script>";
			$_SESSION['newlyRegistered'] = $uname;
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/imageUpload.php'>";
		}
	}
	
    mysqli_close($conn);
?>

<?php 
		session_start();
		
		$conn = mysqli_connect("localhost","root","password","chat");

		if(!$conn)
		{
			$_SESSION['homeMessage'] = "Error establishing connection.";
			echo "<meta http-equiv='refresh' content='0; url=http://localhost/chatRegistration/index.php'>";
		}		

		$name = htmlspecialchars($_POST['name']);
		$email = htmlspecialchars($_POST['email']);
		$contactNumber = htmlspecialchars($_POST['contactNumber']);
		$subject = htmlspecialchars($_POST['subject']);
		$description = htmlspecialchars($_POST['description']);
		$cc = htmlspecialchars($_POST['country']);
		$cc = $cc.$contactNumber;

		echo "<script>alert(".$name." ".$email." ".$cc." ".$subject." ".$description.");</script>";
		
		$stm = "insert into contactus(name, email, contactNumber, subject, description) values('".$name."','".$email."',".$cc.",'".$subject."','".$description."');";

		if(mysqli_query($conn,$stm))
		{
			$_SESSION['homeMessage'] = "We will contact you as early as possible. Thank you for contacting us.";
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		}
		else
		{
			$_SESSION['homeMessage'] = "Error: " . $stm;
			echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
		}
		
        mysqli_close($conn);
?>

<?php
	define('DOCROOT', realpath(dirname(__FILE__)) . '/');
	require(DOCROOT . 'PHPMailer_5.2.4/class.phpmailer.php');
	require(DOCROOT . 'expt.php');
	class notification
	{
		function email($from,$fromName,$username,$password,$to,$subject,$body)
		{
			$mail = new PHPMailer();
        
			$mail->IsSMTP();
			$mail->From = $from;
			$mail->FromName = $fromName;
			$mail->Host = "smtp.gmail.com";
			$mail->SMTPSecure = "ssl";
			$mail->Port = 465;
			$mail->SMTPAuth = true;
			$mail->Username = $username;
			$mail->Password = $password;
			$mail->AddAddress($to);
			$mail->WordWrap = 50;
        
			$mail->IsHTML(true);
			$mail->Subject = $subject;
			$mail->Body = $body;
        
			if($mail->Send())
			{
				return 1;
				//echo "<script>alert('Mail Sent Successfully')</script>";
			}
			else
			{
				return 0;
				//echo "<script>alert('Unsuccessfull Delivery')</script>";
			}
		}	
		
		function message($from,$to,$body)
		{
			$expertTexting = new experttexting_sms(); // Create SMS object.

			$expertTexting->from = $from; // Sender of the SMS – PreRegistered through the Customer Area.
			$expertTexting->to = $to; // The full International mobile number of the without + or 00
			$expertTexting->msgtext = $body; // The SMS content.
			$ali = $expertTexting->send(); // Send SMS method.
			$ali = substr($ali,22);
			return $ali;
		}
	}
?>

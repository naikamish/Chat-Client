<?php
	session_start();
	//echo "<script> alert(".isset($_SESSION['emailSent']).") </script>";
	define('DOCROOT', realpath(dirname(__FILE__)) . '/');
	if(isset($_SESSION['emailSent']))
	{	
		
?>
<html>
	<head> 
		<title> Babble On.. </title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="css/myNewStyles.css">
		<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
		
	</head>
	
	
	
	<body>
		<div class="navbar-wrapper">
		<div class="container">
		<nav class="navbar navbar-fixed-top navbar-inverse">
		<div class="container-fluid">
			
			<div class="navbar-header">
				<a href="#" class="navbar-brand"><span><img src="img/simpleChatIcon.png" height="30" width="50" /></span></a>
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#collapsenavbar">
					<span class="glyphicon glyphicon-menu-hamburger simpletext"> </span>
				</button>
			</div>
			
			<div class="collapse navbar-collapse" id="collapsenavbar">
				<ul class="nav navbar-nav">
					<li><a href="index.php"><span class="glyphicon glyphicon-home"> </span> Home</a><li>
					<li><a href="#"><span class="glyphicon glyphicon-download"> </span> Download</a></li>
					<li class="active"><a href="registerHere.php"><span class="glyphicon glyphicon-user"> </span> Sign Up</a></li>
					<li><a href="#"><span class="glyphicon glyphicon-phone"> </span> Contact</a></li>
				</ul>
				
				<ul class="nav navbar-nav navbar-right">
				<?php 
					//echo "<h1>Sai Baba</h1>";
					
					if(isset($_SESSION['username']) || isset($_SESSION['adminname']))
					{
						if(isset($_SESSION['username']))
						{
							$name = $_SESSION['username'];
						}
						
						if(isset($_SESSION['adminname']))
						{
							$aname = $_SESSION['adminname'];
						}
						
						if($_SESSION['code'] == 1)
						{  ?>
						
						<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-unlock"> </span> Welcome, <?php echo $name; ?> <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="#"> Dashboard </a></li>
							
							<li><a href="#"> Update Profile </a></li>
							<li class="divider"> </li>
							<li><a href="logout.php"> Logout </a></li>
						</ul>
					</li>
				
				<?php	
						echo "<script> alert('All sessions will be removed'); </script>";
						}
						else if($_SESSION['code'] == 2)
						{ echo "<script> alert('All sessions will be removed123'); </script>";?>
						<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-lock"> </span> Welcome Admin, <?php echo $aname; ?> <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="#"> Administration </a></li>
							
							<li><a href="#"> Download Requests </a></li>
							<li class="divider"> </li>
							<li><a href="logout.php"> Logout </a></li>
						</ul>
					</li>
				<?php		
						}
					}else { ?>
					
					<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-log-in"> </span> Login <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="#adminLogin" data-toggle="modal"> Admin Login </a></li>
							<li class="divider"> </li>
							<li><a href="#userLogin" data-toggle="modal"> User Login </a></li>
						</ul>
					</li>
				<?php				
					}
				?>
				</ul>
			</div>
		</div>
		</nav>
		</div>
		</div>
		<script src="js/val.js"></script>
		<div class="modal fade" id="adminLogin" role="dialog">
			<div class="modal-dialog modal-sm">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"> &times; </button>
						<h3 class="modal-title" align="center">Admin Login</h3>
					</div>
					
					<div class="modal-body">
						<form role="form" method="post" action="adminLoginProcess.php">
							<div class="form-group">
								<label for="username"> User Name: </label>
								<input type="text" name="adminusername" class="form-control" placeholder="Username" required/>
							</div>
							
							<div class="form-group">
								<label for="password"> Password: </label>
								<input type="password" name="adminpassword" placeholder="Password" class="form-control" required/>
							</div>
							
							&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
							<input type="submit" class="btn btn-info" value="Login"/>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		
		<div class="modal fade" id="userLogin" role="dialog">
			<div class="modal-dialog modal-sm">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"> &times; </button>
						<h3 class="modal-title" align="center">User Login</h3>
					</div>
					
					<div class="modal-body">
						<form role="form" method="post" action="userLoginProcessor.php">
							<div class="form-group">
								<label for="username" > User Name: </label>
								<input type="text" name="userusername" class="form-control" placeholder="Username" required/>
							</div>
							
							<div class="form-group">
								<label for="password" > Password: </label>
								<input type="password" name="userpassword" placeholder="Password" class="form-control" required/>
							</div>
							
							<a href="#"> Forgot Password </a> <br>
							<a href="forgotusername1.php"> Forgot Username </a> <br> <br>
							&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
							<input type="submit" class="btn btn-info" value="Login"/>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		<div class="jumbotron">
			<div class = "container">
				<div class="row row-content">
				<br>
					<h2 style="text-align:center;"> Profile Picture </h2>
				</div>
			</div>
		</div>
		
		<div class="container">
		
			<div class="row row-content">
				<div class="col-xs-12 col-sm-6 col-sm-offset-3"> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					<h3> Select an image to set as profile picture (Optional). </h3>
					<br>
				</div>
			</div>
			<div class="row row-content">
				
				<div class="col-xs-12 col-sm-6 col-sm-offset-4">
				
				<img src="img/defaultprofile.png" id="image" width="300" height="300" />
				<form action="imageUploadBackend.php" method="post" enctype="multipart/form-data">
					<br> <br>
					<div class="container-fluid">
					<div class="row row-content">
					<div class="col-xs-12 col-sm-6">
					<input type="file" class="form-control-file" action="imageUploadBackend.php" name="fileToUpload" id="fileToUpload">
					<br> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					<p id="error" class="error">  </p>
					<input type="submit" value="Use this Image" name="submit" class="btn btn-success">
					
					<div class="alert alert-warning" role="alert"> <b> Warning: </b> The file size should not exceed 500 KB. </div>
					</div>
					</div>
					</div>
				</form>
				<form method="post" action="finalRegistrationSuccess.php"> 
					&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					<?php
						$_SESSION['imageFile'] = "The Image file has been uploaded successfully.";
					?>
					<input type="submit" value="Next" class="btn btn-primary">
				</form>
				<form action="finalRegistrationSuccess.php" method="post">
					 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					<input type="submit" value="Skip" class="btn btn-primary">
				</form>
				</div>
			</div>
		</div>
		
		
		<?php
		
		if(isset($_SESSION['error']))
		{
			$co = $_SESSION['error'];
			if($co == 3)
			{
				echo "<script> document.getElementById('error').innerHTML = 'Uploaded file is not a image file.'; </script>";
			}
			else if($co == 2)
			{
				echo "<script> document.getElementById('error').innerHTML = 'The file size exceeds 500 KB.'; </script>";
			}
			else if($co == 1)
			{
				echo "<script> document.getElementById('error').innerHTML = 'Image should be of jpg, png, jpeg or gif.'; </script>";
			}
			else if($co == 0)
			{
				$img = $_SESSION['image'];
				echo "<script> document.getElementById('image').src = '".$img."'; alert($img); </script>";
			}
			
			unset($_SESSION['error']);
			unset($_SESSION['image']);
		}
	?>
		
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>

	</body>
</html>
<?php
	}
	else
	{
		echo "<meta http-equiv='refresh' content='0; url=http://32.208.103.211/chatRegistration/index.php'>";
	}
?>
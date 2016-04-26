<html>
	<head> 
		<title> Babble On.. </title>
		<link rel="shortcut icon" href="img/simpleChatIcon.png" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="css/myNewStyles.css">
		<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
		<link href="css/font-awesome.min.css" rel="stylesheet">
		<link href="css/bootstrap-social.css" rel="stylesheet">
	</head>
	
	<?php
	session_start();
	?>
	
	<body>
		<div class="navbar-wrapper">
		<div class="container">
		<nav class="navbar navbar-fixed-top navbar-inverse">
		<div class="container-fluid">
			
			<div class="navbar-header">
				<a href="index.php" class="navbar-brand"><span><img src="img/simpleChatIcon.png" height="30" width="50" /></span></a>
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#collapsenavbar">
					<span class="glyphicon glyphicon-menu-hamburger simpletext"> </span>
				</button>
			</div>
			
			<div class="collapse navbar-collapse" id="collapsenavbar">
				<ul class="nav navbar-nav">
					<li><a href="index.php"><span class="glyphicon glyphicon-home"> </span> Home</a><li>
					<li><a href="downloadPage.php"><span class="glyphicon glyphicon-download"> </span> Download</a></li>
					<li class="active"><a href="registerHere.php"><span class="glyphicon glyphicon-user"> </span> Sign Up</a></li>
					<li><a href="contactUs.php"><span class="glyphicon glyphicon-phone"> </span> Contact</a></li>
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
							
							<a href="forgotpassword1.php"> Forgot Password </a> <br>
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
					<h2 style="text-align:center;"> Successfully Registered </h2>
				</div>
			</div>
		</div>
		
		<div class="container">
			<div class="row row-content">
				<h3> Your account has been activated successfully. You can now download and use the chat application for all platforms. </h3>
			</div>
		</div>
		
		<?php
			include 'footer.html';
		?>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>

	</body>
</html>
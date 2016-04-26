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
		<link href="css/font-awesome.min.css" rel="stylesheet">
		<link href="css/bootstrap-social.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway" />
		<link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	</head>
	
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
					<li class="active"><a href="index.php"><span class="glyphicon glyphicon-home"> </span> Home</a><li>
					<li><a href="downloadPage.php"><span class="glyphicon glyphicon-download"> </span> Download</a></li>
					<li><a href="registerHere.php"><span class="glyphicon glyphicon-user"> </span> Sign Up</a></li>
					<li><a href="contactUs.php"><span class="glyphicon glyphicon-phone"> </span> Contact</a></li>
				</ul>
				
				<ul class="nav navbar-nav navbar-right">
				<?php 
					session_start();
					//echo "<script> alert('All sessions will be removed'); </script>";
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
					<div class="col-xs-12 col-sm-4">
						<br>
						<br>
						<img src="img/simpleChatIcon.png" height="200" width="275" />
					</div>
					
					<div class="col-xs-12 col-sm-8">
					<br>
						<br>
						<br>
						<h1 style="font-family:rawley"> Binge On With Babbleon </h1>
						<br>
					</div>
				</div>
			</div>
		</div>
		
		<div class="container">
			<div class="row row-content">
				<div class="col-xs-12">
					<h3 id="message" style="text-align:center">  </h3>
					
					
					
					<div id="newCarousel" class="carousel slide" data-ride="carousel">
					<ol class="carousel-indicators" id="button">
							<li data-target="#newCarousel" data-slide-to="0" class="active"> </li>
							<li data-target="#newCarousel" data-slide-to="1"> </li>
							<li data-target="#newCarousel" data-slide-to="2"> </li>
							<li data-target="#newCarousel" data-slide-to="3"> </li>
					</ol>
						<!-- Carousel Captions has to be used for all the options. -->
						<div class="carousel-inner" role="listbox">
							<div class="item active">
								<center><img class="img-responsive" src="img/piserver.png" alt="Simple yet powerful interface" width="550" height="400"></center>
								<div class="carousel-caption">
									<h2 class="media-heading"> Pi Server</h2>
									<p> Website + Application is completely hosted on raspberry pi 3 (the size of credit card). </p>
									<p><a class="btn btn-primary btn-xs" href="#">Pi Server &raquo;</a></p>
								</div>
							</div>
							
							<div class="item">
								<center><img class="img-responsive" src="img/techused.png" alt="Best support provided almost instantaneously" width="550" height="400"></center>
								<div class="carousel-caption">
									<h2 class="media-heading"> Technologies Used</h2>
									<p> Best backend and frontend technologies used for best performance. </p>
									<p><a class="btn btn-primary btn-xs" href="#">Technologies &raquo;</a></p>
								</div>
							</div>
							
							<div class="item">
								<center><img class="img-responsive" src="img/devices.png" alt="Works great on multiple platforms" width="550" height="400"></center>
								<div class="carousel-caption">
									<h2 class="media-heading"> Platform Independent</h2>
									<p> Works on all desktop OS's like Windows, Macintosh and Linux, while it also works on android smartphones and tablets. </p>
									<p><a class="btn btn-primary btn-xs" href="#">Platforms &raquo;</a></p>
								</div>
							</div>
							
							<div class="item">
								<center><img class="img-responsive" src="img/doodle.png" alt="AES 256 bit protected" width="550" height="400"></center>
								<div class="carousel-caption">
									<h2 class="media-heading"> Doodles like never before. </h2>
									<p> Why text..? When doodles provide interactive way of communicating with people. </p>
									<p><a class="btn btn-primary btn-xs" href="#">Doodles &raquo;</a></p>
								</div>
							</div>
						</div>
						
						<a class="left carousel-control" href="#newCarousel" role="button" data-slide="prev">
							<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"> </span>
							<span class="sr-only">Previous</span>
						</a>
						
						<a href="#newCarousel" class = "right carousel-control" role="button" data-slide="next">
							<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"> </span>
							<span class="sr-only">Next</span>
						</a>
					</div>
				</div>
			</div>
		</div>
		
		<?php
			
			if(isset($_SESSION['homeMessage']))
			{
				$message = $_SESSION['homeMessage'];
				echo "<script> document.getElementById('message').innerHTML = '".$message."'; </script>";
			}
			
			unset($_SESSION['homeMessage']);
			
			include 'footer.html';
		?>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>

	</body>
</html>
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
					<li class="active"><a href="downloadPage.php"><span class="glyphicon glyphicon-download"> </span> Download</a></li>
					<li><a href="registerHere.php"><span class="glyphicon glyphicon-user"> </span> Sign Up</a></li>
					<li><a href="contactUs.php"><span class="glyphicon glyphicon-phone"> </span> Contact</a></li>
				</ul>
				
				<ul class="nav navbar-nav navbar-right">
				<?php 
					//echo "<h1>Sai Baba</h1>";
					session_start();
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
					<h2 style="text-align:center;"> Downloads </h2>
				</div>
			</div>
		</div>
		
		<div class="container">
			<div class="row row-content">
				<div class="col-xs-12 col-sm-4 col-sm-offset-1">
				<h3 style="text-align:center"> Request for Source Code </h3> <br>
					<form method="post" action="sourceRequest.php" name="rf">
						<div class="form-group">
							<label for="name"> Name </label>
							<input type="text" placeholder="John" name="name" class="form-control">
						</div>
						
						<div class="form-group">
							<label for="email"> Email ID</label>
							<input type="email" name="email" placeholder="johnjack@gmail.com" class="form-control">
						</div>
						
						<div class="form-group">
							<label for="contactNumber"> Contact Number </label>
							<div class="input-group">
							<span class="input-group-addon"> 
							<select name="country" id="country">
								<option value=1> USA (+1) </option>
								<option value=91> India (+91) </option>
								<option value=61> Austrailia (+61) </option>
							</select>
							</span>
							<input type="number" name="contactNumber" class="form-control" placeholder="2031234567">
							</div>
						</div>
						
						<div class="form-group">
							<label for="profession"> Profession </label>
							<select class="form-control" name="profession">
								<option value="Student"> Student </option>
								<option value="Professor"> Professor </option>
								<option value="Developer"> Developer </option>
								<option value="Hobbyist"> Hobbyist </option>
								<option value="Other"> Other </option>
							</select>
						</div>
						
						<div>
							<label for="reason"> Reason for request </label>
							<textarea class="form-control" rows="5" name="reason" placeholder="I like the way it works..." maxlength="300"></textarea>
						</div>
						<br>
						<center><input class="btn btn-info" type="submit" value="Submit"></center>
					</form>
				</div>
				
				<div class="col-xs-12 col-sm-4 col-sm-offset-2">
						<h3 style="text-align:center"> Application Download </h3> <br>
					<form method="post" action="downloadApplication.php">
						<div class="form-group">
							<label for="email">Email ID </label>
							<input type="text" placeholder="johnjack@gmail" name="email" class="form-control">
						</div>
						<br>
						<input type="submit" class="btn btn-info" name="worked" value="Desktop Version"> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
						<input type="submit" class="btn btn-info" name="worked" value="Android Version">
					</form>
					<br> <br> <br> <br>
					
					<div class="panel panel-danger">
						<div class="panel-heading"><h4><b>Important</b></h4></div>
						<div class="panel-body">You can download the application from the links provided above, only if you are registered with us.</div>
					</div>
					
				</div>
			</div>
		</div>
		
		<?php
			include 'footer.html';
		?>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>

	</body>
</html>
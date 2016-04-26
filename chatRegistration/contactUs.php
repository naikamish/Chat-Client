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
					<li><a href="downloadPage.php"><span class="glyphicon glyphicon-download"> </span> Download</a></li>
					<li><a href="registerHere.php"><span class="glyphicon glyphicon-user"> </span> Sign Up</a></li>
					<li class="active"><a href="contactUs.php"><span class="glyphicon glyphicon-phone"> </span> Contact</a></li>
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
					<h2 style="text-align:center;"> Contact Us </h2>
				</div>
			</div>
		</div>
		
		<div class="container">
			<div class="row row-content">
				<div class="col-xs-12 col-sm-4 col-sm-offset-1">
				<h3 style="text-align:center"> Contact Us </h3> <br>
					<form method="post" action="contactUsBackend.php" name="rf">
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
							<label for="subject"> Subject </label>
							<input type="text" name="subject" class="form-control" placeholder="Feedback">
						</div>
						
						<div>
							<label for="reason"> Description </label>
							<textarea class="form-control" name="description" rows="5" placeholder="This works amazing for me.." maxlength="250"></textarea>
						</div>
						<br>
						<center><input class="btn btn-info" type="submit" value="Submit"></center>
					</form>
				</div>
				
				<div class="col-xs-12 col-sm-4 col-sm-offset-2">
						<h3 style="text-align:center"> People behind this.. </h3> <br>
					<!--div class="well well-lg">
					<div class="container-fluid">
					<div class="row row-content">
						<div class="col-xs-12 col-sm-4">
						<img src="img/3.jpg" class="img-circle" alt="Tiger" width="100" height="100">
						</div>
						<div class="col-xs-12 col-sm-8">
						<h4> Amish Naik </h4>
						<h5> Contact Number </h5>
						<h5> 2035432147 </h5>
						</div>
					</div>
					</div>
					</div-->
					
					
					<div class="panel-group" id="accordion"
                      role="tablist" aria-multiselectable="true">
                    
                    
                    <div class="panel panel-primary">
                        <div class="panel-heading" role="tab" id="headingPeter">
                            <h3 class="panel-title">
                                <a role="button" class="heading" data-toggle="collapse" data-parent="#accordion" href="#peter" aria-expanded="true" aria-controls="peter">
                                    Amish Naik</a>
                            </h3>
                        </div>
                        <div role="tabpanel" class="panel-collapse collapse in" id="peter" aria-labelledby="headingPeter">
                            <div class="panel-body">
								<center><img src="img/naik.jpg" height="150" width="220" class="img-thumbnail" /> </center><br>
                                <p><b> Contact:</b> 203-913-5804 </p>
								<p><b> Address:</b> 231 Hill Road, Shelton 06987, CT, USA </p>
								<p><b> Email ID:</b> naika@mail.sacredheart.edu </p>
							</div>
                        </div>
                    </div>
                    
                    <div class="panel panel-primary">
                        <div class="panel-heading" role="tab" id="headingDanny">
                            <h3 class="panel-title">
                                <a role="button" data-toggle="collapse" class="heading" data-parent="#accordion" href="#danny" aria-expanded="true" aria-controls="danny">
									Bala Madhur Yeruva</a>
                            </h3>
                        </div>
                        <div role="tabpanel" class="panel-collapse collapse" id="danny" aria-labelledby="headingDanny">
                            <div class="panel-body">
								<center><img src="img/bala.jpg" height="150" width="220" class="img-thumbnail" /> </center> <br>
                                <p><b> Contact:</b> 339-204-3986 </p>
								<p><b> Address:</b> 54321 Avalon Gates, Trumbull 06611, CT, USA </p>
								<p><b> Email ID:</b> yeruvab@mail.sacredheart.edu </p>
                            </div>
                        </div>
                    </div>
                    
                    <div class="panel panel-primary">
                        <div class="panel-heading" role="tab" id="headingAgumbe">
                            <h3 class="panel-title">
                                <a role="button" data-toggle="collapse" class="heading" data-parent="#accordion" href="#agumbe" aria-expanded="true" aria-controls="agumbe">
                                    Bhargav Lakkur Kusha Kumar</a>
                            </h3>
                        </div>
                        <div role="tabpanel" class="panel-collapse collapse" id="agumbe" aria-labelledby="headingAgumbe">
                            <div class="panel-body">
                                <center><img src="img/lakkur.jpg" height="150" width="220" class="img-thumbnail" /> </center> <br>
                                <p><b> Contact:</b> 203-543-2147 </p>
								<p><b> Address:</b> 339 Huntington Road, Bridgeport 06608, CT, USA </p>
								<p><b> Email ID:</b> bhargavl@mail.sacredheart.edu </p>
							</div>
                        </div>
                    </div>
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
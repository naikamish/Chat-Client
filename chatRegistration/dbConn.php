<?php

	class dbConn
	{
		var $conn;
		
		function __construct()
		{
			$this->conn = mysqli_connect("localhost","root","password","chat");
		}
		
		function execute($sql)
		{
			$r = $this->mysqli_query($conn,$sql);
			return $r;
		}
		
		function close()
		{
			$this->mysqli_close($conn);
		}
	}

?>
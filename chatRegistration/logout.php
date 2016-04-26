<?php

session_start();
session_unset();

session_destroy();

header("Location: http://32.208.103.211/chatRegistration/index.php");

?>
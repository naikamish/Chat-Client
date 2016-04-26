function veri() {
		var pwd = document.rf.password.value;
		var cpwd = document.rf.verpassword.value;
		
		//var compAcii = 0;
		var capStatus = 0;
		var caps = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
		var nums = ["0","1","2","3","4","5","6","7","8","9"];
		var schar = ["~","!","@","#","$","%","^","*","(",")","_","-","=","+","?"];
		var numStatus = 0;
		var scrStatus = 0;
		var pch = pwd.split("");
		var doz = 0;
		var dot = 0;
		var indot = 1;
		
		if(pwd!=cpwd)
		{
			document.getElementById("cpwerror").innerHTML = "Passwords don't match";
			return false;
		}
		else
		{
			document.getElementById("cpwerror").innerHTML = "";
		}
		
		if(pwd.length<8)
		{
			document.getElementById("pwerror").innerHTML = "Password length has to be minimum 8 charecters long";
			return false;			
		}
		else
		{
			document.getElementById("pwerror").innerHTML = "";
		}
		
		for (var j = 0; j < pch.length; j++)
		{
			for (var i = 0; i < caps.length; i++) {
		    	if(pch[j] == caps[i])
		    	{
		    		capStatus = 1;
		    	}
			}
		}
		
		if(Boolean(!capStatus))
		{
			document.getElementById("pwerror").innerHTML = "Capital letter is missing in your password";
			return false;
		}
		else
		{
			document.getElementById("pwerror").innerHTML = "";
		}
		
		for (var j = 0; j < pch.length; j++)
		{
			for (var i = 0; i < nums.length; i++) {
		    	if(pch[j] == nums[i])
		    	{
		    		numStatus = 1;
		    	}
			}
		}
		
		if(Boolean(!numStatus))
		{
			document.getElementById("pwerror").innerHTML = "Number is missing in your password";
			return false;
		}
		else
		{
			document.getElementById("pwerror").innerHTML = "";
		}
		
		for (var j = 0; j < pch.length; j++)
		{
			for (var i = 0; i < schar.length; i++) {
		    	if(pch[j] == schar[i])
		    	{
		    		scrStatus = 1;
		    	}
			}
		}
		
		if(Boolean(!scrStatus))
		{
			document.getElementById("pwerror").innerHTML = "Special Charecter is missing in your password";
			return false;
		}
		else
		{
			document.getElementById("pwerror").innerHTML = "";
		}
		
		var em = eml.split("");
		
		for (var j = 0; j < em.length; j++) {
		    if(em[j]=='.')
		    {
		    	doz = 1;
		    }
		}
		
		}
function validateForm()
{
	if (document.loginForm) {
		var email = document.loginForm.Email.value;
		if (email) {
			var atpos = email.indexOf("@");
			var dotpos = email.lastIndexOf(".");
			if (atpos < 1 || dotpos < atpos+2 || dotpos+2 >= email.length) {
				alert("Not a valid e-mail address");
				return false;
			}
		}
		
		var password = document.loginForm.Password.value;
		if (password == null || password == "")
		{
			alert("Password must be filled out");
			return false;
		}
	}
	
	return true;
}
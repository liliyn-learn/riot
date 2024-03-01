<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="style.css">
		<title>riot here</title>
	</head>
	<body>
		<div class="form">
			<form action="riotservlet" method="post">
			    <label for="pseudo">Pseudo: (exemple Tcejer)</label>
			    <input type="text" id="pseudo" name="pseudo" /><br />
			
			    <label for="tag">Tag: (exemple euw)</label>
			    <input type="text" id="tag" name="tag" /><br />
			
			    <input type="submit" value="Rechercher" />
			</form>
		</div>

		
	</body>
</html>
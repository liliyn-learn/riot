<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tableau des meilleurs joueurs de TFT</title>
<link rel="stylesheet" href="style.css">
</head>
<body>
	<a href="index.jsp">Recherche</a>
	<h1>Les meilleurs joueurs du serveur :</h1>
	<table>
		<tr>
			<th>Nom</th>
			<th>LP</th>
			<th>Accès</th>
		</tr>
		<c:forEach items="${nameLpMap.keySet()}" var="name">
			<tr>
				<td>${name}</td>
				<td>${nameLpMap.get(name)}</td>
				<td><form action="meilleursJoueurs" method="post">
				<input type="hidden" name="summonerName" value="${name}" />
				<button type="submit">
				Acceder au profil
				</button>
				</form></td>
			</tr>
		</c:forEach>
	</table>
<!-- 	<script >
		document.addEventListener('DOMContentLoaded', function() {
		    const buttons = document.querySelectorAll('.profileButton');
		    buttons.forEach(button => {
		        button.addEventListener('click', function() {
		            const playerName = encodeURIComponent(this.getAttribute('data-player'));
		            window.location.href = `riotservlet?pseudo=${playerName}`;
		        });
		    });
		});
	</script> -->
</body>
</html>
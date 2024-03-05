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
				<td><button class="profileButton" data-player="${name}">Acceder
						au profil</button></td>
			</tr>
		</c:forEach>
	</table>
	<script >
		const buttons = document.getElementsByClassName("profileButton");
		Array.from(buttons).forEach(button => {
		button.addEventListener('click', function() {
			const playerName = button.dataset.player;
			window.location.href = '/tft-match-viewer/riotprofile?player=' + playerName;
			});
		});
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<title>Résultat de la Recherche</title>
</head>
<body>
	<h1>Résultat de la Recherche</h1>
	<table>
		<tr>
			<th>PUUID</th>
			<th>Nom</th>
			<th>Rang</th>
			<th>Taux de Victoire</th>
		</tr>
		<tr>
			<td>${joueur.id}</td>
			<td>${joueur.name}</td>
			<td>${joueur.rank}</td>
			<c:if test="${joueur.winRate!=-1}">
				<td>${joueur.winRate}%</td>
			</c:if>
			<c:if test="${joueur.winRate==-1}">
				<td>Donnée insuffisantes</td>
			</c:if>
		</tr>
	</table>

	<h2>Matchs</h2>
	<table>
		<tr>
			<th>Match ID</th>
			<th>Participants PUUIDs</th>
		</tr>
		<c:forEach var="match" items="${matchsMap.keySet()}">
			<tr>
				<td>${match.matchId}</td>
				<td><c:forEach var="participant" items="${match.participants}">
						<c:set var="participantInfo"
							value="${fn:split(participant, ' (')}" />
						<c:set var="puuid"
							value="${fn:substringBefore(participantInfo[1], ')')}" />
						<a href="riotservlet?puuid=${puuid}">${participantInfo[0]}</a>
					</c:forEach></td>
				<td>${matchsMap.get(match)}</td>
			</tr>
		</c:forEach>
	</table>

	<h2>Unités Favorites</h2>
	<table>
		<tr>
			<th>Name</th>
			<th>Presence</th>
		</tr>
		<c:forEach var="maxKey" items="${mapMax.keySet()}">
			<tr>
				<td>${maxKey}</td>
				<td>${mapMax.get(maxKey)}%</td>
			</tr>
		</c:forEach>
	</table>
	
	<a href="index.jsp">Nouvelle Recherche</a>
</body>
</html>

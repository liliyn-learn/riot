<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
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
            <td>${joueur.winRate}%</td>
        </tr>
    </table>
    <a href="riot.jsp">Nouvelle Recherche</a>
</body>
</html>

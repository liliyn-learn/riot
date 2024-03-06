package com.riotgames.tftanalytics.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.riotgames.tftanalytics.bean.Joueur;
import com.riotgames.tftanalytics.bean.Match;
import com.riotgames.tftanalytics.dao.JoueurDAO;
import com.riotgames.tftanalytics.dao.MatchDAO;
import com.riotgames.tftanalytics.exception.RiotException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Servlet implementation class RiotServlet
 */
@WebServlet("/RiotServlet")
public class RiotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RiotServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String puuid = request.getParameter("puuid");
		if (puuid == null || puuid.isEmpty()) {
			response.sendRedirect("index.jsp");
			return;
		}

		RiotAPI api = new RiotAPI();
		Joueur joueur = new Joueur(puuid);

		List<Match> matchs = new ArrayList<>();

		try {
			ArrayList<String> matchIds = api.getMatchIds(puuid);

			// Limiter à 10 derniers matchs si nécessaire
			if (matchIds.size() > 10) {
				matchIds = new ArrayList<>(matchIds.subList(matchIds.size() - 10, matchIds.size()));
			}

			for (String matchId : matchIds) {
				ArrayList<Object> participantPuuidsObjects = api.getMatchPlayers(matchId);

				List<String> participantDetails = new ArrayList<>();
				for (Object participantPuuidObject : participantPuuidsObjects) {
					String participantPuuidObj = (String) participantPuuidObject;
					String participantName = api.getPseudo(participantPuuidObj);
					participantDetails.add(participantName + " (" + participantPuuidObj + ")");
				}

				Match match = new Match(matchId, participantDetails);
				matchs.add(match);
			}
		} catch (Exception e) {
			System.err.println(e);
		}

		request.setAttribute("joueur", joueur);
		request.setAttribute("matchs", matchs);

		request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			String pseudo = request.getParameter("pseudo");
			pseudo = decodeForURL(pseudo);
			String tag = request.getParameter("tag");
			
			if (pseudo.isEmpty()) {
				throw new RiotException("pseudo ne peut pas etre vide");
			}
			if (tag.isEmpty()) {
				throw new RiotException("tag ne peut pas etre vide");
			}

			// Création d'une nouvelle instance de Joueur
			Joueur joueur = new Joueur(pseudo, tag);

			// Liste pour stocker les objets Match

			// Utilisation de getMatchIds pour récupérer les IDs de matchs
			RiotAPI api = new RiotAPI();
			
			ArrayList<String> matchIds = api.getMatchIds(joueur.getId()); // Utiliser l'ID du joueur pour récupérer les matchs

			// Limiter la liste à 10 derniers matchs si elle contient plus de 10 éléments
			if (matchIds.size() > 10) {
				matchIds = new ArrayList<>(matchIds.subList(matchIds.size() - 10, matchIds.size()));
			}
			List<Match> matchs = new ArrayList<>();

			// Pour chaque ID de match, récupérer les identifiants des joueurs participants et créer un objet Match
			for (String matchId : matchIds) {
				ArrayList<Object> participantPuuidsObjects = api.getMatchPlayers(matchId);

				List<String> participantDetails = new ArrayList<>();
				for (Object participantPuuidObject : participantPuuidsObjects) {
					String participantPuuid = (String) participantPuuidObject; // Conversion explicite de Object à String
					// Conversion de PUUID en pseudo
					String participantName = encodeForURL(api.getPseudo(participantPuuid));
					// Ajout du nom et du PUUID sous la forme "pseudo (PUUID)"
					participantDetails.add(participantName + " (" + participantPuuid + ")");
				}

				Match match = new Match(matchId, participantDetails); // Assurez-vous que le constructeur de Match accepte une List<String>
				matchs.add(match);
			}
			new JoueurDAO().save(joueur);
			for (Match m : matchs) {
				new MatchDAO().save(m);
			}

			// Passage des données à la JSP
			request.setAttribute("joueur", joueur);
			request.setAttribute("matchs", matchs);

			// Rediriger vers Result.jsp pour afficher les données
			request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
		} catch (RiotException e) {
			request.setAttribute("erreur", "Joueur Introuvable : "+e.getMess());
			request.getRequestDispatcher("index.jsp").forward(request, response);
			System.err.println(e);
		}
	}



	public String encodeForURL(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// Handle the exception or log it
			return ""; // Or some fallback mechanism
		}
	}

	
	public String decodeForURL(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// Handle the exception or log it
			return ""; // Or some fallback mechanism
		}
	}
}
package com.riotgames.tftanalytics.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.riotgames.tftanalytics.bean.Joueur;
import com.riotgames.tftanalytics.bean.Match;
import com.riotgames.tftanalytics.bean.MatchAnalyzer;
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
		try {

			String puuid = request.getParameter("puuid");
			if (puuid == null || puuid.isEmpty()) {
				response.sendRedirect("index.jsp");
				return;
			}

			RiotAPI api = new RiotAPI();
			Joueur joueur = new Joueur(puuid);

			HashMap<String, Double> unitsPresence = new HashMap<String, Double>();

			ArrayList<String> matchIds = api.getMatchIds(joueur.getId()); // Utiliser l'ID du joueur pour récupérer les matchs

			// Limiter la liste à 10 derniers matchs si elle contient plus de 10 éléments
			if (matchIds.size() > 10) {
				matchIds = new ArrayList<>(matchIds.subList(matchIds.size() - 10, matchIds.size()));
			}
			HashMap<Match, Integer> matchsMap = new HashMap<Match, Integer>();

			// Pour chaque ID de match, récupérer les identifiants des joueurs participants et créer un objet Match
			for (String matchId : matchIds) {
				ArrayList<Object> participantPuuidsObjects = api.getMatchPlayers(matchId);
				MatchAnalyzer matchAnalyzer =  new MatchAnalyzer(joueur.getId(), matchId);

				List<String> participantDetails = new ArrayList<>();
				for (Object participantPuuidObject : participantPuuidsObjects) {
					String participantPuuid = (String) participantPuuidObject; // Conversion explicite de Object à String
					// Conversion de PUUID en pseudo
					String participantName = encodeForURL(api.getPseudo(participantPuuid));
					// Ajout du nom et du PUUID sous la forme "pseudo (PUUID)"
					participantDetails.add(participantName + " (" + participantPuuid + ")");
				}

				ArrayList<String> units = (ArrayList<String>) matchAnalyzer.getUnits();
				for (String unit : units) {
					if (!unitsPresence.keySet().contains(unit)) {
						unitsPresence.put(unit, (double) 1);
					}
					else {
						unitsPresence.put(unit,(double) unitsPresence.get(unit)+1);
					}
				}
				Match match = new Match(matchId, participantDetails); // Assurez-vous que le constructeur de Match accepte une List<String>
				matchsMap.put(match, matchAnalyzer.getPlacement());
			} 

			double sum = 0;
			ArrayList<Double> presences = new ArrayList<>(unitsPresence.values());
			for (double presence : presences) {
				sum += presence;
			}

			HashMap<String, Double> mapMax = new HashMap<String, Double>();

			for (int i = 0; i<=2;i++) {
				String maxKey = null;
				double max = Collections.max(unitsPresence.values());
				for (Map.Entry<String, Double> entry : unitsPresence.entrySet()) {
					if (entry.getValue() == max) {
						maxKey = entry.getKey();
						break; 
					}
				}
				if (maxKey != null) {
					mapMax.put(maxKey, max/sum*100);
					unitsPresence.remove(maxKey);
				}
			}
			// Passage des données à la JSP
			request.setAttribute("joueur", joueur);
			request.setAttribute("matchsMap", matchsMap);
			request.setAttribute("mapMax", mapMax);

			// Rediriger vers Result.jsp pour afficher les données
			request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
		} catch (RiotException e) {
			request.setAttribute("erreur", "Joueur Introuvable : "+e.getMess());
			request.getRequestDispatcher("index.jsp").forward(request, response);
			System.err.println(e);		
		}
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
			
			Joueur joueur = new Joueur(pseudo, tag);

			HashMap<String, Double> unitsPresence = new HashMap<String, Double>();

			// Utilisation de getMatchIds pour récupérer les IDs de matchs
			RiotAPI api = new RiotAPI();

			ArrayList<String> matchIds = api.getMatchIds(joueur.getId()); // Utiliser l'ID du joueur pour récupérer les matchs

			// Limiter la liste à 10 derniers matchs si elle contient plus de 10 éléments
			if (matchIds.size() > 10) {
				matchIds = new ArrayList<>(matchIds.subList(matchIds.size() - 10, matchIds.size()));
			}
			HashMap<Match, Integer> matchsMap = new HashMap<Match, Integer>();

			// Pour chaque ID de match, récupérer les identifiants des joueurs participants et créer un objet Match
			for (String matchId : matchIds) {
				ArrayList<Object> participantPuuidsObjects = api.getMatchPlayers(matchId);
				MatchAnalyzer matchAnalyzer =  new MatchAnalyzer(joueur.getId(), matchId);

				List<String> participantDetails = new ArrayList<>();
				for (Object participantPuuidObject : participantPuuidsObjects) {
					String participantPuuid = (String) participantPuuidObject; // Conversion explicite de Object à String
					// Conversion de PUUID en pseudo
					String participantName = encodeForURL(api.getPseudo(participantPuuid));
					// Ajout du nom et du PUUID sous la forme "pseudo (PUUID)"
					participantDetails.add(participantName + " (" + participantPuuid + ")");
				}

				ArrayList<String> units = (ArrayList<String>) matchAnalyzer.getUnits();
				for (String unit : units) {
					if (!unitsPresence.keySet().contains(unit)) {
						unitsPresence.put(unit, (double) 1);
					}
					else {
						unitsPresence.put(unit,(double) unitsPresence.get(unit)+1);
					}
				}
				Match match = new Match(matchId, participantDetails); // Assurez-vous que le constructeur de Match accepte une List<String>
				matchsMap.put(match, matchAnalyzer.getPlacement());
			}
			
			new JoueurDAO().save(joueur);
			for (Match m : matchsMap.keySet()) {
				new MatchDAO().save(m);
			}

			double sum = 0;
			ArrayList<Double> presences = new ArrayList<>(unitsPresence.values());
			for (double presence : presences) {
				sum += presence;
			}

			HashMap<String, Double> mapMax = new HashMap<String, Double>();

			for (int i = 0; i<=2;i++) {
				String maxKey = null;
				double max = Collections.max(unitsPresence.values());
				for (Map.Entry<String, Double> entry : unitsPresence.entrySet()) {
					if (entry.getValue() == max) {
						maxKey = entry.getKey();
						break; 
					}
				}
				if (maxKey != null) {
					mapMax.put(maxKey, max/sum*100);
					unitsPresence.remove(maxKey);
				}
			}
			// Passage des données à la JSP
			request.setAttribute("joueur", joueur);
			request.setAttribute("matchsMap", matchsMap);
			request.setAttribute("mapMax", mapMax);

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
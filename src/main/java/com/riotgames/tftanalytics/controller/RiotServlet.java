package com.riotgames.tftanalytics.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riotgames.tftanalytics.bean.Joueur;
import com.riotgames.tftanalytics.bean.Match;
import com.riotgames.tftanalytics.exception.RiotException;

/**
 * Servlet implementation class RiotServlet
 */
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/logPlayer.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String pseudo = request.getParameter("pseudo");
	    String tag = request.getParameter("tag");

	    // Création d'une nouvelle instance de Joueur
	    Joueur joueur = new Joueur(pseudo, tag);
	    
	    // Liste pour stocker les objets Match
	    List<Match> matchs = new ArrayList<>();

	    // Utilisation de getMatchIds pour récupérer les IDs de matchs
	    RiotAPI api = new RiotAPI();
	    try {
		    ArrayList<String> matchIds = api.getMatchIds(joueur.getId()); // Utiliser l'ID du joueur pour récupérer les matchs

		    // Limiter la liste à 10 derniers matchs si elle contient plus de 10 éléments
		    if (matchIds.size() > 10) {
		        matchIds = new ArrayList<>(matchIds.subList(matchIds.size() - 10, matchIds.size()));
		    }

		    // Pour chaque ID de match, récupérer les identifiants des joueurs participants et créer un objet Match
		    for (String matchId : matchIds) {
		        ArrayList<Object> participantPuuidsObjects = api.getMatchPlayers(matchId);
		        
		        List<String> participantDetails = new ArrayList<>();
		        for (Object participantPuuidObject : participantPuuidsObjects) {
		            String participantPuuid = (String) participantPuuidObject; // Conversion explicite de Object à String
		            // Conversion de PUUID en pseudo
		            String participantName = api.getPseudo(participantPuuid);
		            // Ajout du nom et du PUUID sous la forme "pseudo (PUUID)"
		            participantDetails.add(participantName + " (" + participantPuuid + ")");
		        }

		        Match match = new Match(matchId, participantDetails); // Assurez-vous que le constructeur de Match accepte une List<String>
		        matchs.add(match);
		    }
		} catch (RiotException e) {
			System.err.println(e);
		}

	    // Passage des données à la JSP
	    request.setAttribute("joueur", joueur);
	    request.setAttribute("matchs", matchs);

	    // Rediriger vers Result.jsp pour afficher les données
	    request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
	}

}

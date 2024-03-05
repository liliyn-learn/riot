package com.riotgames.tftanalytics.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riotgames.tftanalytics.bean.Joueur;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String pseudo = request.getParameter("pseudo");
	    String tag = request.getParameter("tag");

	    // Création d'une nouvelle instance de Joueur
	    Joueur joueur = new Joueur(pseudo, tag);

	    if (joueur.getId()!=null) {
		    request.setAttribute("joueur", joueur);

		    // Rediriger vers une JSP ou renvoyer directement la réponse
		    // Exemple de redirection vers une nouvelle JSP pour afficher le puuid
			request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
	    }
	    else {
	    	request.setAttribute("erreur", "Joueur Introuvable");
			request.getRequestDispatcher("/WEB-INF/logPlayer.jsp").forward(request, response);
	    }
	}

}

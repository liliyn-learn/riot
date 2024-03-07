package com.riotgames.tftanalytics.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riotgames.tftanalytics.exception.RiotException;

/**
 * Servlet implementation class RiotBest
 */
@WebServlet("/meilleursJoueurs")
public class RiotBestPlayersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RiotBestPlayersServlet() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, Integer> nameLpMap = new HashMap<String, Integer>();
		try {
			HashMap<Object, Object> playerInfosMap;
			for (Object object : new RiotAPI().getBestPlayers()) {
				playerInfosMap = (HashMap<Object, Object>) object;
				nameLpMap.put((String)playerInfosMap.get("summonerName"), (Integer)playerInfosMap.get("leaguePoints"));
			}
		} catch (RiotException e) {
			System.err.println(e);
		}
		
		request.setAttribute("nameLpMap", sortByValue(nameLpMap));
		request.getRequestDispatcher("/WEB-INF/bestPlayersTab.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String puuid = new RiotAPI().getPuuid(request.getParameter("summonerName"));
			response.sendRedirect("riotservlet?puuid="+puuid);
		} catch (RiotException e) {
			System.err.println(e);
		}
	}
	
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

}

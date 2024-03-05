package com.riotgames.tftanalytics.controller;

import com.riotgames.tftanalytics.bean.Joueur;
import com.riotgames.tftanalytics.bean.Match;
import com.riotgames.tftanalytics.bean.MatchAnalyzer;
import com.riotgames.tftanalytics.dao.JoueurDAO;
import com.riotgames.tftanalytics.dao.MatchAnalyzerDAO;
import com.riotgames.tftanalytics.dao.MatchDAO;
import com.riotgames.tftanalytics.exception.RiotException;

public class Main {
	public static void main(String[] args) {
		try {
			for (Object o : new RiotAPI().getBestPlayers()) {
				System.out.println(o);
			}
		} catch (RiotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for (Joueur j : m.getParticipants()) {
//			System.out.println();
//		}
	}
}
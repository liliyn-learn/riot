package com.riotgames.tftanalytics.controller;

import com.riotgames.tftanalytics.bean.Joueur;
import com.riotgames.tftanalytics.bean.Match;
import com.riotgames.tftanalytics.bean.MatchAnalyzer;
import com.riotgames.tftanalytics.dao.JoueurDAO;
import com.riotgames.tftanalytics.dao.MatchAnalyzerDAO;
import com.riotgames.tftanalytics.dao.MatchDAO;

public class Main {
	public static void main(String[] args) {
		JoueurDAO daoJ = new JoueurDAO();
		Joueur j = new Joueur("Paddes", "1337");
		MatchDAO daoM = new MatchDAO();
		Match m = new Match("EUW1_6822859668");
		daoM.save(m);
		MatchAnalyzerDAO daoMa = new MatchAnalyzerDAO();
		MatchAnalyzer ma = new MatchAnalyzer(j.getId(), m.getMatchId());
		daoMa.save(ma);
		Joueur j1 = daoJ.get(5);
		Match m1 = daoM.get(1);
		MatchAnalyzer ma1 = daoMa.get(10);
		System.out.println(j1);
		System.out.println(m1);
		System.out.println(ma1);
//		for (Joueur j : m.getParticipants()) {
//			System.out.println();
//		}
	}
}
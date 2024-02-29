package com.riotgames.tftanalytics.bean;

import java.util.HashMap;
import java.util.List;

import com.riotgames.tftanalytics.controller.RiotAPI;

public class MatchAnalyzer {
    private String matchId; // Identifiant du match récupéré de la classe Match
    private String joueurId; // Identifiant du joueur récupéré de la classe Player
    private List<String> units; // Liste des unités choisies par le joueur dans le match
    private int placement; // Placement final du joueur dans le match

    
    /*
     * Constructeur utilisant l'API riot
     */
    public MatchAnalyzer(String joueurId, String matchId) {
		this.matchId = matchId;
		this.joueurId = joueurId;
		HashMap<String, Object> mapInfos = new RiotAPI().getParticipantInfos(joueurId, matchId);
		if (mapInfos!=null) {
			this.placement = (int) mapInfos.get("placement");
			this.units =  (List<String>) mapInfos.get("units");
		}
	}



	// Constructeur
    public MatchAnalyzer(String matchId, String joueurId, List<String> units, int placement) {
        this.matchId = matchId;
        this.joueurId = joueurId;
        this.units = units;
        this.placement = placement;
    }
  
    @Override
	public String toString() {
		return "MatchAnalyzer [matchId=" + matchId + ", joueurId=" + joueurId + ", units=" + units + ", placement="
				+ placement + "]";
	}

	// Getters - Setters pour les attributs
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getJoueurId() {
        return joueurId;
    }

    public void setJoueurId(String joueurId) {
        this.joueurId = joueurId;
    }

    public List<String> getUnits() {
        return units;
    }

    public void setUnits(List<String> units) {
        this.units = units;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

}

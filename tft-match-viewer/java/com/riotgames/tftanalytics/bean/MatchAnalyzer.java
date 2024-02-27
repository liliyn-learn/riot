package com.riotgames.tftanalytics.bean;

import java.util.List;

public class MatchAnalyzer {
    private String matchId; // Identifiant du match récupéré de la classe Match
    private String joueurId; // Identifiant du joueur récupéré de la classe Player
    private List<String> units; // Liste des unités choisies par le joueur dans le match
    private int placement; // Placement final du joueur dans le match

    // Constructeur
    public MatchAnalyzer(String matchId, String joueurId, List<String> units, int placement) {
        this.matchId = matchId;
        this.joueurId = joueurId;
        this.units = units;
        this.placement = placement;
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

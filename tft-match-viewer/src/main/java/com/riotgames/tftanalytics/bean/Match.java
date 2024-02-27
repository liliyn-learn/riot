package com.riotgames.tftanalytics.bean;

import java.util.List;

public class Match {
	private String matchId;
    private List<Joueur> participants; // Supposons que chaque match a une liste de joueurs participants

    public Match(String matchId, List<Joueur> participants, String result) {
        this.matchId = matchId;
        this.participants = participants;
    }

    // Getters - Setters
    public String getMatchId() {
        return matchId;
    }

    public List<Joueur> getParticipants() {
        return participants;
    }
}

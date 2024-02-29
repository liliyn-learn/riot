package com.riotgames.tftanalytics.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.riotgames.tftanalytics.controller.RiotAPI;

@Entity
@Table(name = "MatchPlayed")
public class Match {
	@Id
	private int id;
	
	private String matchId;
    private List<Joueur> participants; // Supposons que chaque match a une liste de joueurs participants

    /*
     * Constructeur utilisant l'API riot
     */
    public Match(String matchId) {
        this.matchId = matchId;
        this.participants = new ArrayList<Joueur>();
        ArrayList<Object> listId = new RiotAPI().getMatchPlayers(this.matchId);
        for (Object o : listId) {
        	this.participants.add(new Joueur((String)o));
        }
	}
    
    public Match(String matchId, List<Joueur> participants, String result) {
        this.matchId = matchId;
        this.participants = participants;
    }

    @Override
	public String toString() {
		return "Match [matchId=" + matchId + ", participants=" + participants + "]";
	}

	// Getters - Setters
    public String getMatchId() {
        return matchId;
    }

    public List<Joueur> getParticipants() {
        return participants;
    }
}

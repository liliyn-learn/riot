package com.riotgames.tftanalytics.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.riotgames.tftanalytics.controller.RiotAPI;
import com.riotgames.tftanalytics.exception.RiotException;

@Entity
@Table(name = "MatchPlayed")
public class Match {
	@Id
	@GeneratedValue
	private int id;
	
	private String matchId;
	
    @OneToMany(cascade = CascadeType.ALL) // CascadeType.ALL assure que les opérations de persistance, mise à jour, suppression, etc., sont propagées aux joueurs
    private List<Joueur> participants; // Supposons que chaque match a une liste de joueurs participants

    /**
     * Constructeur utilisant l'API riot
     */
    public Match(String matchId) {
        this.matchId = matchId;
		try {
			ArrayList<Object> listId = new RiotAPI().getMatchPlayers(this.matchId);
	        this.participants = new ArrayList<Joueur>();
	        for (Object o : listId) {
	        	this.participants.add(new Joueur((String)o));
	        }
		} catch (RiotException e) {
			System.err.println(e);
		}
	}
    
    public Match() {
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

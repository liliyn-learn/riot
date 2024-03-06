package com.riotgames.tftanalytics.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "MatchPlayed")
public class Match {
	@Id
	@GeneratedValue
	private int id;
	
	private String matchId;
	
    @Transient
	private List<String> participantIds;

    /**
     * Constructeur utilisant l'API riot
     */
//    public Match(String matchId) {
//        this.matchId = matchId;
//		try {
//			ArrayList<Object> listId = new RiotAPI().getMatchPlayers(this.matchId);
//	        this.participantIds = new ArrayList<String>();
//	        for (Object o : listId) {
//	        	this.participantIds.add((String)o);
//	        }
//		} catch (RiotException e) {
//			System.err.println(e);
//		}
//	}
    
    public Match() {
	}

	public Match(String matchId, List<String> participants) {
        this.matchId = matchId;
        this.participantIds = participants;
    }

    @Override
	public String toString() {
		return "Match [matchId=" + matchId + ", participants=" + participantIds + "]";
	}

	// Getters - Setters
    public String getMatchId() {
        return matchId;
    }

    public List<String> getParticipants() {
        return participantIds;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getParticipantIds() {
		return participantIds;
	}

	public void setParticipantIds(List<String> participantIds) {
		this.participantIds = participantIds;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
}

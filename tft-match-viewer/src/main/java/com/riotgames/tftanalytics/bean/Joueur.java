package com.riotgames.tftanalytics.bean;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.riotgames.tftanalytics.controller.RiotAPI;

@Entity
@Table(name = "Joueur")
public class Joueur {
	@Id
	private int idJoueur;
	
	private String joueurId;
    private String name;
    private String joueurRank;
    private double winRate; // Autres statistiques pertinentes
    
    /*
     * Constructeur utilisant l'API riot
     */
    public Joueur(String pseudo, String tag) {
		this.name = pseudo;
    	
		RiotAPI api = new RiotAPI();
		this.joueurId = api.getPuuid(this.name, tag);
		
		HashMap<Object, Object> infos = api.getAccountInfos(this.name);
		if (infos==null) {
			this.joueurRank = "None";
			this.winRate = -1;
		}
		else {
			String rank = (String) infos.get("tier");
			this.joueurRank = (rank!=null) ? rank:"None";
			this.winRate = (double) (Integer) infos.get("wins") / (Integer) infos.get("losses");
		}
	}
    
    /*
     * Constructeur utilisant l'API riot
     */
    public Joueur(String id) {
		this.joueurId = id;
    	
		RiotAPI api = new RiotAPI();
		this.name = api.getPseudo(id);
		
		HashMap<Object, Object> infos = api.getAccountInfos(this.name);
		if (infos==null) {
			this.joueurRank = "None";
			this.winRate = -1;
		}
		else {
			String rank = (String) infos.get("tier");
			this.joueurRank = (rank!=null) ? rank:"None";
			this.winRate = (double) (Integer) infos.get("wins") / (Integer) infos.get("losses");
		}
	}

	public Joueur(String id, String name, String rank, double winRate) {
        this.joueurId = id;
        this.name = name;
        this.joueurRank = rank;
        this.winRate = winRate;
    }

    // Getters - Setters
    public String getId() {
        return joueurId;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return joueurRank;
    }

    public double getWinRate() {
        return winRate;
    }

	@Override
	public String toString() {
		return "Joueur [joueurId=" + joueurId + ", name=" + name + ", rank=" + joueurRank + ", winRate=" + winRate + "]";
	}
    
    
}

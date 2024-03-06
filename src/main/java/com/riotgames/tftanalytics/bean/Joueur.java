package com.riotgames.tftanalytics.bean;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.riotgames.tftanalytics.controller.RiotAPI;
import com.riotgames.tftanalytics.exception.RiotException;

@Entity
@Table(name = "Joueur")
public class Joueur {
	@Id
	@GeneratedValue
	private int id;
	
	private String joueurId;
    private String name;
    private String joueurRank;
    private double winRate; // Autres statistiques pertinentes
    
    /**
     * Constructeur utilisant l'API riot
     */
    public Joueur(String pseudo, String tag) {
		this.name = pseudo;
		
		RiotAPI api = new RiotAPI();
		try {
			this.joueurId = api.getPuuid(this.name, tag);
			HashMap<Object, Object> infos = api.getAccountInfos(this.name);
			if (infos!=null) {
				this.joueurRank = (String) infos.get("tier");
				this.winRate = (double) (Integer) infos.get("wins") / ((Integer) infos.get("losses") + (Integer) infos.get("wins")) * 100;
			} else {
				this.joueurRank = "Sans rang";
				this.winRate = -1;
			}
		} catch (RiotException e) {
			System.err.println(e);
		}
	}
    
    /**
     * Constructeur utilisant l'API riot
     */
    public Joueur(String id) {
		this.joueurId = id;
    	
		RiotAPI api = new RiotAPI();
		try {
			this.name = api.getPseudo(id);
		} catch (RiotException e) {
			System.err.println(e);
		}
		
		HashMap<Object, Object> infos;
		try {
			infos = api.getAccountInfos(this.name);
			this.joueurRank = (String) infos.get("tier");
			this.winRate = (double) (Integer) infos.get("wins") / ((Integer) infos.get("losses") + (Integer) infos.get("wins")) * 100;
		} catch (Exception e) {
			this.joueurRank = "None";
			this.winRate = -1;
			System.err.println(e);
		}
	}
    
	public Joueur() {
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

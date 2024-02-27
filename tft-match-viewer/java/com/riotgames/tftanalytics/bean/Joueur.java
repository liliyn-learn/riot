package com.riotgames.tftanalytics.bean;

public class Joueur {
	private String joueurId;
    private String name;
    private String rank;
    private double winRate; // Autres statistiques pertinentes

    public Joueur(String id, String name, String rank, double winRate) {
        this.joueurId = id;
        this.name = name;
        this.rank = rank;
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
        return rank;
    }

    public double getWinRate() {
        return winRate;
    }
}

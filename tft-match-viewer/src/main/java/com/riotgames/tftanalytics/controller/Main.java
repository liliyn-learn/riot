package com.riotgames.tftanalytics.controller;

public class Main {
	public static void main(String[] args) {
		new RiotAPI().getMatchInfos(new RiotAPI().getMatchIds(new RiotAPI().getPuuid("clé","euw")).get(0));
	}
}
package com.riotgames.tftanalytics.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RiotAPI {
	HttpURLConnection connection;

	public String getPuuid(String pseudo, String tag) {
		String response = "";
		String urlAPI = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"+pseudo+"/"+tag;
		response = askAPI(urlAPI);
		HashMap<String, String> map = null;
		ObjectMapper objectMapper = new ObjectMapper();
        try {
			map = objectMapper.readValue(response, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map.get("puuid");
	}
	
	public String getPseudo(String puuid) {
		String response = "";
		String urlAPI = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-puuid/"+puuid;
		response = askAPI(urlAPI);
		HashMap<String, String> map = null;
		ObjectMapper objectMapper = new ObjectMapper();
        try {
			map = objectMapper.readValue(response, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map.get("gameName");
	}
	
	public ArrayList<String> getMatchIds(String puuid) {
		ArrayList<String> listIds = null;
		String urlAPI = "https://europe.api.riotgames.com/tft/match/v1/matches/by-puuid/"+puuid+"/ids?start=0&count=20";
		String response = askAPI(urlAPI);
		ArrayList<String> list = null;
		ObjectMapper objectMapper = new ObjectMapper();
        try {
			list = objectMapper.readValue(response, new TypeReference<ArrayList<String>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		listIds = new ArrayList<String>(Arrays.asList(response.substring(1, response.length()-1).split(",")));
		return list;
	}
	
	public void getMatchInfos(String matchId) {
		ArrayList<String> listIds = null;
		String urlAPI = "https://europe.api.riotgames.com/tft/match/v1/matches/"+matchId;
		String response = askAPI(urlAPI);
		HashMap<String, HashMap<String, String>> map = null;
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	 JsonNode jsonNode = objectMapper.readTree(response).get("info").get("participants");
        	 ArrayList<String> s = objectMapper.treeToValue(jsonNode, ArrayList.class);
        	 System.out.println(s);
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String askAPI(String url) {
		String response = null;
		try {
			URI uri = new URI(url);

			connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.setRequestMethod("GET");

			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 OPR/107.0.0.0");
			connection.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
			connection.setRequestProperty("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Origin", "https://developer.riotgames.com");
			connection.setRequestProperty("X-Riot-Token", "RGAPI-7de4208e-6083-427d-b4bf-eca7151d33c2");

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			response = in.readLine();
					
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}


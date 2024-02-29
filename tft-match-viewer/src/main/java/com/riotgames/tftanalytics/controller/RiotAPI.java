package com.riotgames.tftanalytics.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class RiotAPI {
	private static HttpURLConnection connection;

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
	
	public HashMap<Object, Object> getAccountInfos(String pseudo) {
		ObjectMapper objectMapper = new ObjectMapper();
		String response = null;
		pseudo = pseudo.replace(" ", "%20");
		String urlAPI = "https://euw1.api.riotgames.com/tft/summoner/v1/summoners/by-name/"+pseudo;
		String id = null;
		response = askAPI(urlAPI);
        try {
    		HashMap<Object, Object> map = null;
			map = objectMapper.readValue(response, HashMap.class);
			id = (String) map.get("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        urlAPI = "https://euw1.api.riotgames.com/tft/league/v1/entries/by-summoner/"+id;
        response = askAPI(urlAPI);
        HashMap<Object, Object> playerInfos = null;
        try {
        	ArrayList<Object> list = objectMapper.readValue(response, new TypeReference<ArrayList<Object>>() {});
        	if (!list.isEmpty()) {
            	playerInfos = (HashMap<Object, Object>) list.get(0);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return playerInfos;
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
	
	public ArrayList<Object> getMatchPlayers(String matchId) {
		ArrayList<Object> listIds = null;
		String urlAPI = "https://europe.api.riotgames.com/tft/match/v1/matches/"+matchId;
		String response = askAPI(urlAPI);
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	 JsonNode jsonNode = objectMapper.readTree(response).get("metadata").get("participants");
             ObjectReader reader = objectMapper.readerFor(new TypeReference<ArrayList<Object>>() {
             });
             listIds = reader.readValue(jsonNode);
        } catch (Exception e) {
			e.printStackTrace();
		}
        return (ArrayList<Object>) listIds;
	}
	
	public HashMap<String, Object> getParticipantInfos(String playerId, String matchId) {
		ArrayList<String> units = new ArrayList<String>();
		Integer placement = -1;
		String urlAPI = "https://europe.api.riotgames.com/tft/match/v1/matches/"+matchId;
		String response = askAPI(urlAPI);
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	 JsonNode participants = objectMapper.readTree(response).get("info").get("participants");
        	 for(JsonNode participant : participants) {
        		 if (objectMapper.treeToValue(participant.get("puuid"), String.class).equals(playerId)) {
            		 placement = objectMapper.treeToValue(participant.get("placement"), Integer.class);
            		 for (JsonNode unit : participant.get("units")) {
            			 units.add(objectMapper.treeToValue(unit.get("character_id"), String.class).substring(6));
            		 }
            		 break;
        		 }
        	 }
//        	 listIds = objectMapper.treeToValue(jsonNode, HashMap.class);
        } catch (Exception e) {
			e.printStackTrace();
		}
        HashMap<String, Object> mapInfos = new HashMap<String, Object>();
        mapInfos.put("placement", placement);
        mapInfos.put("units", units);
        return mapInfos;
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
			connection.setRequestProperty("X-Riot-Token", "RGAPI-7dddc329-2f01-4811-806e-c33fbecd54aa");

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			response = in.readLine();
					
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}


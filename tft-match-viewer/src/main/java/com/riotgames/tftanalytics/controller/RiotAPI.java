package com.riotgames.tftanalytics.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class RiotAPI {
	private static HttpURLConnection connection;

	
	public String getPuuid(String pseudo, String tag) {
		System.out.println(pseudo);
		String urlAPI = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + pseudo + "/" + tag;
		String response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			HashMap<String, String> map = new ObjectMapper().readValue(response, HashMap.class);
			return map.get("puuid");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPseudo(String puuid) {
		String response = "";
		String urlAPI = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-puuid/"+puuid;
		response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			HashMap<String, String> map = new ObjectMapper().readValue(response, HashMap.class);
			return map.get("gameName");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public HashMap<Object, Object> getAccountInfos(String pseudo) {
		ObjectMapper objectMapper = new ObjectMapper();
		String response = null;
		pseudo = pseudo.replace(" ", "%20");
		String urlAPI = "https://euw1.api.riotgames.com/tft/summoner/v1/summoners/by-name/"+pseudo;
		String id = "";
		response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			HashMap<Object, Object> map = objectMapper.readValue(response, new TypeReference<HashMap<Object,Object>>(){});
			id = (String) map.get("id");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (id == null || response.isEmpty()) {
			return null;
		}
		urlAPI = "https://euw1.api.riotgames.com/tft/league/v1/entries/by-summoner/"+id;
		response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			ArrayList<Object> list = objectMapper.readValue(response,ArrayList.class);
			if (!list.isEmpty()) {
				HashMap<Object, Object> playerInfos = (HashMap<Object, Object>) list.get(0);
				return playerInfos;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public ArrayList<String> getMatchIds(String puuid) {
		ArrayList<String> listIds = null;
		String urlAPI = "https://europe.api.riotgames.com/tft/match/v1/matches/by-puuid/"+puuid+"/ids?start=0&count=20";
		String response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			ArrayList<String> list = new ObjectMapper().readValue(response,ArrayList.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Object> getMatchPlayers(String matchId) {
		ArrayList<Object> listIds = null;
		String urlAPI = "https://europe.api.riotgames.com/tft/match/v1/matches/"+matchId;
		String response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			TreeNode jsonNode = objectMapper.readTree(response).get("metadata").get("participants");
			ObjectReader reader = objectMapper.readerFor(ArrayList.class);
			listIds = reader.readValue((JsonParser) jsonNode);
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
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode participants = objectMapper.readTree(response).get("info").get("participants");
			for(JsonNode participant : participants) {
				if (objectMapper.treeToValue(participant.get("puuid"), String.class).equals(playerId)) {
					placement = objectMapper.treeToValue(participant.get("placement"), Integer.class);
					for (TreeNode unit : participant.get("units")) {
						units.add(objectMapper.treeToValue(unit.get("character_id"), String.class).substring(6));
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		HashMap<String, Object> mapInfos = new HashMap<String, Object>();
		mapInfos.put("placement", placement);
		mapInfos.put("units", units);
		return mapInfos;
	}

	private String askAPI(String url) {
		try {			
			url = url.replace(" ", "%20");
			url = url.replace("Ã©", "é");

			URI uri = new URI(url);

			connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.setRequestMethod("GET");

			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 OPR/107.0.0.0");
			connection.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
			connection.setRequestProperty("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Origin", "https://developer.riotgames.com");
			connection.setRequestProperty("X-Riot-Token", "RGAPI-5e3ecaa2-0e76-49f3-babc-1767526f0ca2");

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String response = in.readLine();

			in.close();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


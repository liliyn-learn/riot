package com.riotgames.tftanalytics.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.riotgames.tftanalytics.exception.RiotException;

/*
 * Permet d'obtenir toutes sortes d'informations disponible sur l'API Riot
 */
public class RiotAPI {
	private static HttpURLConnection connection;

	/**
	 * @param 	puuid 	Le puuid du joueur
	 * @return 			null en cas d'erreur, sinon le pseudo du joueur
	 * @throws RiotException 
	 */
	public String getPseudo(String puuid) throws RiotException {
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
			throw new RiotException("Données reçu invalides");
		}
	}
	
	/**
	 * @param 	pseudo 	Le pseudo du joueur
	 * @param 	tag 	Une information supplémentaire pour identfier le joueur
	 * @return 			null en cas d'erreur, sinon le puuid du joueur
	 * @throws RiotException 
	 */
	public String getPuuid(String pseudo, String tag) throws RiotException {
		pseudo = URLEncoder.encode(pseudo, StandardCharsets.UTF_8);
		String urlAPI = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + pseudo + "/" + tag;
		String response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			HashMap<String, String> map = new ObjectMapper().readValue(response, HashMap.class);
			return map.get("puuid");
		} catch (Exception e) {
			throw new RiotException("Données reçu invalides");
		}
	}
	
	/**
	 * @param 	pseudo 	Le pseudo du joueur
	 * @return 			null en cas d'erreur, sinon le puuid du joueur
	 * @throws RiotException 
	 */
	public String getPuuid(String pseudo) throws RiotException {
		ObjectMapper objectMapper = new ObjectMapper();
		String response = null;
		if (pseudo == null || pseudo.isEmpty()) {
			return null;
		}
		pseudo = URLEncoder.encode(pseudo, StandardCharsets.UTF_8);
		String urlAPI = "https://euw1.api.riotgames.com/tft/summoner/v1/summoners/by-name/"+pseudo;
		String id = "";
		response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			HashMap<Object, Object> map = objectMapper.readValue(response, new TypeReference<HashMap<Object,Object>>(){});
			id = (String) map.get("puuid");
		} catch (Exception e) {
			throw new RiotException("Données reçu invalides");
		}

		return id;
	}

	/**
	 * @param 	pseudo 	Le pseudo du joueur
	 * @return 			null en cas d'erreur, sinon un fonction allant chercher des informations sur le joueur, comme son rang
	 * @throws RiotException 
	 */
	public HashMap<Object, Object> getAccountInfos(String pseudo) throws RiotException {
		ObjectMapper objectMapper = new ObjectMapper();
		String response = null;
		if (pseudo == null || pseudo.isEmpty()) {
			return null;
		}
		pseudo = URLEncoder.encode(pseudo, StandardCharsets.UTF_8);
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
			throw new RiotException("Données reçu invalides");
		}

		return getSummonerInfos(id);
	}
	
	/**
	 * @param 	summonerId 	Le summonerId du joueur
	 * @return 				null en cas d'erreur, sinon des informations sur le joueur, comme son rang
	 * @throws RiotException 
	 */
	public HashMap<Object, Object> getSummonerInfos(String summonerId) throws RiotException {
		String response = null;
		String urlAPI = "https://euw1.api.riotgames.com/tft/league/v1/entries/by-summoner/"+summonerId;
		response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			ArrayList<Object> list = new ObjectMapper().readValue(response,ArrayList.class);
			if (!list.isEmpty()) {
				HashMap<Object, Object> playerInfos = (HashMap<Object, Object>) list.get(0);
				return playerInfos;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			throw new RiotException("Données reçu invalides");
		}
	}

	
	/**
	 * @param 	puuid 	Le puuid du joueur
	 * @return 			null en cas d'erreur, sinon une liste des ids des 5 derniers match que le joueur a joué
	 * @throws RiotException 
	 */
	public ArrayList<String> getMatchIds(String puuid) throws RiotException {
	    ArrayList<String> listIds = new ArrayList<>();
	    String urlAPI = "https://europe.api.riotgames.com/tft/match/v1/matches/by-puuid/" + puuid + "/ids?start=0&count=5";
	    try {
	        String response = askAPI(urlAPI);
	        
	        if (response != null && !response.isEmpty()) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            listIds = objectMapper.readValue(response, new TypeReference<ArrayList<String>>() {});
	        }
	    } catch (IOException e) {
	        if (e instanceof HttpRetryException) {
	            HttpRetryException httpRetryException = (HttpRetryException) e;
	            // Logique de réessai basée sur httpRetryException.getRetryAfter()
	        } else {
				throw new RiotException("Données reçu invalides");
	        }
	    } catch (Exception e) {
			throw new RiotException("Données reçu invalides");
	    }
	    return listIds;
	}

	/**
	 * @param 	matchId L'id d'un match
	 * @return 			null en cas d'erreur, sinon une liste des 8 participants du match
	 * @throws RiotException 
	 */
	public ArrayList<Object> getMatchPlayers(String matchId) throws RiotException {
		String urlAPI = "https://europe.api.riotgames.com/tft/match/v1/matches/"+matchId;
		String response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			TreeNode jsonNode = objectMapper.readTree(response).get("metadata").get("participants");
			ArrayList<Object> listIds = objectMapper.treeToValue(jsonNode, ArrayList.class);
//			ObjectReader reader = objectMapper.readerFor(ArrayList.class);
//			ArrayList<Object> listIds = reader.readValue((JsonParser) jsonNode);
			return listIds;
		} catch (Exception e) {
			throw new RiotException("Données reçu invalides");
		}
	}

	/**
	 * @param 	playerId 	Le puuid d'un joueur ayant joué le match
	 * @param 	matchId 	L'id d'un match que le joueur à joué
	 * @return 				null en cas d'erreur, sinon des informations sur comment le joueur a joué le match
	 * @throws RiotException 
	 */
	public HashMap<String, Object> getParticipantInfos(String puuid, String matchId) throws RiotException {
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
				if (objectMapper.treeToValue(participant.get("puuid"), String.class).equals(puuid)) {
					placement = objectMapper.treeToValue(participant.get("placement"), Integer.class);
					for (TreeNode unit : participant.get("units")) {
						units.add(objectMapper.treeToValue(unit.get("character_id"), String.class).split("_")[1]);
					}
					break;
				}
			}
		} catch (Exception e) {
			throw new RiotException("Données reçu invalides");
		}
		HashMap<String, Object> mapInfos = new HashMap<String, Object>();
		mapInfos.put("placement", placement);
		mapInfos.put("units", units);
		return mapInfos;
	}
	
	/**
	 * @return 			null en cas d'erreur, sinon une liste des meilleurs joueurs
	 * @throws RiotException 
	 */
	public ArrayList<Object> getBestPlayers() throws RiotException {
		String urlAPI = "https://euw1.api.riotgames.com/tft/league/v1/challenger?queue=RANKED_TFT";
		String response = askAPI(urlAPI);
		if (response == null || response.isEmpty()) {
			return null;
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			TreeNode jsonNode = objectMapper.readTree(response).get("entries");
			ArrayList<Object> listIds = objectMapper.treeToValue(jsonNode, ArrayList.class);
			return listIds;
		} catch (Exception e) {
			throw new RiotException("Données reçu invalides");
		}
	}

	/**
	 * @param 	url 	l'url de la requete à envoyer à l'API Riot
	 * @return 			null en cas d'erreur, sinon le fichier json renvoyée par l'API au format String
	 * @throws RiotException 
	 */
	private String askAPI(String url) throws RiotException {
		try {
 			URI uri = new URI(url);

			connection = (HttpURLConnection) uri.toURL().openConnection();//bug encore avec certains pseudo très particuliers
			connection.setRequestMethod("GET");

			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 OPR/107.0.0.0");
			connection.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
			connection.setRequestProperty("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Origin", "https://developer.riotgames.com");
			connection.setRequestProperty("X-Riot-Token", "RGAPI-f17adcb3-a02a-4047-83d4-ef604411082f");

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String response = in.readLine();

			in.close();
			return response;
		} catch (Exception e) {
			throw new RiotException("Probleme lors de la connexion à l'API Riot");
		}
	}
}

